package com.example.pocketnature.model

class Price {
    var age: Int
    var price: Double
    var possibleSale: String

    constructor(age: Int, price: Double, possibleSale: String) {
        this.age = age
        this.price = price
        this.possibleSale = possibleSale
    }
}