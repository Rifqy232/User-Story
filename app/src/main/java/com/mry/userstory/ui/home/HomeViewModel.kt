package com.mry.userstory.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mry.userstory.data.CustomResult
import com.mry.userstory.data.Repository
import com.mry.userstory.data.response.StoriesResponse

class HomeViewModel(private val repository: Repository) : ViewModel() {
    private var _stories = MutableLiveData<CustomResult<StoriesResponse>>()
    val stories: LiveData<CustomResult<StoriesResponse>>
        get() = _stories

    fun getAllStories() {
        val tempStories = repository.getStories()
        _stories = tempStories as MutableLiveData<CustomResult<StoriesResponse>>
    }
}