package com.example.pocketnature.signups

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pocketnature.R
import com.example.pocketnature.databinding.ActivitySignUpBiomonitorBinding
import com.example.pocketnature.places.HomeActivity

class SignUpBiomonitorActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBiomonitorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_biomonitor)

        binding.login.setOnClickListener {startActivity(Intent(this, HomeActivity::class.java))}
    }
}