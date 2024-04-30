package com.example.pocketnature.model

class Specie {

    var weather: String
    var specie: String
    var commonName: String
    var scientificName: String
    var individuals: Int
    var sightnings: Int
    var photo: String

    constructor(
        weather: String,
        specie: String,
        commonName: String,
        scientificName: String,
        individuals: Int,
        sightnings: Int,
        photo: String
    ) {
        this.weather = weather
        this.specie = specie
        this.commonName = commonName
        this.scientificName = scientificName
        this.individuals = individuals
        this.sightnings = sightnings
        this.photo = photo
    }
}