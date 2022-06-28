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


public class EEWriter(private val outputPath: File) : ComputeMonitor {

    private val file = FileWriter(outputPath)

    private val buffer = BufferedWriter(file)

    private val csvPrinter = CSVPrinter(buffer, CSVFormat.DEFAULT)

    init {
        csvPrinter.printRecord(headers)
    }

    override fun record(reader: HostTableReader) {
        val entry = listOf(
            reader.timestamp,
            reader.host.id,
            reader.powerTotal,
            reader.powerUsage,
            reader.cpuUsage,
            reader.cpuUtilization
        )

        csvPrinter.printRecord(entry)
    }


    fun close() {
        csvPrinter.close(true)
    }

    private companion object {
        val headers = listOf(
            "Timestamp [ms]",
            "host_id",
            "power_total [J]",
            "power_usage [W]",
            "cpu_usage",
            "cpu_util"
        )
    }
}


