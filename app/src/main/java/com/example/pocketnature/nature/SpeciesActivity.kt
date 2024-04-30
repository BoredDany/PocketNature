package com.example.pocketnature.nature

import android.os.Bundle
import android.view.MenuItem
import com.example.pocketnature.databinding.ActivitySpeciesBinding
import com.example.pocketnature.utils.DrawerMenuController

class SpeciesActivity : DrawerMenuController() {
    lateinit var binding: ActivitySpeciesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpeciesBinding.inflate(layoutInflater)
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