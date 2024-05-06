package com.example.pocketnature

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.pocketnature.databinding.ActivityMainBinding
import com.example.pocketnature.utils.Permission
import com.example.pocketnature.places.HomeActivity
import com.example.pocketnature.signups.SignUpActivity
import com.example.pocketnature.utils.DataBase
import com.example.pocketnature.utils.UserManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private val TAG = "FIREBASE"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        permisoUbicacion()
        binding.btnSignUp.setOnClickListener { startActivity(Intent(this, SignUpActivity::class.java)) }
    }

    //LIFECYCLE
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        } else {
            binding.username.setText("")
            binding.password.setText("")
        }
    }

    fun permisoUbicacion(){

        when {
            ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                binding.login.setOnClickListener {
                    //LOGIN
                    login()
                }
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION) -> {
                Toast.makeText(this, "Requiere acceso a ubicación", Toast.LENGTH_SHORT).show()
                requestPermissions(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    Permission.MY_PERMISSION_REQUEST_LOCATION)
            }
            else -> {
                requestPermissions(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    Permission.MY_PERMISSION_REQUEST_LOCATION)
            }
        }
    }

    private fun login(){
        if(validateForm() && isEmailValid(binding.username.text.toString())){
            auth.signInWithEmailAndPassword(binding.username.text.toString(),
                binding.password.text.toString())
                .addOnCompleteListener(this) { task ->
                    // Sign in task
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithEmail:onComplete:success")
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.w(TAG, "signInWithEmail:onComplete:failure", task.exception)
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        binding.username.setText("")
                        binding.password.setText("")
                    }
                }
        }else{
            Toast.makeText(this, "Incorrect fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateForm(): Boolean {
        var valid = true
        val email = binding.username.text.toString()
        if (TextUtils.isEmpty(email)) {
            binding.username.error = "Required."
            valid = false
        } else {
            binding.username.error = null
        }
        val password = binding.password.text.toString()
        if (TextUtils.isEmpty(password)) {
            binding.password.error = "Required."
            valid = false
        } else {
            binding.password.error = null
        }
        return valid
    }

    private fun isEmailValid(email: String): Boolean {
        if (!email.contains("@") ||
            !email.contains(".") ||
            email.length < 5)
            return false
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Permission.MY_PERMISSION_REQUEST_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    binding.login.setOnClickListener { startActivity(Intent(this, HomeActivity::class.java)) }
                    Toast.makeText(this, "Permiso de ubicación concedido", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permiso de ubicación negado", Toast.LENGTH_SHORT).show()
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }
}