package com.example.finalyearproject.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.finalyearproject.R

class NotificationHelper(private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val channelId = "posture_monitor_channel"

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Posture Monitor",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for posture monitoring"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showPostureNotification(message: String) {
        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Posture Alert")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }

    fun showBreakNotification(message: String) {
        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Break Time")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(2, notification)
    }
}