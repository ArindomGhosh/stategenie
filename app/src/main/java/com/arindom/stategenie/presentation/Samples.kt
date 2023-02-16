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
package com.arindom.stategenie.presentation

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.arindom.stategenie.annotations.DataStateConfig
import com.arindom.stategenie.annotations.DataStateGenie
import com.arindom.stategenie.annotations.StateGenie
import com.arindom.stategenie.annotations.ToState
import kotlinx.parcelize.Parcelize

@StateGenie(
  rootName = "NewUiSate",
  isParcelable = true
)
interface UiState : BaseUIiState<List<String>> {
  @ToState(
    stateName = "LoggedOut"
  )
  val loggedOut: Unit

  @ToState(
    stateName = "foo"
  )
  val foo: Boolean

  @ToState(
    stateName = "bar"
  )
  val bar: String
}

@StateGenie(
  rootName = "MovieUiState"
)
interface MovieStates : BaseUIiState<List<String>>

@Immutable
@Parcelize
data class DummyUiState(
  val data: List<String>
) : Parcelable

@Immutable
data class PosterExtensive(
  val posters: List<Poster>
)

@Immutable
data class Poster(
  val name: String,
  val poster: String,
  val playtime: String
)

fun List<Poster>.toExtensive(): PosterExtensive {
  return PosterExtensive(this)
}

@Stable
@Immutable
@DataStateGenie(
  DataStateConfig(
    name = "Poster",
    errorType = UserThrowable::class,
    isLoadingDefault = true
  )
)
data class PosterDetails(
  val name: String = "",
  val release: String = "",
  val playtime: String = "",
  val description: String? = null,
  val poster: String = "",
  val gif: String = ""
)

sealed class UserThrowable(message: String) : Throwable(message) {
  object NoDataFoundThrowable : UserThrowable("No Data Found")

  data class ApiException(override val message: String) : UserThrowable(message)
}