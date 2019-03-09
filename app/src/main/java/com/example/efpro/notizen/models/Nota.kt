package com.example.efpro.notizen.models

import com.google.firebase.database.IgnoreExtraProperties

class Nota(var nombre:String,var descripcion:String,var etiquetas:List<String>,versiones :List<List<String>>,privacidad : Boolean) {
}

@IgnoreExtraProperties
class Nota {

    lateinit var nombre:String
    lateinit var descripcion:String
    lateinit var etiquetas:List<String>
    lateinit var versiones :List<List<String>>
    var privacidad : Boolean = false

    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    constructor(email: String,biografia:String,nombre:String,seguidores:List<String>,seguidos: List<String>) {
        this.biografia = biografia
        this.email = email
        this.nombre=nombre
        this.seguidores=seguidores
        this.seguidos=seguidos
    }

}<