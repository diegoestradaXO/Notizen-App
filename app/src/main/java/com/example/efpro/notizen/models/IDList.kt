package com.example.efpro.notizen.models

import com.example.efpro.notizen.data.User.User

/*This is useful to set an applicationExt*/
interface IDList {

    val contactlist: ArrayList<User> // Contactos

    fun add(element: User): Boolean // Agregar elemento

    fun del(elementIndex: Int) // Elimina elemento
}