package com.example.scheduler.tests

import org.junit.Test
import java.util.Calendar

class CalenderTests {

    @Test
    fun addition_isCorrect() {
        var calender = Calendar.getInstance()
        var weeksInYear = calender.weeksInWeekYear
        calender.set(Calendar.DAY_OF_YEAR, 1)
        var week = 1
        print("${calender.get(Calendar.DAY_OF_YEAR)} ${calender.get(Calendar.YEAR)} ${calender.get(Calendar.DAY_OF_MONTH)} ${calender.get(Calendar.DAY_OF_WEEK)} \n")
        while (week < weeksInYear) {
            print("\n week $week \n")
            for (day_of_week in 1..7)
            {
                calender.set(Calendar.DAY_OF_WEEK, day_of_week)
                print("${calender.get(Calendar.DAY_OF_YEAR)} ${calender.get(Calendar.YEAR)} ${calender.get(Calendar.DAY_OF_MONTH)} ${calender.get(Calendar.DAY_OF_WEEK)} \n")
            }
            week++
            calender.set(Calendar.WEEK_OF_YEAR, week)
        }
    }

    private fun add(a: Int, b: Int): Int {
        return a + b
    }
}