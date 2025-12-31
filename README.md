# 🎓 VocaPop - Cross-Platform Language Learning App

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-2.0-purple?logo=kotlin" alt="Kotlin"/>
  <img src="https://img.shields.io/badge/Jetpack_Compose-1.7-blue?logo=android" alt="Compose"/>
  <img src="https://img.shields.io/badge/SwiftUI-5.0-orange?logo=swift" alt="SwiftUI"/>
  <img src="https://img.shields.io/badge/Laravel-11-red?logo=laravel" alt="Laravel"/>
  <img src="https://img.shields.io/badge/License-MIT-green" alt="MIT License"/>
</p>

VocaPop is a modern, **spaced-repetition flashcard app** for learning vocabulary in any language. Built with **Kotlin Multiplatform (KMP)** for shared business logic, native **Jetpack Compose** (Android) and **SwiftUI** (iOS) UIs, and a **Laravel** REST API backend.

## ✨ Features

- 🧠 **Spaced Repetition (SM-2)** - Science-backed algorithm for long-term memory retention
- 📱 **Cross-Platform** - Shared Kotlin code for Android & iOS with native UI
- 🎨 **Beautiful UI** - Modern, animated flashcard experience with 3D flip effects
- 🌍 **Multi-Language** - Support for learning French, Spanish, Japanese & more
- 📊 **Progress Tracking** - Daily goals, streaks, and mastery statistics
- 🔊 **Text-to-Speech** - Native TTS integration for pronunciation
- 💾 **Offline-First** - Full local database with SQLDelight

## 📁 Project Structure

```
VocaPop/
├── shared/                 # Kotlin Multiplatform shared module
│   ├── commonMain/        # Shared business logic, models, SRS algorithm
│   ├── androidMain/       # Android-specific implementations (DB driver)
│   └── iosMain/           # iOS-specific implementations
├── androidApp/            # Android app (Jetpack Compose)
├── iosApp/                # iOS app (SwiftUI)
└── backend/               # Laravel REST API
```

## 🛠 Tech Stack

| Layer | Technology |
|-------|------------|
| **Shared Logic** | Kotlin Multiplatform, Coroutines, Koin DI |
| **Local Database** | SQLDelight |
| **Networking** | Ktor Client |
| **Android UI** | Jetpack Compose, Material 3 |
| **iOS UI** | SwiftUI |
| **Backend** | Laravel 11, MySQL, Sanctum Auth |

## 🚀 Getting Started

### Prerequisites

- **Android**: Android Studio Hedgehog+ | JDK 17+
- **iOS**: Xcode 15+ | macOS
- **Backend**: PHP 8.2+ | Composer | MySQL

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/VocaPop.git
cd VocaPop
```

### 2. Android App

1. Open the project in **Android Studio**
2. Let Gradle sync complete
3. Select a device/emulator and click **Run** ▶️

### 3. iOS App (macOS only)

1. Open `iosApp/iosApp.xcodeproj` in **Xcode**
2. Build and run on a Simulator or device

### 4. Backend Setup (Optional)

```bash
cd backend
composer install
cp .env.example .env
php artisan key:generate
# Configure DB credentials in .env
php artisan migrate --seed
php artisan serve --host=0.0.0.0
```

> **Note**: To connect Android emulator to local backend, use `10.0.2.2` as the host IP.

## 🧪 Algorithm: Spaced Repetition (SM-2)

VocaPop implements a modified **SM-2 algorithm** for optimal learning:

- **Ease Factor**: Adjusts based on answer quality (Again/Hard/Good/Easy)
- **Interval Calculation**: Cards reappear at scientifically optimal times
- **Minimum Intervals**: Prevents overwhelming users with too many reviews

```kotlin
// Core formula
newInterval = previousInterval * easeFactor
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

Built with ❤️ by **Shubham Karande**

---

<p align="center">
  <b>Pop a Card. Learn a Word. Get Fluent.</b>
</p>
