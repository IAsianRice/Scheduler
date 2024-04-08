package com.example.scheduler.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotas")
data class QuotaItem(
    @PrimaryKey(autoGenerate = true)
    val quotaId: Long = 0,
    val title: String,
    val description: String,
)