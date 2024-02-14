package com.example.scheduler.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.scheduler.database.dao.ActiveScheduleItemDao
import com.example.scheduler.database.dao.QuotaItemDao
import com.example.scheduler.database.dao.ScheduleItemDao
import com.example.scheduler.database.entities.ActiveScheduleItem
import com.example.scheduler.database.entities.QuotaItem
import com.example.scheduler.database.entities.ScheduleItem

@Database(entities = [ActiveScheduleItem::class, ScheduleItem::class, QuotaItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scheduleItemDao(): ScheduleItemDao
    abstract fun quotaItemDao(): QuotaItemDao
    abstract fun activeScheduleItemDao(): ActiveScheduleItemDao
}