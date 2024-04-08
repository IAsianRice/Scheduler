package com.example.scheduler.receiver

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.format.DateUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.scheduler.R
import com.example.scheduler.services.SchedulerNotificationService

class ScheduleNotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val timeMillis = intent!!.getLongExtra("timeMillis", 0)
        val scheduleId = intent!!.getLongExtra("scheduleId", 0)
        Log.d("Testing Notifications", "HMMM")
        val subNotificationBuilder = NotificationCompat.Builder(context!!,
            SchedulerNotificationService.channelId
        )
            .setContentTitle("$scheduleId")
            .setContentText( DateUtils.formatDateTime(context, timeMillis, DateUtils.FORMAT_SHOW_TIME))
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setGroup(SchedulerNotificationService.groupId)
            .setAutoCancel(true)

        updateNotification(context, subNotificationBuilder.build())
    }

    private fun updateNotification(context: Context?, notification: Notification, notificationId: Int = 1000) {
        val notificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notification)
    }
}