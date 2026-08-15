<div align="center">

# 💬 BirGram

### A Telegram Client for Android, built from scratch

Built with **Kotlin**, **Jetpack Compose**, **Material 3**, **Hilt**, and **TDLib**

![Android](https://img.shields.io/badge/Android-9.0%2B-3DDC84?style=for-the-badge&logo=android)
![Kotlin](https://img.shields.io/badge/Kotlin-2.4-7F52FF?style=for-the-badge&logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack-Compose-4285F4?style=for-the-badge)
![Material 3](https://img.shields.io/badge/Material-3-6750A4?style=for-the-badge)
![TDLib](https://img.shields.io/badge/TDLib-Telegram-26A5E4?style=for-the-badge&logo=telegram)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

</div>

---

## ✨ About

**BirGram** is an open-source Telegram client for Android, written entirely in Kotlin with Jetpack Compose. It talks to Telegram through **TDLib** (the official Telegram Database Library), which is bundled as a precompiled native library rather than fetched from Maven.

The project is a personal/learning build focused on:

- 🏛 A clean, modular architecture (separate Gradle modules for the app, the Telegram service layer, and the raw TDLib bindings)
- 🔄 MVVM + unidirectional UI state with Kotlin Flow
- 🎨 A hand-built Material 3 UI, including a custom vector icon set
- 📦 Dependency injection with Hilt

> **Status:** BirGram is under active development. Core flows like login, browsing chats, and reading messages work; some capabilities (see [Features](#-features) below) are still partial.

---

## 📱 Features

### 🔐 Authentication
- Phone-number login via TDLib
- Verification via Telegram message, SMS, call, or flash/missed call, with automatic resend flow
- Two-factor (cloud password) login support
- Log out

### 💬 Chats & Messaging
- Private chats, basic groups, supergroups, and channels
- Chat list with unread counters, online status, drafts, and last-message previews
- Archived chats
- Paginated message history
- **Sending:** plain text messages, with per-chat draft saving
- **Viewing:** rich rendering and download of photos, videos, voice & video messages, documents, and stickers, with in-app playback for audio/video via Media3 (ExoPlayer)

### 🔍 Search
- Fast local (on-device) chat search and filtering

### 🎨 UI/UX
- Material 3 theming with light/dark mode (persisted via DataStore)
- Custom-drawn icon set (not just default Material icons)
- Animated splash screen and screen transitions
- Type-safe navigation (Navigation Compose + Kotlin Serialization routes)

### 🧭 Not yet implemented
- Sending media (photos/files/voice messages)
- Reactions, stickers picker, calls, stories
- These are tracked in the [Roadmap](#-roadmap) below.

---

## 🏗 Architecture

BirGram is split into three Gradle modules, plus feature packages inside the app module:

```text
BirGram
│
├── :app                    → UI, navigation, DI wiring, feature screens
│   └── org.gaziz.birgram
│       ├── core/
│       │   ├── datastore/      # user preferences (theme, etc.) via DataStore
│       │   ├── navigation/     # NavHost + type-safe routes
│       │   └── ui/             # shared theme, components, icons, mappers
│       └── features/
│           ├── auth/           # phone number → code → 2FA password
│           ├── splash/         # startup / auth-state routing
│           ├── chatList/       # chat list + archive
│           ├── chat/           # message list, input bar, media previews
│           └── searchChats/    # local chat search
│
├── :core:telegram          → org.gaziz.telegram
│   ├── api/                    # service interfaces + domain models
│   ├── impl/                   # service implementations
│   ├── internal/               # TDLib client manager, update dispatcher
│   │   ├── mapper/              # TdApi → domain model mapping
│   │   └── updaters/             # TDLib update → state reducers
│   └── di/                     # Hilt modules exposing each service
│
└── :core:tdlib              → org.gaziz.tdlib
    └── Vendored TDLib JNI bindings (TdApi.java, Client.java)
        + prebuilt native libraries (armeabi-v7a, arm64-v8a, x86, x86_64)
```

### Design principles

- 🧩 Feature-based package structure
- 🔄 MVVM with unidirectional state
- 🌊 Kotlin Flow + Coroutines throughout
- 🔌 A dedicated Telegram service layer (`AuthService`, `ChatService`, `MessageService`, `UserService`, `GroupService`, `FileService`, `ErrorService`) so the UI never talks to TDLib directly
- 📦 Hilt for dependency injection, KSP for annotation processing

---

## 🛠 Tech Stack

| Technology | Notes |
|------------|-------|
| Kotlin 2.4 | Application language (100% Kotlin in app code) |
| Jetpack Compose | UI toolkit, with Compose BOM `2026.08.00` |
| Material 3 | Design system |
| TDLib | Telegram protocol implementation, vendored as prebuilt `.so` + Java bindings |
| Hilt 2.60 | Dependency injection |
| Kotlin Coroutines & Flow | Async & reactive state |
| Navigation Compose 2.9 | Type-safe navigation with Kotlin Serialization routes |
| DataStore Preferences | Persisting user settings (e.g. dark mode) |
| Coil 3 | Image, GIF, and video thumbnail loading |
| Media3 / ExoPlayer | Audio & video playback |
| Lottie | Vector animations |
| AGP 9.2 / Gradle 9.5 | Build tooling |

---

## 🚀 Getting Started

### Requirements

- Android Studio (a recent version supporting AGP 9.2 and compileSdk 37)
- JDK 21 — the project pins its Gradle daemon toolchain to JDK 21 (JetBrains distribution) and will auto-provision it via the Foojay resolver if it isn't already installed
- Android SDK with platform 37 installed
- A physical device or emulator running **Android 9.0 (API 28) or newer**

### Get Telegram API credentials

BirGram needs its own Telegram API credentials to talk to TDLib:

1. Register an application at [my.telegram.org](https://my.telegram.org) to obtain an **`api_id`** and **`api_hash`**.
2. In the project root, create (or edit) a `local.properties` file — this file is git-ignored and never committed — and add:

   ```properties
   api_id=YOUR_API_ID
   api_hash=YOUR_API_HASH
   ```

   These values are wired into `BuildConfig.API_ID` / `BuildConfig.API_HASH` by the `:core:telegram` module and are required for the app to build.

### Clone the repository

```bash
git clone https://github.com/yourusername/BirGram.git
cd BirGram
```

### Build

Debug build:

```bash
./gradlew assembleDebug
```

Release build:

```bash
./gradlew assembleRelease
```

> TDLib's native libraries are already bundled for `armeabi-v7a`, `arm64-v8a`, `x86`, and `x86_64` — no separate native build step is required.

---

## ⚡ Performance

> [!IMPORTANT]
> **Release builds provide the best performance.**

Debug builds are intended for development and include extra runtime checks, debug instrumentation, extensive logging, and disabled compiler optimizations. As a result, debug APKs may show lower FPS, slower startup, and UI lag. For accurate performance testing, always use a **release APK**.

---

## 🗺 Roadmap

- 📤 Sending media (photos, files, voice messages)
- 😀 Message reactions
- 🎭 Sticker picker
- 📸 Stories
- 📞 Voice & video calls
- 📱 Tablet-optimized layouts
- ⌚ Wear OS support

---

## 🤝 Contributing

Contributions are welcome!

1. Fork the repository.
2. Create a feature branch.
3. Commit your changes.
4. Open a Pull Request.

---

## ❤️ Acknowledgements

- [Telegram](https://telegram.org) & [TDLib](https://github.com/tdlib/td)
- The Jetpack Compose team
- The Android open-source community

---

## 📄 License

This project is licensed under the **MIT License** — see [LICENSE](LICENSE) for details.

---

<div align="center">

### ⭐ Enjoying BirGram?

If you like this project, consider giving it a **star** on GitHub.

Made with ❤️ using Kotlin & Jetpack Compose.

</div>
