/*
 * Copyright 2023 Arindom Ghosh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.arindom.stategenie

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType


internal fun Project.configureAndroidCompose(
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