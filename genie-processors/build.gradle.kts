plugins {
    id("com.arindom.stategenie.library")
}

dependencies {
    implementation(project(":genie-annotations"))
    implementation(libs.ksp.symbol.processing)
    implementation(libs.ksp.symbol.processing.api)
    implementation(libs.kotlin.poet)

    testImplementation(libs.junit4)
    testImplementation(libs.kotlin.compile.testing)
}
