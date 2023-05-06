package com.mry.userstory.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mry.userstory.data.Repository
import com.mry.userstory.data.response.ListStoryItem

class HomeViewModel(private val repository: Repository) : ViewModel() {
    val story: LiveData<PagingData<ListStoryItem>> = repository.getStories().cachedIn(viewModelScope)
}