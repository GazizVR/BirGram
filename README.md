<div align="center">

# 💬 BirGram

### Modern Telegram Client for Android

Built with **Kotlin**, **Jetpack Compose**, **Material 3**, and **TDLib**

![Android](https://img.shields.io/badge/Android-8.0%2B-3DDC84?style=for-the-badge&logo=android)
![Kotlin](https://img.shields.io/badge/Kotlin-2.x-7F52FF?style=for-the-badge&logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack-Compose-4285F4?style=for-the-badge)
![Material 3](https://img.shields.io/badge/Material-3-6750A4?style=for-the-badge)
![TDLib](https://img.shields.io/badge/TDLib-Telegram-26A5E4?style=for-the-badge&logo=telegram)

*A clean, fast, and modern Telegram client for Android.*

</div>

---

# ✨ About

**BirGram** is an open-source Telegram client for Android built with modern Android technologies.

The project focuses on:

- ⚡ High performance
- 🎨 Modern Material 3 UI
- 🧩 Modular architecture
- 🏛 Clean Architecture
- 🚀 Scalability
- 🛠 Easy maintenance

BirGram uses **Telegram TDLib** as its networking layer and is built entirely with **Jetpack Compose**.

---

# 📱 Features

## 💬 Messaging

- Telegram authentication
- Private chats
- Groups
- Supergroups
- Channels
- Chat list
- Message history
- Send text messages
- Image support
- File support

## 🔍 Search

- Fast local chat search
- Optimized filtering
- Instant search results

## 🎨 User Interface

- Material 3
- Jetpack Compose
- Dark Theme
- Splash Screen
- Adaptive layouts
- Smooth animations

---

# 🏗 Architecture

BirGram follows a modular architecture with a clear separation of responsibilities.

```text
Application
│
├── Core
│
├── Features
│   ├── Auth
│   ├── Chats
│   ├── Search
│   └── Splash
│
├── Telegram
│   ├── API
│   ├── Implementation
│   ├── Internal
│   ├── Mapper
│   └── Services
│
└── UI
```

### Design Principles

- 🧩 Feature-based modules
- 🏛 Clean Architecture
- 🔄 MVVM
- 🌊 Kotlin Flow
- ⚙ Kotlin Coroutines
- 📦 Dependency Injection
- 🔌 Service-oriented Telegram layer
- 🎯 Immutable UI state

---

# 🛠 Tech Stack

| Technology | Description |
|------------|-------------|
| Kotlin | Main programming language |
| Jetpack Compose | Declarative UI Toolkit |
| Material 3 | Modern Android Design |
| TDLib | Telegram Client Library |
| Kotlin Coroutines | Asynchronous programming |
| Kotlin Flow | Reactive data streams |
| AndroidX | Android Jetpack libraries |
| Navigation Compose | Navigation |
| MVVM | Presentation architecture |
| Gradle Version Catalog | Dependency management |

---

# 📂 Project Structure

```text
app/
core/

features/
├── auth
├── chats
├── search
└── splash

telegram/
├── api
├── impl
├── internal
├── mapper
└── services

ui/
```

---

# 🚀 Getting Started

## Requirements

- Android Studio Hedgehog or newer
- Android SDK
- JDK 17+
- Gradle 8+
- Android 8.0+

## Clone the Repository

```bash
git clone https://github.com/yourusername/BirGram.git
```

## Build

Debug build:

```bash
./gradlew assembleDebug
```

Release build:

```bash
./gradlew assembleRelease
```

---

# ⚡ Performance

> [!IMPORTANT]
> **Release builds provide the best performance.**

Debug builds are intended for development and include:

- Additional runtime checks
- Debug instrumentation
- Extensive logging
- Disabled compiler optimizations

As a result, **Debug APKs may experience**:

- Lower FPS
- Slower startup
- UI lag
- Reduced responsiveness

For the best user experience and accurate performance testing, always use a **Release APK**.

---

# 🗺 Roadmap

- 🎙 Voice messages
- 😀 Message reactions
- 📸 Stories
- 📞 Voice & video calls
- 🎭 Stickers
- 📁 Improved media viewer
- 📱 Tablet optimization
- ⌚ Wear OS support

---

# 🤝 Contributing

Contributions are welcome!

If you'd like to improve BirGram:

1. Fork the repository.
2. Create a feature branch.
3. Commit your changes.
4. Open a Pull Request.

---

# ❤️ Acknowledgements

Special thanks to:

- Telegram
- TDLib
- Jetpack Compose Team
- Android Open Source Community

---

# 📄 License

This project is licensed under the **MIT License**.

---

<div align="center">

### ⭐ Enjoying BirGram?

If you like this project, consider giving it a **star** on GitHub.

Made with ❤️ using Kotlin & Jetpack Compose.

</div>
