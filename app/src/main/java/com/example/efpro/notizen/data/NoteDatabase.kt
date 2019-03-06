package com.example.efpro.notizen.data

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.efpro.notizen.data.Note.Note
import com.example.efpro.notizen.data.Note.NoteDao
import com.example.efpro.notizen.data.User.User
import com.example.efpro.notizen.data.User.UserDao
import java.util.*

@Database(entities = [Note::class, User::class], version = 2)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
    abstract fun userDao(): UserDao

    companion object {
        private var instance: NoteDatabase? = null

        fun getInstance(context: Context): NoteDatabase? {
            if (instance == null) {
                synchronized(NoteDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NoteDatabase::class.java, "app_database"
                    )
                        .fallbackToDestructiveMigration() // when version increments, it migrates (deletes db and creates new) - else it crashes
                        .addCallback(roomCallback)
                        .build()
                }
            }
            return instance
        }

        fun destroyInstance() {
            instance = null
        }

        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDbAsyncTask(instance)
                    .execute()
            }
        }
    }

    class PopulateDbAsyncTask(db: NoteDatabase?) : AsyncTask<Unit, Unit, Unit>() {
        private val noteDao = db?.noteDao()
        private val userDao = db?.userDao()

        override fun doInBackground(vararg p0: Unit?) {
            val fans = listOf(1,2,3)
            val following = listOf(0,1,2)
            val items = listOf("Prueba")
            noteDao?.insert(
                Note(
                    "Prueba",
                    "description 1",
                    1,
                    0,
                    "No information",
                    0,
                    items,
                    0,
                    Date() as java.sql.Date
                )
            )
            userDao?.insert(
                User(
                    "Administrador",
                    "scontreraig@gmail.com",
                    "Just one of the creators",
                    fans,
                    following))
        }
    }

}