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
package com.arindom.stategenie.annotations

import kotlin.reflect.KClass

/**
 * [DataStateGenie] is applied on data class which contains all the properties which would impact your ui state.
 * This would generate a single data class ${name}UiState and extention functions to update individual parameters,
 * loading state and  error object of subtype of [Throwable] and default constructor function to get the default
 * object as: ${name}UiState().
 *
 * @param dataStateConfig To provide the configuration for the generated UI state class
 * @see [DataStateConfig]
 * */
@Retention(
  AnnotationRetention.SOURCE
)
@Target(
  AnnotationTarget.CLASS
)
annotation class DataStateGenie(
  val dataStateConfig: DataStateConfig = DataStateConfig()
)

/**
 * [DataStateConfig] is used by [DataStateGenie] to provide configuration used by the SymbolProcessor
 * to generate th ui state.
 *
 * @param name The optional name parameter is used for the name generated stata class as: ${name}UiState.
 * In case not provided the name of the data class is used by default as: ${classname}UIState
 * @param errorType The optional errorType is subtype of [Throwable] to make it customizable to be used
 * to represent a set of allowed [Throwable] subtypes like sealed classes. In case not provided it defaults to
 * [Throwable]
 * @param isLoadingDefault The optional isLoadingDefault is set false by default.
 * */

@Retention(
  AnnotationRetention.SOURCE
)
annotation class DataStateConfig(
  val name: String = "",
  val errorType: KClass<out Throwable> = Throwable::class,
  val isLoadingDefault: Boolean = false
)
