package com.mry.userstory.ui.welcome.login

import androidx.lifecycle.ViewModel
import com.mry.userstory.data.Repository

class LoginViewModel(private val repository: Repository) : ViewModel() {
    fun login(email: String, password: String) = repository.login(email, password)
}