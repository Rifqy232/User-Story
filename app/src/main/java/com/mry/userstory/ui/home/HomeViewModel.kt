package com.mry.userstory.ui.home

import androidx.lifecycle.ViewModel
import com.mry.userstory.data.Repository

class HomeViewModel(private val repository: Repository): ViewModel() {
    val stories = getAllStories()

    private fun getAllStories() = repository.getStories()
}