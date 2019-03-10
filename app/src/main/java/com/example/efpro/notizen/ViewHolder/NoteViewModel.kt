package com.example.efpro.notizen.ViewHolder

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.efpro.notizen.models.Nota
import com.example.efpro.notizen.models.User

class NoteViewModel(application: android.app.Application): AndroidViewModel(application){

    companion object {
        public var allNotes: MutableList<Nota> = mutableListOf()
        public var allUsers: MutableList<User> = mutableListOf()
        public var Notes: MutableList<Nota> = mutableListOf()
    }

    fun insert(nota: Nota){
        //repository.insert(nota)
    }

    fun update(nota: Nota){
        //repository.update((nota))
    }

    fun delete(nota: Nota){
        //repository.delete(nota)
    }

    fun getAllNotes(): List<Nota> {
        return allNotes
    }
}