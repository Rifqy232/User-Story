package com.mry.userstory.ui.detail

import androidx.lifecycle.ViewModel
import com.mry.userstory.data.Repository

class DetailViewModel(private val repository: Repository): ViewModel() {
    fun getDetailStory(id: String) = repository.getDetailStory(id)
}