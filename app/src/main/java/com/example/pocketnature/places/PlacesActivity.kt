package com.example.pocketnature.places

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pocketnature.R
import com.example.pocketnature.databinding.ActivityAvistamientosBinding
import com.example.pocketnature.databinding.ActivityPlacesBinding

class PlacesActivity : AppCompatActivity() {
    lateinit var binding: ActivityPlacesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlacesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}