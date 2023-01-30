package com.arindom.stategenie.processors

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspWithCompilation
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.junit.Assert
import org.junit.Test

class StateGenieProcessorTest {

    @Test
    fun `GeneState annotation should compile with success`() {
        val sourceFile = """
            package geneistate
            
            import com.arindom.stategenie.annotations.StateGenie
            import com.arindom.stategenie.annotations.ToState

            interface BaseUIiState<T : Any> {
                @ToState(
                    stateName = "Success"
                )
                val data: T
            
                @ToState(
                    stateName = "Loading"
                )
                val loading: Unit
            
                @ToState(
                    stateName = "Error"
                )
                val error: Throwable
            }

            @StateGenie(
                rootName = "NewUiSate",
                isParcelable = false
            )
            interface UiState : BaseUIiState<String> {
                @ToState(
                    stateName = "LoggedOut"
                )
                val loggedOut: Unit

                @ToState(
                    stateName = "Wierd"
                )
                val someWierd: Boolean

                @ToState(
                    stateName = "NonUi"
                )
                val nonUiStateValue: String

            }
        """.trimIndent()

        val kotlinSource = SourceFile.kotlin(
            name = "UiState.kt",
            contents = sourceFile
        )

        val result = KotlinCompilation()
            .apply {
                sources = listOf(kotlinSource)
                inheritClassPath = true
                symbolProcessorProviders = listOf(StateGenieSymbolProcessorProvider())
                verbose = false
                kspWithCompilation = true
            }.compile()
        Assert.assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
    }
}