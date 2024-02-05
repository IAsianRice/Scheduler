package com.example.scheduler.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.scheduler.database.dao.UserDao
import com.example.scheduler.database.entities.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}