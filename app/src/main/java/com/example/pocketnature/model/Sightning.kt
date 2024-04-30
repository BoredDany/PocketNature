package com.example.pocketnature.model

class Sightning {

    var fecha: String
    var hora: String
    var latitud: Double
    var longitud: Double
    var clima: String
    var clasificacionEspecia: String
    var nombreComun: String
    var scientificName: String
    var cantidadIndividuos: Int
    var photo: String

    constructor(
        fecha: String,
        hora: String,
        latitud: Double,
        longitud: Double,
        clima: String,
        clasificacionEspecia: String,
        nombreComun: String,
        scientificName: String,
        cantidadIndividuos: Int,
        photo: String
    ) {
        this.fecha = fecha
        this.hora = hora
        this.latitud = latitud
        this.longitud = longitud
        this.clima = clima
        this.clasificacionEspecia = clasificacionEspecia
        this.nombreComun = nombreComun
        this.scientificName = scientificName
        this.cantidadIndividuos = cantidadIndividuos
        this.photo = photo
    }
}