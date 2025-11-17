package com.example.musique.data.repository

import com.example.musique.data.model.Genre
import com.example.musique.data.network.ApiService
import com.example.musique.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GenreRepository(private val apiService: ApiService) {
    
    suspend fun getGenres(): Resource<List<Genre>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getGenres()
                if (response.isSuccessful && response.body() != null) {
                    Resource.Success(response.body()!!)
                } else {
                    Resource.Error(response.message() ?: "Failed to fetch genres")
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Network error")
            }
        }
    }
}
