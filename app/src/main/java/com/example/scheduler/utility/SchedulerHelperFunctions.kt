package com.example.scheduler.utility

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import com.example.scheduler.database.entities.ScheduleItem
import java.util.Calendar

class SchedulerHelperFunctions {
    companion object {
        fun scheduleListToDictionary(list: List<ScheduleItem>): Map<Long, ScheduleItem> {
            val dictionary = mutableMapOf<Long, ScheduleItem>()
            for (element in list) {
                dictionary[element.scheduleId] = element
            }
            return dictionary
        }
        fun getDayInMillis(year: Int, dayOfYear: Int): Long {
            val calendar = Calendar.getInstance()
            calendar.clear()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.DAY_OF_YEAR, dayOfYear)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            return calendar.timeInMillis
        }
        fun getStartOfTodayInMillis(dayOffset: Int = 0): Long {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, dayOffset)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            return calendar.timeInMillis
        }
        @Composable
        fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

    }
}