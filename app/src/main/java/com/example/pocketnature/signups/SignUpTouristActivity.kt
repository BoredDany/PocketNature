package com.example.pocketnature.signups

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.pocketnature.R
import com.example.pocketnature.databinding.ActivitySignUpBinding
import com.example.pocketnature.databinding.ActivitySignUpTouristBinding
import com.example.pocketnature.model.User
import com.example.pocketnature.places.HomeActivity
import com.example.pocketnature.utils.DataBase
import com.example.pocketnature.utils.UserManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SignUpTouristActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpTouristBinding
    private val calendar = Calendar.getInstance()
    private lateinit var auth: FirebaseAuth
    private val TAG = "FIREBASE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpTouristBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.login.setOnClickListener {
            signUp()
        }

        binding.btnCalendar.setOnClickListener {
            showCalendar()
        }

    }

    private fun showCalendar(){
        val datePickerDialog = DatePickerDialog(this, {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, monthOfYear, dayOfMonth)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate.time)
            binding.birthday.text = formattedDate
        },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }
    private fun signUp(){
        if(validate() && UserManager.validateEmail(binding.email.text.toString()) &&
            UserManager.validatePassword(binding.password.text.toString())){

            auth.createUserWithEmailAndPassword(binding.email.text.toString(), binding.password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful)
                        val user = auth.currentUser
                        if (user != null) {
                            saveUserToDatabase(user)
                        }
                    } else {
                        Toast.makeText(this, "createUserWithEmail:Failure: " + task.exception.toString(),
                            Toast.LENGTH_SHORT).show()
                        task.exception?.message?.let { Log.e(TAG, it) }
                    }
                }

        }else{
            Toast.makeText(this, "Incorrect fields", Toast.LENGTH_SHORT).show()
        }

    }

    private fun saveUserToDatabase(user: FirebaseUser) {
        val database = FirebaseDatabase.getInstance().reference
        val userRef = database.child(DataBase.PATH_TOURISTS).child(user.uid) // Use user.uid as unique identifier

        val userToSave = User(binding.name.text.toString(), binding.email.text.toString(),
            binding.password.text.toString(), binding.phone.text.toString(),
            binding.nationality.text.toString(),binding.birthday.text.toString(),UserManager.TOURIST)

        userRef.setValue(userToSave)
            .addOnSuccessListener {
                Log.d(TAG, "User saved to database successfully!")
                updateUI(user)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Failed to save user to database: $exception")
                Toast.makeText(this, "Failed to create account", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            val intent = Intent(this, HomeActivity::class.java)
            //UserManager.currentUser = currentUser
            startActivity(intent)
        }
    }

    private fun validate(): Boolean {
        var valid = true

        if (TextUtils.isEmpty(binding.name.text.toString())) {
            binding.name.error = "Required."
            valid = false
        } else {
            binding.name.error = null
        }

        if (TextUtils.isEmpty(binding.email.text.toString())) {
            binding.email.error = "Required."
            valid = false
        } else {
            binding.email.error = null
        }

        if (TextUtils.isEmpty(binding.phone.text.toString())) {
            binding.phone.error = "Required."
            valid = false
        } else {
            binding.phone.error = null
        }

        if (TextUtils.isEmpty(binding.nationality.text.toString())) {
            binding.nationality.error = "Required."
            valid = false
        } else {
            binding.nationality.error = null
        }

        if (TextUtils.isEmpty(binding.birthday.text.toString())) {
            binding.birthday.error = "Required."
            valid = false
        } else {
            binding.birthday.error = null
        }

        if (TextUtils.isEmpty(binding.password.text.toString())) {
            binding.password.error = "Required."
            valid = false
        } else {
            binding.password.error = null
        }
        return valid
    }


}