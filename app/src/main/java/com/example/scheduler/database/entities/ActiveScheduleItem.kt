package com.example.scheduler.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "active_schedules",
    foreignKeys = [ForeignKey(
        entity = ScheduleItem::class,
        parentColumns = ["scheduleId"],
        childColumns = ["schedule_id"],
        onDelete = ForeignKey.CASCADE // Optional: define behavior on delete
    )])
data class ActiveScheduleItem(
    @PrimaryKey(autoGenerate = true)
    val activeScheduleId: Long = 0,
    @ColumnInfo(name = "schedule_id")
    val scheduleID: Long,
    val repeatFlag: Int,
    val durationInMillis: Long,
    val startTimeInMillis: Long,
)
/**
 * (repeatFlag)
 * Precedence Chart
 * From Highest (Top) to Lowest (Bottom)
 * ****************************
 * One Time Schedule = 0
 * Yearly Repeat = 1
 * Monthly Repeat = 2
 * Weekly Repeat = 3
 * Daily Repeat = 4
 */