package com.example.efpro.notizen.ViewHolder

import android.content.ContentValues.TAG
import android.nfc.Tag
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.efpro.notizen.models.Nota
import com.example.efpro.notizen.models.User
import com.google.android.material.tabs.TabLayout
import java.math.BigInteger

class NoteViewModel(application: android.app.Application): AndroidViewModel(application){

    companion object {
        public var allNotes: MutableList<Nota> = mutableListOf()
        public var allUsers: MutableList<User> = mutableListOf()
        public var Notes: MutableList<Nota> = mutableListOf()
        public var versions: MutableList<MutableList<String>> = mutableListOf()

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


}

