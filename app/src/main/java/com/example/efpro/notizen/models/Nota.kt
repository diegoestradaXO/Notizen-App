package com.example.efpro.notizen.models

import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
class Nota {

    var nombre:String = ""
    var descripcion:String = ""
    var etiquetas:List<String> = listOf()
    var versiones :List<List<String>> = listOf()
    var privacidad : Boolean = false
    var userid:String = ""

    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    constructor(nombre:String,descripcion:String,etiquetas:List<String>,versiones :List<List<String>>, privacidad : Boolean,userid:String) {
        this.nombre =nombre
        this.descripcion=descripcion
        this.etiquetas=etiquetas
        this.versiones=versiones
        this.privacidad=privacidad
        this.userid=userid
    }

}