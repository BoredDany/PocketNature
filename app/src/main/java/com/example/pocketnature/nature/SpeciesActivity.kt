package com.example.pocketnature.nature

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pocketnature.R
import com.example.pocketnature.databinding.ActivityAvistamientosBinding
import com.example.pocketnature.databinding.ActivitySpeciesBinding

class SpeciesActivity : AppCompatActivity() {
    lateinit var binding: ActivitySpeciesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpeciesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}