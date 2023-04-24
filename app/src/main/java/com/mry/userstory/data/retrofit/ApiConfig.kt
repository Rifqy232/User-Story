package com.mry.userstory.data.retrofit

import android.content.Context
import com.mry.userstory.utils.UserPreferences
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        fun getApiService(context: Context): ApiService {
            val userPreferences = UserPreferences(context)
            val interceptor = Interceptor { chain ->
                val token = userPreferences.getUserToken()
                val requestToken = "Bearer $token"
                val req = chain.request()
                val requestHeader = req.newBuilder()
                    .addHeader("Authorization", requestToken)
                    .build()
                chain.proceed(requestHeader)
            }

            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(interceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://story-api.dicoding.dev/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}