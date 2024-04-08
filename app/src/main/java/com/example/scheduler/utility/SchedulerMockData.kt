package com.example.scheduler.utility

import com.example.scheduler.database.entities.ActiveScheduleItem
import com.example.scheduler.database.entities.ActiveScheduleQuotaCrossRef
import com.example.scheduler.database.entities.QuotaItem
import com.example.scheduler.database.entities.ScheduleItem
import com.example.scheduler.database.entities.TagItem
import com.example.scheduler.utility.SchedulerHelperValues.Companion.MILLIS_IN_HOUR
import java.util.Calendar
import kotlin.random.Random

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
        fun randomizeActiveScheduleItem(): ActiveScheduleItem {
            val random = Random.Default
            val minDuration = MILLIS_IN_HOUR * 24 * 3
            val maxDuration = MILLIS_IN_HOUR * 40 * 4
            val durationInMillis = random.nextLong(minDuration, maxDuration) // Random duration between min and max
            var cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            cal.add(Calendar.DAY_OF_YEAR, -3)
            val start = cal.timeInMillis
            cal.set(Calendar.HOUR_OF_DAY, 23)
            val end = cal.timeInMillis
            val startTimeInMillis = random.nextLong(start, end)
            val repeatFlag = random.nextInt(6) // Generates a random number between 0 and 5
            val scheduleID = (1001L..5000L).random() // Static schedule ID

            return ActiveScheduleItem(
                durationInMillis = durationInMillis,
                startTimeInMillis = startTimeInMillis,
                repeatFlag = repeatFlag,
                scheduleID = scheduleID
            )
        }

        fun randomizeActiveScheduleItemFromNow(spread: Long): ActiveScheduleItem {
            val random = Random.Default
            val minDuration = 3600000L // Minimum duration (1 minute)
            val maxDuration = 3600000L * 7 // Maximum duration (1 hour)
            val durationInMillis = random.nextLong(minDuration, maxDuration + 1) // Random duration between min and max
            var cal = Calendar.getInstance()
            val start = cal.timeInMillis
            val startTimeInMillis = random.nextLong(start, start + spread)
            val repeatFlag = random.nextInt(6) // Generates a random number between 0 and 5
            val scheduleID = (1001L..5000L).random() // Static schedule ID

            return ActiveScheduleItem(
                durationInMillis = durationInMillis,
                startTimeInMillis = startTimeInMillis,
                repeatFlag = repeatFlag,
                scheduleID = scheduleID
            )
        }
        fun randomizeActiveScheduleItemList(size: Int): List<ActiveScheduleItem> {
            return List(size) { randomizeActiveScheduleItem() }
        }
        fun randomizeActiveScheduleItemFromNowList(size: Int, spread: Long): List<ActiveScheduleItem> {
            return List(size) { randomizeActiveScheduleItemFromNow(spread) }
        }

        fun generateRandomTags(count: Int): List<TagItem> {
            val scheduleTypeTags = mapOf(
                "health" to listOf("Fitness", "Diet", "Yoga", "Meditation", "Wellness"),
                "work" to listOf("Meeting", "Project", "Deadline", "Presentation", "Task"),
                "maintenance" to listOf("Cleaning", "Repair", "Maintenance", "Organization", "Service")
                // Add more schedule types and related tags as needed
            )

            return List(count) {
                val scheduleType = scheduleTypeTags.keys.random()
                val tagsForType = scheduleTypeTags.getValue(scheduleType)
                val randomName = tagsForType.random()
                val randomDescription = "Description for $randomName"
                TagItem(name = randomName, description = randomDescription)
            }
        }
        fun generateRandomQuotas(count: Int): List<QuotaItem> {
            val mockTitles = listOf("Pushups", "Sit-ups", "Running", "Cycling", "Swimming")
            val mockDescriptions = listOf("Exercise for upper body strength", "Core strengthening exercise", "Cardiovascular workout", "Aerobic exercise", "Full body workout")

            return List(count) {
                val randomTitle = mockTitles.random()
                val randomDescription = mockDescriptions.random()
                QuotaItem(title = randomTitle, description = randomDescription)
            }
        }

        fun generateRandomUniqueQuotas(count: Int): List<QuotaItem> {
            val mockTitles = listOf("Pushups", "Sit-ups", "Running", "Cycling", "Swimming")
            val mockDescriptions = listOf("Exercise for upper body strength", "Core strengthening exercise", "Cardiovascular workout", "Aerobic exercise", "Full body workout")

            return List(count) {
                val randomTitle = mockTitles.random()
                val randomDescription = mockDescriptions.random()
                QuotaItem(quotaId = it.toLong() ,title = randomTitle, description = randomDescription)
            }
        }
        fun generateRandomUniqueScheduleItems(count: Int): List<ScheduleItem> {
            val mockTitles = listOf("Pushups", "Sit-ups", "Running", "Cycling", "Swimming")
            val mockDescriptions = listOf("Exercise for upper body strength", "Core strengthening exercise", "Cardiovascular workout", "Aerobic exercise", "Full body workout")

            return List(count) {
                val randomTitle = mockTitles.random()
                val randomDescription = mockDescriptions.random()
                ScheduleItem(scheduleId = it.toLong() ,title = randomTitle, description = randomDescription)
            }
        }
        fun generateRandomUniqueActiveScheduleItems(count: Int): List<ActiveScheduleItem> {

            return List(count) {
                ActiveScheduleItem(activeScheduleId = it.toLong() , scheduleID = it.toLong(), durationInMillis = 0L, startTimeInMillis = 0L, repeatFlag = 0)
            }
        }
        fun generateRandomUniqueActiveScheduleQuotaCrossRefs(count: Int, multiples: Int): List<ActiveScheduleQuotaCrossRef> {
            return List(count * multiples) {
                ActiveScheduleQuotaCrossRef(quotaId = (it%multiples).toLong(), activeScheduleId = (it%count).toLong(), quotaFinished = (1..100).random(), quotaNeeded = (1..100).random())
            }
        }
    }
}