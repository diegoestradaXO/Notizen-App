package com.example.efpro.notizen.data.Note

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.efpro.notizen.data.NoteRepository
import java.sql.Date

@Entity(tableName = "note_table")
@TypeConverters(DateConverter::class)
data class Note(

    var nombre: String,
    var description: String,
    var rating: Int,
    var setSecondVersion: Int, //gives the id of the note version, and 0 when it is the first version
    var content: String,
    var userId: Int,
    var tags:List<String>,
    var version: Int,
    @ColumnInfo(name = "date")
    val date: Date
) {

    /*we need to set the autoGenerate to false, because some notes will
    have the same id, The notes that are other versions of the same
    notes will belong to this same id.
     */
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "_id")
    var id:Int = generateID(setSecondVersion)


    fun generateID(setSecondVersion: Int): Int {
        return if(setSecondVersion!=0){
            (setSecondVersion)
        } else{
            (NoteRepository.counter +1)
        }
    }

}