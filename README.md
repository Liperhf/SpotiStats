# 🎵 SpotiStats

**SpotiStats** — современное Android-приложение для анализа статистики прослушивания Spotify с использованием Clean Architecture и Jetpack Compose.

## 📱 Скриншоты

<!-- Вставьте сюда скриншоты вашего приложения -->
<div align="center">
  <img src="screenshots/main_screen.png" width="250" alt="Главный экран">
  <img src="screenshots/stats_screen.png" width="250" alt="Экран статистики">
  <img src="screenshots/profile_screen.png" width="250" alt="Профиль пользователя">
</div>

<div align="center">
  <img src="screenshots/auth_screen.png" width="250" alt="Авторизация">
  <img src="screenshots/settings_screen.png" width="250" alt="Настройки">
  <img src="screenshots/recently_played.png" width="250" alt="Недавно прослушанное">
</div>

## ✨ Основные возможности

- 📊 **Детальная статистика** — просмотр топ треков и исполнителей за разные периоды
- 🎨 **Современный UI** — Material Design 3 с Jetpack Compose
- 🔐 **Безопасная авторизация** — интеграция с Spotify Web API
- 📱 **Адаптивный дизайн** — оптимизация для разных размеров экранов
- 🌐 **Многоязычность** — поддержка нескольких языков
- 💾 **Локальное кэширование** — работа с данными офлайн
- 🎵 **История прослушиваний** — отслеживание недавно воспроизведенных треков

## 🏗️ Архитектура

Проект построен с использованием **Clean Architecture** и разделен на три модуля:

```
📦 SpotiStats
├── 📱 app (Presentation Layer)
│   ├── UI Components (Jetpack Compose)
│   ├── ViewModels
│   ├── Navigation
│   └── DI Modules
├── 📊 data (Data Layer)
│   ├── Repository Implementations
│   ├── API Services (Retrofit)
│   ├── Local Database (Room)
│   └── Data Sources
└── 🎯 domain (Domain Layer)
    ├── Use Cases
    ├── Repository Interfaces
    └── Domain Models
```

## 🛠️ Технологический стек

### **Frontend & UI**
- **Jetpack Compose** — современный декларативный UI toolkit
- **Material Design 3** — актуальные принципы дизайна Google
- **Navigation Compose** — навигация между экранами
- **Coil** — загрузка и кэширование изображений

### **Архитектура & DI**
- **Clean Architecture** — разделение ответственности по слоям
- **MVVM Pattern** — архитектурный паттерн для presentation слоя
- **Dagger Hilt** — dependency injection framework
- **Repository Pattern** — абстракция доступа к данным

### **Networking & Data**
- **Retrofit 2** — HTTP клиент для работы с REST API
- **OkHttp** — сетевой слой с логированием запросов
- **Gson** — сериализация/десериализация JSON
- **Room Database** — локальная база данных SQLite

### **Reactive Programming**
- **Kotlin Coroutines** — асинхронное программирование
- **StateFlow/SharedFlow** — reactive streams для UI состояний

### **Authentication & Security**
- **Spotify Android Auth SDK** — официальная авторизация Spotify
- **OAuth 2.0** — безопасная аутентификация

### **Additional Libraries**
- **UCrop** — обрезка изображений профиля
- **Accompanist** — дополнительные компоненты для Compose
- **SwipeRefresh** — pull-to-refresh функциональность

### **Testing**
- **JUnit 4** — unit тестирование
- **MockK** — мокирование для Kotlin
- **Mockito** — дополнительный фреймворк для моков
- **Coroutines Test** — тестирование асинхронного кода
- **Compose UI Test** — тестирование UI компонентов

## 🚀 Установка и запуск

### Предварительные требования

- Android Studio Hedgehog | 2023.1.1 или новее
- JDK 17
- Android SDK API 26+
- Spotify Developer Account

### Настройка проекта

1. **Клонируйте репозиторий:**
```bash
git clone https://github.com/yourusername/SpotiStats.git
cd SpotiStats
```

2. **Создайте Spotify App:**
   - Перейдите в [Spotify Developer Dashboard](https://developer.spotify.com/dashboard)
   - Создайте новое приложение
   - Добавьте redirect URI: `spotistats://callback`

3. **Настройте конфигурацию:**
   Создайте файл `local.properties` в корне проекта:
```properties
SPOTIFY_CLIENT_ID="your_spotify_client_id"
SPOTIFY_CLIENT_SECRET="your_spotify_client_secret"
SPOTIFY_REDIRECT_URI="spotistats://callback"
```

4. **Соберите и запустите проект:**
```bash
./gradlew assembleDebug
```

## 📋 API Requirements

Приложение использует следующие Spotify Web API endpoints:
- `GET /v1/me` — информация о пользователе
- `GET /v1/me/top/tracks` — топ треки пользователя
- `GET /v1/me/top/artists` — топ исполнители
- `GET /v1/me/player/recently-played` — недавно прослушанные треки

## 🔧 Конфигурация сборки

- **Minimum SDK:** API 26 (Android 8.0)
- **Target SDK:** API 35 (Android 15)
- **Compile SDK:** API 35
- **Java Version:** 17
- **Kotlin Version:** 2.0.0

## 📝 Лицензия

Этот проект создан в образовательных целях и демонстрирует современные подходы к разработке Android приложений.

## 👨‍💻 Автор

**Ваше имя** - Android Developer

- 📧 Email: your.email@example.com
- 💼 LinkedIn: [your-linkedin-profile](https://linkedin.com/in/your-profile)
- 🐙 GitHub: [@yourusername](https://github.com/yourusername)

---

<div align="center">
  <p>Создано с ❤️ используя современные Android технологии</p>
</div>
