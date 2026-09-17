package com.andyha.musicservice.session.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private var notificationManager: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    companion object {
        private const val NOTIFICATION_ID = 200
        private const val NOTIFICATION_CHANNEL_NAME = "demo_music_channel"
        private const val NOTIFICATION_CHANNEL_ID = "demo_music_channel_id"
    }

    init {
        val channel =
            NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT,
            )
        notificationManager.createNotificationChannel(channel)
    }

    @UnstableApi
    fun startNotificationService(mediaSessionService: MediaSessionService) {
        val notification =
            Notification.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setAutoCancel(true)
                .build()
        mediaSessionService.startForeground(NOTIFICATION_ID, notification)
    }

    @UnstableApi
    fun stopNotificationService(mediaSessionService: MediaSessionService) {
        notificationManager.deleteNotificationChannel(NOTIFICATION_CHANNEL_ID)
        mediaSessionService.stopForeground(Service.STOP_FOREGROUND_DETACH)
    }
}
