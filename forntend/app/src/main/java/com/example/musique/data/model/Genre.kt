package com.example.musique.data.model

data class Genre(
    val _id: String,
    val name: String,
    val description: String? = null,
    val slug: String? = null,
    val imageUrl: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
