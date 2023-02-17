/*
 * Copyright 2023 Arindom Ghosh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

val ktlintVersion = "0.48.1"

initscript {
    val spotlessVersion = "6.10.0"

    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("com.diffplug.spotless:spotless-plugin-gradle:$spotlessVersion")
    }
}

rootProject {
    subprojects {
        apply<com.diffplug.gradle.spotless.SpotlessPlugin>()
        extensions.configure<com.diffplug.gradle.spotless.SpotlessExtension> {
            ratchetFrom("origin/main")
            kotlin {
                target("**/*.kt")
                targetExclude("$buildDir/**/*.kt")
                ktlint()
                    .setUseExperimental(true)
                    .editorConfigOverride(
                        mapOf(
                            "indent_size" to "2",
                        )
                    )
                licenseHeaderFile(rootProject.file("spotless/copyright.license.kt"))
                trimTrailingWhitespace()
                endWithNewline()
            }
            format("kts") {
                target("**/*.kts")
                targetExclude("$buildDir/**/*.kt")
                // Look for the first line that doesn't have a block comment (assumed to be the license)
                licenseHeaderFile(
                    rootProject.file("spotless/copyright.license.kt"),
                    "(^(?![\\/ ]\\*).*$)"
                )
                trimTrailingWhitespace()
                endWithNewline()
            }
        }
    }
}