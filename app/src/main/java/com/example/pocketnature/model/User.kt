package com.example.pocketnature.model

class User {
    var name: String
    var email: String
    var phone: String
    var country: String
    var birthday: String
    var password: String
    var type: String
    var photo: String

    constructor(
        name: String,
        email: String,
        password: String,
        phone: String,
        country: String,
        birthday: String,
        type:String
    ) {
        this.name = name
        this.email = email
        this.password = password
        this.phone = phone
        this.country = country
        this.birthday = birthday
        this.type = type
        this.photo = ""
    }
}