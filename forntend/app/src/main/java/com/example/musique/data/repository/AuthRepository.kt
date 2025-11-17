package com.example.musique.data.repository

import com.example.musique.data.model.AuthResponse
import com.example.musique.data.model.LoginRequest
import com.example.musique.data.model.RegisterRequest
import com.example.musique.data.network.ApiService
import com.example.musique.utils.PreferenceManager
import com.example.musique.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(
    private val apiService: ApiService,
    private val preferenceManager: PreferenceManager
) {
    
    suspend fun login(username: String, password: String): Resource<AuthResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.login(LoginRequest(username, password))
                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()!!
                    preferenceManager.saveAuthData(
                        authResponse.token,
                        authResponse._id,
                        authResponse.username,
                        authResponse.displayName,
                        authResponse.role
                    )
                    Resource.Success(authResponse)
                } else {
                    Resource.Error(response.message() ?: "Login failed")
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Network error")
            }
        }
    }
    
    suspend fun register(username: String, displayName: String, password: String): Resource<AuthResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.register(RegisterRequest(username, displayName, password))
                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()!!
                    preferenceManager.saveAuthData(
                        authResponse.token,
                        authResponse._id,
                        authResponse.username,
                        authResponse.displayName,
                        authResponse.role
                    )
                    Resource.Success(authResponse)
                } else {
                    Resource.Error(response.message() ?: "Registration failed")
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Network error")
            }
        }
    }
    
    fun logout() {
        preferenceManager.clearAuthData()
    }
    
    fun isLoggedIn(): Boolean = preferenceManager.isLoggedIn()
}
