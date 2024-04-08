package com.example.scheduler.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedules")
data class ScheduleItem(
    @PrimaryKey(autoGenerate = true)
    val scheduleId: Long = 0,
    val title: String,
    val description: String
) {
    override fun toString(): String {
        return title
    }
}