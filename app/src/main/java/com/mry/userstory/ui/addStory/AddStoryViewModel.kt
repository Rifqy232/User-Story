package com.mry.userstory.ui.addStory

import androidx.lifecycle.ViewModel
import com.mry.userstory.data.Repository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val repository: Repository) : ViewModel() {
    fun uploadStory(imageMultiPart: MultipartBody.Part, description: RequestBody) =
        repository.uploadStory(imageMultiPart, description)
}