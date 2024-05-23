package com.example.pocketnature.signups

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pocketnature.R
import com.example.pocketnature.databinding.ActivitySignUpBusinessBinding
import com.example.pocketnature.databinding.ActivitySignUpTouristBinding

class SignUpBusinessActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBusinessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBusinessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.login.setOnClickListener { startActivity(Intent(this, SignUpBusinessDetailActivity::class.java)) }
    }
}