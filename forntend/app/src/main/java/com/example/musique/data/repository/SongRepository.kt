package com.example.musique.data.repository

import com.example.musique.data.model.Song
import com.example.musique.data.model.SongsResponse
import com.example.musique.data.network.ApiService
import com.example.musique.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SongRepository(private val apiService: ApiService) {
    
    suspend fun getSongs(keyword: String? = null, page: Int = 1, limit: Int = 10): Resource<SongsResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getSongs(keyword, page, limit)
                if (response.isSuccessful && response.body() != null) {
                    Resource.Success(response.body()!!)
                } else {
                    Resource.Error(response.message() ?: "Failed to fetch songs")
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Network error")
            }
        }
    }
    
    suspend fun getTopSongs(limit: Int = 10): Resource<List<Song>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getTopSongs(limit)
                if (response.isSuccessful && response.body() != null) {
                    Resource.Success(response.body()!!)
                } else {
                    Resource.Error(response.message() ?: "Failed to fetch top songs")
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Network error")
            }
        }
    }
    
    suspend fun getSongById(id: String): Resource<Song> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getSongById(id)
                if (response.isSuccessful && response.body() != null) {
                    Resource.Success(response.body()!!)
                } else {
                    Resource.Error(response.message() ?: "Failed to fetch song")
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Network error")
            }
        }
    }
    
    suspend fun incrementPlayCount(songId: String): Resource<Int> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.incrementPlayCount(songId)
                if (response.isSuccessful && response.body() != null) {
                    Resource.Success(response.body()!!.playCount)
                } else {
                    Resource.Error(response.message() ?: "Failed to update play count")
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Network error")
            }
        }
    }
}
