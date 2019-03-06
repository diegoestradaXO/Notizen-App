package com.example.efpro.notizen.data.Note

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.efpro.notizen.data.Note.Note

@Dao
interface NoteDao {

    @Insert
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("DELETE FROM note_table")
    fun deleteAllNotes()

    @Query("SELECT * FROM note_table ORDER BY rating DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM note_table WHERE _id == :givenID")
    fun getSameID(givenID: Int):Array<Note>

}