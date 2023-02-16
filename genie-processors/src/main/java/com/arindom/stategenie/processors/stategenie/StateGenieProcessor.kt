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
