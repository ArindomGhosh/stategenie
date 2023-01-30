package com.arindom.stategenie.processors

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class StateGenieSymbolProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return StateGenieProcessor(
            logger = environment.logger,
            codeGenerator = environment.codeGenerator,
            options = environment.options
        )
    }
}