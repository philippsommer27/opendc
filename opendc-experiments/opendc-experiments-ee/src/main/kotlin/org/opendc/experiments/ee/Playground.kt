package org.opendc.experiments.ee

import org.opendc.compute.workload.ComputeWorkloadLoader
import org.opendc.trace.bitbrains.BitbrainsTraceFormat
import java.io.File

fun main() {

    val bitbrainsPath = "traces/bitbrains/fastStorage"
    val loader = ComputeWorkloadLoader(File(bitbrainsPath))

}
