package com.example.efpro.notizen.data.User

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("DELETE FROM user_table")
    fun deleteAllUsers()

    @Query("SELECT * FROM user_table ORDER BY _id DESC")
    fun getAllUsers(): LiveData<List<User>>

    @Query("SELECT _id from USER_TABLE")
    fun getUserIds(): List<User>

    @Query("SELECT * From user_table WHEN :mail == email")
    fun getByMail(mail:String): List<User>

}