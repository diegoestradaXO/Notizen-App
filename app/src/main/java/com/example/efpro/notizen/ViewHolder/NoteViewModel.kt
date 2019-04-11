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
        var allNotes: MutableList<Nota> = mutableListOf()
        var allUsers: MutableList<User> = mutableListOf()
        var Notes: MutableList<Nota> = mutableListOf()
        var versions: MutableList<MutableList<String>> = mutableListOf()

    }

    fun delete(){

    }


}

