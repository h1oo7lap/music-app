package com.example.musique.data.network

import com.example.musique.utils.PreferenceManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.example.musique.utils.Constants


object RetrofitClient {
    
//    private const val BASE_URL = "http://10.0.2.2:5000/api/"
//    for real phone
    private const val BASE_URL = "http://192.168.0.103:5000/api/"


    private var retrofit: Retrofit? = null
    
    fun getClient(preferenceManager: PreferenceManager): Retrofit {
        if (retrofit == null) {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            
            val client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(preferenceManager))
                .addInterceptor(loggingInterceptor)
                .connectTimeout(Constants.NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(Constants.NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .build()
            
            retrofit = Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
    
    fun getApiService(preferenceManager: PreferenceManager): ApiService {
        return getClient(preferenceManager).create(ApiService::class.java)
    }
}
