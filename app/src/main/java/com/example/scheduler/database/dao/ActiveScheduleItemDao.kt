package com.example.scheduler.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.scheduler.database.entities.ActiveScheduleItem
import com.example.scheduler.database.entities.ActiveSchedulesWithQuota
import java.util.Calendar

@Dao
interface ActiveScheduleItemDao {
    @Insert
    suspend fun insert(activeScheduleItem: ActiveScheduleItem): Long

    @Query("SELECT * FROM active_schedules")
    suspend fun getAllActiveSchedules(): List<ActiveScheduleItem>

    @Query("SELECT * FROM active_schedules WHERE :endTime > startTimeInMillis AND :startTime < startTimeInMillis + durationInMillis ORDER BY startTimeInMillis")
    suspend fun getSchedulesWithinTimeFrame(startTime: Long, endTime: Long): List<ActiveScheduleItem>

    @Query("SELECT * FROM active_schedules WHERE :scheduleId == schedule_id")
    suspend fun getSchedulesOfScheduleId(scheduleId: Long): List<ActiveScheduleItem>

    @Query("DELETE FROM active_schedules WHERE (startTimeInMillis + durationInMillis) < :currentTimeInMillis")
    suspend fun deleteSchedulesBeforeTime(currentTimeInMillis: Long = Calendar.getInstance().timeInMillis)

    @Query("DELETE FROM active_schedules WHERE activeScheduleId = :id")
    suspend fun deleteSchedulesByID(id: Long)

    @Query("SELECT * FROM active_schedules WHERE startTimeInMillis > :currentTimeInMillis ORDER BY startTimeInMillis")
    suspend fun getNextUpcomingSchedules(currentTimeInMillis: Long = Calendar.getInstance().timeInMillis): List<ActiveScheduleItem>

    @Query("SELECT * FROM active_schedules WHERE startTimeInMillis + durationInMillis >= :currentTimeInMillis AND :currentTimeInMillis >= startTimeInMillis")
    suspend fun getInProgressSchedules(currentTimeInMillis: Long = Calendar.getInstance().timeInMillis): List<ActiveScheduleItem>

    @Transaction
    @Query("SELECT * FROM active_schedules")
    suspend fun getActiveSchedulesWithQuotas(): List<ActiveSchedulesWithQuota>
}