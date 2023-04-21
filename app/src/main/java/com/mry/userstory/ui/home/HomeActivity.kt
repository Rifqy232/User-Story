package com.mry.userstory.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.mry.userstory.R
import com.mry.userstory.databinding.ActivityHomeBinding
import com.mry.userstory.ui.welcome.WelcomeActivity
import com.mry.userstory.utils.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

class HomeActivity : AppCompatActivity(), MenuItem.OnMenuItemClickListener {
    private lateinit var binding: ActivityHomeBinding
    private var pref: UserPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = UserPreferences.getInstance(dataStore)

        val homeFragment = HomeFragment()
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().apply {
            add(R.id.container, homeFragment, HomeFragment::class.java.simpleName)
            commit()
        }

        supportActionBar?.apply {
            title = "User Story"
            setBackgroundDrawable(ColorDrawable(Color.parseColor("#AEE2FF")))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)

        val optionLogout = menu?.findItem(R.id.option_logout)
        optionLogout?.setOnMenuItemClickListener(this)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        lifecycleScope.launch {
            when (item.itemId) {
                R.id.option_logout -> {
                    pref?.logout()
                    Toast.makeText(this@HomeActivity, "Logout Success", Toast.LENGTH_SHORT).show()
                    val welcomeIntent = Intent(applicationContext, WelcomeActivity::class.java)
                    startActivity(welcomeIntent)
                    finish()
                }
            }
        }

        return true
    }
}