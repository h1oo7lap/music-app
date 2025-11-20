// File: app/java/com/h1oo7/musicapp/network/RetrofitClient.java
package com.h1oo7.musicapp.network;

import com.h1oo7.musicapp.App;
import com.h1oo7.musicapp.utils.Constants;
import com.h1oo7.musicapp.utils.SharedPrefManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            // In log để debug
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Tự động thêm token vào header
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        String token = SharedPrefManager.getInstance(App.getContext()).getToken();

                        Request.Builder builder = original.newBuilder();
                        if (token != null) {
                            builder.addHeader("Authorization", "Bearer " + token);
                        }
                        builder.addHeader("Content-Type", "application/json");

                        return chain.proceed(builder.build());
                    })
                    .addInterceptor(logging)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}