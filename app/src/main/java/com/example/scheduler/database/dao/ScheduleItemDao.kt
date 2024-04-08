package com.example.scheduler.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.scheduler.database.entities.ScheduleItem
import com.example.scheduler.database.entities.SchedulesWithTags

@Dao
interface ScheduleItemDao {

    @Update
    suspend fun updateSchedule(scheduleItem: ScheduleItem)
    @Insert
    suspend fun insert(scheduleItem: ScheduleItem): Long
    @Query("SELECT * FROM schedules WHERE scheduleId = :idToFind")
    suspend fun getScheduleByID(idToFind: Long): ScheduleItem
    @Query("SELECT * FROM schedules")
    suspend fun getAllSchedules(): List<ScheduleItem>

    @Transaction
    @Query("SELECT * FROM schedules WHERE scheduleId = :scheduleId")
    suspend fun getSchedulesWithTags(scheduleId: Long): SchedulesWithTags?
    @Transaction
    @Query("SELECT * FROM schedules")
    suspend fun getSchedulesWithTags(): List<SchedulesWithTags>


}