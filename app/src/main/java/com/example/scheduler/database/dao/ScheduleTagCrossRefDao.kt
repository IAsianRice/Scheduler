package com.example.scheduler.database.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.scheduler.database.entities.ScheduleTagCrossRef

@Dao
interface ScheduleTagCrossRefDao {
    @Insert
    suspend fun insertScheduleTagCrossRef(crossRef: ScheduleTagCrossRef)
}