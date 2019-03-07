package com.example.efpro.notizen.models

data class LocalUser(var userID: String, var notesID: String) {
    fun compare(localUser: LocalUser): Boolean {
        return (this.notesID != localUser.notesID) && (this.userID==localUser.userID)//devolvemos el resultado
    }
}