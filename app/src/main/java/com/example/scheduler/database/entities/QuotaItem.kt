package com.example.scheduler.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotas")
data class QuotaItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val amount: String,
    val deadline: String,
    val daily: String,
    val weekly: String,
    val monthly: String,
    val yearly: String,
)