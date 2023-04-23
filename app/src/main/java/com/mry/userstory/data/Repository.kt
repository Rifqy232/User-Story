package com.mry.userstory.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.mry.userstory.data.response.DetailStoryResponse
import com.mry.userstory.data.response.ListStoryItem
import com.mry.userstory.data.response.LoginResponse
import com.mry.userstory.data.response.LoginResult
import com.mry.userstory.data.response.RegisterResponse
import com.mry.userstory.data.response.StoriesResponse
import com.mry.userstory.data.retrofit.ApiService

class Repository private constructor(private val apiService: ApiService) {
    fun getDetailStory(id: String): LiveData<CustomResult<DetailStoryResponse>> = liveData {
        emit(CustomResult.Loading(true))
        try {
            val response = apiService.getDetailStory(id)
            emit(CustomResult.Success(response))
        } catch (e: Exception) {
            emit(CustomResult.Error(e.toString()))
        }
    }

    fun getStories(): LiveData<CustomResult<StoriesResponse>> = liveData {
        emit(CustomResult.Loading(true))
        try {
            val response = apiService.getStories()
            emit(CustomResult.Success(response))
        } catch (e: Exception) {
            emit(CustomResult.Error(e.message.toString()))
        }
    }

    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<CustomResult<RegisterResponse>> = liveData {
        emit(CustomResult.Loading(true))
        try {
            val response = apiService.register(name, email, password)
            emit(CustomResult.Success(response))
        } catch (e: Exception) {
            emit(CustomResult.Error(e.message.toString()))
        }
    }

    fun login(email: String, password: String): LiveData<CustomResult<LoginResponse>> = liveData {
        emit(CustomResult.Loading(true))
        try {
            val response = apiService.login(email, password)
            emit(CustomResult.Success(response))
        } catch (e: Exception) {
            emit(CustomResult.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService)
            }.also { instance = it }
    }
}