package com.example.pocketnature.signups

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pocketnature.R
import com.example.pocketnature.databinding.ActivitySignUpBusinessBinding
import com.example.pocketnature.databinding.ActivitySignUpBusinessDetailBinding
import com.example.pocketnature.places.HomeActivity

class SignUpBusinessDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBusinessDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBusinessDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.login.setOnClickListener {startActivity(Intent(this, HomeActivity::class.java))}

    }
}