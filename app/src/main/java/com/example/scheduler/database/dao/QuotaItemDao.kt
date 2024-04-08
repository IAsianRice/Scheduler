package com.example.scheduler.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.scheduler.database.entities.QuotaItem
import com.example.scheduler.database.entities.QuotaWithActiveSchedules
import com.example.scheduler.database.entities.QuotasWithTags

@Dao
interface QuotaItemDao {
    @Insert
    suspend fun insert(quotaItem: QuotaItem): Long

    @Query("SELECT * FROM quotas")
    suspend fun getAllQuotas(): List<QuotaItem>

    @Transaction
    @Query("SELECT * FROM quotas")
    suspend fun getQuotasFromActiveSchedules(): List<QuotaWithActiveSchedules>


    @Transaction
    @Query("SELECT * FROM quotas WHERE quotaId = :quotaId")
    suspend fun getQuotasWithTags(quotaId: Long): QuotasWithTags?
    @Transaction
    @Query("SELECT * FROM quotas")
    suspend fun getQuotasWithTags(): List<QuotasWithTags>
}