package com.example.scheduler.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.scheduler.database.entities.ActiveScheduleItem
import com.example.scheduler.database.entities.ScheduleItem

@Dao
interface ActiveScheduleItemDao {
    @Insert
    suspend fun insert(activeScheduleItem: ActiveScheduleItem)

    @Query("SELECT * FROM active_schedules")
    suspend fun getAllActiveSchedules(): List<ActiveScheduleItem>
}