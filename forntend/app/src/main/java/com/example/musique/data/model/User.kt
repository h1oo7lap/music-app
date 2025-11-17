package com.example.musique.data.model

data class User(
    val _id: String,
    val username: String,
    val displayName: String,
    val role: String,
    val favoriteSongs: List<String> = emptyList(),
    val createdAt: String? = null,
    val updatedAt: String? = null
)
