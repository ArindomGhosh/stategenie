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

package com.arindom.stategenie.processors.datastategenie

import com.arindom.stategenie.annotations.DataStateGenie
import com.arindom.stategenie.processors.ProgaurdConfig
import com.arindom.stategenie.processors.util.writeTo
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import java.util.*
import kotlin.random.Random

class DataGenieVisitor(
  private val codeGenerator: CodeGenerator,
  private val logger: KSPLogger
) : KSVisitorVoid() {
  override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
    val packageName = classDeclaration.packageName.asString()
    val sourceClassName = classDeclaration.simpleName.asString()

    val dataStateConfig = classDeclaration.annotations.filter {
      it.shortName.asString() == DataStateGenie::class.simpleName
    }.first()
      .arguments.map { it.value as KSAnnotation }
      .first().arguments

    val suggestedName = dataStateConfig.first { it.name?.asString() == NAME_ARG }.value as String
    val errorType = dataStateConfig.first { it.name?.asString() == ERROR_TYPE_ARG }.value as KSType
    val isLoadingByDefault =
      dataStateConfig.first { it.name?.asString() == IS_LOADING_DEFAULT_ARG }.value as Boolean

    val uiStateClassName = (suggestedName.ifBlank { sourceClassName }).plus("UiState")
    val uiStateTypeName = ClassName(packageName, uiStateClassName)
    val propertyMap = createPropertyMap(classDeclaration, errorType)
    val file = FileSpec.builder(packageName, uiStateClassName)
      .apply {
        addType(getUiStateClass(propertyMap = propertyMap, uiStateClassName))
        addFunction(
          getUiStateBuilderFun(
            sourceClassName,
            uiStateClassName,
            uiStateTypeName,
            propertyMap,
            isLoadingByDefault
          )
        )
        getUpdateFunctions(
          uiStateClassName,
          uiStateTypeName,
          propertyMap,
        ).forEach {
          addFunction(it)
        }
        generateProgaurdRules(
          targetKClssDecleration = classDeclaration,
          extensiveName = uiStateClassName,
          errorType
        )
      }
      .build()

    file.writeTo(codeGenerator, true)
  }

  private fun generateProgaurdRules(
    targetKClssDecleration: KSClassDeclaration,
    extensiveName: String,
    errorType: KSType
  ) {
    val paramList = buildList {
      add(Boolean::class.asClassName().reflectionName())
      add(errorType.toClassName().reflectionName())
      targetKClssDecleration.primaryConstructor?.parameters?.map { param ->
        add(param.type.resolve().toClassName().reflectionName())
      }
    }
    val progaurdConfig = ProgaurdConfig(
      targetClass = targetKClssDecleration.toClassName(),
      targetClassKind = targetKClssDecleration.classKind,
      extensiveName = extensiveName,
      extensiveConstructorParam = paramList

    )

    progaurdConfig.writeTo(
      codeGenerator,
      targetKClssDecleration.containingFile
    )
  }

  private fun getUpdateFunctions(
    uiStateClassName: String,
    uiStateTypeName: TypeName,
    propertyMap: Map<String, TypeName>
  ): List<FunSpec> {
    return buildList {
      propertyMap.forEach { (name, typeName) ->
        when (name) {
          ERROR -> {
            add(FunSpec.builder("update${
              name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
              }
            }").addParameter(ParameterSpec(name, typeName.copy(false)))
              .receiver(uiStateTypeName)
              .returns(uiStateTypeName)
              .addStatement("return this.copy($name = $name, $LOADING = false)").build())
          }
          LOADING -> {
            add(
              FunSpec.builder("isLoading")
                .receiver(uiStateTypeName)
                .returns(uiStateTypeName)
                .addStatement("return this.copy($name = true, $ERROR = null)").build()
            )
          }
          else -> {
            add(FunSpec.builder("update${
              name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
              }
            }").addParameter(ParameterSpec(name, typeName))
              .receiver(uiStateTypeName)
              .returns(uiStateTypeName)
              .addStatement("return this.copy($name = $name, error = null, $LOADING = false)")
              .build())
          }
        }
      }
    }
  }

  private fun getUiStateBuilderFun(
    sourceClassName: String,
    uiStateClassName: String,
    uiStateTypeName: TypeName,
    propertyMap: Map<String, TypeName>,
    isLoadingByDefault: Boolean
  ): FunSpec {
    val defaultValue = "_${sourceClassName.replaceFirstChar { it.lowercase(Locale.getDefault()) }}"
    return FunSpec.builder(uiStateClassName)
      .apply {
        addStatement("val $defaultValue = $sourceClassName()")
        addStatement(
          "return ${uiStateClassName}(" +
                  buildString {
                    propertyMap.forEach { (variable, _) ->
                      when (variable) {
                        LOADING -> {
                          append("$LOADING = $isLoadingByDefault,\n")
                        }
                        ERROR -> {
                          append("$ERROR = null,\n")
                        }
                        else -> {
                          append("$variable = ${defaultValue}.$variable,\n")
                        }
                      }
                    }
                  } +
                  ")"
        )
      }
      .returns(uiStateTypeName)
      .build()
  }


  private fun getUiStateClass(
    propertyMap: Map<String, TypeName>,
    uiStateClassName: String
  ): TypeSpec {
    val random = Random.nextInt(0, 100)
    return TypeSpec.classBuilder(uiStateClassName)
      .apply {
        addModifiers(KModifier.DATA)
        primaryConstructor(FunSpec.constructorBuilder()
          .apply {
            propertyMap.forEach { (name, typeName) ->
              addParameter(ParameterSpec.builder(name, typeName)
                .apply {
                  if (typeName.isNullable) {
                    defaultValue("null")
                  }
                }
                .build())
            }
          }
          .build())

        propertyMap.forEach { (name, typeName) ->
          addProperty(
            PropertySpec.builder(name, typeName)
              .initializer(name)
              .build()
          )
        }

        addFunction(
          FunSpec.builder(
            "equals"
          ).apply {
            addModifiers(KModifier.OVERRIDE)
              .returns(Boolean::class).addParameter(
                ParameterSpec
                  .builder("other", Any::class.asClassName().copy(nullable = true))
                  .build()
              )
            addStatement("if (this === other) return true")
            addStatement("if (javaClass != other?.javaClass) return false")
            addStatement("other as $uiStateClassName")
            propertyMap.forEach { (name, _) ->
              addStatement("if($name != other.$name) return false")
            }
            addStatement("return true")
          }
            .build()
        )

        addFunction(
          FunSpec.builder(
            "hashCode"
          ).apply {
            addModifiers(
              KModifier.OVERRIDE
            ).returns(Int::class)
            propertyMap.onEachIndexed() { index, (name, typeName) ->
              if (index == 0) {
                addStatement(
                  "var result = ${
                    if (typeName.isNullable) {
                      "(${name}?.hashCode() ?: 0)"
                    } else {
                      "${name}.hashCode()"
                    }
                  }"
                )
              }
              addStatement("result = $random * result + ${
                if (typeName.isNullable) {
                  "(${name}?.hashCode() ?: 0)"
                } else {
                  "${name}.hashCode()"
                }
              }")
            }
            addStatement("return result")
          }
            .build()
        )
      }
      .build()
  }

  private fun createPropertyMap(
    classDeclaration: KSClassDeclaration,
    errorType: KSType
  ): Map<String, TypeName> {
    return buildMap {
      put(LOADING, Boolean::class.asClassName())
      put(ERROR, errorType.toClassName().copy(nullable = true))
      classDeclaration.getAllProperties()
        .forEach {
          put(
            it.simpleName.asString(), it.type.toTypeName()
          )
        }
    }
  }

  companion object {
    private const val LOADING: String = "loading"
    private const val ERROR: String = "error"
    private const val NAME_ARG = "name"
    private const val ERROR_TYPE_ARG = "errorType"
    private const val IS_LOADING_DEFAULT_ARG = "isLoadingDefault"
  }
}