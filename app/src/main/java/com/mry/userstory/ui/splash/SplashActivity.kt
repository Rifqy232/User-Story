package com.mry.userstory.ui.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.mry.userstory.R
import com.mry.userstory.ui.home.HomeActivity
import com.mry.userstory.ui.welcome.WelcomeActivity
import com.mry.userstory.utils.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        hideSystemUI()

        if (savedInstanceState == null) {
            val pref = UserPreferences(this)
            val token = pref.getUserToken()
            if (token.isNotEmpty()) {
                Handler(Looper.getMainLooper()).postDelayed({
                    val homeIntent = Intent(applicationContext, HomeActivity::class.java)
                    startActivity(homeIntent)
                    finish()
                }, 3000)
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    val welcomeIntent = Intent(applicationContext, WelcomeActivity::class.java)
                    startActivity(welcomeIntent)
                    finish()
                }, 3000)
            }
        }
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}