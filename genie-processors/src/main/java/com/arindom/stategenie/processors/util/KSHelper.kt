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
package com.arindom.stategenie.processors.util

import com.arindom.stategenie.processors.ProgaurdConfig
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSFile

internal fun Sequence<KSAnnotation>.getAnnotation(target: String): KSAnnotation {
  return getAnnotationIfExist(target)
    ?: throw NoSuchElementException("Sequence contains no element matching the $target.")
}

internal fun Sequence<KSAnnotation>.getAnnotationIfExist(target: String): KSAnnotation? {
  for (element in this) if (element.shortName.asString() == target) return element
  return null
}

internal fun Sequence<KSAnnotation>.hasAnnotation(target: String): Boolean {
  for (element in this) if (element.shortName.asString() == target) return true
  return false
}

internal fun ProgaurdConfig.writeTo(codeGenerator: CodeGenerator, originatingKSFile: KSFile?) {
  codeGenerator.createNewFile(
    dependencies = Dependencies(
      aggregating = false,
      sources = originatingKSFile?.let { arrayOf(it) } ?: emptyArray()
    ),
    packageName = "",
    fileName = outputFile,
    extensionName = ""
  ).bufferedWriter().use(::writeTo)
}
