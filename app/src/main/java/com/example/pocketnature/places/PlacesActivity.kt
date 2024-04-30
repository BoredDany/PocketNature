package com.example.pocketnature.places

import android.os.Bundle
import android.view.MenuItem
import com.example.pocketnature.databinding.ActivityPlacesBinding
import com.example.pocketnature.utils.DrawerMenuController

class PlacesActivity : DrawerMenuController() {
    lateinit var binding: ActivityPlacesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlacesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return false
    }
}