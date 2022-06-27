plugins {
    `kotlin-conventions`
    `testing-conventions`
    `jacoco-conventions`
    `benchmark-conventions`
    distribution
}

dependencies {
    api(projects.opendcCompute.opendcComputeWorkload)

    implementation(projects.opendcSimulator.opendcSimulatorCore)
    implementation(projects.opendcSimulator.opendcSimulatorCompute)
    implementation(projects.opendcCompute.opendcComputeSimulator)

    implementation(libs.clikt)
    implementation(libs.progressbar)
    implementation(libs.kotlin.logging)
    implementation(libs.jackson.dataformat.csv)

    runtimeOnly(projects.opendcTrace.opendcTraceOpendc)
    runtimeOnly(projects.opendcTrace.opendcTraceApi)
    runtimeOnly(libs.log4j.slf4j)
}
