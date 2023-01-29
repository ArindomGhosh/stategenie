package com.arindom.stategenie.processors

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.Modifier
import com.google.devtools.ksp.validate

class GenieSymbolValidator(
    private val logger: KSPLogger
) {
    fun isValid(ksAnnotated: KSAnnotated): Boolean {
        return ksAnnotated.validate()
                && ksAnnotated is KSClassDeclaration
                && ksAnnotated.classKind == ClassKind.INTERFACE
                && !ksAnnotated.modifiers.contains(Modifier.FUN)
    }
}