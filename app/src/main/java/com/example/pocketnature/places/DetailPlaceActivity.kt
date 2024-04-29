package com.example.pocketnature.places

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.pocketnature.R
import com.example.pocketnature.databinding.ActivityDetailPlaceBinding
import com.example.pocketnature.databinding.ActivityHomeBinding
import com.example.pocketnature.navigation.DrawerMenuController

class DetailPlaceActivity : DrawerMenuController() {
    lateinit var binding: ActivityDetailPlaceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDrawer(binding.drawerLayout, binding.navView)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return false
    }
}