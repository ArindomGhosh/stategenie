package com.arindom.stategenie

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType


fun Project.configureAndroidCompose(
    commonExtention: CommonExtension<*, *, *, *>
) {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

    commonExtention.apply {
        buildFeatures {
            compose = true
        }
        composeOptions {
            kotlinCompilerExtensionVersion =
                libs.findVersion("androidxComposeCompiler").get().toString()
        }
        dependencies {
            val bom = libs.findLibrary("androidx.compose.bom").get()

            add("implementation", platform(bom))
            add("implementation", libs.findLibrary("androidx.compose.ui").get())
            add("implementation", libs.findLibrary("androidx.compose.ui.tooling.preview").get())
            add("implementation", libs.findLibrary("androidx.compose.material").get())
            add("implementation", libs.findLibrary("androidx.compose.material").get())

            add("debugImplementation", libs.findLibrary("androidx.compose.ui.tooling").get())
            add("debugImplementation", libs.findLibrary("androidx.compose.ui.test.manifest").get())

            add("androidTestImplementation", platform(bom).get())

            add("androidTestImplementation", libs.findLibrary("androidx.compose.ui.test.juit4").get())
        }
    }
}