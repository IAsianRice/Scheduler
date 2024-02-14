package com.example.scheduler.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.scheduler.database.entities.QuotaItem
import com.example.scheduler.database.entities.ScheduleItem

@Dao
interface QuotaItemDao {
    @Insert
    suspend fun insert(quotaItem: QuotaItem)

    @Query("SELECT * FROM quotas")
    suspend fun getAllQuotas(): List<QuotaItem>
}