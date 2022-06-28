import mu.KotlinLogging
import org.opendc.compute.api.Server
import org.opendc.compute.service.scheduler.FilterScheduler
import org.opendc.compute.service.scheduler.filters.ComputeFilter
import org.opendc.compute.service.scheduler.filters.RamFilter
import org.opendc.compute.service.scheduler.filters.VCpuFilter
import org.opendc.compute.service.scheduler.weights.CoreRamWeigher
import org.opendc.compute.workload.ComputeServiceHelper
import org.opendc.compute.workload.ComputeWorkloadLoader
import org.opendc.compute.workload.export.parquet.ParquetComputeMonitor
import org.opendc.compute.workload.telemetry.ComputeMetricReader
import org.opendc.compute.workload.topology.apply
import org.opendc.compute.workload.trace
import org.opendc.experiments.capelin.topology.clusterTopology
import org.opendc.simulator.core.runBlockingSimulation
import writer.EEWriter
import java.io.File
import java.time.Duration
import java.util.*

fun main(args: Array<String>) {
    val logger = KotlinLogging.logger() {}

    val traceLoc = File("traces")

    val loader = ComputeWorkloadLoader(traceLoc)

    val computeScheduler = FilterScheduler(
        filters = listOf(ComputeFilter(), VCpuFilter(16.0), RamFilter(1.0)),
        weighers = listOf(CoreRamWeigher(multiplier = 1.0))
    )

    val workload = trace("solvinity")

    val topology = clusterTopology(File("traces/topology/topology.txt"))

    val outputPath = File("output/out.csv")

    val writer = EEWriter(outputPath)

    runBlockingSimulation {
        val seeder = Random(0)

        val (vms, _) = workload.resolve(loader, seeder)

        val runnerService = ComputeServiceHelper(coroutineContext, clock, computeScheduler)

        val servers = mutableListOf<Server>()

        val exporter = ComputeMetricReader(
            this,
            clock,
            runnerService.service,
            servers,
            writer,
            exportInterval = Duration.ofMinutes(5)
        )

        runnerService.use { runner ->
            // Instantiate the desired topology
            runner.apply(topology, optimize = true)

            // Run the workload trace
            runner.run(vms, seeder.nextLong(), servers)

            // Stop the metric collection
            exporter.close()
        }
    }
}
