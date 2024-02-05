package com.example.scheduler.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.scheduler.database.entities.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>
}