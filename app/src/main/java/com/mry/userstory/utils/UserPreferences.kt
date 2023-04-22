package com.mry.userstory.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class UserPreferences(context: Context) {
    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val USER_TOKEN = "user_token"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val editor = preferences.edit()
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