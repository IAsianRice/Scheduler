package com.example.scheduler.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tags")
data class TagItem(
    @PrimaryKey(autoGenerate = true)
    val tagId: Long = 0,
    val name: String,
    val description: String,
)