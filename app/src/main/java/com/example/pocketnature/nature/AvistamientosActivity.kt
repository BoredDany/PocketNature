package com.example.pocketnature.nature

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.pocketnature.MainActivity
import com.google.android.material.navigation.NavigationView
import com.example.pocketnature.R
import com.example.pocketnature.databinding.ActivityAvistamientosBinding
import com.example.pocketnature.databinding.ActivityMainBinding
import com.example.pocketnature.nature.AvistamientosActivity
import com.example.pocketnature.places.HomeActivity

class AvistamientosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAvistamientosBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAvistamientosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Call findViewById on the DrawerLayout
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.myProfile -> startActivity(Intent(this, MainActivity::class.java))
            }
            true
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return false
    }


}