package com.mry.userstory.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mry.userstory.R
import com.mry.userstory.databinding.ActivityHomeBinding
import com.mry.userstory.ui.maps.MapsActivity
import com.mry.userstory.ui.welcome.WelcomeActivity
import com.mry.userstory.utils.UserPreferences

class HomeActivity : AppCompatActivity(), MenuItem.OnMenuItemClickListener {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            setupFragment()
        }

        supportActionBar?.apply {
            title = "User Story"
            elevation = 0f
        }
    }

    private fun setupFragment() {
        val homeFragment = HomeFragment()
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().apply {
            add(R.id.container, homeFragment, HomeFragment::class.java.simpleName)
            commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)

        val optionLogout = menu?.findItem(R.id.option_logout)
        optionLogout?.setOnMenuItemClickListener(this)

        val optionMaps = menu?.findItem(R.id.option_maps)
        optionMaps?.setOnMenuItemClickListener(this)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_logout -> {
                val userPreferences = UserPreferences(this)
                userPreferences.removeUserToken()
                Toast.makeText(this@HomeActivity, "Logout Success", Toast.LENGTH_SHORT).show()
                val welcomeIntent = Intent(applicationContext, WelcomeActivity::class.java)
                startActivity(welcomeIntent)
                finish()
            }
            R.id.option_maps -> {
                val mapsIntent = Intent(applicationContext, MapsActivity::class.java)
                startActivity(mapsIntent)
            }
        }
        return true
    }
}