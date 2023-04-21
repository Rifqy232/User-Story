package com.mry.userstory.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.mry.userstory.data.response.LoginResponse
import com.mry.userstory.data.response.LoginResult
import com.mry.userstory.data.response.RegisterResponse
import com.mry.userstory.data.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(private val apiService: ApiService) {
    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<CustomResult<RegisterResponse>> = liveData {
        emit(CustomResult.Loading)
        try {
            val response = apiService.register(name, email, password)
            CustomResult.Success(response)
        } catch (e: Exception) {
            CustomResult.Error(e.message.toString())
        }
    }

    fun login(email: String, password: String): LiveData<CustomResult<LoginResult>> = liveData {
        emit(CustomResult.Loading)
        try {
            val response = apiService.login(email, password)
            CustomResult.Success(response.loginResult)
        } catch (e: Exception) {
            CustomResult.Error(e.message.toString())
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