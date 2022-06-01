package org.opendc.experiments.ee

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.defaultLazy
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import java.io.File


internal class EnergyExperimentsCommand: CliktCommand(name = "ee") {

    /**
     * The path to the experiment output.
     */
    private val outputPath by option("-O", "--output", help = "path to experiment output")
        .file(canBeDir = true, canBeFile = false)
        .defaultLazy { File("output") }

    /**
     * Disable writing output.
     */
    private val disableOutput by option("--disable-output", help = "disable output").flag()

    private val tracePath by option("--trace-path", help = "path to trace directory")
        .file(canBeDir = true, canBeFile = false)
        .defaultLazy { File("input/traces") }

    override fun run() {

    }


}

