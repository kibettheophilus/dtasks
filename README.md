# dTasks
A simple task manager app

## Building and running the app.
- You will need android studio and an emulator or a physical device
- Clone the repository and import to android studio
- Setup the baseUrl under `gradle.properties` (more on this below)
- Hit the run icon 

## Starting the mock server.
- Install mock server
```bash
npm install -g json-server
```
- Open the terminal and cd to `project/db` then run the following command
```bash
json-server --watch db.json --port 3000
```
The server will run at : `http://localhost:3000`
- Find a way to access this from your device, I used [ngrok](https://ngrok.com/) to forward the server.
  - Once you have ngrok installed, just run
  ```bash
  ngrok http 3000
  ```
- Add the baseUrl under `gradle.properties` as string
```
baseUrl=""
```

## Architecture overview.
The app has been built using a modularized layered architecture. The app has the following modules:
- **app** - entry point to application, contains navigation and overral di logic
- **details** - feature module to handle logic related to a single character details i.e has a viewmodel and composambles
- **list** - feature module to handle logic related to list of characters i.e has a viewmodel and composambles
- **data** - contains the implementation of selecting data between `local` and `network` modules.
- **local** - contains logic for saving data to local device storage
- **network** - contains logic for fetching data from network
- **build-logic** - not an application module but rather a configuration module for setting reausable configs across the project

NB:
This was a simple app and did not need to be modularized, I however decided to use this approach as a way to showcase
my knowledge of working in a modularized codebase.

### Screenshots
|                     List                     |                     Details                     |                                    
|:--------------------------------------------:|:-----------------------------------------------:|
| <img src="screenshots/list.png" width="200"> | <img src="screenshots/details.png" width="200"> | 

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
- 
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