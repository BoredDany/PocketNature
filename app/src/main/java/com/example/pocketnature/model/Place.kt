package com.example.pocketnature.model

class Place {
    var name: String
    var company: String
    var linkPlace: String
    var photo: String
    var latitude: Double
    var longitude: Double
    var description: String
    var categories: List<String>
    var rating: Int
    var recomendations: String
    var servicesIncluded: String
    var price: List<Price>

    constructor(
        name: String,
        company: String,
        linkPlace: String,
        photo: String,
        latitude: Double,
        longitude: Double,
        description: String,
        categories: List<String>,
        rating: Int,
        recomendations: String,
        servicesIncluded: String,
        price: List<Price>
    ) {
        this.name = name
        this.company = company
        this.linkPlace = linkPlace
        this.photo = photo
        this.latitude = latitude
        this.longitude = longitude
        this.description = description
        this.categories = categories
        this.rating = rating
        this.recomendations = recomendations
        this.servicesIncluded = servicesIncluded
        this.price = price
    }
}