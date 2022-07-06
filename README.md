# Project Architecture
### Communication between layers
1. UI calls methods from ViewModel.
2. ViewModel executes one or multiple Repository function.
3. The Repository returns data from one or multiple Data Sources (Reqres REST API, Local Shared Preferences). The repository is the single source of truth.
4. Information flows back to the UI where display the data fetched from data sources.

# Project Structure
* Data
    * Repository implementation class as well as the remote and local data sources and mappers
* UI
    * Views related code, packages are divided by feature.
* DI
    * Dependency injection and its configuration.
* Utils
    * Extension functions and helper files.

Project Tests
---------------
The project has Instrumented and Unit tests.

Libraries Used
---------------
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
* [LiveData](https://developer.android.com/jetpack/arch/livedata)
* [ViewBinding](https://developer.android.com/topic/libraries/view-binding/)
* [Coroutine](https://github.com/Kotlin/kotlinx.coroutines#user-content-android)
* [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/)
* [Retrofit](https://square.github.io/retrofit/)
* [Dagger Hilt](https://dagger.dev/hilt/)
* [Espresso](https://developer.android.com/training/testing/espresso/)
* [JUnit](https://junit.org/junit4/)
