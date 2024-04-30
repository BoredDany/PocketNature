package com.example.pocketnature.places

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.example.pocketnature.databinding.ActivityHomeBinding
import com.example.pocketnature.model.Place
import com.example.pocketnature.model.Price
import com.example.pocketnature.utils.DrawerMenuController

class HomeActivity : DrawerMenuController() {
    lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDrawer(binding.drawerLayout, binding.navView)

        //INITIALIZE CONTENT
        initializePlaces()

        //SET WEATHER
        setWeather()

    }

    //INITIALIZE CONTENT
    private fun initializePlaces(){
        val places = List(10) {
            Place(
                name = "name",
                company = "company",
                linkPlace = "linkPlace",
                photo = "photoUrl",
                latitude = 0.0,
                longitude = 0.0,
                description = "description",
                categories = listOf("category1", "category2"),
                rating = 5,
                recomendations = "recomendations",
                servicesIncluded = "servicesIncluded",
                price = listOf(Price(4,4.0,""))
            )
        }

        val adapter = PlaceAdapter(this, places)

        binding.places.adapter = adapter


        binding.places.setOnItemClickListener(object: AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val intent = Intent(baseContext, DetailPlaceActivity::class.java)
                startActivity(intent)
            }
        })
    }

    //SET WEATHER
    private fun setWeather(){

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return false
    }
}