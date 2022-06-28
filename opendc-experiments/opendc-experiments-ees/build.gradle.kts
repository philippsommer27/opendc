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
    implementation(projects.opendcTrace.opendcTraceApi)
    implementation(projects.opendcTrace.opendcTraceParquet)
    implementation(libs.jackson.dataformat.csv)

    implementation(libs.clikt)
    implementation(libs.progressbar)
    implementation(libs.kotlin.logging)
    implementation(libs.jackson.dataformat.csv)
    implementation("org.apache.commons:commons-csv:1.9.0")

    runtimeOnly(projects.opendcTrace.opendcTraceOpendc)
    runtimeOnly(projects.opendcTrace.opendcTraceApi)
    runtimeOnly(libs.log4j.slf4j)
}
