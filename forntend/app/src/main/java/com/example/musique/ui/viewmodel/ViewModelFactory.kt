package com.example.musique.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musique.data.repository.*

class ViewModelFactory(
    private val authRepository: AuthRepository? = null,
    private val songRepository: SongRepository? = null,
    private val genreRepository: GenreRepository? = null,
    private val playlistRepository: PlaylistRepository? = null,
    private val userRepository: UserRepository? = null
) : ViewModelProvider.Factory {
    
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(authRepository!!) as T
            }
            modelClass.isAssignableFrom(SongViewModel::class.java) -> {
                SongViewModel(songRepository!!) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
