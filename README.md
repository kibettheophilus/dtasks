# dTasks
A simple task manager app

## Building and running the app.
- You will need android studio and an emulator or a physical device
- Clone the repository and import to android studio
- Hit the run icon 

## Starting the mock server.
- Run the following command:
```bash
chmod +x script.sh
./startserver.sh
```
---
**INFO**
- Installs json-server
- Runs the server
- Installs [ngrok](https://ngrok.com) based on OS
- Exposes local host via chrome
- sets the `baserUrl` in `gradle.properties`
---

## Architecture overview.
The app has been built using a MVVM architecture. 
```
UI -> ViewModel -> Repository -> sources(local and remote)
```
NB:
This was a simple app and did not need to be modularized.

### Screenshots


#### Libraries and tech stack
- [Kotlin](https://kotlinlang.org/) - programming language
- [Jetpack Compose](https://developer.android.com/compose) - Android’s recommended modern toolkit for building native UI.
- [Koin](https://github.com/google/hilt](https://insert-koin.io/)) - a pragmatic lightweight dependency injection framework for Kotlin & Kotlin Multiplatform.
- [Retrofit](https://square.github.io/retrofit/) - networking client framework
- [KotlinX Serialization](https://github.com/Kotlin/kotlinx.serialization) - Serialization/Deserialization of JSON response from network.
- [KtLint](https://github.com/pinterest/ktlint) - An anti-bikeshedding Kotlin linter with built-in formatter
- [Room](https://developer.android.com/kotlin/multiplatform/room) - persistence library providing an abstraction layer over SQLite
- [Datastore](https://developer.android.com/topic/libraries/architecture/datastore) - for key value pair storage

#### CI/CD
- [Github Actions](https://github.com/kibettheophilus/dtasks/tree/master/.github/workflows)
  - Run Tests
  - Run Lint Checks
  - Deploy to Playstore

## Conflict resolution strategy.
- I decided to go with the approach of using timestamps when doing a sync
- I have two workers:
   - Immediate -> triggered once during the login process
   - Periodic -> triggered on application setup and runs after every 15 minutes
  
## Running tests.
- Unit Tests
```bash
./gradlew testDebugUnitTest
```
- Instrumented Tests
```bash
./gradlew testDebugAndroidTest
```
## Kwown Issues & Improvements
- Pushing to server isn't working because `POST /sync` does not exist.
- Improve test coverage
- UI improvements