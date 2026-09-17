package com.andyha.musicservice.session

import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession.ControllerInfo
import com.andyha.musicservice.session.notification.NotificationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.system.exitProcess

@AndroidEntryPoint
open class MediaPlaybackService : MediaLibraryService() {
    @Inject
    lateinit var mediaLibrarySession: MediaLibrarySession

    @Inject
    lateinit var notificationManager: NotificationManager

    @UnstableApi
    override fun onCreate() {
        super.onCreate()
        notificationManager.startNotificationService(this)
    }

    override fun onGetSession(controllerInfo: ControllerInfo): MediaLibrarySession {
        return mediaLibrarySession
    }

    @OptIn(UnstableApi::class)
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        notificationManager.stopNotificationService(this)
        mediaLibrarySession.release()
        mediaLibrarySession.player.release()
        stopSelf()
        exitProcess(0)
    }

    @OptIn(UnstableApi::class)
    override fun onDestroy() {
        super.onDestroy()
        notificationManager.stopNotificationService(this)
        mediaLibrarySession.release()
        mediaLibrarySession.player.release()
        exitProcess(0)
    }
}
