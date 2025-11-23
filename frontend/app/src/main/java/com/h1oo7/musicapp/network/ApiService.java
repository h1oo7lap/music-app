// app/java/com/h1oo7/musicapp/network/ApiService.java
package com.h1oo7.musicapp.network;

import com.h1oo7.musicapp.model.*;
import retrofit2.Call;
import retrofit2.http.*;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import java.util.List;
import java.util.Map;

public interface ApiService {

    // Đăng nhập & đăng ký
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body RegisterRequest request);

    @POST("api/auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    // -----------------------------------------------------
    // Bài hát

    @GET("api/songs")
    Call<SongResponse> getAllSongs(
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("keyword") String keyword
    );

    // Lấy top N bài hát
    @GET("api/songs/top")
    Call<List<Song>> getTopSongs(@Query("limit") int limit);

    // Lấy một bài hát theo ID
    @GET("api/songs/{id}")
    Call<Song> getSongById(@Path("id") String songId);

    // Tăng lượt nghe của bài hát
    @POST("api/songs/{id}/listen")
    Call<Void> incrementListen(@Path("id") String songId);

    // Admin: Upload bài hát mới (Multipart: nhạc + ảnh + text)
    @Multipart
    @POST("api/songs")
    Call<Song> uploadSong(
            @Part("title") RequestBody title,
            @Part("artist") RequestBody artist,
            @Part("genre") RequestBody genreId,
            @Part MultipartBody.Part songFile,
            @Part MultipartBody.Part imageFile
    );

    // Admin: Cập nhật bài hát (PUT, Multipart)
    @Multipart
    @PUT("api/songs/{id}")
    Call<Song> updateSong(
            @Path("id") String songId,
            @Part("title") RequestBody title,
            @Part("artist") RequestBody artist,
            @Part("genre") RequestBody genreId,
            @Part MultipartBody.Part songFile,      // optional
            @Part MultipartBody.Part imageFile      // optional
    );

    // Admin: Xóa bài hát
    @DELETE("api/songs/{id}")
    Call<Void> deleteSong(@Path("id") String songId);

    // ---------------------------------------------
    // Thể loại

    // Lấy tất cả thể loại (dùng ở HomeFragment)
    @GET("api/genres")
    Call<List<Genre>> getAllGenres();

    // Lấy 1 thể loại theo ID (sau này dùng khi click vào thể loại)
    @GET("api/genres/{id}")
    Call<Genre> getGenreById(@Path("id") String genreId);

    // Tạo thể loại mới (Admin)
    @POST("api/genres")
    Call<Genre> createGenre(@Body Genre genre);

    // Cập nhật thể loại (Admin)
    @PUT("api/genres/{id}")
    Call<Genre> updateGenre(@Path("id") String genreId, @Body Genre genre);

    // Xóa thể loại (Admin)
    @DELETE("api/genres/{id}")
    Call<Void> deleteGenre(@Path("id") String genreId);

    // ------------------------------------------------------------------
    // Playlist
    // Playlist
    @GET("api/playlists/my")
    Call<List<Playlist>> getMyPlaylists(@Header("Authorization") String token);

    @POST("api/playlists")
    Call<Playlist> createPlaylist(
            @Header("Authorization") String token,
            @Body Map<String, Object> body // { "name": "Playlist 1", "isPublic": true }
    );

    @DELETE("api/playlists/{id}")
    Call<Void> deletePlaylist(
            @Header("Authorization") String token,
            @Path("id") String playlistId
    );

    @PUT("api/playlists/add")
    Call<Playlist> addSongToPlaylist(
            @Header("Authorization") String token,
            @Body Map<String, String> body // { "playlistId": "...", "songId": "..." }
    );

    @PUT("api/playlists/remove")
    Call<Playlist> removeSongFromPlaylist(
            @Header("Authorization") String token,
            @Body Map<String, String> body
    );


}