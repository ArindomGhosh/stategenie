# StateGenie

StateGenie provides:
- `@StateGenie`: To generate `sealed interface` representing a defined set of states.
It gives you the flexibility of reusing a base model in different scenarios while also enabling you to add new
models over them based on your requirements reducing the repeated boiler plate code. Link to the [@StateGenie wiki](https://github.com/ArindomGhosh/stategenie/wiki/@StateGenie)
- `@DataStateGenie`: To generate a single `data class` representing UiState and extension functions to update individual parameters, 
loading state and error object of subtype of Throwable and default constructor function to get the default UiState. Link to the [@DataStateGenie wiki](https://github.com/ArindomGhosh/stategenie/wiki/@DataStateGenie)

### Gradle Setup
1. Enable KSP in your app or module `build.gradle.kts`:
```kotlin
plugins {
    id("com.google.devtools.ksp") version "1.8.0-1.0.9"
}
```

2. Add repository in repositories to resolve dependencies `settings.gradle.kts`:
```kotlin
dependencyResolutionManagement {
    repositories {
        maven {
            url = URI("https://arindomghosh.jfrog.io/artifactory/punkypumpkin")
        }
    }
}
```

3. Add dependencies in your app or module level `build.gradle.kts`:
```kotlin
dependencies{
  val stateGenieVersion = CHECK_THE_RELEASES
  implementation("com.arindom.stategenie:genie-annotations:$stateGenieVersion")
  ksp("com.arindom.stategenie:genie-processors:$stateGenieVersion")
}
```

4. Android 
- build.gradle.kts (app level)
```kotlin
kotlin {
  sourceSets.configureEach {
    kotlin.srcDir("$buildDir/generated/ksp/$name/kotlin/")
  }
}
```
- build.gradle
```groovy
android {
   applicationVariants.all { variant ->
      kotlin.sourceSets {
         def name = variant.name
         getByName(name) {
             kotlin.srcDir("build/generated/ksp/$name/kotlin")
         }
      }
   }
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
