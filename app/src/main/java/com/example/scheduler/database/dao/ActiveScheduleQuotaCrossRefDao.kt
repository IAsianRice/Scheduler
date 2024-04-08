package com.example.scheduler.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.scheduler.database.entities.ActiveScheduleQuotaCrossRef

@Dao
interface ActiveScheduleQuotaCrossRefDao {
    @Insert
    suspend fun insert(crossRef: ActiveScheduleQuotaCrossRef)
    @Update
    suspend fun update(crossRef: ActiveScheduleQuotaCrossRef)

    @Query("SELECT * FROM active_schedule_quota_cross_ref")
    suspend fun getAllActiveScheduleQuotaCrossRefs(): List<ActiveScheduleQuotaCrossRef>
}