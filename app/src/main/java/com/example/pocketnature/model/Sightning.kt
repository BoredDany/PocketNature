package com.example.pocketnature.model

class Sightning {
    var fecha: String = ""
    var hora: String = ""
    var latitud: Double = 0.0
    var longitud: Double = 0.0
    var clima: String = ""
    var clasificacionEspecia: String = ""
    var nombreComun: String = ""
    var scientificName: String = ""
    var cantidadIndividuos: Int = 0
    var photo: String = ""

    constructor() {}

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