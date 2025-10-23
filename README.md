# Currency List Demo

Android application demonstrating a currency list with search functionality, Room database operations, and MVVM architecture.

## Features

- Currency list display with search
- Room database operations (insert, query, clear)
- Search with name/symbol matching rules
- 5 action buttons: Clear DB, Insert Data, Load Crypto List, Load Fiat List, Display Purchasable

## Tech Stack

- Kotlin
- Jetpack Compose + Material Design 3
- MVVM + Repository pattern
- Room Database
- Dagger 2
- Coroutines & Flow
- JUnit + MockK

## Getting Started

### Prerequisites
- Android Studio Hedgehog+
- JDK 17
- Android SDK 24+

### Build & Run
```bash
./gradlew clean assembleDebug
```

### Run Tests
```bash
./gradlew test
```

## Project Structure

```
com.example.demo/
├── data/          # Room DAO, Database, Repository
├── domain/        # Models & Use Cases
├── di/            # Dagger 2 modules
└── ui/            # Compose screens & ViewModels
```

## Usage

1. Launch app
2. Click "Insert Data" to populate database
3. Load currency lists (Crypto/Fiat/Purchasable)
4. Use search to filter currencies
5. Clear database when needed
