package com.mry.userstory.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.mry.userstory.data.response.LoginResponse
import com.mry.userstory.data.response.LoginResult
import com.mry.userstory.data.response.RegisterResponse
import com.mry.userstory.data.retrofit.ApiService

class UserRepository private constructor(private val apiService: ApiService) {
    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<CustomResult<RegisterResponse>> = liveData {
        emit(CustomResult.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(CustomResult.Success(response))
        } catch (e: Exception) {
            emit(CustomResult.Error(e.message.toString()))
        }
    }

    fun login(email: String, password: String): LiveData<CustomResult<LoginResponse>> = liveData {
        emit(CustomResult.Loading)
        try {
            val response = apiService.login(email, password)
            emit(CustomResult.Success(response))
        } catch (e: Exception) {
            emit(CustomResult.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService)
            }.also { instance = it }
    }
}