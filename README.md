# dTasks

A modern, intuitive task management application built with Jetpack Compose.

## Features

- **Simple Task Management**: Create, edit, and delete tasks with ease
- **Task Filtering**: View all tasks, due tasks, or completed tasks
- **Offline Support**: Works seamlessly offline with automatic sync

## Screenshots

https://github.com/user-attachments/assets/1bd6e188-acc7-4f73-959d-57345f2cd85e

## Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- Android SDK with minimum API level 24
- JDK 11 or higher
- An Android device or emulator

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/dtasks.git
   cd dtasks
   ```

2. **Open in Android Studio**
  - Launch Android Studio
  - Select "Open an existing project"
  - Navigate to the cloned repository and select it

3. **Build and Run**
  - Wait for Gradle sync to complete
  - Click the "Run" button or press `Ctrl+R` (Windows/Linux) or `Cmd+R` (Mac)

## Mock Server Setup

To run the app with a local backend server:

1. **Start the mock server**
   ```bash
   chmod +x startserver.sh
   ./startserver.sh
   ```

2. **What the script does:**
  - Installs and configures `json-server`
  - Starts the local development server
  - Installs [ngrok](https://ngrok.com) based on your OS
  - Exposes localhost via secure tunnel
  - Automatically updates `baseUrl` in `gradle.properties`

3. **Manual server setup** (alternative):
   ```bash
   npm install -g json-server
   json-server --watch db.json --port 3000
   ```

## Architecture

This app follows **MVVM (Model-View-ViewModel)** architecture with Clean Architecture principles:

```
┌─────────────────┐    ┌──────────────┐    ┌─────────────────┐    ┌──────────────────┐
│   UI (Compose)  │───▶│  ViewModel   │───▶│   Repository    │───▶│  Data Sources    │
└─────────────────┘    └──────────────┘    └─────────────────┘    └──────────────────┘
                                                                   │  • Remote (API)  │
                                                                   │  • Local (Room)  │
                                                                   │  • Preferences   │
                                                                   └──────────────────┘
```

### Key Components

- **Presentation Layer**: Jetpack Compose UI + ViewModels
- **Domain Layer**: business logic
- **Data Layer**: Repositories, local database, and remote API
- **Dependency Injection**: Koin for managing dependencies

## Tech Stack

### Core Technologies

- **[Kotlin](https://kotlinlang.org/)** - Modern programming language for Android
- **[Jetpack Compose](https://developer.android.com/compose)** - Declarative UI toolkit

### Architecture & DI

- **[Koin](https://insert-koin.io/)** - Lightweight dependency injection
- **[Navigation Compose](https://developer.android.com/jetpack/compose/navigation)** - Type-safe
  navigation

### Networking & Serialization

- **[Retrofit](https://square.github.io/retrofit/)** - HTTP client for API calls
- **[KotlinX Serialization](https://github.com/Kotlin/kotlinx.serialization)** - JSON parsing
- **[OkHttp](https://square.github.io/okhttp/)** - HTTP client with interceptors

### Local Storage

- **[Room](https://developer.android.com/kotlin/multiplatform/room)** - SQLite abstraction layer
- **[DataStore](https://developer.android.com/topic/libraries/architecture/datastore)** - Key-value
  storage (preferences)

### Background Work

- **[WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)** -
  Background task scheduling

### Code Quality

- **[KtLint](https://github.com/pinterest/ktlint)** - Kotlin linter and formatter

## Data Synchronization

The app implements a robust conflict resolution strategy using timestamps:

### Sync Workers

- **Immediate Sync**: Triggered during login process
- **Periodic Sync**: Runs every 15 minutes in the background

### Conflict Resolution

- Uses timestamp-based conflict resolution
- Local changes take precedence when conflicts occur
- Ensures data consistency across devices

## Testing

### Running Tests

**Unit Tests**
```bash
./gradlew testDebugUnitTest
```

**Instrumented Tests**
```bash
./gradlew connectedDebugAndroidTest
```

**All Tests**

```bash
./gradlew test
```

### Test Coverage

```bash
./gradlew testDebugUnitTestCoverage
```

## CI/CD Pipeline

Automated workflows using [GitHub Actions](.github/workflows/):

- **Code Quality**: Runs KtLint
- **Testing**: Executes unit tests
- **Build**: Compiles and validates the app
- **Deploy**: Automated deployment to Google Play Store

## Project Structure

```
app/
├── src/main/java/com/theophiluskibet/dtasks/
│   ├── data/
│   │   ├── local/          # Room database, preferences
│   │   ├── remote/         # API services
│   │   └── repository/     # Repository implementations
│   ├── domain/
│   │   ├── models/         # Domain models
│   │   ├── repository/     # Repository interfaces
│   │   └── usecases/       # Business logic
│   ├── presentation/
│   │   ├── components/     # Reusable UI components
│   │   ├── navigation/     # Navigation setup
│   │   ├── screens/        # Screen composables
│   │   └── theme/          # App theming
│   └── di/                 # Dependency injection modules
```

## Known Issues & Future Improvements

### Current Issues

- [ ] Server sync endpoint (`POST /sync`)
- [ ] Limited offline capabilities for task creation
- [ ] Network error handling could be more robust

### Planned Improvements

- [ ] **Enhanced UI/UX**: Add animations and micro-interactions
- [ ] **Test Coverage**: Increase test coverage to 80%+
- [ ] **Performance**: Implement pagination for large task lists
- [ ] **Features**: Add task categories and labels
- [ ] **Accessibility**: Improve accessibility support
- [ ] **Dark Mode**: Complete dark theme implementation

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request
