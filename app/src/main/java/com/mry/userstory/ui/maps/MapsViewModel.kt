package com.mry.userstory.ui.maps

import androidx.lifecycle.ViewModel
import com.mry.userstory.data.Repository

class MapsViewModel(private val repository: Repository): ViewModel() {
    fun getStoriesLocation(location: Int) = repository.getStoriesLocation(location)
}