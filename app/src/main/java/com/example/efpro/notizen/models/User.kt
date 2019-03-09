package com.example.efpro.notizen.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class User {

    lateinit var email: String
    lateinit var biografia:String
    lateinit var nombre:String
    lateinit var seguidores:List<String>
    lateinit var seguidos: List<String>

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

}
