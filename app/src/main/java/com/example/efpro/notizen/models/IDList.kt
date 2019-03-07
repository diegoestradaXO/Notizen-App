package com.example.efpro.notizen.models
/*This is useful to set an applicationExt*/
interface IDList {

    val contactlist: ArrayList<LocalUser> // Contactos

    fun add(element: LocalUser): Boolean // Agregar elemento

    fun del(elementIndex: Int) // Elimina elemento
}