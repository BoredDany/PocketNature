package com.example.pocketnature.signups

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pocketnature.R
import com.example.pocketnature.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        binding.login.setOnClickListener {
            when(binding.accType.selectedItem.toString()){
                "Turista" -> { startActivity(Intent(this, SignUpTouristActivity::class.java))}
                "Biomonitor" -> { startActivity(Intent(this, SignUpBiomonitorActivity::class.java))}
                "Negocio" -> { startActivity(Intent(this, SignUpBusinessActivity::class.java)) }
            }
        }
    }
}