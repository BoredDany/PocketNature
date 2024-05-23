package com.example.pocketnature.nature

import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.example.pocketnature.databinding.ActivityDetailSpecieBinding
import com.example.pocketnature.utils.DrawerMenuController

class DetailSpecieActivity : DrawerMenuController() {
    lateinit var binding: ActivityDetailSpecieBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSpecieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDrawer(binding.drawerLayout, binding.navView)

        val imgPlace = binding.photo
        val imageViewWidth = imgPlace.width
        val imageViewHeight = imgPlace.height
        Glide.with(this)
            .load("https://powerpestcontrol.ca/wp-content/uploads/2017/03/EW10266_PowerPest_B1_3.29.2017_KV.jpg")
            .override(imageViewWidth, imageViewHeight) // Cargar la imagen con la resolución correcta
            .into(imgPlace)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return false
    }
}