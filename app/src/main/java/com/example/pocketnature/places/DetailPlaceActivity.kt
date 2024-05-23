package com.example.pocketnature.places

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.pocketnature.R
import com.example.pocketnature.databinding.ActivityDetailPlaceBinding
import com.example.pocketnature.utils.DrawerMenuController

class DetailPlaceActivity : DrawerMenuController() {
    lateinit var binding: ActivityDetailPlaceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDrawer(binding.drawerLayout, binding.navView)

        val imgPlace = binding.imgPlace
        val imageViewWidth = imgPlace.width
        val imageViewHeight = imgPlace.height
        Glide.with(this)
            .load("https://tierracolombiana.org/wp-content/uploads/2018/05/3.jpg")
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