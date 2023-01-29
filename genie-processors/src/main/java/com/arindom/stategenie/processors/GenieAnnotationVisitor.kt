package com.arindom.stategenie.processors

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.writeTo

class GenieAnnotationVisitor(
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator,
    private val options: Map<String, String>
) : KSVisitorVoid() {
    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val packageName = classDeclaration.packageName.asString()
        val className = classDeclaration.simpleName.asString()

        val fileName = "$className\$Generated"
        val file = FileSpec.builder(packageName, fileName)
            .build()
        file.writeTo(codeGenerator, false)
    }
}