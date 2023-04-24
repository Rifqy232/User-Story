package com.mry.userstory.utils

import android.content.Context

internal class UserPreferences(context: Context) {
    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val USER_TOKEN = "user_token"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val editor = preferences.edit()
    fun saveUserToken(token: String) {
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun getUserToken(): String = preferences.getString(USER_TOKEN, "").toString()

    fun removeUserToken() {
        editor.remove(USER_TOKEN)
        editor.apply()
    }
}