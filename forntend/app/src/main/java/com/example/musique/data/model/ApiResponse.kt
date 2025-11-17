package com.example.musique.data.model

data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val displayName: String,
    val password: String
)

data class AuthResponse(
    val _id: String,
    val username: String,
    val displayName: String,
    val role: String,
    val token: String
)

data class SongsResponse(
    val songs: List<Song>,
    val page: Int,
    val limit: Int,
    val totalSongs: Int,
    val totalPages: Int
)

data class PlayCountResponse(
    val message: String,
    val playCount: Int
)

data class MessageResponse(
    val message: String
)
