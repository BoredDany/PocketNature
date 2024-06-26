package com.example.pocketnature.nature

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.pocketnature.R
import com.example.pocketnature.databinding.ActivityRegisterAvistamientoBinding
import com.example.pocketnature.model.Sightning
import com.example.pocketnature.utils.DrawerMenuController
import com.example.pocketnature.utils.Map
import com.example.pocketnature.utils.Weather
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
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterAvistamientoActivity : DrawerMenuController() {
    //VIEW
    lateinit var binding: ActivityRegisterAvistamientoBinding

    //MAPA
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private var map : MapView? = null
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mLocationCallback: LocationCallback
    private var currentLocationmarker: Marker? = null
    private var searchMarker: Marker? = null
    private var mGeocoder: Geocoder? = null

    //SIGHTNING INFO
    private val calendar = Calendar.getInstance()
    private var weather: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterAvistamientoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDrawer(binding.drawerLayout, binding.navView)

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
    }
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
        map!!.onPause()
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
                                createMarker(position, "New sightning:\n" + addressString, null, R.drawable.baseline_location_red, Map.SEARCH)
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
        binding.btnNext.setOnClickListener {
            if(binding.date.text.toString().isEmpty() || binding.hour.text.toString().isEmpty() || searchMarker == null || weather.isEmpty()){
                Toast.makeText(this, "DATA IS MISSING", Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(this, RegisterAvistamientoIIActivity::class.java)
                intent.putExtra("date", binding.date.text.toString())
                intent.putExtra("hour", binding.hour.text.toString())
                intent.putExtra("latitude", searchMarker!!.position.latitude)
                intent.putExtra("longitude", searchMarker!!.position.longitude)
                intent.putExtra("weather", weather)
                startActivity(intent)
            }
        }

        binding.btnMyLocation.setOnClickListener {
            val p = GeoPoint(currentLocationmarker!!.position.latitude, currentLocationmarker!!.position.longitude)
            val address = Map.getAddressFromCoordinates(p.latitude, p.longitude)
            createMarker(p, address, null, R.drawable.baseline_location_red, Map.SEARCH)
            binding.location.setText(address)

            val mapController = map!!.controller
            mapController.setZoom(15)
            mapController.setCenter(p);

            Toast.makeText(this, "Use my current location", Toast.LENGTH_SHORT).show()
        }

        binding.btnCalendar.setOnClickListener {
            showCalendar()
        }

        binding.btnClock.setOnClickListener {
            showTimePicker()
        }
        binding.btnCloudy.setOnClickListener {
            it.setBackgroundResource(R.drawable.round_corner_border_blue)
            binding.btnSunny.setBackgroundResource(R.drawable.round_corner_white)
            binding.btnRainy.setBackgroundResource(R.drawable.round_corner_white)
            weather = Weather.CLOUDY
        }

        binding.btnSunny.setOnClickListener {
            it.setBackgroundResource(R.drawable.round_corner_border_blue)
            binding.btnCloudy.setBackgroundResource(R.drawable.round_corner_white)
            binding.btnRainy.setBackgroundResource(R.drawable.round_corner_white)
            weather = Weather.SUNNY
        }

        binding.btnRainy.setOnClickListener {
            it.setBackgroundResource(R.drawable.round_corner_border_blue)
            binding.btnSunny.setBackgroundResource(R.drawable.round_corner_white)
            binding.btnCloudy.setBackgroundResource(R.drawable.round_corner_white)
            weather = Weather.RAINY
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

    private fun showCalendar(){
        val datePickerDialog = DatePickerDialog(this, {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, monthOfYear, dayOfMonth)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate.time)
            binding.date.text = formattedDate
        },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    private fun showTimePicker(){
        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { view: TimePicker?, hourOfDay: Int, minute: Int ->
                // Obtener la hora seleccionada
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)

                // Formatear la hora seleccionada
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val formattedTime = timeFormat.format(calendar.time)

                // Actualizar el texto del campo de texto con la hora seleccionada
                binding.hour.text = formattedTime
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true // Indica si se muestra el modo de 24 horas o no
        )

        // Mostrar el TimePickerDialog
        timePickerDialog.show()
    }

    //MAP
    private fun createOverlayEvents(): MapEventsOverlay {
        val overlayEventos = MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                return false
            }
            override fun longPressHelper(p: GeoPoint): Boolean {
                longpress(p)
                return true
            }
        })
        return overlayEventos
    }

    private fun longpress(p: GeoPoint){
        val address = Map.getAddressFromCoordinates(p.latitude, p.longitude)

        createMarker(p, address, null, R.drawable.baseline_location_red, Map.SEARCH)
        searchMarker?.let { map!!.overlays.add(it) }
        map!!.controller.setCenter(searchMarker!!.position)
        binding.location.setText(address)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return false
    }

}