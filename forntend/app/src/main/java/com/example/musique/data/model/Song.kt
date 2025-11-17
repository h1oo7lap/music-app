package com.example.musique.data.model

data class Song(
    val _id: String,
    val title: String,
    val artist: String,
    val genre: Genre,
    val duration: Int,
    val imageUrl: String?,
    val songUrl: String,
    val isPublic: Boolean = true,
    val playCount: Int = 0,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
