# Musique - Android Music Streaming App

A complete Android music streaming application built with Kotlin, featuring authentication, song browsing, search functionality, and audio playback.

## Features Implemented

### 🔐 Authentication
- User login and registration
- JWT token-based authentication
- Secure token storage using SharedPreferences
- Auto-login if token exists

### 🎵 Music Features
- **Home Screen**: Display top songs by play count
- **Search**: Real-time song search with debouncing
- **Music Player**: ExoPlayer-based audio streaming with:
  - Play/Pause controls
  - Background playback service
  - Foreground notification
  - Song metadata display
- **Profile**: User information and logout

### 🏗️ Architecture
- **MVVM Pattern**: ViewModels, LiveData, Repository pattern
- **Networking**: Retrofit with OkHttp for API calls
- **Image Loading**: Glide for album artwork
- **Navigation**: Jetpack Navigation Component with Bottom Navigation
- **Async**: Coroutines for background operations

### 📦 Project Structure
```
app/src/main/java/com/example/musique/
├── data/
│   ├── model/          # Data models (Song, User, Genre, Playlist)
│   ├── network/        # Retrofit API service & interceptors
│   └── repository/     # Repository layer
├── ui/
│   ├── activities/     # Login, Register activities
│   ├── adapter/        # RecyclerView adapters
│   ├── fragments/      # Home, Search, Profile fragments
│   └── viewmodel/      # ViewModels
├── player/             # ExoPlayer music service & manager
└── utils/              # Utilities (PreferenceManager, Extensions)
```

## API Configuration

The app connects to the backend at `http://10.0.2.2:5000/api/` (Android emulator localhost).

To change the backend URL, edit:
```kotlin
// RetrofitClient.kt
private const val BASE_URL = "http://10.0.2.2:5000/api/"
```

For physical devices, replace `10.0.2.2` with your computer's IP address.

## Permissions

- `INTERNET` - Network access
- `ACCESS_NETWORK_STATE` - Check network status
- `FOREGROUND_SERVICE` - Background music playback
- `FOREGROUND_SERVICE_MEDIA_PLAYBACK` - Media playback service
- `POST_NOTIFICATIONS` - Playback notifications (Android 13+)

## Building & Running

1. Open project in Android Studio
2. Sync Gradle files
3. Ensure backend server is running on port 5000
4. Run on emulator or physical device
5. Register a new account or login with existing credentials

## Dependencies

- **Retrofit** - REST API communication
- **OkHttp** - HTTP client with logging
- **Gson** - JSON serialization
- **Coroutines** - Asynchronous operations
- **Lifecycle** - ViewModels and LiveData
- **Navigation** - Fragment navigation
- **Media3** - ExoPlayer for audio playback
- **Glide** - Image loading
- **Material Components** - UI components

## Screenshots Functionality

### Login/Register Flow
- Validates input fields
- Shows loading states
- Displays error messages
- Navigates to main app on success

### Home Screen
- Displays top 10 songs
- Clickable song items
- Play button on each song
- Loading and error states

### Search Screen
- Search bar with real-time filtering
- Debounced search (500ms delay)
- Displays all matching songs
- Same playback functionality

### Profile Screen
- Shows username and display name
- Logout button
- Clears authentication and returns to login

### Music Playback
- Streams audio from backend
- Background service for continuous playback
- Notification with song info
- Play count tracking

## Future Enhancements

- Playlist management UI
- Favorite songs functionality
- Genre browsing
- Song details screen
- Mini player bar
- Queue management
- Offline caching
- Admin features (song upload)
