package com.example.musique.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
    
    private val prefs: SharedPreferences = 
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    companion object {
        private const val PREFS_NAME = "musique_prefs"
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USERNAME = "username"
        private const val KEY_DISPLAY_NAME = "display_name"
        private const val KEY_ROLE = "user_role"
    }
    
    fun saveAuthData(token: String, userId: String, username: String, displayName: String, role: String) {
        prefs.edit().apply {
            putString(KEY_TOKEN, token)
            putString(KEY_USER_ID, userId)
            putString(KEY_USERNAME, username)
            putString(KEY_DISPLAY_NAME, displayName)
            putString(KEY_ROLE, role)
            apply()
        }
    }
    
    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)
    
    fun getUserId(): String? = prefs.getString(KEY_USER_ID, null)
    
    fun getUsername(): String? = prefs.getString(KEY_USERNAME, null)
    
    fun getDisplayName(): String? = prefs.getString(KEY_DISPLAY_NAME, null)
    
    fun getRole(): String? = prefs.getString(KEY_ROLE, null)
    
    fun isLoggedIn(): Boolean = getToken() != null
    
    fun clearAuthData() {
        prefs.edit().apply {
            remove(KEY_TOKEN)
            remove(KEY_USER_ID)
            remove(KEY_USERNAME)
            remove(KEY_DISPLAY_NAME)
            remove(KEY_ROLE)
            apply()
        }
    }
}
