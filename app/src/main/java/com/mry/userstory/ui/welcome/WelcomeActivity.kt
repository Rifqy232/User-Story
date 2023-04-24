package com.mry.userstory.ui.welcome

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mry.userstory.R

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        if (savedInstanceState == null) {
            setupFragment()
        }
    }

    private fun setupFragment() {
        val welcomeFragment = WelcomeFragment()
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .add(R.id.frame_container, welcomeFragment, WelcomeFragment::class.java.simpleName)
            .commit()
    }
}