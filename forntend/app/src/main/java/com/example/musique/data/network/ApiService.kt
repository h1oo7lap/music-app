package com.example.musique.data.network

import com.example.musique.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
    
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
    
    @GET("songs")
    suspend fun getSongs(
        @Query("keyword") keyword: String? = null,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): Response<SongsResponse>
    
    @GET("songs/top")
    suspend fun getTopSongs(@Query("limit") limit: Int = 10): Response<List<Song>>
    
    @GET("songs/{id}")
    suspend fun getSongById(@Path("id") id: String): Response<Song>
    
    @POST("songs/{id}/listen")
    suspend fun incrementPlayCount(@Path("id") id: String): Response<PlayCountResponse>
    
    @GET("genres")
    suspend fun getGenres(): Response<List<Genre>>
    
    @GET("genres/{id}")
    suspend fun getGenreById(@Path("id") id: String): Response<Genre>
    
    @GET("playlists")
    suspend fun getPlaylists(): Response<List<Playlist>>
    
    @GET("playlists/user")
    suspend fun getUserPlaylists(): Response<List<Playlist>>
    
    @GET("playlists/{id}")
    suspend fun getPlaylistById(@Path("id") id: String): Response<Playlist>
    
    @POST("playlists")
    suspend fun createPlaylist(@Body playlist: Map<String, String>): Response<Playlist>
    
    @PUT("playlists/{id}/songs/{songId}")
    suspend fun addSongToPlaylist(
        @Path("id") playlistId: String,
        @Path("songId") songId: String
    ): Response<Playlist>
    
    @DELETE("playlists/{id}/songs/{songId}")
    suspend fun removeSongFromPlaylist(
        @Path("id") playlistId: String,
        @Path("songId") songId: String
    ): Response<Playlist>
    
    @DELETE("playlists/{id}")
    suspend fun deletePlaylist(@Path("id") id: String): Response<MessageResponse>
    
    @GET("users/profile")
    suspend fun getUserProfile(): Response<User>
    
    @PUT("users/favorites/{songId}")
    suspend fun toggleFavorite(@Path("songId") songId: String): Response<User>
}
