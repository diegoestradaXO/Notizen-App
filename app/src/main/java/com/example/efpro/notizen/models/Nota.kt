package com.example.efpro.notizen.models

import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
class Nota {

    lateinit var nombre: String
    lateinit var descripcion: String
    lateinit var etiquetas: List<String>
    lateinit var versiones : List<List<String>>
    lateinit var privacidad : String
    lateinit var userid: String
    lateinit var id:String

    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    constructor(
        nombre:String,
        descripcion:String,
        etiquetas: List<String>,
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