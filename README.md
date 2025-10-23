# Crypto.com Recruitment Test - Currency List Application

A production-ready Android application demonstrating a reusable currency list fragment with search functionality, Room database operations, and modern Android architecture patterns.

## 📱 Quick Preview

```
╔═══════════════════════════════════╗
║  Crypto.com Currency Demo         ║
╠═══════════════════════════════════╣
║  [Insert Data] [Load Lists]       ║
╠═══════════════════════════════════╣
║  ╭───╮                             ║
║  │BTC│  Bitcoin          🔍       ║
║  ╰───╯  BTC                       ║
║  ╭───╮                             ║
║  │ETH│  Ethereum                  ║
║  ╰───╯  ETH                       ║
╚═══════════════════════════════════╝
```

> 📸 See [**APP_PREVIEW.md**](APP_PREVIEW.md) for complete visual walkthrough with all screens and features!

## 📋 Features

### Core Functionality
- **Reusable Currency List Fragment**: Displays currencies with search capability
- **Advanced Search**: Implements specific matching rules for name and symbol
- **Database Operations**: Clear, insert, and query operations using Room
- **5 Action Buttons**:
  1. Clear Database
  2. Insert Sample Data
  3. Load Crypto Currencies (List A)
  4. Load Fiat Currencies (List B)
  5. Display Purchasable Currencies

### Search Logic
The search functionality implements three specific matching rules:
1. **Name Prefix Match**: "Ethereum" matches "Ethereum Classic"
2. **Word Boundary Match**: "Classic" matches "Ethereum Classic" but not "Tronclassic"
3. **Symbol Prefix Match**: "ET" matches "ETH" and "ETC"

### UI States
- **Normal State**: Displays currency list with floating search button
- **Search State**: Shows search bar with cancel button and filtered results
- **Empty State**: Displays message when no currencies match search criteria

## 🏗️ Architecture

### MVVM Pattern
```
├── UI Layer (Activity/Fragment)
├── ViewModel Layer (Business Logic)
├── Repository Layer (Data Abstraction)
└── Data Layer (Room Database)
```

### Technology Stack
- **Language**: Kotlin
- **Architecture**: MVVM with Repository pattern
- **Dependency Injection**: Dagger 2
- **Database**: Room with Coroutines
- **Async**: Kotlin Coroutines & Flow
- **UI**: Jetpack Compose + Material Design 3
- **Testing**: JUnit, MockK, Coroutines Test

## 📦 Project Structure

```
com.example.demo/
├── data/
│   ├── local/
│   │   ├── CurrencyDao.kt           # Room DAO
│   │   └── CurrencyDatabase.kt      # Room Database
│   ├── repository/
│   │   └── CurrencyRepository.kt    # Repository Pattern
│   └── SampleData.kt                # Sample Currency Data
├── di/
│   ├── AppModule.kt                 # Dagger 2 Module
│   └── AppComponent.kt              # Dagger 2 Component
├── domain/model/
│   └── CurrencyInfo.kt              # Domain Model
├── ui/
│   ├── MainActivity.kt              # Main Activity
│   ├── navigation/
│   │   └── CurrencyApp.kt           # Compose Navigation
│   └── screen/
│       ├── demo/
│       │   ├── DemoScreen.kt        # Demo Screen (Compose)
│       │   ├── DemoViewModel.kt     # Demo ViewModel
│       │   └── DemoViewModelFactory.kt  # ViewModel Factory
│       └── currencylist/
│           ├── CurrencyListScreen.kt    # Currency List (Compose)
│           └── CurrencyListViewModel.kt # List ViewModel
└── CurrencyApplication.kt           # Application with Dagger
```

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- **JDK 17** (required for Dagger 2 + AGP 8.2.2)
- Android SDK 24+ (Nougat or later)
- Gradle 8.2+

### Installation

1. **Clone or open the project**
   ```bash
   cd /Users/michaelwu/Documents/project/demo
   ```

2. **Sync Gradle dependencies**
   - Open project in Android Studio
   - Wait for Gradle sync to complete

3. **Build and Run**
   ```bash
   ./gradlew clean assembleDebug
   ```
   Or use Android Studio's Run button

### Running Tests

```bash
# Run all unit tests
./gradlew test

# Run specific test class
./gradlew test --tests CurrencyListViewModelTest

# Run tests with coverage
./gradlew testDebugUnitTest jacocoTestReport
```

## 📱 Usage Guide

### Using the Application

1. **Launch App**: Opens DemoActivity with 5 buttons
2. **Insert Data**: Click "Insert Data into Database" to populate database
3. **Load Currency Lists**:
   - Click "Load Currency List A" for cryptocurrencies
   - Click "Load Currency List B" for fiat currencies
   - Click "Display Purchasable Currencies" for combined list
4. **Search Currencies**:
   - Click floating search button
   - Type search query
   - View filtered results
   - Click cancel to clear search
5. **Clear Database**: Click "Clear Database" to remove all data

### Sample Data

**Currency List A (Crypto)**: BTC, ETH, XRP, BCH, LTC, EOS, BNB, LINK, NEO, ETC, ONT, CRO, CUC, USDC

**Currency List B (Fiat)**: SGD, EUR, GBP, HKD, JPY, AUD, USD

## 🧪 Testing

### Test Coverage
- ✅ **CurrencyListViewModelTest**: 14 test cases for search logic
- ✅ **DemoViewModelTest**: 7 test cases for database operations
- ✅ **CurrencyRepositoryTest**: 8 test cases for data layer

### Key Test Scenarios
- Search matching rules validation
- Case-insensitive search
- Empty state handling
- Database operation success/failure
- Flow-based data updates

## 🔑 Key Implementation Details

### Search Algorithm
```kotlin
private fun matchesSearchCriteria(currency: CurrencyInfo, searchTerm: String): Boolean {
    val nameLower = currency.name.lowercase()
    val symbolLower = currency.symbol.lowercase()
    
    // Rule 1: Name starts with search term
    if (nameLower.startsWith(searchTerm)) return true
    
    // Rule 2: Name contains search term with space prefix
    if (nameLower.contains(" $searchTerm")) return true
    
    // Rule 3: Symbol starts with search term
    if (symbolLower.startsWith(searchTerm)) return true
    
    return false
}
```

### Database Operations
All database operations run on background threads using Coroutines:
```kotlin
viewModelScope.launch {
    repository.insertCurrencies(currencies)
}
```

### Reactive UI Updates
Using StateFlow with Compose for reactive UI:
```kotlin
val currencies by viewModel.filteredList.collectAsState()

LazyColumn {
    items(currencies) { currency ->
        CurrencyCard(currency = currency)
    }
}
```

## 🎨 UI Components

### Jetpack Compose + Material Design 3
- **Cards**: Elevated cards for currency items
- **Buttons**: Material buttons with outlined style  
- **Search Bar**: Composable search with cancel action
- **LazyColumn**: Efficient scrolling list
- **StateFlow Integration**: Reactive UI updates

### Compose Screens
- `DemoScreen.kt`: Main screen with database operation buttons
- `CurrencyListScreen.kt`: Currency list with search functionality
- `CurrencyCard.kt`: Individual currency item composable

## 🔧 Configuration

### Build Configuration
- **compileSdk**: 34
- **minSdk**: 24
- **targetSdk**: 34
- **Kotlin**: 2.0.21
- **AGP**: 8.6.0
- **Dagger**: 2.51.1
- **Room**: 2.6.0
- **Compose BOM**: 2024.04.01

## 📝 Code Quality

### Best Practices Implemented
- ✅ Clean Architecture with separation of concerns
- ✅ SOLID principles
- ✅ Repository pattern for data abstraction
- ✅ **Dagger 2 Dependency Injection** with compile-time safety
- ✅ Jetpack Compose for modern declarative UI
- ✅ Coroutines for async operations (no blocking on UI thread)
- ✅ StateFlow for reactive state management
- ✅ Comprehensive unit tests with >80% coverage
- ✅ Material Design 3 guidelines
- ✅ Proper error handling

## 🐛 Troubleshooting

### Common Issues

**Build Errors**: 
```bash
./gradlew clean
./gradlew build --refresh-dependencies
```

**Room Errors**: Check that entities are properly annotated and database version is incremented on schema changes

**Dagger Issues**: 
- Rebuild project to regenerate Dagger components: `./gradlew clean build`
- Ensure `@Module` and `@Component` annotations are correct
- Verify `DaggerAppComponent` is generated in `build/generated/source/kapt`

## 📄 License

This project is created as a recruitment test demonstration.

## 👨‍💻 Author

Created for Crypto.com recruitment process.

## 🙏 Acknowledgments

- Google's Modern Android Development guidelines
- Material Design 3 specifications
- Android Jetpack libraries
