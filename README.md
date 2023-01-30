# StateGenie

`StateGenie` provides annotation processor to generate `sealed interface` representing a defined set of states.
It gives you the flexibility of reusing a base model in different scenarios while also enabling you to add new
models over them based on your requirements reducing the repeated boiler plate code.

## Usage

`StateGenie` provides following annotations:
- `@StateGenie`: To define the top level `sealed interface`.
- `@ToState`: To define the individual sub states (`data` and `object`) for the given extensive `sealed class`

## Use Cases
### Standalone State provided with `rootName`

#### Your Code
```kotlin
@StateGenie(
    rootName = "NewUiSate",
)
interface UiState{
    @ToState(
        stateName = "Success"
    )
    val data: String

    @ToState(
        stateName = "Loading"
    )
    val loading: Unit

    @ToState(
        stateName = "Error"
    )
    val error: Throwable
}
```
#### Generated Code
```kotlin
public sealed interface NewUiSate {
  public data class SuccessState(
    public final val `data`: String,
  ) : NewUiSate

  public object LoadingState : NewUiSate

  public data class ErrorState(
    public final val error: Throwable,
  ) : NewUiSate
}
```


### Standalone State provided with `rootName` and `isParcelable`

#### Your Code
```kotlin
@StateGenie(
    rootName = "NewUiSate",
    isParcelable = true
)
interface UiState{
    @ToState(
        stateName = "Success"
    )
    val data: String

    @ToState(
        stateName = "Loading"
    )
    val loading: Unit

    @ToState(
        stateName = "Error"
    )
    val error: Throwable
}
```
#### Generated Code
```kotlin
public sealed interface NewUiSate : Parcelable {
  @Parcelize
  public data class SuccessState(
    public final val `data`: String,
  ) : NewUiSate

  @Parcelize
  public object LoadingState : NewUiSate

  @Parcelize
  public data class ErrorState(
    public final val error: Throwable,
  ) : NewUiSate
}
```


### Standalone State

#### Your Code
```kotlin
@StateGenie()
interface UiState{
    @ToState(
        stateName = "Success"
    )
    val data: String

    @ToState(
        stateName = "Loading"
    )
    val loading: Unit

    @ToState(
        stateName = "Error"
    )
    val error: Throwable
}
```
#### Generated Code
```kotlin
public sealed interface `UiState$Generated` {
  public data class SuccessState(
    public final val `data`: String,
  ) : `UiState$Generated`

  public object LoadingState : `UiState$Generated`

  public data class ErrorState(
    public final val error: Throwable,
  ) : `UiState$Generated`
}
```


### Standalone State provided with `isParcelable`

#### Your Code
```kotlin
@StateGenie(isParcelable = true)
interface UiState{
    @ToState(
        stateName = "Success"
    )
    val data: String

    @ToState(
        stateName = "Loading"
    )
    val loading: Unit

    @ToState(
        stateName = "Error"
    )
    val error: Throwable
}
```
#### Generated Code 
```kotlin
public sealed interface `UiState$Generated` : Parcelable {
  @Parcelize
  public data class SuccessState(
    public final val `data`: String,
  ) : `UiState$Generated`

  @Parcelize
  public object LoadingState : `UiState$Generated`

  @Parcelize
  public data class ErrorState(
    public final val error: Throwable,
  ) : `UiState$Generated`
}
```


### Inherited State 

#### Your Code
```kotlin
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

@StateGenie(
    rootName = "MovieUiState",
)
interface MovieStates : BaseUIiState<List<String>>
```
#### Generated Code
```kotlin
public sealed interface MovieUiState {
  public data class SuccessState(
    public final val `data`: List<String>,
  ) : MovieUiState

  public object LoadingState : MovieUiState

  public data class ErrorState(
    public final val error: Throwable,
  ) : MovieUiState
}
```


### Inherited State with custom states

#### Your Code
```kotlin
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

@StateGenie(
    rootName = "NewUiSate",
    isParcelable = true
)
iinterface UiState : BaseUIiState<List<String>> {
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
```
#### Generated Code
```kotlin
public sealed interface NewUiSate : Parcelable {
  @Parcelize
  public object LoggedOutState : NewUiSate

  @Parcelize
  public data class FooState(
    public final val foo: Boolean,
  ) : NewUiSate

  @Parcelize
  public data class BarState(
    public final val bar: String,
  ) : NewUiSate

  @Parcelize
  public data class SuccessState(
    public final val `data`: List<String>,
  ) : NewUiSate

  @Parcelize
  public object LoadingState : NewUiSate

  @Parcelize
  public data class ErrorState(
    public final val error: Throwable,
  ) : NewUiSate
}
```
# License
```xml
Copyright 2023 Arindom Ghosh

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
