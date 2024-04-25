package com.example.pocketnature.places

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pocketnature.R
import com.example.pocketnature.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }
}