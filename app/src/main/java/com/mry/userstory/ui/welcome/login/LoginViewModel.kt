package com.mry.userstory.ui.welcome.login

import androidx.lifecycle.ViewModel
import com.mry.userstory.data.UserRepository

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun login(email: String, password: String) = userRepository.login(email, password)
}