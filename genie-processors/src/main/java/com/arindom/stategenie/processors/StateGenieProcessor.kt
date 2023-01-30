package com.arindom.stategenie.processors

import com.arindom.stategenie.annotations.StateGenie
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.validate

class StateGenieProcessor(
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator,
    private val options: Map<String, String>
) : SymbolProcessor {
    private val _genieAnnotation = StateGenie::class.qualifiedName
    private val _genieSymbolValidator = StateGenieSymbolValidator(logger)
    private val _genieSymbolVisitor = StateGenieAnnotationVisitor(
        logger = logger,
        codeGenerator = codeGenerator,
        options = options
    )

    override fun process(resolver: Resolver): List<KSAnnotated> {
        var unresolved = emptyList<KSAnnotated>()
        if (_genieAnnotation != null) {
            val resolvedSymbols = resolver.getSymbolsWithAnnotation(_genieAnnotation).toList()
            val validated = resolvedSymbols.filter { it.validate() }
            validated.filter {
                _genieSymbolValidator.isValid(it)
            }.forEach {
                it.accept(_genieSymbolVisitor, Unit)
            }
            unresolved = resolvedSymbols - validated.toSet()
        }
        return unresolved
    }
}