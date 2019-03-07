package com.example.efpro.notizen.ViewModel

import android.database.Cursor
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.efpro.notizen.data.Note.Note
import com.example.efpro.notizen.data.NoteRepository
import com.example.efpro.notizen.data.User.User

class NoteViewModel(application: android.app.Application): AndroidViewModel(application){

    private var repository : NoteRepository = NoteRepository(application)
    private var allNotes: LiveData<List<Note>> = repository.getAllNotes()
    private var allUsers: LiveData<List<User>> = repository.getAllUsers()

    fun insert(note: Note){
        repository.insert(note)
    }

    fun update(note: Note){
        repository.update((note))
    }

    fun delete(note: Note){
        repository.delete(note)
    }

    fun deleteAllNotes(){
        repository.deleteAllNotes()
    }

    fun getAllNotes(): LiveData<List<Note>> {
        return allNotes
    }

    fun insert(user: User){
        repository.insert(user)
    }

    fun update(user: User){
        repository.update((user))
    }

    fun delete(user: User){
        repository.delete(user)
    }

    fun deleteAllUsers(){
        repository.deleteAllUsers()
    }

    fun getAllUsers(): LiveData<List<User>> {
        return allUsers
    }

    fun getSameID(givenID :Int): LiveData<List<Note>>? {
        return repository.getSameID(givenID)
    }

    fun getUserIds(): LiveData<List<Int>>? {
        return repository.getUserIds()
    }

    fun getByMail(mail:String): LiveData<List<User>>? {
        return repository.getByMail(mail)
    }

    fun getState(id:Int): LiveData<List<User>> {
        return repository.getState(id)
    }
}