package com.example.pocketnature.nature

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.pocketnature.R
import com.example.pocketnature.databinding.ActivityRegisterAvistamientoIiactivityBinding
import com.example.pocketnature.model.Sightning
import com.example.pocketnature.utils.DataBase
import com.example.pocketnature.utils.DrawerMenuController
import com.example.pocketnature.utils.Permission
import com.example.pocketnature.utils.SpecieCategory
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegisterAvistamientoIIActivity : DrawerMenuController() {
    //VIEW
    lateinit var binding: ActivityRegisterAvistamientoIiactivityBinding

    //SIGHTNING INFO
    var date: String = ""
    var hour: String = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var weather: String = ""
    var specie: String = ""
    var localPhoto: Uri? = null
    var url: String = ""
    private lateinit var photoUri: Uri

    //DATABASE
    private val database = FirebaseDatabase.getInstance()
    private lateinit var myRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterAvistamientoIiactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDrawer(binding.drawerLayout, binding.navView)

        //INITIALIZE
        initialize()

        //LISTENNERS
        setListenners()
    }

    //INITIALIZE
    private fun initialize(){
        date = intent.getStringExtra("date").toString()
        hour = intent.getStringExtra("hour").toString()
        latitude = intent.getDoubleExtra("latitude", 0.0)
        longitude = intent.getDoubleExtra("longitude", 0.0)
        weather = intent.getStringExtra("weather").toString()
    }

    //LISTENNERS
    private fun setListenners(){
        binding.btnRegister.setOnClickListener {
            if(specie.isEmpty() || binding.commonName.text.toString().isEmpty() ||
                binding.scientificName.text.toString().isEmpty() ||
                binding.number.text.toString().isEmpty() || localPhoto == null){
                Toast.makeText(this, "DATA IS MISSING", Toast.LENGTH_SHORT).show()
            }else{
                saveImage()
            }
        }

        val buttons = listOf(binding.btnMammals, binding.btnBirds,
            binding.btnAmphibians, binding.btnInsects, binding.btnReptiles)

        for(button in buttons){
            button.setOnClickListener {
                it.setBackgroundResource(R.drawable.round_corner_border_blue)

                when(it.id){
                    binding.btnMammals.id -> { specie = SpecieCategory.MAMMALS }
                    binding.btnBirds.id -> { specie = SpecieCategory.BIRDS }
                    binding.btnAmphibians.id -> { specie = SpecieCategory.AMPHIBIANS }
                    binding.btnReptiles.id -> { specie = SpecieCategory.REPTILES }
                    binding.btnInsects.id -> { specie = SpecieCategory.INSECTS }
                }

                Toast.makeText(this, specie, Toast.LENGTH_SHORT).show()

                for(otherbutton in buttons){
                    if(otherbutton != it){
                        otherbutton.setBackgroundResource(R.drawable.round_corner_white)
                    }
                }
            }
        }

        binding.btnGallery.setOnClickListener {
            permissionGallery()
        }

        binding.btnCamera.setOnClickListener {
            permissionCamera()
        }
    }

    private fun saveImage(){
        //referencia de la imagen en la bd
        val storageRef = FirebaseStorage.getInstance().reference
        val imagesRef = storageRef.child(DataBase.PATH_SIGHTNINGS_IMAGES)

        // Crea una referencia única para la imagen (por ejemplo, usando el timestamp actual)
        val imageName = "image_${System.currentTimeMillis()}.jpg"
        val imageRef = imagesRef.child(imageName)

        // Sube la imagen al Firebase Storage
        imageRef.putFile(localPhoto!!).addOnSuccessListener(object : OnSuccessListener<UploadTask.TaskSnapshot> {
            override fun onSuccess(taskSnapshot: UploadTask.TaskSnapshot) {
                // Get a URL to the uploaded content
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    url = uri.toString()
                    saveSightning()
                }.addOnFailureListener { exception ->
                    // Error al obtener la URL de descarga
                    Log.i("FBApp", "Failed saving url image")
                }
                Log.i("FBApp", "Successfully uploaded image")
            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(exception: Exception) {
                // Handle unsuccessful uploads
                Log.i("FBApp", "Failed uploading image")
            }
        })
    }

    private fun saveSightning(){
        //save sightning
        val newSightning = Sightning(date, hour, latitude, longitude, weather, specie,
            binding.commonName.text.toString(), binding.scientificName.text.toString(),
            binding.number.text.toString().toInt(), url)

        myRef = database.getReference(DataBase.PATH_SIGHTNINGS)
        val key = myRef.push().key
        myRef = database.getReference(DataBase.PATH_SIGHTNINGS + key)
        myRef.setValue(newSightning)

        Toast.makeText(this, "Sightning registered", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, AvistamientosActivity::class.java))
    }
    private fun permissionGallery(){
        when {
            ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                selectPhoto()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this, android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                Toast.makeText(this, "WE NEED PERMISSION TO ACCESS GALLERY", Toast.LENGTH_SHORT).show()
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    Permission.MY_PERMISSION_REQUEST_GALLERY
                )
            }
            else -> {
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    Permission.MY_PERMISSION_REQUEST_GALLERY
                )
            }
        }
    }
    private fun permissionCamera(){
        when {
            ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                takePic()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this, android.Manifest.permission.CAMERA) -> {
                Toast.makeText(this, "WE NEED PERMISSION TO ACCESS CAMERA", Toast.LENGTH_SHORT).show()
                requestPermissions(
                    arrayOf(android.Manifest.permission.CAMERA),
                    Permission.MY_PERMISSION_REQUEST_CAMERA)
            }
            else -> {
                requestPermissions(
                    arrayOf(android.Manifest.permission.CAMERA),
                    Permission.MY_PERMISSION_REQUEST_CAMERA)
            }
        }
    }
    fun selectPhoto () {
        val permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            val pickImage = Intent(Intent.ACTION_PICK)
            pickImage.type = "image/*"
            startActivityForResult(pickImage, Permission.IMAGE_PICKER_REQUEST)
        } else {
            Toast.makeText(this, "NO PERMISSION TO ACCESS GALLERY", Toast.LENGTH_SHORT).show()
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                Permission.MY_PERMISSION_REQUEST_GALLERY)
        }
    }
    private fun takePic() {
        val permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    ex.printStackTrace()
                    null
                }
                photoFile?.also {
                    photoUri = FileProvider.getUriForFile(
                        this,
                        "com.example.pocketnature.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    startActivityForResult(takePictureIntent, Permission.REQUEST_IMAGE_CAPTURE)
                }
            } else {
                Toast.makeText(this, "No hay una cámara disponible en este dispositivo", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "No hay permiso de cámara", Toast.LENGTH_SHORT).show()
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), Permission.MY_PERMISSION_REQUEST_CAMERA)
        }
    }
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            Permission.IMAGE_PICKER_REQUEST ->{
                if(resultCode == Activity.RESULT_OK){
                    try {
                        //Logica de seleccion de imagen
                        val selectedImageUri = data!!.data
                        if(data.data != null){
                            binding.photo.setImageURI(selectedImageUri)
                            localPhoto = selectedImageUri
                        }
                    } catch (e: FileNotFoundException){
                        e.printStackTrace()
                    }
                }
            }
            Permission.REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK) {
                    // Load the full-quality image into ImageView
                    localPhoto = photoUri
                    binding.photo.setImageURI(photoUri)
                }
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            Permission.MY_PERMISSION_REQUEST_CAMERA -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    takePic()
                    Toast.makeText(this, "Permiso de camara concedido", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permiso de camara negado", Toast.LENGTH_SHORT).show()

                }
            }
            Permission.MY_PERMISSION_REQUEST_GALLERY -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectPhoto()
                    Toast.makeText(this, "Permiso de galería concedido", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permiso de galería denegado", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                // Ignore all other requests.
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