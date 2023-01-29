package com.arindom.stategenie.processors

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class GenieStateSymbolProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return GenieStateProcessor(
            logger = environment.logger,
            codeGenerator = environment.codeGenerator,
            options = environment.options
        )
    }
}