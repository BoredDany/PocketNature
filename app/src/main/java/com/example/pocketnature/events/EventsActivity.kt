package com.example.pocketnature.events

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pocketnature.R
import com.example.pocketnature.databinding.ActivityAvistamientosBinding
import com.example.pocketnature.databinding.ActivityEventsBinding

class EventsActivity : AppCompatActivity() {
    lateinit var binding: ActivityEventsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}