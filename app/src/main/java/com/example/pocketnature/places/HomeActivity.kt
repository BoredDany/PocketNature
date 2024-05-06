package com.example.pocketnature.places

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.example.pocketnature.databinding.ActivityHomeBinding
import com.example.pocketnature.model.Place
import com.example.pocketnature.model.Price
import com.example.pocketnature.model.Sightning
import com.example.pocketnature.utils.DrawerMenuController
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.StandardCharsets

class HomeActivity : DrawerMenuController() {
    lateinit var binding: ActivityHomeBinding
    val jsonFile = "places.json"

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

        val adapter = PlaceAdapter(this, getPlaces())

        binding.places.adapter = adapter


        binding.places.setOnItemClickListener(object: AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val intent = Intent(baseContext, DetailPlaceActivity::class.java)
                startActivity(intent)
            }
        })
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

    fun getPlacesJson(): JSONArray {
        val json = JSONObject(loadJSONFromAsset())
        return json.getJSONArray("places")
    }

    fun getPlaces(): MutableList<Place> {
        try {
            val jsonArray = getPlacesJson()
            val placesList = mutableListOf<Place>()
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val name: String = jsonObject.getString("name")
                val company: String = jsonObject.getString("company")
                val linkPlace: String = jsonObject.getString("linkPlace")
                val photo: String = jsonObject.getString("photo")
                val latitude: Double = jsonObject.getDouble("latitude")
                val longitude: Double = jsonObject.getDouble("longitude")
                val description: String = jsonObject.getString("description")
                val categoriesArray = jsonObject.getJSONArray("categories")
                val categories = mutableListOf<String>()
                for (j in 0 until categoriesArray.length()) {
                    categories.add(categoriesArray.getString(j))
                }
                val rating: Int = jsonObject.getInt("rating")
                val recomendations: String = jsonObject.getString("recomendations")
                val servicesIncluded: String = jsonObject.getString("servicesIncluded")
                val pricesArray = jsonObject.getJSONArray("price")
                val prices = mutableListOf<Price>()
                for (j in 0 until pricesArray.length()) {
                    val priceObject = pricesArray.getJSONObject(j)
                    val age: Int = priceObject.getInt("age")
                    val price: Double = priceObject.getDouble("price")
                    val possibleSale: String = priceObject.getString("possibleSale")
                    prices.add(Price(age, price, possibleSale))
                }

                val place = Place(name, company, linkPlace, photo, latitude, longitude, description, categories, rating, recomendations, servicesIncluded, prices)
                placesList.add(place)
            }
            return placesList
        } catch (ex: Exception) {
            ex.printStackTrace()
            return mutableListOf()
        }
    }

    fun readJsonFromAssets(context: Context, fileName: String): JSONObject? {
        val json: String?
        try {
            val inputStream = context.assets.open(fileName)
            json = inputStream.bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return JSONObject(json)
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