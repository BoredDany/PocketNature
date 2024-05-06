package com.example.pocketnature.utils

import com.example.pocketnature.model.User

class UserManager {
    companion object {

        lateinit var currentUser: User
        const val TOURIST = "Tourist"
        const val BUSSINESS = "Bussiness"
        const val BIOMONITOR = "Biomonitor"

        fun validateEmail(email: String): Boolean{
            if (!email.contains("@") ||
                !email.contains(".") ||
                email.length < 5)
                return false
            return true
        }

        fun validatePassword(password: String): Boolean{
            if (password.length < 8)
                return false
            return true
        }
    }
}