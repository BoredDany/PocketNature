package com.example.pocketnature.nature

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.example.pocketnature.databinding.ActivitySpeciesBinding
import com.example.pocketnature.model.Place
import com.example.pocketnature.model.Price
import com.example.pocketnature.model.Specie
import com.example.pocketnature.places.PlaceAdapter
import com.example.pocketnature.utils.DrawerMenuController
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.StandardCharsets

class SpeciesActivity : DrawerMenuController() {
    lateinit var binding: ActivitySpeciesBinding
    val jsonFile = "species.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpeciesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDrawer(binding.drawerLayout, binding.navView)

        val adapter = SpeciesAdapter(this, getSpecies())

        binding.species.adapter = adapter

        binding.btnRegisterSpecie.setOnClickListener {
            startActivity(Intent(this, RegisterSpecieActivity::class.java))
        }

    }

    fun loadJSONFromAsset(): String? {
        try {
            val assets = this.assets
            val inputStream = assets.open(jsonFile)
            val buffer = ByteArray(inputStream.available())
            inputStream.read(buffer)
            inputStream.close()
            return String(buffer, StandardCharsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
    }

    fun getSpeciesJson(): JSONArray {
        val json = JSONObject(loadJSONFromAsset())
        return json.getJSONArray("species")
    }

    fun getSpecies(): MutableList<Specie> {
        try {
            val jsonArray = getSpeciesJson()
            val speciesList = mutableListOf<Specie>()
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val weather: String = jsonObject.getString("weather")
                val specie: String = jsonObject.getString("specie")
                val commonName: String = jsonObject.getString("commonName")
                val scientificName: String = jsonObject.getString("scientificName")
                val photo: String = jsonObject.getString("photo")

                val specieObj = Specie(weather, specie, commonName, scientificName, photo)
                speciesList.add(specieObj)
            }
            return speciesList
        } catch (ex: Exception) {
            ex.printStackTrace()
            return mutableListOf()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return false
    }
}