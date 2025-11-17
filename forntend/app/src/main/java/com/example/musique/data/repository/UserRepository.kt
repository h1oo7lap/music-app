package com.example.musique.data.repository

import com.example.musique.data.model.User
import com.example.musique.data.network.ApiService
import com.example.musique.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val apiService: ApiService) {
    
    suspend fun getUserProfile(): Resource<User> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getUserProfile()
                if (response.isSuccessful && response.body() != null) {
                    Resource.Success(response.body()!!)
                } else {
                    Resource.Error(response.message() ?: "Failed to fetch profile")
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Network error")
            }
        }
    }
    
    suspend fun toggleFavorite(songId: String): Resource<User> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.toggleFavorite(songId)
                if (response.isSuccessful && response.body() != null) {
                    Resource.Success(response.body()!!)
                } else {
                    Resource.Error(response.message() ?: "Failed to update favorites")
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Network error")
            }
        }
    }
}
