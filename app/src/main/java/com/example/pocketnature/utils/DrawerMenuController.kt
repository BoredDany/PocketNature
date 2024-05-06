package com.example.pocketnature.utils

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.pocketnature.MainActivity
import com.example.pocketnature.R
import com.example.pocketnature.account.AccountActivity
import com.example.pocketnature.events.EventsActivity
import com.example.pocketnature.nature.AvistamientosActivity
import com.example.pocketnature.nature.CamerasActivity
import com.example.pocketnature.nature.SpeciesActivity
import com.example.pocketnature.nature.StatisticsActivity
import com.example.pocketnature.places.HomeActivity
import com.example.pocketnature.places.PlacesActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

open class DrawerMenuController: AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var toggle: ActionBarDrawerToggle

    fun setupDrawer(drawerLayoutA: DrawerLayout, navigationViewA: NavigationView) {
        drawerLayout = drawerLayoutA
        navigationView = navigationViewA

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.Home -> startActivity(Intent(this, HomeActivity::class.java))

                R.id.Sightings -> startActivity(Intent(this, AvistamientosActivity::class.java))

                R.id.Species -> startActivity(Intent(this, SpeciesActivity::class.java))

                R.id.Events -> startActivity(Intent(this, EventsActivity::class.java))

                R.id.Places -> startActivity(Intent(this, PlacesActivity::class.java))

                R.id.Cameras -> startActivity(Intent(this, CamerasActivity::class.java))

                R.id.Statistics -> startActivity(Intent(this, StatisticsActivity::class.java))

                R.id.Logout -> logoutUser()

                R.id.Account -> startActivity(Intent(this, AccountActivity::class.java))

            }
            true
        }
    }

    fun logoutUser() {
        val auth = FirebaseAuth.getInstance()
        auth.signOut()

        // Clear back stack to prevent navigation back to previous screens
        finishAffinity()  // Clear all activities in the task and exit app

        // Start the LoginActivity after logout
        startActivity(Intent(this, MainActivity::class.java)) // Replace with your actual login activity

        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
    }

}