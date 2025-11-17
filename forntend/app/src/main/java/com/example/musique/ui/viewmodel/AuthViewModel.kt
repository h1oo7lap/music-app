package com.example.musique.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musique.data.model.AuthResponse
import com.example.musique.data.repository.AuthRepository
import com.example.musique.utils.Resource
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    
    private val _loginState = MutableLiveData<Resource<AuthResponse>>()
    val loginState: LiveData<Resource<AuthResponse>> = _loginState
    
    private val _registerState = MutableLiveData<Resource<AuthResponse>>()
    val registerState: LiveData<Resource<AuthResponse>> = _registerState
    
    fun login(username: String, password: String) {
        _loginState.value = Resource.Loading()
        viewModelScope.launch {
            val result = repository.login(username, password)
            _loginState.value = result
        }
    }
    
    fun register(username: String, displayName: String, password: String) {
        _registerState.value = Resource.Loading()
        viewModelScope.launch {
            val result = repository.register(username, displayName, password)
            _registerState.value = result
        }
    }
    
    fun logout() {
        repository.logout()
    }
    
    fun isLoggedIn(): Boolean = repository.isLoggedIn()
}
