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

package com.arindom.stategenie.processors

import com.arindom.stategenie.processors.datastategenie.DataStateGenieSymbolProcessorProvider
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspWithCompilation
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.junit.Assert
import org.junit.Test

class DataGenieProcessorTest {

  @Test
  fun `DataStateGenie annotation should compile with success`() {
    val sourceFile = """
      package geniestate
      import com.arindom.stategenie.annotations.DataStateConfig
      import com.arindom.stategenie.annotations.DataStateGenie

      @DataStateGenie(
        DataStateConfig(
          name = "Poster",
          isLoadingDefault = true
        )
      )

      data class PosterDetails(
        val name: String = "",
        val release: String = "",
        val playtime: String = "",
        val description: String? = null,
        val poster: String = "",
        val gif: String = ""
      )
    """.trimIndent()

    val kotlinSource = SourceFile.kotlin(
      name = "PosterDetails.kt",
      contents = sourceFile
    )

    val result = KotlinCompilation()
      .apply {
        sources = listOf(kotlinSource)
        inheritClassPath = true
        symbolProcessorProviders = listOf(DataStateGenieSymbolProcessorProvider())
        verbose = false
        kspWithCompilation = true
      }.compile()
    Assert.assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
  }
}