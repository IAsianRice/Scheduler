package com.example.scheduler.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.scheduler.database.entities.TagItem
import com.example.scheduler.database.entities.TagsWithQuotas
import com.example.scheduler.database.entities.TagsWithSchedules

@Dao
interface TagItemDao {
    @Insert
    suspend fun insert(tagItem: TagItem)

    @Query("SELECT * FROM tags")
    suspend fun getAllTags(): List<TagItem>

    @Transaction
    @Query("SELECT * FROM tags")
    fun getTagsWithSchedules(): List<TagsWithSchedules>

    @Transaction
    @Query("SELECT * FROM tags")
    fun getTagsWithQuotas(): List<TagsWithQuotas>
}