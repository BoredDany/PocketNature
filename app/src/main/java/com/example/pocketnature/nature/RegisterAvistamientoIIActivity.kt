package com.example.pocketnature.nature

import android.os.Bundle
import android.view.MenuItem
import com.example.pocketnature.databinding.ActivityRegisterAvistamientoIiactivityBinding
import com.example.pocketnature.utils.DrawerMenuController

class RegisterAvistamientoIIActivity : DrawerMenuController() {
    lateinit var binding: ActivityRegisterAvistamientoIiactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterAvistamientoIiactivityBinding.inflate(layoutInflater)
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