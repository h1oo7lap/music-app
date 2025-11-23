// app/java/com/h1oo7/musicapp/network/ApiService.java
package com.h1oo7.musicapp.network;

import androidx.annotation.Nullable;

import com.h1oo7.musicapp.model.*;
import com.h1oo7.musicapp.model.request.AddRemoveSongRequest;
import com.h1oo7.musicapp.model.request.CreatePlaylistRequest;
import com.h1oo7.musicapp.model.request.RegisterRequest;
import com.h1oo7.musicapp.model.request.ToggleFavoriteRequest;
import com.h1oo7.musicapp.model.response.FavoriteResponse;
import com.h1oo7.musicapp.model.response.GenericResponse;
import com.h1oo7.musicapp.model.response.LoginResponse;
import com.h1oo7.musicapp.model.response.PlaylistResponse;
import com.h1oo7.musicapp.model.response.RegisterResponse;
import com.h1oo7.musicapp.model.response.SongResponse;

import retrofit2.Call;
import retrofit2.http.*;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import java.util.List;

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
            @Part("genre") RequestBody genre,
            @Part MultipartBody.Part songFile,
            @Nullable @Part MultipartBody.Part albumImage
    );

    @Multipart
    @PUT("api/songs/{id}")
    Call<Song> updateSong(
            @Path("id") String id,
            @Part("title") RequestBody title,
            @Part("artist") RequestBody artist,
            @Part("genre") RequestBody genre,
            @Nullable @Part MultipartBody.Part songFile,
            @Nullable @Part MultipartBody.Part albumImage
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
    // === PLAYLIST ===
    @POST("api/playlists")
    Call<PlaylistResponse> createPlaylist(@Body CreatePlaylistRequest request);

    @GET("api/playlists/my")
    Call<List<Playlist>> getMyPlaylists();

    // XÓA PLAYLIST – CHỈ TRẢ VỀ MESSAGE → DÙNG Void HOẶC GenericResponse
    @HTTP(method = "DELETE", path = "api/playlists/{id}", hasBody = true)
    Call<GenericResponse> deletePlaylist(@Path("id") String playlistId);

    @PUT("api/playlists/add")
    Call<PlaylistResponse> addSongToPlaylist(@Body AddRemoveSongRequest request);

    @PUT("api/playlists/remove")
    Call<PlaylistResponse> removeSongFromPlaylist(@Body AddRemoveSongRequest request);

    // === YÊU THÍCH ===
    @PUT("api/users/favorites")
    Call<FavoriteResponse> toggleFavorite(@Body ToggleFavoriteRequest request);

    @GET("api/users/favorites")
    Call<List<Song>> getFavoriteSongs();

}