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
package com.arindom.stategenie.processors.stategenie

import com.arindom.stategenie.annotations.StateGenie
import com.arindom.stategenie.annotations.ToState
import com.arindom.stategenie.processors.ProgaurdConfig
import com.arindom.stategenie.processors.util.getAnnotationIfExist
import com.arindom.stategenie.processors.util.writeTo
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo

class StateGenieAnnotationVisitor(
  private val logger: KSPLogger,
  private val codeGenerator: CodeGenerator,
  private val options: Map<String, String>
) : KSVisitorVoid() {
  override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
    val packageName = classDeclaration.packageName.asString()
    val className = classDeclaration.simpleName.asString()
    val subSetMap = mutableMapOf<String, Pair<String, KSType>>()

    val arguments = classDeclaration.annotations.filter {
      it.shortName.asString() == StateGenie::class.simpleName
    }.first().arguments
    val rootName = arguments.first { it.name?.asString() == StateGenie.ROOT_NAME }.value as String
    val isParcelable =
      arguments.first { it.name?.asString() == StateGenie.IS_PARCELABLE }.value as Boolean

    val generatedClassName = if (rootName.isNotBlank()) {
      if (rootName == className) "$rootName\$Generated"
      else rootName
    } else {
      "$className\$Generated"
    }

    classDeclaration.getAllProperties().forEach {
      val subTypeDef = getSubTypDef(it)
      if (subTypeDef != null) subSetMap[subTypeDef.first] = subTypeDef.second
    }

    val file = FileSpec.builder(packageName, generatedClassName).apply {
      addFileComment(format = "This is a generated file.")
      addType(
        TypeSpec.interfaceBuilder(generatedClassName)
          .addKdoc("An extensive sealed interface generated for $className.").apply {
            if (isParcelable) {
              addSuperinterface(ClassName("android.os", "Parcelable"))
            }
            addModifiers(KModifier.SEALED)
            subSetMap.forEach { (subTypeName, typeDef) ->
              addType(
                subType(
                  superType = ClassName(
                    packageName,
                    generatedClassName
                  ),
                  subTypeName = subTypeName,
                  subTypeDef = typeDef,
                  isParcelable = isParcelable
                )
              )
            }
          }.build()
      )
      generateProgaurdRules(
        targetKClssDecleration = classDeclaration,
        extensiveName = generatedClassName
      )
    }.build()
    val dependencySource = classDeclaration.containingFile
    val sources = if (dependencySource != null) arrayOf(dependencySource) else emptyArray()
    file.writeTo(
      codeGenerator = codeGenerator,
      dependencies = Dependencies(
        aggregating = true,
        sources = sources
      )
    )
    subSetMap.clear()
  }

  private fun generateProgaurdRules(
    targetKClssDecleration: KSClassDeclaration,
    extensiveName: String
  ) {
    val progaurdConfig = ProgaurdConfig(
      targetClass = targetKClssDecleration.toClassName(),
      targetClassKind = targetKClssDecleration.classKind,
      extensiveName = extensiveName,
      extensiveConstructorParam = targetKClssDecleration.primaryConstructor?.parameters?.map { param ->
        param.type.resolve().toClassName().reflectionName()
      } ?: emptyList()
    )

    progaurdConfig.writeTo(
      codeGenerator,
      targetKClssDecleration.containingFile
    )
  }

  private fun getSubTypDef(ksPropertyDeclaration: KSPropertyDeclaration): Pair<String, Pair<String, KSType>>? {
    val toStateAnnotation = ToState::class.simpleName
    val toState = ksPropertyDeclaration.annotations.getAnnotationIfExist(toStateAnnotation!!)
    return if (toState != null) {
      val suggestedName = toState.arguments.first {
        it.name?.asString() == ToState.STATE_NAME
      }.value as String
      val stateName = suggestedName.ifBlank {
        ksPropertyDeclaration.simpleName.asString()
      }.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase() else it.toString()
      }.plus("State")

      Pair(
        stateName,
        Pair(
          ksPropertyDeclaration.simpleName.asString(),
          ksPropertyDeclaration.type.resolve()
        )
      )
    } else null
  }

  private fun subType(
    superType: TypeName,
    subTypeName: String,
    subTypeDef: Pair<String, KSType>,
    isParcelable: Boolean
  ): TypeSpec {
    val typeSpecBuilder = if (subTypeDef.second.toClassName().simpleName != "Unit") {
      TypeSpec.classBuilder(subTypeName).apply {
        val flux = FunSpec.constructorBuilder()
          .addParameter(subTypeDef.first, subTypeDef.second.toTypeName()).build()
        addModifiers(KModifier.DATA)
        primaryConstructor(flux)
        addProperty(
          PropertySpec.builder(subTypeDef.first, subTypeDef.second.toTypeName())
            .addModifiers(KModifier.FINAL).initializer(subTypeDef.first).build()

        )
      }
    } else {
      TypeSpec.objectBuilder(subTypeName)
    }

    return typeSpecBuilder.apply {
      if (isParcelable) {
        addAnnotation(ClassName("kotlinx.parcelize", "Parcelize"))
      }
      addSuperinterface(superType)
    }.build()
  }
}
