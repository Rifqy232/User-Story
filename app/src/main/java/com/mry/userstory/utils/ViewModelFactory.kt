package com.mry.userstory.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mry.userstory.di.Injection
import com.mry.userstory.ui.detail.DetailViewModel
import com.mry.userstory.ui.home.HomeViewModel
import com.mry.userstory.ui.welcome.login.LoginViewModel
import com.mry.userstory.ui.welcome.register.RegisterViewModel

class ViewModelFactory(private val context: Context) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                return RegisterViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                return LoginViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                return HomeViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                return DetailViewModel(Injection.provideRepository(context)) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}