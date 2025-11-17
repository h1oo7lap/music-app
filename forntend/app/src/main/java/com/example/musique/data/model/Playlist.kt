package com.example.musique.data.model

data class Playlist(
    val _id: String,
    val name: String,
    val description: String? = null,
    val userId: String,
    val songs: List<Song> = emptyList(),
    val isPublic: Boolean = true,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
