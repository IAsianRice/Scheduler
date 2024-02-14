package com.example.scheduler.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.scheduler.database.entities.QuotaItem
import com.example.scheduler.database.entities.ScheduleItem
import com.example.scheduler.database.entities.TagItem

@Dao
interface TagItemDao {
    @Insert
    suspend fun insert(tagItem: TagItem)

    @Query("SELECT * FROM tags")
    suspend fun getAllTags(): List<TagItem>
}