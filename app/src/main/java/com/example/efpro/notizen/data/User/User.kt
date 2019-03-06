package com.example.efpro.notizen.data.User

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.efpro.notizen.data.Note.DateConverter

@Entity(tableName = "user_table")
@TypeConverters(DateConverter::class)
data class User(

    var nombre: String,
    var email: String,
    var biography: String, //gives the id of the note version, and 0 when it is the first version
    var followers:String,
    var following:String,
    var password:String,
    var state: Int
) {

    /*we need to set the autoGenerate to true, almost we need two id
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id:Int = 0
}