package com.example.pocketnature.utils

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
class Map {
    companion object {
        const val CURRENT = "current"
        const val SEARCH = "search"
        const val SIGHTNING = "sightning"
        const val CAMERA = "camera"

        fun getAddressFromCoordinates(latitude: Double, longitude: Double): String {
            val client = OkHttpClient()
            val url = "https://nominatim.openstreetmap.org/reverse?format=json&lat=$latitude&lon=$longitude&zoom=17&addressdetails=1"

            val request = Request.Builder()
                .url(url)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body().string()

            var displayName = ""

            // Procesado del json recibido especificando el atributo requerido
            val gson = Gson()
            val jsonObject = gson.fromJson(responseBody, com.google.gson.JsonObject::class.java)
            displayName = jsonObject.getAsJsonPrimitive("display_name").asString

            return displayName
        }
    }
}