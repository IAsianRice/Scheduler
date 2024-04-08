package com.example.scheduler.services

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.room.Room
import com.example.scheduler.MainActivity
import com.example.scheduler.database.AppDatabase
import com.example.scheduler.database.entities.ActiveScheduleItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar


class SchedulerTrackerService : Service(){
    companion object {
        const val NEW_SCHEDULE_STARTED = "com.example.scheduler.NEW_SCHEDULE_STARTED"
        const val SCHEDULE_ENDED = "com.example.scheduler.SCHEDULE_ENDED"
        const val UPDATE_SCHEDULES = "com.example.scheduler.UPDATE_SCHEDULES"
        const val ACTION_STOP = "com.example.scheduler.ACTION_STOP"
    }

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private val _activeScheduleItemListStateFlow = MutableStateFlow<List<ActiveScheduleItem>>(listOf())
    private val _currentActiveScheduleItemListStateFlow = MutableStateFlow<List<ActiveScheduleItem>>(listOf())
    val currentActiveScheduleItemListStateFlow = _currentActiveScheduleItemListStateFlow.asStateFlow()
    private val _upcomingActiveScheduleItemListStateFlow = MutableStateFlow<List<ActiveScheduleItem>>(listOf())
    val upcomingActiveScheduleItemListStateFlow = _upcomingActiveScheduleItemListStateFlow.asStateFlow()

    private var nextStartPendingIntent: PendingIntent? = null
    private var nextEndPendingIntent: PendingIntent? = null

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var database: AppDatabase

    // Service Calls from processes unrelated to the program
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when (intent?.action) {
            NEW_SCHEDULE_STARTED -> {
                // our intent has finished
                nextStartPendingIntent = null
                // now update the schedules
                updateUpcomingSchedules()
                // now as long as we have more schedules we will load the next one.
                if (_upcomingActiveScheduleItemListStateFlow.value.isNotEmpty())
                {
                    setupNextStart(this@SchedulerTrackerService, _upcomingActiveScheduleItemListStateFlow.value.first())
                }

                if (_currentActiveScheduleItemListStateFlow.value.isNotEmpty())
                {
                    setupEndStart(this@SchedulerTrackerService, _currentActiveScheduleItemListStateFlow.value.first())
                }

                Log.d("onStartCommand/NEW_SCHEDULE_STARTED", "Started New Track!")
            }
            SCHEDULE_ENDED -> {
                nextEndPendingIntent = null
                updateUpcomingSchedules()
                if (_currentActiveScheduleItemListStateFlow.value.isNotEmpty())
                {
                    setupEndStart(this@SchedulerTrackerService, _currentActiveScheduleItemListStateFlow.value.first())
                }
                Log.d("onStartCommand/SCHEDULE_ENDED", "Ended a Schedule")
            }
            UPDATE_SCHEDULES -> {
            }
            ACTION_STOP -> {
            }
        }
        return START_STICKY
    }

    // Service Calls from processes related to the program
    inner class SchedulerTrackerBinder : Binder() {
        fun getService(): SchedulerTrackerService {
            return this@SchedulerTrackerService
        }
        fun getActiveScheduleItemListStateFlow(): StateFlow<List<ActiveScheduleItem>> {
            return _activeScheduleItemListStateFlow.asStateFlow()
        }

        fun update()
        {
            updateUpcomingSchedules()
        }

        fun getInProgressActiveScheduleItemListStateFlow(): StateFlow<List<ActiveScheduleItem>> {
            return currentActiveScheduleItemListStateFlow
        }
        fun getUpcomingActiveScheduleItemListStateFlow(): StateFlow<List<ActiveScheduleItem>> {
            return upcomingActiveScheduleItemListStateFlow
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return SchedulerTrackerBinder()
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
    override fun onCreate() {
        super.onCreate()
        // Get relavent Data from the database
        Log.d("onCreate", "HMMM")
        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app_database").build()
        scope.launch {
            _activeScheduleItemListStateFlow.value = database.activeScheduleItemDao().getAllActiveSchedules()
            _currentActiveScheduleItemListStateFlow.value = database.activeScheduleItemDao().getInProgressSchedules()
            _upcomingActiveScheduleItemListStateFlow.value = database.activeScheduleItemDao().getNextUpcomingSchedules()
                /*listOf(
                ActiveScheduleItem(startTimeInMillis = Calendar.getInstance().timeInMillis+(MILLIS_IN_SECOND*5L), durationInMillis = (MILLIS_IN_SECOND*30L), repeatFlag = 0, scheduleID = 0),
                ActiveScheduleItem(startTimeInMillis = Calendar.getInstance().timeInMillis+(MILLIS_IN_SECOND*5L)+100L, durationInMillis = (MILLIS_IN_SECOND*30L), repeatFlag = 0, scheduleID = 1),
                ActiveScheduleItem(startTimeInMillis = Calendar.getInstance().timeInMillis+(MILLIS_IN_SECOND*5L)+200L, durationInMillis = (MILLIS_IN_SECOND*30L), repeatFlag = 0, scheduleID = 2),
                ActiveScheduleItem(startTimeInMillis = Calendar.getInstance().timeInMillis+(MILLIS_IN_SECOND*5L)+300L, durationInMillis = (MILLIS_IN_SECOND*30L), repeatFlag = 0, scheduleID = 3),
                ActiveScheduleItem(startTimeInMillis = Calendar.getInstance().timeInMillis+(MILLIS_IN_SECOND*5L)+400L, durationInMillis = (MILLIS_IN_SECOND*30L), repeatFlag = 0, scheduleID = 4),
                ActiveScheduleItem(startTimeInMillis = Calendar.getInstance().timeInMillis+(MILLIS_IN_SECOND*5L)+500L, durationInMillis = (MILLIS_IN_SECOND*30L), repeatFlag = 0, scheduleID = 5),
                ActiveScheduleItem(startTimeInMillis = Calendar.getInstance().timeInMillis+(MILLIS_IN_SECOND*5L)+600L, durationInMillis = (MILLIS_IN_SECOND*30L), repeatFlag = 0, scheduleID = 6),
                ActiveScheduleItem(startTimeInMillis = Calendar.getInstance().timeInMillis+(MILLIS_IN_SECOND*5L)+700L, durationInMillis = (MILLIS_IN_SECOND*30L), repeatFlag = 0, scheduleID = 7),
                ActiveScheduleItem(startTimeInMillis = Calendar.getInstance().timeInMillis+(MILLIS_IN_SECOND*5L)+800L, durationInMillis = (MILLIS_IN_SECOND*30L), repeatFlag = 0, scheduleID = 8),
                ActiveScheduleItem(startTimeInMillis = Calendar.getInstance().timeInMillis+(MILLIS_IN_SECOND*5L)+900L, durationInMillis = (MILLIS_IN_SECOND*30L), repeatFlag = 0, scheduleID = 9),
                ActiveScheduleItem(startTimeInMillis = Calendar.getInstance().timeInMillis+(MILLIS_IN_SECOND*5L)+1000L, durationInMillis = (MILLIS_IN_SECOND*30L), repeatFlag = 0, scheduleID = 10),
                ActiveScheduleItem(startTimeInMillis = Calendar.getInstance().timeInMillis+(MILLIS_IN_SECOND*10L), durationInMillis = (MILLIS_IN_SECOND*30L), repeatFlag = 0, scheduleID = 11),
                ActiveScheduleItem(startTimeInMillis = Calendar.getInstance().timeInMillis+(MILLIS_IN_SECOND*15L), durationInMillis = (MILLIS_IN_SECOND*30L), repeatFlag = 0, scheduleID = 12),
                ActiveScheduleItem(startTimeInMillis = Calendar.getInstance().timeInMillis+(MILLIS_IN_SECOND*20L), durationInMillis = (MILLIS_IN_SECOND*30L), repeatFlag = 0, scheduleID = 13),
                ActiveScheduleItem(startTimeInMillis = Calendar.getInstance().timeInMillis+(MILLIS_IN_SECOND*25L), durationInMillis = (MILLIS_IN_SECOND*30L), repeatFlag = 0, scheduleID = 14),
            )*/
            if (_upcomingActiveScheduleItemListStateFlow.value.isNotEmpty())
            {
                setupNextStart(this@SchedulerTrackerService, _upcomingActiveScheduleItemListStateFlow.value.first())
            }
            if (_currentActiveScheduleItemListStateFlow.value.isNotEmpty())
            {
                setupEndStart(this@SchedulerTrackerService, _currentActiveScheduleItemListStateFlow.value.first())
            }
            //setupAlarmsForSchedules(this@SchedulerTrackerService)
            //updateUpcomingSchedules()
        }
    }

    private fun updateUpcomingSchedules() {
        // update by removing started schedules
        val startedSchedules = _upcomingActiveScheduleItemListStateFlow.value.filter { it.startTimeInMillis <= Calendar.getInstance().timeInMillis }
        _currentActiveScheduleItemListStateFlow.value = (_currentActiveScheduleItemListStateFlow.value + startedSchedules).sortedBy { it.startTimeInMillis + it.durationInMillis }

        val finishedSchedules = _currentActiveScheduleItemListStateFlow.value.filter { it.startTimeInMillis + it.durationInMillis <= Calendar.getInstance().timeInMillis }
        _currentActiveScheduleItemListStateFlow.value = _currentActiveScheduleItemListStateFlow.value.filter { it.startTimeInMillis + it.durationInMillis > Calendar.getInstance().timeInMillis }
        _upcomingActiveScheduleItemListStateFlow.value = _upcomingActiveScheduleItemListStateFlow.value.filter { it.startTimeInMillis > Calendar.getInstance().timeInMillis }

        scope.launch {
            database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app_database").build()
            val schedules = database.scheduleItemDao().getAllSchedules().associateBy { it.scheduleId }
            for (i in startedSchedules)
            {
                val contentIntent = Intent(this@SchedulerTrackerService, MainActivity::class.java).apply {
                    putExtra("schedule_id", i.scheduleID)
                    action = MainActivity.TRACK_SCHEDULE
                }

                contentIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

                val notificationIntent = Intent(this@SchedulerTrackerService, SchedulerNotificationService::class.java).apply {
                    putExtra("title", schedules[i.scheduleID]?.title ?: "Error")
                    putExtra("schedule_id", i.scheduleID)
                    putExtra("content_intent", contentIntent)
                    action = SchedulerNotificationService.SCHEDULE_STARTED
                }
                startService(notificationIntent)
            }
            for (i in finishedSchedules)
            {
                val notificationIntent = Intent(this@SchedulerTrackerService, SchedulerNotificationService::class.java).apply {
                    putExtra("schedule_id", i.scheduleID)
                    action = SchedulerNotificationService.SCHEDULE_FINISHED
                }
                startService(notificationIntent)
            }
            database.activeScheduleItemDao().deleteSchedulesBeforeTime()
        }


        Log.d("updateUpcomingSchedules", "upcomingSchedules Left ${_upcomingActiveScheduleItemListStateFlow.value.size}")
        Log.d("updateUpcomingSchedules", "upcomingSchedules Passed ${startedSchedules.size}")
        Log.d("updateUpcomingSchedules", "currentSchedules Active ${_currentActiveScheduleItemListStateFlow.value.size}")
    }
    private fun setupNextStart(context: Context, upcomingSchedule: ActiveScheduleItem) {
        // get alarm manager
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // check if there is an intent pending
        if (nextStartPendingIntent != null)
        {
            alarmManager.cancel(nextStartPendingIntent!!)
        }
        // make a new intent (GC the old one)
        val nextStart = Intent(context, SchedulerTrackerService::class.java).apply {
            putExtra("timeMillis", upcomingSchedule.startTimeInMillis + upcomingSchedule.durationInMillis)
            putExtra("scheduleId", upcomingSchedule.scheduleID)
            action = NEW_SCHEDULE_STARTED
        }
        // make the new pending intent
        val startPendingIntent = PendingIntent.getService(
            context,
            upcomingSchedule.activeScheduleId.toInt(),
            nextStart,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        // set the timer for the pending intent
        Log.d("setupNextStart", "currentTime in millis: ${Calendar.getInstance().timeInMillis}")
        Log.d("setupNextStart", "upcomingTime in millis: ${upcomingSchedule.startTimeInMillis}")
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            upcomingSchedule.startTimeInMillis,
            startPendingIntent
        )
        // remember this new pending intent
        nextStartPendingIntent = startPendingIntent
    }
    private fun setupEndStart(context: Context, currentSchedule: ActiveScheduleItem) {
        // get alarm manager
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // check if there is an intent pending
        if (nextEndPendingIntent != null)
        {
            alarmManager.cancel(nextEndPendingIntent!!)
        }
        // make a new intent (GC the old one)
        val nextStart = Intent(context, SchedulerTrackerService::class.java).apply {
            putExtra("timeMillis", currentSchedule.startTimeInMillis + currentSchedule.durationInMillis)
            putExtra("scheduleId", currentSchedule.scheduleID)
            action = SCHEDULE_ENDED
        }
        // make the new pending intent
        val pendingIntent = PendingIntent.getService(
            context,
            currentSchedule.activeScheduleId.toInt(),
            nextStart,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        // set the timer for the pending intent
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            currentSchedule.startTimeInMillis+currentSchedule.durationInMillis,
            pendingIntent
        )
        // remember this new pending intent
        nextEndPendingIntent = pendingIntent
    }
    fun setupAlarmsForInProgressSchedules(context: Context, inProgressSchedules: List<ActiveScheduleItem>) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        for (schedule in inProgressSchedules) {
            // Set up alarm for schedule start time
            val startIntent = Intent(context, SchedulerTrackerService::class.java).apply {
                putExtra("timeMillis", schedule.startTimeInMillis + schedule.durationInMillis)
                putExtra("scheduleId", schedule.scheduleID)
                //setAction(action) // Set the action for the intent
            }
            val startPendingIntent = PendingIntent.getService(
                context,
                schedule.activeScheduleId.toInt(),
                startIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                schedule.startTimeInMillis,
                startPendingIntent
            )
        }
    }

    fun setupAlarmsForUpcomingSchedules(context: Context, upcomingSchedules: List<ActiveScheduleItem>) {

    }


    @SuppressLint("ScheduleExactAlarm")
    fun setupAlarmsForSchedules(context: Context, schedules: List<ActiveScheduleItem>) {
        Log.d("setupAlarmsForSchedules", "${schedules.size}")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        for (schedule in schedules) {
            // Set up alarm for schedule start time
            val startIntent = Intent(context, SchedulerTrackerService::class.java).apply {
                putExtra("timeMillis", schedule.startTimeInMillis)
                putExtra("scheduleId", schedule.scheduleID)
                //setAction(action) // Set the action for the intent
            }
            val startPendingIntent = PendingIntent.getService(context, schedule.activeScheduleId.toInt(), startIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, schedule.startTimeInMillis, startPendingIntent)

            // Set up alarm for schedule end time
            /*val endIntent = Intent(context, ScheduleNotificationBroadcastReceiver::class.java).apply {
                putExtra("timeMillis", schedule.startTimeInMillis + schedule.durationInMillis)
                putExtra("scheduleId", schedule.scheduleID)
                //setAction(action) // Set the action for the intent
            }
            val endPendingIntent = PendingIntent.getBroadcast(context, schedule.id.toInt() + 1000, endIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, schedule.startTimeInMillis + schedule.durationInMillis, endPendingIntent)

             */
        }
    }

    /*fun testNotifExecutions(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        for (schedule in randomizeActiveScheduleItemFromNowList(10, 360000)) {
            // Set up alarm for schedule start time
            val startIntent = Intent(context, ScheduleNotificationBroadcastReceiver::class.java).apply {
                putExtra("timeMillis", schedule.startTimeInMillis)
                putExtra("scheduleId", schedule.scheduleID)
                //setAction(action) // Set the action for the intent
            }
            val startPendingIntent = PendingIntent.getBroadcast(context,schedule.startTimeInMillis.hashCode(), startIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1000..60000).random(), startPendingIntent)
/*
            // Set up alarm for schedule end time
            val endIntent = Intent(context, SchedulerNotificationService.ScheduleBroadcastReceiver::class.java).apply {
                putExtra("timeMillis", schedule.startTimeInMillis + schedule.durationInMillis)
                putExtra("scheduleId", schedule.scheduleID)
                //setAction(action) // Set the action for the intent
            }
            val endPendingIntent = PendingIntent.getBroadcast(context, 0, endIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1000..60000).random(), endPendingIntent)

 */
        }
    }

    fun testNotifExecutionsServiceVersion(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        for (schedule in randomizeActiveScheduleItemFromNowList(50, 360000)) {

            // Set up alarm for schedule start time
            val startIntent = Intent(context, SchedulerNotificationService::class.java).apply {
                putExtra("timeMillis", schedule.startTimeInMillis)
                putExtra("scheduleId", schedule.scheduleID)
                //setAction(action) // Set the action for the intent
            }
            val startPendingIntent = PendingIntent.getService(context,schedule.scheduleID.toInt(), startIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1000..60000).random(), startPendingIntent)
            /*
                        // Set up alarm for schedule end time
                        val endIntent = Intent(context, SchedulerNotificationService.ScheduleBroadcastReceiver::class.java).apply {
                            putExtra("timeMillis", schedule.startTimeInMillis + schedule.durationInMillis)
                            putExtra("scheduleId", schedule.scheduleID)
                            //setAction(action) // Set the action for the intent
                        }
                        val endPendingIntent = PendingIntent.getBroadcast(context, 0, endIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1000..60000).random(), endPendingIntent)

             */
        }
    }

     */
}