package com.example.pocketnature.utils

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.json.JSONObject
import java.io.File

class DataBase {
    companion object {
        const val PATH_SIGHTNINGS_IMAGES = "sightnings/"
        const val PATH_SPECIES_IMAGES = "species/"
        const val PATH_PROFILES_IMAGES = "profiles/"
        const val PATH_PLACES_IMAGES = "places/"
        const val PATH_SIGHTNINGS = "sightnings/"
        const val PATH_SPECIES = "species/"
        const val PATH_USERS = "users/"
        const val PATH_TOURISTS = "users/tourists"
        const val PATH_BUSSINESSES = "users/bussinesses"
        const val PATH_BIOMONITORS = "users/biomonitors"

    }
}