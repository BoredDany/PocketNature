package com.example.pocketnature.signups

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pocketnature.R
import com.example.pocketnature.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
    }
}