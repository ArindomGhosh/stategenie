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
package com.arindom.stategenie.processors.datastategenie

import com.arindom.stategenie.annotations.DataStateGenie
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.validate

class DataGenieStateSymbolProcessor(
  private val logger: KSPLogger,
  private val codeGenerator: CodeGenerator,
  private val option: Map<String, String>
) : SymbolProcessor {

  private val _dataStateGenieAnnonation = DataStateGenie::class.qualifiedName
  private val _dataGenieStateValidator = DataGenieSymbolValidator()
  private val _dataGenieStateVisitor =
    DataGenieVisitor(
      codeGenerator = codeGenerator,
      logger = logger
    )

  override fun process(resolver: Resolver): List<KSAnnotated> {
    var unresolvedSymbols = emptyList<KSAnnotated>()
    if (_dataStateGenieAnnonation != null) {
      val resolved = resolver.getSymbolsWithAnnotation(_dataStateGenieAnnonation)
        .distinct()
        .toList()
      if (!resolved.iterator().hasNext()) return emptyList()
      val validated = resolved.filter { it.validate() }
      validated.filter {
        _dataGenieStateValidator.isValid(it)
      }.forEach {
        it.accept(_dataGenieStateVisitor, Unit)
      }
      unresolvedSymbols = resolved - validated.toSet()
    }
    return unresolvedSymbols
  }
}
