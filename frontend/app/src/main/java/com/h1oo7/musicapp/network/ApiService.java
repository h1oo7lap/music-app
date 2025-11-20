// app/java/com/h1oo7/musicapp/network/ApiService.java
package com.h1oo7.musicapp.network;

import com.h1oo7.musicapp.model.*;
import retrofit2.Call;
import retrofit2.http.*;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import java.util.List;

public interface ApiService {

    // Đăng nhập & đăng ký (cùng kiểu trả về)
    // Chỉ sửa 2 dòng này thôi, còn lại giữ nguyên
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body RegisterRequest request);

    @POST("api/auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    // Lấy danh sách bài hát
//    @GET("api/songs")
//    Call<List<Song>> getAllSongs(
//            @Header("Authorization") String token
//    );

    @GET("api/songs")
    Call<SongResponse> getAllSongs();

    // Lấy top bài hát
    @GET("api/songs/top")
    Call<List<Song>> getTopSongs();

    // Tăng lượt nghe
    @POST("api/songs/{id}/listen")
    Call<Void> incrementListen(@Path("id") String songId);

    // Admin: Upload bài hát mới
    @Multipart
    @POST("api/songs")
    Call<Song> uploadSong(
            @Part("title") RequestBody title,
            @Part("artist") RequestBody artist,
            @Part("genre") RequestBody genreId,
            @Part("duration") RequestBody duration,
            @Part MultipartBody.Part songFile,
            @Part MultipartBody.Part imageFile
    );

    // Admin: Xóa bài hát
    @DELETE("api/songs/{id}")
    Call<Void> deleteSong(@Path("id") String songId);

    // Lấy danh sách thể loại (dùng khi upload)
    @GET("api/genres")
    Call<List<Genre>> getGenres();
}