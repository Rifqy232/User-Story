package com.mry.userstory.data

sealed class CustomResult<out R> private constructor() {
    data class Success<out T>(val data: T) : CustomResult<T>()
    data class Error(val error: String) : CustomResult<Nothing>()
    data class Loading<out T>(val state: Boolean) : CustomResult<T>()
}