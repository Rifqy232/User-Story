package com.mry.userstory.di

import android.content.Context
import com.mry.userstory.data.Repository
import com.mry.userstory.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService(context)
        return Repository.getInstance(apiService)
    }
}