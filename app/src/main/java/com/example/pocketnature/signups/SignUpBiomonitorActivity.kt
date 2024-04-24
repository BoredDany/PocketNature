package com.example.pocketnature.signups

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pocketnature.R
import com.example.pocketnature.databinding.ActivitySignUpBiomonitorBinding

class SignUpBiomonitorActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBiomonitorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_biomonitor)
    }
}