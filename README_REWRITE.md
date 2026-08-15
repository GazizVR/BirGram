# BirGram — Android Telegram Client

<div align="center">

![Android](https://img.shields.io/badge/Android-8.0%2B-3DDC84?style=for-the-badge&logo=android)
![Kotlin](https://img.shields.io/badge/Kotlin-2.x-7F52FF?style=for-the-badge&logo=kotlin)
![TDLib](https://img.shields.io/badge/TDLib-Telegram-26A5E4?style=for-the-badge&logo=telegram)

A clean, modern, and open-source Telegram client for Android — built with Jetpack Compose and TDLib.
</div>

---

## About

BirGram is an open-source Android Telegram client implemented from scratch. It combines modern Android UI patterns with a modular, maintainable architecture and uses TDLib as the Telegram protocol implementation.

Note: the repository contains both Java and Kotlin code (predominantly Java with Kotlin components).

---

## Key Features

- Telegram authentication (via TDLib)
- 1:1 chats, groups, supergroups, and channels
- Message history, sending text, images and files
- Chat list with fast local search and filters
- Material 3-based UI with dark theme support
- Adaptive layouts and smooth Compose animations

Planned / roadmap items (examples):
- Voice messages and recordings
- Message reactions and improved media viewer
- Calls (voice & video), stickers, tablet & Wear OS optimizations

---

## Architecture Overview

BirGram follows a modular, feature-based structure and Clean Architecture principles:

- Feature modules (auth, chats, search, splash)
- Core shared utilities
- Telegram layer (TDLib API wrappers, mappers, services)
- UI implemented with Jetpack Compose
- Presentation with MVVM, Kotlin Flow, and Coroutines

Simple layout:

app/
core/
features/
  ├─ auth/
  ├─ chats/
  ├─ search/
  └─ splash/
telegram/
  ├─ api/
  ├─ impl/
  ├─ internal/
  ├─ mapper/
  └─ services/
ui/

---

## Tech Stack

- TDLib — Telegram client library (native)
- Java & Kotlin — application code (mix of both)
- Jetpack Compose & Material 3 — UI
- AndroidX libraries — platform support
- Kotlin Coroutines & Flow — async and reactive streams
- MVVM — presentation architecture
- Gradle (Version Catalog) — dependency management

---

## Getting Started

Prerequisites:
- Android Studio (Hedgehog or newer recommended)
- JDK 17+
- Android SDK with target API compatible with Android 8.0+ (minSdk 26+)
- Gradle 8+

Clone the repository:
```bash
git clone https://github.com/GazizVR/BirGram.git
cd BirGram
```

Build (debug):
```bash
./gradlew assembleDebug
```

Build (release):
```bash
./gradlew assembleRelease
```

Notes:
- TDLib requires native bindings; ensure the native TDLib binaries are available/packaged as part of the build if needed.
- Use a Release build for realistic performance measurements (debug builds include extra checks and logging).

---

## Running & Testing

- Open the project in Android Studio.
- Let Gradle sync and resolve dependencies.
- Configure an Android device or emulator (recommended: API 26+).
- Run the `app` module via the Run/Debug configurations.

If TDLib needs manual setup (native libs or specific build steps), consult the telegram/ or docs/ folder in the repo for instructions (or open an issue if instructions are missing).

---

## Contributing

Contributions are welcome — bug fixes, features, documentation, or tests.

1. Fork the repository.
2. Create a feature branch: git checkout -b feature/your-feature
3. Implement changes with tests where appropriate.
4. Commit and push: git push origin feature/your-feature
5. Open a Pull Request describing your changes.

Please follow the repository's code style and include a clear description of the change and rationale.

---

## Code of Conduct

Be respectful and constructive. Report unacceptable behavior by opening an issue or contacting the maintainers.

---

## Acknowledgements

- Telegram & TDLib
- Jetpack Compose and AndroidX teams
- Open-source Android community

---

## License

This project is licensed under the MIT License. See LICENSE for details.

---

Made with ❤ using Jetpack Compose, TDLib, and a mix of Java & Kotlin.
