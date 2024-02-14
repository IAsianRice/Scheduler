package com.example.scheduler.utility

import com.example.scheduler.database.entities.ScheduleItem

class SchedulerMockData {
    companion object{
        fun generateRandomScheduleItems(count: Int): List<ScheduleItem> {
            val titles = listOf("Meeting", "Appointment", "Event", "Task")
            val descriptions = listOf(
                "Discuss project progress",
                "Doctor's appointment",
                "Team building event",
                "Complete homework"
            )

            return List(count) {
                ScheduleItem(
                    title = titles.random(),
                    description = descriptions.random()
                )
            }
        }

    }
}