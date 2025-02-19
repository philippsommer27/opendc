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

package org.opendc.compute.workload.topology

import org.opendc.simulator.compute.kernel.SimFairShareHypervisorProvider
import org.opendc.simulator.compute.kernel.SimHypervisorProvider
import org.opendc.simulator.compute.model.MachineModel
import org.opendc.simulator.compute.power.PowerDriver
import java.util.*

/**
 * Description of a physical host that will be simulated by OpenDC and host the virtual machines.
 *
 * @param uid Unique identifier of the host.
 * @param name The name of the host.
 * @param meta The metadata of the host.
 * @param model The physical model of the machine.
 * @param powerDriver The [PowerDriver] to model the power consumption of the machine.
 * @param hypervisor The hypervisor implementation to use.
 */
public data class HostSpec(
    val uid: UUID,
    val name: String,
    val meta: Map<String, Any>,
    val model: MachineModel,
    val powerDriver: PowerDriver,
    val hypervisor: SimHypervisorProvider = SimFairShareHypervisorProvider()
)
