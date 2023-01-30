package com.arindom.stategenie.presentation

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.arindom.stategenie.annotations.StateGenie
import com.arindom.stategenie.annotations.ToState
import kotlinx.parcelize.Parcelize


interface BaseUIiState<T : Any> {
    @ToState(
        stateName = "Success"
    )
    val data: T

    @ToState(
        stateName = "Loading"
    )
    val loading: Unit

    @ToState(
        stateName = "Error"
    )
    val error: Throwable
}
// : BaseUIiState<DummyUiState>
//  isParcelable = true

//rootName = "NewUiSate",
//isParcelable = true
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
    rootName = "MovieUiState",
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
data class PosterDetails(
    val name: String,
    val release: String,
    val playtime: String,
    val description: String,
    val poster: String,
    val gif: String
)