/*
 * Copyright (c) 2021 AtLarge Research
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.opendc.workflow.workload

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.opendc.compute.api.ComputeClient
import org.opendc.workflow.api.Job
import org.opendc.workflow.service.WorkflowService
import java.time.Clock
import kotlin.coroutines.CoroutineContext

/**
 * Helper class to simulate workflow-based workloads in OpenDC.
 *
 * @param context [CoroutineContext] to run the simulation in.
 * @param clock [Clock] instance tracking simulation time.
 * @param computeClient A [ComputeClient] instance to communicate with the cluster scheduler.
 * @param schedulerSpec The configuration of the workflow scheduler.
 */
public class WorkflowServiceHelper(
    private val context: CoroutineContext,
    private val clock: Clock,
    private val computeClient: ComputeClient,
    private val schedulerSpec: WorkflowSchedulerSpec
) : AutoCloseable {
    /**
     * The [WorkflowService] that is constructed by this runner.
     */
    public val service: WorkflowService = WorkflowService(
        context,
        clock,
        computeClient,
        schedulerSpec.schedulingQuantum,
        jobAdmissionPolicy = schedulerSpec.jobAdmissionPolicy,
        jobOrderPolicy = schedulerSpec.jobOrderPolicy,
        taskEligibilityPolicy = schedulerSpec.taskEligibilityPolicy,
        taskOrderPolicy = schedulerSpec.taskOrderPolicy,
    )

    /**
     * Run the specified list of [jobs] using the workflow service and suspend execution until all jobs have
     * finished.
     */
    public suspend fun replay(jobs: List<Job>) {
        // Sort jobs by their arrival time
        val orderedJobs = jobs.sortedBy { it.metadata.getOrDefault("WORKFLOW_SUBMIT_TIME", Long.MAX_VALUE) as Long }
        if (orderedJobs.isEmpty()) {
            return
        }

        // Wait until the trace is started
        val startTime = orderedJobs[0].metadata.getOrDefault("WORKFLOW_SUBMIT_TIME", Long.MAX_VALUE) as Long
        var offset = 0L

        if (startTime != Long.MAX_VALUE) {
            offset = startTime - clock.millis()
            delay(offset.coerceAtLeast(0))
        }

        coroutineScope {
            for (job in orderedJobs) {
                val submitTime = job.metadata.getOrDefault("WORKFLOW_SUBMIT_TIME", Long.MAX_VALUE) as Long
                if (submitTime != Long.MAX_VALUE) {
                    delay(((submitTime - offset) - clock.millis()).coerceAtLeast(0))
                }

                launch { service.invoke(job) }
            }
        }
    }

    override fun close() {
        computeClient.close()
        service.close()
    }
}
