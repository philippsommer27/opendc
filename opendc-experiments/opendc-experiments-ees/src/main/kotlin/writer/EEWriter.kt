package writer

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.opendc.compute.workload.telemetry.ComputeMonitor
import org.opendc.compute.workload.telemetry.table.HostTableReader
import org.opendc.compute.workload.telemetry.table.ServerTableReader
import org.opendc.compute.workload.telemetry.table.ServiceTableReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.nio.charset.StandardCharsets


public class EEWriter(private val outputPath: File) : ComputeMonitor {

    private val file = FileWriter(outputPath, StandardCharsets.UTF_8)

    private val buffer = BufferedWriter(file)

    private val csvPrinter = CSVPrinter(buffer, CSVFormat.DEFAULT)

    init {
        csvPrinter.printRecord(header)
    }

    override fun record(reader: HostTableReader) {
        val entry = listOf(
            reader.timestamp.toEpochMilli(),
            reader.host.id,
            reader.powerTotal,
            reader.powerUsage
        )

        csvPrinter.printRecord(entry)
    }

    private companion object {
        val header : List<String> = listOf(
            "timestamp",
            "host_id",
            "power_total",
            "power_usage"
        )
    }
}
