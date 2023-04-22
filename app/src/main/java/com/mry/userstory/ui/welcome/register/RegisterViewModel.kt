package com.mry.userstory.ui.welcome.register

import androidx.lifecycle.ViewModel
import com.mry.userstory.data.Repository

class RegisterViewModel(private val repository: Repository) : ViewModel() {
    fun register(name: String, email: String, password: String) =
        repository.register(name, email, password)
}