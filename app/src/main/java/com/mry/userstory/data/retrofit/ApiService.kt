package com.mry.userstory.data.retrofit

import com.mry.userstory.data.response.DetailStoryResponse
import com.mry.userstory.data.response.ListStoryItem
import com.mry.userstory.data.response.LoginResponse
import com.mry.userstory.data.response.RegisterResponse
import com.mry.userstory.data.response.StoriesResponse
import com.mry.userstory.data.response.StoryUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoriesResponse

    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Path("id") id: String
    ): DetailStoryResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): StoryUploadResponse

    @GET("stories")
    suspend fun getStoriesLocation(
        @Query("location") location: Int,
    ): StoriesResponse
}