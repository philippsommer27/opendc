package org.opendc.experiments.ee

import mu.KotlinLogging
import org.opendc.compute.api.Server
import org.opendc.compute.service.scheduler.ComputeScheduler
import org.opendc.compute.workload.ComputeServiceHelper
import org.opendc.compute.workload.ComputeWorkloadLoader
import org.opendc.compute.workload.createComputeScheduler
import org.opendc.compute.workload.export.parquet.ParquetComputeMonitor
import org.opendc.compute.workload.telemetry.ComputeMetricReader
import org.opendc.compute.workload.telemetry.ComputeMonitor
import org.opendc.compute.workload.topology.HostSpec
import org.opendc.compute.workload.topology.Topology
import org.opendc.compute.workload.topology.apply
import org.opendc.simulator.compute.power.SimplePowerDriver
import org.opendc.simulator.core.runBlockingSimulation
import org.opendc.trace.bitbrains.BitbrainsTraceFormat
import java.io.File
import java.time.Clock
import java.util.*
import kotlin.coroutines.coroutineContext
import kotlin.random.Random

fun main() {
    val logger = KotlinLogging.logger {}

    val bitbrainsPath = "traces/bitbrains/fastStorage"
    val loader = ComputeWorkloadLoader(File(bitbrainsPath))

    val computeScheduler = createComputeScheduler("random", java.util.Random())

    val computeMonitor = ParquetComputeMonitor(File("output"), "partition", 4096)

    try {
        runBlockingSimulation {

            val simulator = ComputeServiceHelper(coroutineContext, clock, computeScheduler)

            val servers = mutableListOf<Server>()
            val reader = ComputeMetricReader(this, clock, simulator.service, servers, computeMonitor)


            try {
                // Instantiate the topology onto the simulator
                simulator.apply(topology)
                // Run workload trace
                simulator.run(vms, java.util.Random().nextLong(), servers)

                val serviceMetrics = simulator.service.getSchedulerStats()
                logger.debug {
                    "Scheduler " +
                        "Success=${serviceMetrics.attemptsSuccess} " +
                        "Failure=${serviceMetrics.attemptsFailure} " +
                        "Error=${serviceMetrics.attemptsError} " +
                        "Pending=${serviceMetrics.serversPending} " +
                        "Active=${serviceMetrics.serversActive}"
                }
            } finally {
                simulator.close()
                reader.close()
            }
        }

    } catch (e: Exception) {
        logger.error("Something went wrong...")
    }
}

fun makeExampleTop (): Topology {
    return object : Topology {
        override fun resolve(): List<HostSpec> {


            val spec = HostSpec(
                UUID.randomUUID(),
                "node-x",
                mapOf("cluster" to 0),
                machineModel,
                SimplePowerDriver(powerModel)
            )


        }

    }
}
