package com.example.efpro.notizen.data

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.example.efpro.notizen.data.Note.Note
import com.example.efpro.notizen.data.Note.NoteDao
import com.example.efpro.notizen.data.User.User
import com.example.efpro.notizen.data.User.UserDao

class NoteRepository(application: Application) {

    private var noteDao: NoteDao
    private var userDao: UserDao
    private var allNotes: LiveData<List<Note>>
    private var allUsers: LiveData<List<User>>


    init {
        val database: NoteDatabase = NoteDatabase.getInstance(
            application.applicationContext
        )!!
        noteDao = database.noteDao()
        userDao = database.userDao()
        allNotes = noteDao.getAllNotes()
        allUsers = userDao.getAllUsers()
    }

    fun insert(note: Note) {
        counter = counter+1
        val insertNoteAsyncTask = InsertNoteAsyncTask(noteDao).execute(note)
    }

    fun insert(user: User){
        val insertUserAsyncTask = InsertUserAsyncTask(userDao).execute(user)

    }

    fun update(note: Note) {
        val updateNoteAsyncTask = UpdateNoteAsyncTask(noteDao).execute(note)
    }

    fun update(user: User){
        val updateUserAsyncTask = UpdateUserAsyncTask(userDao).execute(user)
    }

    fun delete(note: Note) {
        val deleteNoteAsyncTask = DeleteNoteAsyncTask(noteDao).execute(note)
    }

    fun delete(user: User) {
        val deleteUserAsyncTask = DeleteUserAsyncTask(userDao).execute(user)
    }

    fun deleteAllNotes() {
        val deleteAllNotesAsyncTask = DeleteAllNotesAsyncTask(
            noteDao
        ).execute()
    }

    fun deleteAllUsers() {
        val deleteAllUserAsyncTask = DeleteAllUserAsyncTask(
            userDao
        ).execute()
    }

    fun getAllNotes(): LiveData<List<Note>> {
        return allNotes
    }

    fun getAllUsers(): LiveData<List<User>> {
        return allUsers
    }

    fun getSameID(givenID :Int): LiveData<List<Note>>? {
       val getSameIDAsyncTask=getSameIDAsyncTask(noteDao).execute(givenID).get()
        return  getSameIDAsyncTask
    }

    fun getUserIds(): LiveData<List<Int>>? {
        val getUserIdsAsyncTask=getUserIdsAsyncTask(userDao).execute().get()
        return getUserIdsAsyncTask
    }

    fun getByMail(mail:String): LiveData<List<User>>? {
        val getByMailAsyincTask = getByMailAsyncTask(userDao).execute(mail).get()
        return getByMailAsyincTask
    }

    fun getState(id:Int): LiveData<List<User>> {
        val getState = getByIdAsyncTask(userDao).execute(id).get()
        return getState
    }

    companion object {
        var counter: Int=0//will count every note in the database, this will be helpful to generate id.

        private class getByIdAsyncTask(userDao: UserDao) : AsyncTask<Int, Unit, LiveData<List<User>>>() {

            val userDao = userDao

            override fun doInBackground(vararg p0: Int?): LiveData<List<User>>? {
                return userDao.getState(p0)
            }

        }

        private class getSameIDAsyncTask(noteDao: NoteDao) : AsyncTask<Int, Unit, LiveData<List<Note>>>() {

            val noteDao = noteDao

            override fun doInBackground(vararg p0: Int?): LiveData<List<Note>> {
                return noteDao.getSameID(p0)
            }

        }

        private class getByMailAsyncTask(userDao: UserDao) : AsyncTask<String, Unit, LiveData<List<User>>>() {

            val userDao = userDao

            override fun doInBackground(vararg p0: String?): LiveData<List<User>>? {
                return userDao.getByMail(p0)
            }
        }

        private class getUserIdsAsyncTask(val userDao: UserDao) : AsyncTask<Unit, Unit, LiveData<List<Int>>>() {

            override fun doInBackground(vararg p0: Unit?): LiveData<List<Int>> {
                return userDao.getUserIds()
            }
        }


        private class InsertNoteAsyncTask(noteDao: NoteDao) : AsyncTask<Note, Unit, Unit>() {
            val noteDao = noteDao

            override fun doInBackground(vararg p0: Note?) {
                noteDao.insert(p0[0]!!)
            }
        }

        private class InsertUserAsyncTask(userDao: UserDao) : AsyncTask<User, Unit, Unit>() {
            val userDao = userDao

            override fun doInBackground(vararg p0: User?) {
                userDao.insert(p0[0]!!)
            }
        }

        private class UpdateNoteAsyncTask(noteDao: NoteDao) : AsyncTask<Note, Unit, Unit>() {
            val noteDao = noteDao

            override fun doInBackground(vararg p0: Note?) {
                noteDao.update(p0[0]!!)
            }
        }

        private class UpdateUserAsyncTask(userDao: UserDao) : AsyncTask<User, Unit, Unit>() {
            val userDao = userDao

            override fun doInBackground(vararg p0: User?) {
                userDao.update(p0[0]!!)
            }
        }

        private class DeleteNoteAsyncTask(noteDao: NoteDao) : AsyncTask<Note, Unit, Unit>() {
            val noteDao = noteDao

            override fun doInBackground(vararg p0: Note?) {
                noteDao.delete(p0[0]!!)
            }
        }

        private class DeleteUserAsyncTask(userDao: UserDao) : AsyncTask<User, Unit, Unit>() {
            val userDao = userDao

            override fun doInBackground(vararg p0: User?) {
                userDao.delete(p0[0]!!)
            }
        }

        private class DeleteAllNotesAsyncTask(noteDao: NoteDao) : AsyncTask<Unit, Unit, Unit>() {
            val noteDao = noteDao

            override fun doInBackground(vararg p0: Unit?) {
                noteDao.deleteAllNotes()
            }
        }

        private class DeleteAllUserAsyncTask(userDao: UserDao) : AsyncTask<Unit, Unit, Unit>() {
            val userDao = userDao

            override fun doInBackground(vararg p0: Unit?) {
                userDao.deleteAllUsers()
            }
        }
    }
}