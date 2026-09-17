package com.andyha.musicservice.session.di

import android.app.Service
import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.session.MediaLibraryService
import com.andyha.musicservice.session.MediaPlaybackService
import com.andyha.musicservice.session.MediaPlaybackSessionCallback
import com.andyha.musicservice.session.notification.NotificationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
class ServiceModule {
    @Provides
    @ServiceScoped
    fun provideAudioAttributes(): AudioAttributes = AudioAttributes.Builder()
        .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()

    @Provides
    @ServiceScoped
    @UnstableApi
    fun providePlayer(
        @ApplicationContext context: Context,
        audioAttributes: AudioAttributes,
    ): Player = ExoPlayer.Builder(context)
        .setAudioAttributes(audioAttributes, true)
        .setHandleAudioBecomingNoisy(true)
        .setTrackSelector(DefaultTrackSelector(context))
        .build()

    @Provides
    @ServiceScoped
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager =
        NotificationManager(
            context = context,
        )

    @Provides
    @ServiceScoped
    fun provideMediaLibrarySession(
        service: Service,
        player: Player,
        callback: MediaPlaybackSessionCallback,
    ): MediaLibraryService.MediaLibrarySession {
        return MediaLibraryService
            .MediaLibrarySession
            .Builder(service as MediaPlaybackService, player, callback)
            .build()
    }
}
