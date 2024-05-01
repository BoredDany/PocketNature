package com.example.pocketnature.nature

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.util.TypedValue
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.compose.ui.graphics.Paint
import androidx.core.app.ActivityCompat
import com.example.pocketnature.databinding.ActivityAvistamientosBinding
import com.example.pocketnature.utils.DrawerMenuController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import com.example.pocketnature.R
import com.example.pocketnature.model.Sightning
import com.example.pocketnature.model.Specie
import com.example.pocketnature.utils.DataBase
import com.example.pocketnature.utils.Map
import com.example.pocketnature.utils.SpecieCategory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.json.JSONArray
import org.json.JSONObject
import org.osmdroid.config.Configuration
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets


class AvistamientosActivity : DrawerMenuController() {

    //VIEW
    private lateinit var binding: ActivityAvistamientosBinding

    //MAPA
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private var map : MapView? = null
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mLocationCallback: LocationCallback
    private var currentLocationmarker: Marker? = null
    private var searchMarker: Marker? = null
    private var sightningMarker: Marker? = null
    var mGeocoder: Geocoder? = null
    val jsonFile = "sightnings.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAvistamientosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDrawer(binding.drawerLayout, binding.navView)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        Configuration.getInstance().setUserAgentValue("com.example.pocketnature")

        //INICIALIZACION DE VARIABLES
        initialize()

        //LISTENER UBICACION
        listennerGeocoderLocation()

        //LISTENNERS DE LA PANTALLA
        setListenners()

        map!!.overlays.add(createOverlayEvents())

    }

    //LIFECYCLE
    override fun onResume() {
        super.onResume()
        map!!.onResume()


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates()
            mFusedLocationProviderClient.lastLocation.addOnSuccessListener(this) { location ->
                Log.i("LOCATION", "onSuccess location")
                if (location != null) {
                    Log.i("LOCATION", "Longitud: " + location.longitude)
                    Log.i("LOCATION", "Latitud: " + location.latitude)
                    val mapController = map!!.controller
                    mapController.setZoom(15)
                    val startPoint = GeoPoint(location.latitude, location.longitude);
                    mapController.setCenter(startPoint);
                } else {
                    Log.i("LOCATION", "FAIL location")
                }
            }
        }

        //recorrer lista de avistamientos y createMarkerRetMark y poner listenner
        val sightnings = getSightings ()
        for(s in sightnings){
            var iconID = R.drawable.baseline_location_blue
            val point = GeoPoint(s.latitud, s.longitud)
            val title = s.nombreComun + ":" + s.clasificacionEspecia + "\n" + s.cantidadIndividuos + " individuals\n" + s.fecha + " - " + s.hora
            when(s.clasificacionEspecia){
                SpecieCategory.REPTILES ->{ iconID = R.drawable.crocodile_icon }
                SpecieCategory.MAMMALS ->{ iconID = R.drawable.monkey_icon }
                SpecieCategory.BIRDS ->{ iconID = R.drawable.bird_icon }
                SpecieCategory.AMPHIBIANS ->{ iconID = R.drawable.frog_icon }
                SpecieCategory.INSECTS ->{ iconID = R.drawable.bug_icon }
            }
            sightningMarker = createMarkerRetMark(point, title, null, iconID)
            sightningMarker?.let { map!!.overlays.add(it) }
        }

    }

    private fun getSightings(): MutableList<Sightning>{
        val jsonObject = readJsonFromAssets(this, jsonFile)

        if (jsonObject != null) {
            val sightingsList = mutableListOf<Sightning>()

            jsonObject.keys().forEach { key ->
                val sightingObject = jsonObject.getJSONObject(key)
                val sighting = Sightning(
                    sightingObject.getString("fecha"),
                    sightingObject.getString("hora"),
                    sightingObject.getDouble("latitud"),
                    sightingObject.getDouble("longitud"),
                    sightingObject.getString("clima"),
                    sightingObject.getString("clasificacionEspecia"),
                    sightingObject.getString("nombreComun"),
                    sightingObject.getString("scientificName"),
                    sightingObject.getInt("cantidadIndividuos"),
                    sightingObject.getString("photo")
                )
                sightingsList.add(sighting)
            }
            // Aquí puedes hacer lo que quieras con la lista de avistamientos
            return sightingsList
        } else {
            println("Error al leer el archivo JSON desde assets.")
        }
        return mutableListOf()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
        map!!.onPause()
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

    private fun stopLocationUpdates() {
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
    }

    //INICIALIZACION DE VARIABLES
    private fun initialize(){
        mGeocoder = Geocoder(this)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        mLocationRequest = createLocationRequest()

        map = binding.map
        map!!.setTileSource(TileSourceFactory.MAPNIK)
        map!!.setMultiTouchControls(true)
    }

    private fun createLocationRequest(): LocationRequest =
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,10000).apply {
            setMinUpdateIntervalMillis(5000)
        }.build()
    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null)
        }
    }


    //LISTENER UBICACION
    private fun listennerGeocoderLocation(){
        binding.location.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                val addressString = binding.location.text.toString()
                if (addressString.isNotEmpty()) {
                    try {
                        if (map != null && mGeocoder != null) {
                            val addresses = mGeocoder!!.getFromLocationName(addressString, 1)
                            if (addresses != null && addresses.isNotEmpty()) {
                                val addressResult = addresses[0]
                                val position = GeoPoint(addressResult.latitude, addressResult.longitude)

                                Log.i("Geocoder", "Dirección encontrada: ${addressResult.getAddressLine(0)}")
                                Log.i("Geocoder", "Latitud: ${addressResult.latitude}, Longitud: ${addressResult.longitude}")

                                //Agregar Marcador al mapa
                                createMarker(position, addressString, null, R.drawable.baseline_location_red, Map.SEARCH)
                                searchMarker?.let { map!!.overlays.add(it) }
                                map!!.controller.setCenter(searchMarker!!.position)

                            } else {
                                Log.i("Geocoder", "Dirección no encontrada:" + addressString)
                                Toast.makeText(this, "Dirección no encontrada", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } else {
                    Toast.makeText(this, "La dirección esta vacía", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }

    }

    //SET LISTENNERS
    private fun setListenners(){
        binding.btnRegisterSighting.setOnClickListener {
            startActivity(Intent(this, RegisterAvistamientoActivity::class.java))
        }

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                Log.i("LOCATION", "Location update in the callback: $location")
                if (location != null) {
                    val point = GeoPoint(location.latitude, location.longitude)
                    createMarker(point, "you", null, R.drawable.baseline_location_blue, Map.CURRENT)
                    currentLocationmarker?.let { map!!.overlays.add(it) }
                }
            }
        }
    }

    fun createMarker(p: GeoPoint, title: String?, desc: String?, iconID: Int, markerType: String) {
        if (map != null) {

            lateinit var myIcon: Drawable
            if (iconID != 0) {
                myIcon = resources.getDrawable(iconID, this.theme)
            }

            when(markerType){
                Map.CURRENT -> {
                    currentLocationmarker = currentLocationmarker ?: Marker(map)
                    title?.let { currentLocationmarker!!.title = it }
                    desc?.let { currentLocationmarker!!.subDescription = it }
                    currentLocationmarker!!.icon = myIcon
                    currentLocationmarker!!.position = p
                    currentLocationmarker!!.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                }

                Map.SEARCH -> {
                    searchMarker = searchMarker ?: Marker(map)
                    title?.let { searchMarker!!.title = it }
                    desc?.let { searchMarker!!.subDescription = it }
                    searchMarker!!.icon = myIcon
                    searchMarker!!.position = p
                    searchMarker!!.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                }
            }
        }
    }

    private fun createMarkerRetMark(p: GeoPoint, title: String?, desc: String?, iconID: Int): Marker? {
        var marker: Marker? = null
        if (map != null) {
            marker = Marker(map)
            title?.let { marker.title = it }
            desc?.let { marker.subDescription = it }
            if (iconID != 0) {
                val myIcon = resources.getDrawable(iconID, this.theme)
                val newWidthDp = 24  // Define your desired width here in dp
                val newHeightDp = 24  // Define your desired height here in dp

                val newWidthPx = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    newWidthDp.toFloat(),
                    resources.displayMetrics
                ).toInt()

                val newHeightPx = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    newHeightDp.toFloat(),
                    resources.displayMetrics
                ).toInt()

                val bitmap = Bitmap.createBitmap(myIcon.intrinsicWidth, myIcon.intrinsicHeight, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                myIcon.setBounds(0, 0, canvas.width, canvas.height)
                myIcon.draw(canvas)
                val scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidthPx, newHeightPx, false)

                // Create a new bitmap for the background with the same size as the scaled bitmap
                val backgroundBitmap = Bitmap.createBitmap(newWidthPx, newHeightPx, Bitmap.Config.ARGB_8888)
                val backgroundCanvas = Canvas(backgroundBitmap)

                // Draw a white circle on the background bitmap
                val paint = Paint()
                paint.color = Color.WHITE
                backgroundCanvas.drawCircle(newWidthPx / 2f, newHeightPx / 2f, newWidthPx / 2f, paint)

                // Draw the scaled bitmap on top of the background bitmap
                backgroundCanvas.drawBitmap(scaledBitmap, 0f, 0f, null)

                val bitmapDrawable = BitmapDrawable(resources, backgroundBitmap)
                marker.icon = bitmapDrawable
            }
            marker.position = p
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        }

        return marker
    }

    //NAVIGATION DRAWER
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return false
    }

    //MAP
    private fun createOverlayEvents(): MapEventsOverlay {
        val overlayEventos = MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                return false
            }
            override fun longPressHelper(p: GeoPoint): Boolean {
                longpress()
                return true
            }
        })
        return overlayEventos
    }

    private fun longpress(){
        map!!.controller.setCenter(currentLocationmarker!!.position)
    }

}