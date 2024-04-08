package com.example.scheduler.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.scheduler.R
import kotlinx.coroutines.SupervisorJob
import java.util.Timer
import kotlin.random.Random


class SchedulerNotificationService : Service(){

    private val timer = Timer()
    private val job = SupervisorJob()
    private val scheduleNotificationIds = mutableMapOf<Long, Int>()
    /*private val scope = CoroutineScope(Dispatchers.IO + job)
    private lateinit var activeScheduleItems: List<ActiveScheduleItem>
    private val pendingIntentMap = HashMap<Int, PendingIntent>()
    private lateinit var upcomingActiveScheduleItems: List<ActiveScheduleItem>
    private lateinit var currentActiveScheduleItems: List<ActiveScheduleItem>

    private val timeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            /*if (intent?.action == TimeService.ACTION_TIME_CHANGED) {
                val currentTime = intent.getLongExtra(TimeService.EXTRA_TIME, 0)
                // Handle time change and trigger notifications
            }*/
        }
    }*/
    companion object {
        val channelId = "ForegroundServiceChannel"
        val groupId = "scheduler_group_id"
        const val SCHEDULE_STARTED = "com.example.scheduler.SCHEDULE_STARTED"
        const val SCHEDULE_FINISHED = "com.example.scheduler.SCHEDULE_FINISHED"

    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        // Cancel the timer when the service is destroyed
        timer.cancel()
        job.cancel()
        super.onDestroy()
    }
    // On Start
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Schedule periodic work to check for upcoming schedules
        /*val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        startForeground(notificationId, createNotification("Last Updated: ($currentTime)"))
        trackSchedules()*/
        when (intent?.action) {
            SCHEDULE_STARTED -> {
                val schedule_id = intent.getLongExtra("schedule_id", -1L)
                val notificationId = (Random.nextInt() % 1000) + 1001 // Generate a unique notification ID
                scheduleNotificationIds[schedule_id] = notificationId
                val title = intent.getStringExtra("title")
                val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager



                // Create a PendingIntent to handle the click action
                val pendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableExtra("content_intent", Intent::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        intent.getParcelableExtra<Intent>("content_intent")
                    },
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                val notification = NotificationCompat.Builder(this, channelId)
                    .setContentTitle("$title Has Started!")
                    .setContentText( "Tap here to track $title schedule!")
                    .setSmallIcon(R.drawable.app_icon)
                    .setGroup(groupId)
                    .setContentIntent(pendingIntent) // Set the PendingIntent
                    .build()

                notificationManager.notify(notificationId, notification)



                // Show the summary notification
                val inboxStyle = NotificationCompat.InboxStyle()
                val summaryNotificationBuilder = NotificationCompat.Builder(this, channelId)
                    .setGroup(groupId)
                    .setGroupSummary(true)
                    .setStyle(inboxStyle)
                    .setSmallIcon(R.drawable.app_icon)
                    .setAutoCancel(true)
                    .build()
                notificationManager.notify(1000, summaryNotificationBuilder)
            }
            SCHEDULE_FINISHED -> {
                val schedule_id = intent.getLongExtra("schedule_id", -1L)
                val notificationId = scheduleNotificationIds[schedule_id]
                scheduleNotificationIds.remove(schedule_id)
                val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (notificationId != null)
                {
                    notificationManager.cancel(notificationId)
                }
            }
        }

        return START_STICKY
    }
    private fun sendStartNotification() {
/*
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Create the InboxStyle object for the main notification
        val expandedView = RemoteViews(packageName, R.layout.expanded_notification)
        expandedView.setTextViewText(
            R.id.timestamp,
            DateUtils.formatDateTime(this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME)
        )
        val collapsedView = RemoteViews(packageName, R.layout.collapsed_notification)
        collapsedView.setTextViewText(
            R.id.timestamp,
            DateUtils.formatDateTime(this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME)
        )

        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, channelId) // these are the three things a NotificationCompat.Builder object requires at a minimum
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setGroup(groupId)
                .setContentTitle("Test Notif")
                .setContentText("Hey Man") // notification will be dismissed when tapped
                .setAutoCancel(true) // tapping notification will open MainActivity
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView) // setting style to DecoratedCustomViewStyle() is necessary for custom views to display
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())

        // retrieves android.app.NotificationManager
        val subNotifications = listOf<String>("The Crazy", "Not So much", "Wow!")
        val inboxStyle = NotificationCompat.InboxStyle()
        inboxStyle.setBigContentTitle("Notifications")

        // Add each sub-notification to the InboxStyle
        for (subNotification in subNotifications) {
            inboxStyle.addLine(subNotification)
        }


        // Build the main notification
        val mainNotificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Notifications")
            .setSmallIcon(androidx.core.R.drawable.notification_bg)
            .setStyle(inboxStyle)
            //.setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setGroup(groupId)

        // Get the NotificationManager
        // Show the main notification
        notificationManager.notify(notificationId, builder.build())
        notificationManager.notify(notificationId + 1, mainNotificationBuilder.build())

        // Build sub-notifications
        for (i in subNotifications.indices) {
            val subNotificationBuilder = NotificationCompat.Builder(this, channelId)
                .setContentTitle("Title?")
                .setContentText(subNotifications[i])
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setGroup(groupId)
                .setAutoCancel(true)

            // Show each sub-notification
            notificationManager.notify(notificationId + i + 2, subNotificationBuilder.build())
        }

        // Create a summary notification for the group
        val summaryNotificationBuilder = NotificationCompat.Builder(this, channelId)
            .setGroup(groupId)
            .setGroupSummary(true)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setAutoCancel(true)

        // Show the summary notification
        updateNotification(this, summaryNotificationBuilder.build(), 1000)*/

        val inboxStyle = NotificationCompat.InboxStyle()

        val summaryNotificationBuilder = NotificationCompat.Builder(this, channelId)
            .setGroup(groupId)
            .setGroupSummary(true)
            .setStyle(inboxStyle)
            .setSmallIcon(R.drawable.app_icon)
            .setAutoCancel(true)

        // Show the summary notification
        //notificationManager.notify(1000, summaryNotificationBuilder.build())
        //updateNotification(summaryNotificationBuilder.build(), 1000)
    }

    // Update The Notification Format
    override fun onBind(intent: Intent): IBinder? {
        return null
    }
/*
    private fun animateNotification(contentView: RemoteViews) {
        // Animate the icon
        val iconAnimator = ObjectAnimator.ofFloat(contentView, "Alpha", 0.2f, 1f).apply {
            duration = 1000 // Animation duration in milliseconds
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

        // Start the icon animation
        iconAnimator.start()
    }
*/
}