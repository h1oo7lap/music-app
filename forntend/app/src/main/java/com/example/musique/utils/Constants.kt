package com.example.musique.utils

object Constants {
    
    // ⚠️ CHANGE THIS BASED ON YOUR ENVIRONMENT ⚠️
    
    // For Android Emulator (localhost on host machine)
    // private const val BASE_URL_HOST = "http://10.0.2.2:5000"
    
    // For Real Device (replace with your computer's IP)
    private const val BASE_URL_HOST = "http://192.168.0.103:5000"
    
    // For Production (HTTPS)
    // private const val BASE_URL_HOST = "https://yourdomain.com"
    
    
    // API Configuration (DO NOT MODIFY BELOW)
    const val API_BASE_URL = "$BASE_URL_HOST/api/"
    const val MEDIA_BASE_URL = BASE_URL_HOST
    
    // API Endpoints
    const val ENDPOINT_LOGIN = "auth/login"
    const val ENDPOINT_REGISTER = "auth/register"
    const val ENDPOINT_SONGS = "songs"
    const val ENDPOINT_GENRES = "genres"
    const val ENDPOINT_PLAYLISTS = "playlists"
    const val ENDPOINT_USERS = "users"
    
    // Pagination
    const val DEFAULT_PAGE_SIZE = 20
    const val SEARCH_DEBOUNCE_MS = 500L
    
    // Timeouts
    const val NETWORK_TIMEOUT_SECONDS = 30L
    
    // SharedPreferences
    const val PREFS_NAME = "musique_prefs"
    const val KEY_TOKEN = "auth_token"
    const val KEY_USER_ID = "user_id"
}
