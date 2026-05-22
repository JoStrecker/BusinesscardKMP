# Business Card - Kotlin Multiplatform

A simple digital business card application built with **Compose Multiplatform**. It allows users to create and display a personalized digital business card on both **Android** and **iOS**.

## Features

- **Personal Profile**: Display your name, job title, and profile picture.
- **Contact Actions**: Quick links to call, email, or visit GitHub and LinkedIn profiles.
- **QR Codes**: Generate QR codes for contact details to easily share them.
- **Data Persistence**: Settings are saved locally using DataStore.
- **Cross-Platform**: Shared UI and logic across Android and iOS.

## Project Structure

- `shared`: Contains the core logic and UI built with Compose Multiplatform.
    - `commonMain`: Shared UI (`App.kt`, `CardScreen.kt`, `SettingsScreen.kt`) and business logic.
    - `androidMain` & `iosMain`: Platform-specific implementations (e.g., image picking, phone calls).
- `androidApp`: Entry point for the Android application.
- `iosApp`: Entry point for the iOS application.

## Getting Started

### Prerequisites

- **Android Studio** or **IntelliJ IDEA** with the KMP plugin.
- **Xcode** (for running the iOS app).

### Running the App

#### Android
```bash
./gradlew :androidApp:run
```
Or use the run configuration in your IDE.

#### iOS
1. Open the `iosApp/iosApp.xcodeproj` in Xcode.
2. Select a simulator or physical device and click **Run**.

## License

May you do good and not evil.
May you find forgiveness for yourself and forgive others.
May you share freely, never taking more than you give.