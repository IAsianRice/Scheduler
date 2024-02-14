package com.example.scheduler.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.scheduler.database.entities.ScheduleItem

@Dao
interface ScheduleItemDao {
    @Insert
    suspend fun insert(scheduleItem: ScheduleItem)

    @Query("SELECT * FROM schedules WHERE id = :idToFind")
    suspend fun getScheduleByID(idToFind: Long): ScheduleItem
    @Query("SELECT * FROM schedules")
    suspend fun getAllSchedules(): List<ScheduleItem>
}