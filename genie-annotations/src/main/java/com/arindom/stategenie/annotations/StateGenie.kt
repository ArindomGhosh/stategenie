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

/**
 * [StateGenie] triggers our implementation SymbolProcessor from Kotlin Symbol Processor(KSP) where validate the Symbols for the following:
 * is `resolvable` &&
 * is `KSClassDeclaration` &&
 * is `classKind == ClassKind.INTERFACE` &&
 * is modifiers does not contains `Modifier.FUN`.
 *
 * After the validation each symbols are processed by an implementation of `KSVisitor` provided by KSP
 * to create the [sealed class] for the respective symbols.
 *
 * ### Following source code:
 * ```kotlin
 * @StateGenie(
 *   rootName = "NewUiSate",
 *   isParcelable = true
 * )
 * interface UiState{
 *   @ToState(
 *     stateName = "Success"
 *   )
 *   val data: String
 *
 *   @ToState(
 *     stateName = "Loading"
 *   )
 *   val loading: Unit
 *
 *   @ToState(
 *     stateName = "Error"
 *   )
 *   val error: Throwable
 * }
 * ```
 * ### Would generate:
 * ```kotlin
 * public sealed interface NewUiSate : Parcelable {
 *   @Parcelize
 *   public data class SuccessState(
 *     public final val `data`: String,
 *   ) : NewUiSate
 *
 *   @Parcelize
 *   public object LoadingState : NewUiSate
 *
 *   @Parcelize
 *   public data class ErrorState(
 *     public final val error: Throwable,
 *   ) : NewUiSate
 * }
 * ```
 * */

@Retention(
    AnnotationRetention.SOURCE
)
@Target(
    AnnotationTarget.CLASS
)
annotation class StateGenie(
    val rootName: String = "",
    val isParcelable: Boolean = false
) {
    companion object {

        const val ROOT_NAME = "rootName"
        const val IS_PARCELABLE = "isParcelable"
    }
}

/**
 *
 * [ToState] is to store information about the individual state for a set of states.
 * They are used with properties of the given interface representing the complete set of states e.g:
 * ```kotlin
 * @ToState(
 *     stateName = "Error"
 *   )
 * val error: Throwable
 * ```
 * Individual state could be either of following types:
 * 1) [data]: represented state as a wrapper for given DataType.
 * 2) [object]: represent the state.
 *
 * ### Following source code:
 * ```kotlin
 * @ToState(
 *     stateName = "Error"
 * )
 * val error: Throwable
 * ```
 * ### Would generate:
 * ```kotlin
 * public data class ErrorState(
 *     public final val error: Throwable,
 * )
 * ```
 *
 * ### Following source code:
 * ```kotlin
 * @ToState(
 *     stateName = "Loading"
 * )
 * val loading: Unit
 * ```
 * ### Would generate:
 * ```kotlin
 * @Parcelize
 * public object LoadingState : NewUiSate
 * ```
 *
 * */
@Retention(
    AnnotationRetention.SOURCE
)
@Target(
    AnnotationTarget.PROPERTY
)
annotation class ToState(val stateName: String = "") {
    companion object {
        const val STATE_NAME = "stateName"
    }
}