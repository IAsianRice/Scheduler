package com.example.scheduler.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "active_schedules",
    foreignKeys = [ForeignKey(
        entity = ScheduleItem::class,
        parentColumns = ["id"],
        childColumns = ["schedule_id"],
        onDelete = ForeignKey.CASCADE // Optional: define behavior on delete
    )])
data class ActiveScheduleItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "schedule_id")
    val scheduleID: Long,
    val repeatFactor: Int, // e.g Biweekly (2), Triweekly(3)
    val durationHour: Int, // Required
    val durationMinute: Int, // Required
    val durationSecond: Int, // Required
    val startingTimeHour: Int, // ...
    val startingTimeMinute: Int, // If Specified, Repeat Daily
    val dayOfWeek: Int, // If Specified, Repeat Weekly (Ignored if dayOfMonth is specified)
    val dayOfMonth: Int, // If Specified, Repeat Monthly
    val month: Int, // If Specified, Repeat Yearly
    val year: Int, // If Specified, One Time schedule!
)
/**
 * Precedence Chart
 * From Highest (Top) to Lowest (Bottom)
 * ****************************
 * One Time Schedule
 * Yearly Repeat
 * Monthly Repeat
 * Weekly Repeat
 * Daily Repeat
 */