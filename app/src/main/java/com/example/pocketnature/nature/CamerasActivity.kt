package com.example.pocketnature.nature

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pocketnature.R
import com.example.pocketnature.databinding.ActivityAvistamientosBinding
import com.example.pocketnature.databinding.ActivityCamerasBinding

class CamerasActivity : AppCompatActivity() {
    lateinit var binding: ActivityCamerasBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCamerasBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}