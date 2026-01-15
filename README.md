# 🍽️ EatsBuddy

**Your Culinary Companion** - A modern Android recipe and meal planning app built with Kotlin and Jetpack Compose.

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)

---

## 📱 About

EatsBuddy is a recipe discovery and meal planning application developed as a final project for **Mobile Application Development 2** at the **Royal University of Phnom Penh (RUPP)**. The app helps users discover new recipes, plan their weekly meals, and manage grocery lists - all in one place.

## ✨ Features

### 🔐 Authentication
- User registration and login with Firebase Authentication
- Profile customization with name and profile picture
- Persistent sessions across app restarts

### 🍳 Recipe Discovery
- Browse hundreds of recipes from TheMealDB API
- Search recipes by name or keyword
- Filter by categories (Beef, Chicken, Seafood, Vegetarian, Dessert, etc.)
- Detailed recipe view with:
  - Ingredients list
  - Step-by-step instructions
  - Video tutorials (YouTube integration)
  - Nutritional information

### 📅 Meal Planning
- Weekly meal planner with day-by-day organization
- Add recipes directly to your meal plan
- Visual calendar interface
- Easy meal management

### 🛒 Grocery List
- Automatically add ingredients from recipes
- Check off items while shopping
- Organize by recipe source
- Sync across devices with Firebase

### ❤️ Favorites
- Save your favorite recipes for quick access
- View all favorites from your profile
- Synced with cloud storage

### 🎨 User Experience
- Modern Material Design 3 interface
- Dark mode / Light mode toggle
- Smooth animations and transitions
- Optimized for performance on all devices

## 🛠️ Technology Stack

| Category | Technology |
|----------|------------|
| **Language** | Kotlin |
| **UI Framework** | Jetpack Compose |
| **Design System** | Material Design 3 |
| **Architecture** | MVVM (Model-View-ViewModel) |
| **Networking** | Retrofit |
| **Image Loading** | Coil |
| **Authentication** | Firebase Auth |
| **Database** | Firebase Firestore |
| **Storage** | Firebase Cloud Storage |
| **Navigation** | Navigation Compose |
| **Async** | Kotlin Coroutines & Flow |
| **Local Storage** | SharedPreferences, DataStore |

## 📋 Requirements

- **Minimum SDK:** 24 (Android 7.0 Nougat)
- **Target SDK:** 35 (Android 15)
- **Compile SDK:** 36
- Android Studio Hedgehog or newer
- JDK 11+

## 🚀 Getting Started

### Prerequisites

1. [Android Studio](https://developer.android.com/studio) (latest version recommended)
2. A Firebase project with:
   - Authentication enabled (Email/Password)
   - Firestore Database
   - Cloud Storage

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/EatsBuddy.git
   cd EatsBuddy
   ```

2. **Set up Firebase**
   - Create a new project in [Firebase Console](https://console.firebase.google.com/)
   - Add an Android app with package name: `com.example.eatsbuddy`
   - Download `google-services.json` and place it in the `app/` directory
   - Enable Email/Password authentication
   - Create a Firestore database
   - Set up Cloud Storage

3. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory
   - Wait for Gradle sync to complete

4. **Run the app**
   - Connect an Android device or start an emulator
   - Click the "Run" button or press `Shift + F10`

## 📁 Project Structure

```
app/
├── src/main/
│   ├── java/com/example/eatsbuddy/
│   │   ├── data/
│   │   │   ├── api/          # Retrofit API services
│   │   │   ├── model/        # Data models
│   │   │   └── repository/   # Data repositories
│   │   ├── ui/
│   │   │   ├── components/   # Reusable UI components
│   │   │   ├── screens/      # App screens/pages
│   │   │   └── theme/        # Colors, typography, themes
│   │   ├── viewmodel/        # ViewModels for each feature
│   │   └── MainActivity.kt   # Main entry point
│   └── res/
│       ├── drawable/         # Icons and graphics
│       ├── values/           # Colors, strings, themes
│       └── ...
└── build.gradle.kts          # App-level dependencies
```

## 🌐 API Reference

This app uses [TheMealDB API](https://www.themealdb.com/api.php) for recipe data.

**Key Endpoints:**
- Search by name: `search.php?s={query}`
- Filter by category: `filter.php?c={category}`
- Get meal details: `lookup.php?i={id}`
- List categories: `categories.php`
- Random meal: `random.php`

## 👨‍💻 Team

| Name | Role | Contributions |
|------|------|---------------|
| **Tang Elite** | Lead Developer & UI/UX Designer | App architecture, UI design, Compose implementation |
| **Tang Piseth** | Backend Developer | API integration, Firebase setup, data management |

## 🎓 Academic Information

| | |
|---|---|
| **Institution** | Royal University of Phnom Penh (RUPP) |
| **Faculty** | Faculty of Engineering |
| **Department** | Information Technology Engineering (ITE) |
| **Course** | Mobile Application Development 2 |
| **Year** | Year 4 |
| **Semester** | 2025-2026 |

## 📄 License

This project is developed for educational purposes as part of our university coursework.

## 🙏 Acknowledgements

- [TheMealDB](https://www.themealdb.com/) for providing the free recipe API
- Our professor and teaching assistants at RUPP
- The Android and Kotlin communities for excellent documentation
- [Material Design](https://m3.material.io/) for design guidelines

---

<p align="center">
  Made by group 16
  <br>
  © 2025-2026 EatsBuddy Team
</p>
