package com.mry.userstory.di

import android.content.Context
import com.mry.userstory.data.UserRepository
import com.mry.userstory.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService)
    }
}