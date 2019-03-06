package com.example.efpro.notizen.models
/*This is useful to set an applicationExt*/
interface IDList {

    val contactlist: ArrayList<Int> // Contactos

    fun add(element: Int): Boolean // Agregar elemento

    fun del(elementIndex: Int) // Elimina elemento
}