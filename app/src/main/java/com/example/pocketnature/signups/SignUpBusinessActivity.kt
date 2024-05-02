package com.example.pocketnature.signups

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pocketnature.R
import com.example.pocketnature.databinding.ActivitySignUpBusinessBinding

class SignUpBusinessActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBusinessBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_business)

        binding.login.setOnClickListener { startActivity(Intent(this, SignUpBusinessDetailActivity::class.java)) }
    }
}