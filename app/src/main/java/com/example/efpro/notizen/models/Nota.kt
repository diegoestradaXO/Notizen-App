package com.example.efpro.notizen.models

import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
class Nota {

    var nombre: String? = null
    var descripcion: String? = null
    var etiquetas: List<String>? = null
    var versiones : List<List<String>>? = null
    var privacidad : String? = null
    var userid: String? = null
    var id:String?=null

    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    constructor(
        nombre:String,
        descripcion:String,
        etiquetas:List<String>,
        versiones:List<List<String>>, privacidad: String,
        userid:String) {
        this.nombre =nombre
        this.descripcion=descripcion
        this.etiquetas=etiquetas
        this.versiones=versiones
        this.privacidad=privacidad
        this.userid=userid
        this.id=nombre+userid
    }

}