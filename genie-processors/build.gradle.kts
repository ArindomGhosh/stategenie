plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation(project(":genie-annotations"))
    implementation(libs.ksp.symbol.processing)
    implementation(libs.ksp.symbol.processing.api)
    implementation(libs.kotlin.poet)
}