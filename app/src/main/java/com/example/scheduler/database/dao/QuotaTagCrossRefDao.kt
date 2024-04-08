package com.example.scheduler.database.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.scheduler.database.entities.QuotaTagCrossRef

@Dao
interface QuotaTagCrossRefDao {
    @Insert
    suspend fun insertQuotaTagCrossRef(crossRef: QuotaTagCrossRef)
}