package com.example.pocketnature.nature

import android.os.Bundle
import android.view.MenuItem
import com.example.pocketnature.databinding.ActivityAvistamientosBinding
import com.example.pocketnature.navigation.DrawerMenuController

class AvistamientosActivity : DrawerMenuController() {
    private lateinit var binding: ActivityAvistamientosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAvistamientosBinding.inflate(layoutInflater)
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