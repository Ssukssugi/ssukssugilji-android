# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

SsukssukDiary is an Android plant care diary application built with Kotlin and Jetpack Compose. Users can track plants, write diaries, and manage their plant care routines. The app uses social login (Kakao, Naver, Google) and communicates with a backend API.

## Common Development Commands

### Building
```bash
./gradlew build                    # Build all modules
./gradlew assembleDebug            # Build debug APK
./gradlew assembleRelease          # Build release APK (requires keystore)
./gradlew clean                    # Clean build artifacts
```

### Testing
```bash
./gradlew test                     # Run all unit tests
./gradlew testDebugUnitTest        # Run debug unit tests
./gradlew connectedDebugAndroidTest # Run instrumented tests on device/emulator
```

### Installation
```bash
./gradlew installDebug             # Install debug build on connected device
./gradlew installRelease           # Install release build
./gradlew uninstallAll             # Uninstall all variants
```

### Code Quality
```bash
./gradlew lint                     # Run lint checks
./gradlew lintFix                  # Apply safe lint fixes
./gradlew check                    # Run all verification tasks
```

### Local Configuration

The project requires `local.properties` in the root directory with:
```properties
KAKAO_APP_KEY=your_kakao_app_key
NAVER_CLIENT_ID=your_naver_client_id
NAVER_CLIENT_SECRET=your_naver_client_secret
SIGNING_STORE_PASSWORD=your_keystore_password
SIGNING_KEY_ALIAS=your_key_alias
SIGNING_KEY_PASSWORD=your_key_password
```

## Architecture Overview

### Multi-Module Structure

The codebase follows clean architecture with strict module boundaries:

**Core Modules** (shared infrastructure):
- `core/model` - Domain models, enums, and interfaces (TokenProvider, CareType, LoginType)
- `core/navigator` - Navigation infrastructure with serializable RouteModel objects
- `core/network` - Retrofit client, interceptors, API services
- `core/data` - Repository layer, Result handling, error handlers
- `core/datastore` - Persistent storage (AuthDataStore for tokens, LocalDataStore for settings)
- `core/designsystem` - Compose UI components and design tokens

**Feature Modules** (isolated features):
- `feature/main` - Root navigation host and MainActivity
- `feature/login` - Social authentication
- `feature/signup` - Multi-step onboarding
- `feature/home` - Plant list and diary timeline
- `feature/diary` - Plant management and diary entries
- `feature/profile` - User settings and account management

**Dependency Rules**:
- Feature modules only depend on core modules
- Feature modules NEVER depend on other feature modules
- Navigation is injected via callbacks, not called directly
- This enables independent development and feature toggling

### Navigation Architecture

**Type-Safe Route-Based Navigation**:
- Routes are defined as `@Serializable` data classes in `core/navigator/model/RouteModels.kt`
- All routes implement the `RouteModel` interface
- Parameters are passed through route object construction (e.g., `DiaryWrite(imageUri: String)`)
- Uses Compose Navigation with serialization for type safety

**Two-Layer Navigation Pattern**:

1. **Local Navigation** - Handled within NavHost via `MainNavigator` wrapper
   - Extension functions like `navigateToHome()`, `navigateToDiaryWrite()`
   - Manages backstack behavior and NavOptions
   - Passed as callbacks to feature screens

2. **Global Navigation** - Handled via `NavigationEventHandler` (SharedFlow)
   - Used for authentication-triggered navigation (e.g., token expiration)
   - `AuthNavigationEventHandler` is a singleton injected with `@AuthNavigation` qualifier
   - `MainViewModel` subscribes to this flow and navigates to login on token expiration

**Navigation Flow**:
```
Feature Screen → Navigation Callback → MainNavigator → NavController
                                                     ↓
MainViewModel ← AuthNavigationEventHandler ← TokenExpirationHandler
```

### Data Layer

**Repository Pattern with Result Type**:
- Repositories expose `suspend` functions returning `Result<T>` (sealed: Success | Error)
- `handleResult()` wrapper catches exceptions and transforms to Result
- `Result.handle()` extension provides callback-based consumption (onSuccess, onError, onFinish)

**Data Flow**:
```
ViewModel → Repository → Result<T> → Service (Retrofit)
         ↓
    DataStore ← Transform DTOs to domain models
```

**Token Management**:
- Tokens stored in `AuthDataStore` (separate from user settings)
- `TokenInterceptor` adds tokens to all HTTP requests via Cookie headers
- Uses `runBlocking` in interceptor to synchronously fetch tokens (OkHttp constraint)
- Token refresh: Response with Set-Cookie → parse and update tokens asynchronously
- Token expiration: 401/403 with empty token → clear auth data → navigate to login

**Offline-First Pattern** (in ProfileRepository):
- Network request succeeds → cache locally
- Network error → return cached values
- No cached values → return sensible defaults

### Dependency Injection (Hilt)

**Module Organization**:
- `DataModule` - Binds repositories and TokenProvider
- `ApiModule` - Provides OkHttpClient, Retrofit, JSON configuration
- `NavigationModule` - Binds AuthNavigationEventHandler with `@AuthNavigation` qualifier
- `DataStoreModule` - Provides separate DataStore instances with qualifiers
- `ServiceModule` - Provides API services

**Scoping**:
- All repositories, network components, and DataStore instances are `@Singleton`
- ViewModels are `@HiltViewModel` with automatic scoping

### MVVM with Orbit MVI

Most features use **Orbit MVI** for unidirectional data flow:

```kotlin
class SomeViewModel @Inject constructor(...) : ContainerHost<UiState, SideEffect> {
    // State reduction
    intent { reduce { state.copy(isLoading = true) } }

    // Side effects (navigation, snackbars)
    intent { postSideEffect(SomeEvent.NavigateToDetail) }
}
```

**Key Patterns**:
- `reduce { }` for state updates
- `postSideEffect()` for one-time events (navigation, toasts)
- Sealed UiState classes with content states (Loading | Empty | Success)
- Intent functions for user actions

Some simpler ViewModels (e.g., LoginViewModel) use `MutableStateFlow` without Orbit.

### Convention Plugins

Custom Gradle plugins in `build-logic/` enforce consistency:

- `ssukssukdiary.android.library` - Base library with Hilt, coroutines, Kotlin config
- `ssukssukdiary.android.feature` - Extends library, adds Compose dependencies
- `ssukssukdiary.android.application` - Application-specific config
- `ssukssukdiary.android.compose` - Compose-specific configuration

**Usage in module build files**:
```kotlin
plugins {
    id("ssukssukdiary.android.feature")  // For feature modules
    // or
    id("ssukssukdiary.android.library")  // For core modules
}
```

## Key Architectural Decisions

### Serializable Route Objects
Unlike traditional navigation args (Bundle-based), routes are serializable objects. This provides type safety while keeping navigation decoupled from Android framework classes.

### Split DataStore Strategy
- `AuthDataStore` - Sensitive authentication data (tokens), cleared on logout
- `LocalDataStore` - User preferences (notification settings), persists across sessions
This allows independent management with different retention policies.

### Hybrid Navigation Approach
- Most navigation uses local callbacks (passed down through composables)
- Only critical auth events (token expiration) use global SharedFlow
- Avoids over-engineering while handling essential global navigation scenarios

### Token Expiration Handling
When tokens expire (401/403 with empty access token):
1. `TokenInterceptor` detects expiration
2. `TokenExpirationHandler` clears all datastores
3. `AuthNavigationEventHandler` emits navigation event
4. `MainViewModel` observes and navigates to login with cleared backstack
5. User sees login screen with fresh state

### Feature Module Independence
Features never directly call navigation or depend on other features. All inter-feature communication goes through:
- Navigation callbacks passed from MainNavHost
- Shared domain models in core/model
- Repositories in core/data

This enables parallel development and easy feature toggling.

## Working with Navigation

### Adding a New Route
1. Define route in `core/navigator/model/RouteModels.kt`:
```kotlin
@Serializable
data object MyNewRoute : RouteModel
```

2. Add navigation extension in `MainNavigator`:
```kotlin
fun NavController.navigateToMyNewRoute() {
    navigate(MyNewRoute)
}
```

3. Add NavGraph in feature module:
```kotlin
fun NavGraphBuilder.myNewNavGraph(onBack: () -> Unit) {
    composable<MyNewRoute> {
        MyNewScreen(onBack = onBack)
    }
}
```

4. Register in `MainNavHost`:
```kotlin
myNewNavGraph(onBack = navigator::navigateUp)
```

### Passing Parameters in Routes
Use data class properties (must be serializable):
```kotlin
@Serializable
data class DiaryDetail(val diaryId: Int) : RouteModel

// Usage
navigator.navigateToDiaryDetail(diaryId = 123)
```

## Working with Repositories

### Creating a New Repository
1. Define interface in `core/data/repository/`:
```kotlin
interface MyRepository {
    suspend fun fetchData(): Result<MyModel>
}
```

2. Implement in same package:
```kotlin
class DefaultMyRepository @Inject constructor(
    private val service: MyService
) : MyRepository {
    override suspend fun fetchData(): Result<MyModel> = handleResult {
        service.getData().toDomainModel()
    }
}
```

3. Bind in `DataModule`:
```kotlin
@Binds
abstract fun bindMyRepository(impl: DefaultMyRepository): MyRepository
```

### Using Result Type
```kotlin
repository.fetchData().handle(
    onSuccess = { data -> /* handle success */ },
    onError = { error -> /* handle error */ },
    onFinish = { /* cleanup */ }
)
```

## Working with ViewModels

### Orbit MVI Pattern
```kotlin
@HiltViewModel
class MyViewModel @Inject constructor(
    private val repository: MyRepository
) : ContainerHost<MyUiState, MyEvent>, ViewModel() {

    override val container = container<MyUiState, MyEvent>(MyUiState())

    fun loadData() = intent {
        reduce { state.copy(isLoading = true) }

        repository.fetchData().handle(
            onSuccess = { data ->
                reduce { state.copy(isLoading = false, data = data) }
            },
            onError = { error ->
                reduce { state.copy(isLoading = false) }
                postSideEffect(MyEvent.ShowError(error.message))
            }
        )
    }
}
```

## Testing Notes

- Unit tests are located in `src/test/` for each module
- Instrumented tests in `src/androidTest/`
- Test runner: `androidx.test.runner.AndroidJUnitRunner`
- Run module-specific tests: `./gradlew :module-name:test`
- Run single test class: `./gradlew :module-name:testDebugUnitTest --tests "com.sabo.package.TestClass"`

## Important Files

- `settings.gradle.kts` - Module configuration and repository setup (includes Kakao Maven)
- `build-logic/` - Custom Gradle convention plugins
- `core/navigator/model/RouteModels.kt` - All navigation routes
- `core/data/di/DataModule.kt` - Repository bindings
- `core/network/interceptor/TokenInterceptor.kt` - Token injection and refresh
- `core/data/handler/TokenExpirationHandler.kt` - Token expiration handling
- `feature/main/MainViewModel.kt` - Global auth navigation observer
- `feature/main/component/MainNavHost.kt` - Root navigation composition

## Third-Party Libraries

- Retrofit 3.0 - HTTP client with kotlinx-serialization
- Orbit MVI 10.0 - Unidirectional data flow
- Compose 2025.05 - UI toolkit
- Hilt 2.56 - Dependency injection
- DataStore 1.1 - Persistent storage
- Coil 3.2 - Image loading
- Firebase - Analytics and messaging
- Kakao/Naver/Google SDKs - Social authentication
