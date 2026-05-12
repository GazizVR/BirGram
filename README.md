<div align="center">

<h1>
  <br>
  🐦 BirGram
</h1>

<p><strong>A modern, open-source Telegram client for Android — built with Kotlin & TDLib</strong></p>

[![Android](https://img.shields.io/badge/Platform-Android%2026%2B-3DDC84?style=flat-square&logo=android&logoColor=white)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin%202.3-7F52FF?style=flat-square&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white)](https://developer.android.com/compose)
[![TDLib](https://img.shields.io/badge/API-TDLib%201.8.61-2CA5E0?style=flat-square&logo=telegram&logoColor=white)](https://core.telegram.org/tdlib)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)](LICENSE)
[![Version](https://img.shields.io/badge/Version-1.0.0-orange?style=flat-square)](https://github.com/your-username/BirGram/releases)

<br>

</div>

---

## Overview

**BirGram** is a custom Android Telegram client that prioritizes a clean architecture and a native Material You experience. Instead of forking the official app, BirGram is built from the ground up on top of [TDLib](https://core.telegram.org/tdlib) — Telegram's own cross-platform library — giving it full access to the Telegram API while keeping the codebase lean, readable, and modern.

The project follows contemporary Android development best practices: a unidirectional data flow driven by `StateFlow`, a clear separation of concerns across data/domain/presentation layers, and a fully declarative UI written in Jetpack Compose.

---

## Tech Stack

| Layer | Technology |
|---|---|
| **Language** | Kotlin 2.3 |
| **UI** | Jetpack Compose + Material 3 |
| **Architecture** | MVVM with MVI-style unidirectional data flow |
| **Telegram API** | TDLib 1.8.61 (via `tdlib-android`) |
| **DI** | Hilt 2.59 |
| **Navigation** | Navigation Compose |
| **Image Loading** | Coil 3 (OkHttp backend) |
| **Persistence** | DataStore Preferences |
| **Build System** | Gradle with Version Catalog (`libs.versions.toml`) |
| **Min SDK** | Android 8.0 (API 26) |
| **Target SDK** | Android 16 (API 36) |

### Architecture Deep-Dive

BirGram uses a three-layer architecture:

```
presentation/      ← Compose screens + ViewModels (MVVM)
   auth/
   chatList/
   chat/
   searchChats/
domain/            ← Use cases, repository interfaces, domain models
   model/
   repository/
   usecase/
data/              ← TDLib integration, mappers
   remote/         ← TelegramManager, TelegramEventLoop
   mapper/         ← TdApi → domain model converters
```

The `TelegramEventLoop` acts as a central event bus: it creates the TDLib client, handles all incoming `TdApi` updates (new chats, messages, auth state changes, file downloads, etc.), and exposes reactive `StateFlow` streams that ViewModels collect from. This keeps TDLib concerns isolated from the rest of the app.

---

## Features

### ✅ Implemented
- **Full Authentication Flow** — phone number input → OTP code (SMS, Telegram message, call, Flash Call, Fragment, Firebase) → 2FA password
- **Chat List** — loads and displays all chats from the main list with real-time position updates driven by TDLib events
- **Chat View** — opens individual chats, loads message history, displays messages in a scrollable list
- **Message Sending** — text message input and sending via TDLib
- **Chat Search** — search across existing chats by query string
- **Live Updates** — unread counts, last message, draft messages, and online status update in real-time via the TDLib event loop
- **Avatar Loading** — chat photos downloaded on demand via TDLib file API and rendered with Coil
- **Chat Types** — supports Private, Basic Group, Supergroup, Channel, and Secret Chat types
- **User Online Status** — displays last seen / online status per chat

---

## How to Build

### Prerequisites

- Android Studio Meerkat (2024.3) or newer
- JDK 11+
- A Telegram API ID and API Hash — obtain them at [my.telegram.org](https://my.telegram.org)

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/GazizVR/BirGram.git
   cd BirGram
   ```

2. **Add your API credentials**

   Create a `local.properties` file in the project root (alongside `build.gradle.kts`) and add:
   ```properties
   sdk.dir=/path/to/your/Android/sdk
   api_id="YOUR_API_ID"
   api_hash="YOUR_API_HASH"
   ```
   > ⚠️ **Never commit `local.properties` to version control.** It is already listed in `.gitignore`.

3. **Build & run**

   Open the project in Android Studio and click **Run**, or use the command line:
   ```bash
   ./gradlew assembleDebug
   ```
   The APK will be at `app/build/outputs/apk/debug/app-debug.apk`.

---

## Roadmap

The following items represent the planned development trajectory, ordered from infrastructure foundations through to user-facing features.

- [ ] Implement comprehensive caching layer and background update processing
- [ ] Fix chat loading lag in the chat list and search screens
- [ ] Handle rich message content types in the message card (photos, videos, documents, stickers, etc.)
- [ ] Push notification system
- [ ] Support for additional chat list sources — archived chats and folder-based lists
- [ ] Discover new chats via QR code / invite link scanning

---

## Contributing

Contributions are welcome. Please open an issue first to discuss what you'd like to change, then submit a pull request against the `main` branch.

---

## License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

> BirGram is an independent project and is not affiliated with, endorsed by, or sponsored by Telegram Messenger LLP.
