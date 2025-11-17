package com.example.musique.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musique.data.model.Song
import com.example.musique.data.model.SongsResponse
import com.example.musique.data.repository.SongRepository
import com.example.musique.utils.Resource
import kotlinx.coroutines.launch

class SongViewModel(private val repository: SongRepository) : ViewModel() {
    
    private val _songsState = MutableLiveData<Resource<SongsResponse>>()
    val songsState: LiveData<Resource<SongsResponse>> = _songsState
    
    private val _topSongsState = MutableLiveData<Resource<List<Song>>>()
    val topSongsState: LiveData<Resource<List<Song>>> = _topSongsState
    
    private val _currentSong = MutableLiveData<Song?>()
    val currentSong: LiveData<Song?> = _currentSong
    
    fun getSongs(keyword: String? = null, page: Int = 1, limit: Int = 20) {
        _songsState.value = Resource.Loading()
        viewModelScope.launch {
            val result = repository.getSongs(keyword, page, limit)
            _songsState.value = result
        }
    }
    
    fun getTopSongs(limit: Int = 10) {
        _topSongsState.value = Resource.Loading()
        viewModelScope.launch {
            val result = repository.getTopSongs(limit)
            _topSongsState.value = result
        }
    }
    
    fun setCurrentSong(song: Song) {
        _currentSong.value = song
    }
    
    fun incrementPlayCount(songId: String) {
        viewModelScope.launch {
            repository.incrementPlayCount(songId)
        }
    }
}
