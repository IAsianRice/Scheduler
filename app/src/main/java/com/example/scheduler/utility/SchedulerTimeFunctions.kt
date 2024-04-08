package com.example.scheduler.utility

import java.util.Calendar


class SchedulerTimeFunctions {
    companion object {
        fun generateWeeks(year: Int): List<List<Int?>> {
            var daysInMonth = intArrayOf(31, 28 + if (isLeapYear(year)) 1 else 0, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
            val firstDayOfWeek = dayOfWeekOfAMonthOfAYear(year, 1)
            var days: List<Int?> = (1..firstDayOfWeek - 1).map { null }
            for(index in 0..11)
            {
                days += (1..daysInMonth[index]).map {day -> day}
            }
            return days.chunked(7)
        }
        fun numberOfDaysInMonth(year: Int, month: Int): Int {
            val daysInMonth = intArrayOf(31, 28 + if (isLeapYear(year)) 1 else 0, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
            return daysInMonth[month - 1]
        }

        fun isLeapYear(year: Int): Boolean {
            return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
        }
        fun dayOfWeekOfAMonthOfAYear(year: Int, month: Int): Int {
            var y = year
            var m = month // January
            if (m == 1 || m == 2) {
                y--
                m += 12
            }
            val h = (1 + ((m + 1) * 26) / 10 + y + y / 4 + 6 * (y / 100) + y / 400) % 7
            return (h + 6) % 7 // Convert Zeller's result to the standard day of the week format (0 for Sunday, 1 for Monday, etc.)
        }
        enum class Months {
            JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER;

            companion object {
                fun from(value: Int): Months {
                    return values()[value - 1]
                }
            }
        }
        fun getDayOfWeekName(dayOfWeek: Int): String {
            return when (dayOfWeek) {
                Calendar.SUNDAY -> "Sunday"
                Calendar.MONDAY -> "Monday"
                Calendar.TUESDAY -> "Tuesday"
                Calendar.WEDNESDAY -> "Wednesday"
                Calendar.THURSDAY -> "Thursday"
                Calendar.FRIDAY -> "Friday"
                Calendar.SATURDAY -> "Saturday"
                else -> "Weird"
            }
        }
        fun formatTime(hour: Int, minute: Int): String {
            val formattedHour = hour.toString().padStart(2, '0') // Pad single-digit hours with leading zero
            val formattedMinute = minute.toString().padStart(2, '0') // Pad single-digit minutes with leading zero
            return "$formattedHour:$formattedMinute"
        }
    }
}