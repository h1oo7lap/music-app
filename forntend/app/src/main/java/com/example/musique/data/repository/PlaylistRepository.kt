package com.example.musique.data.repository

import com.example.musique.data.model.Playlist
import com.example.musique.data.network.ApiService
import com.example.musique.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaylistRepository(private val apiService: ApiService) {
    
    suspend fun getUserPlaylists(): Resource<List<Playlist>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getUserPlaylists()
                if (response.isSuccessful && response.body() != null) {
                    Resource.Success(response.body()!!)
                } else {
                    Resource.Error(response.message() ?: "Failed to fetch playlists")
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Network error")
            }
        }
    }
    
    suspend fun getPlaylistById(id: String): Resource<Playlist> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getPlaylistById(id)
                if (response.isSuccessful && response.body() != null) {
                    Resource.Success(response.body()!!)
                } else {
                    Resource.Error(response.message() ?: "Failed to fetch playlist")
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Network error")
            }
        }
    }
    
    suspend fun createPlaylist(name: String, description: String): Resource<Playlist> {
        return withContext(Dispatchers.IO) {
            try {
                val playlistData = mapOf("name" to name, "description" to description)
                val response = apiService.createPlaylist(playlistData)
                if (response.isSuccessful && response.body() != null) {
                    Resource.Success(response.body()!!)
                } else {
                    Resource.Error(response.message() ?: "Failed to create playlist")
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Network error")
            }
        }
    }
    
    suspend fun addSongToPlaylist(playlistId: String, songId: String): Resource<Playlist> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.addSongToPlaylist(playlistId, songId)
                if (response.isSuccessful && response.body() != null) {
                    Resource.Success(response.body()!!)
                } else {
                    Resource.Error(response.message() ?: "Failed to add song")
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Network error")
            }
        }
    }
    
    suspend fun deletePlaylist(id: String): Resource<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.deletePlaylist(id)
                if (response.isSuccessful) {
                    Resource.Success("Playlist deleted successfully")
                } else {
                    Resource.Error(response.message() ?: "Failed to delete playlist")
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Network error")
            }
        }
    }
}
