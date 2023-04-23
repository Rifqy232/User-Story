package com.mry.userstory.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mry.userstory.data.CustomResult
import com.mry.userstory.data.Repository
import com.mry.userstory.data.response.DetailStoryResponse

class DetailViewModel(private val repository: Repository): ViewModel() {
    private var _detailStory = MutableLiveData<CustomResult<DetailStoryResponse>>()
    val detailStory: LiveData<CustomResult<DetailStoryResponse>>
        get() = _detailStory
    fun getDetailStory(id: String) {
        val tempDetail = repository.getDetailStory(id)
        _detailStory = tempDetail as MutableLiveData<CustomResult<DetailStoryResponse>>
    }
}