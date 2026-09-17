package com.andyha.musicservice.browser.state

import com.andyha.musicservice.browser.MediaPlayerBrowser
import com.andyha.musicservice.browser.state.ControllerState.FastForward20X
import com.andyha.musicservice.browser.state.ControllerState.FastForward4X
import com.andyha.musicservice.browser.state.ControllerState.Rewind20X
import com.andyha.musicservice.browser.state.ControllerState.Rewind4X
import androidx.media3.common.MediaItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds


class FastForwardRewindController(
    private val browser: MediaPlayerBrowser,
    private val currentMediaItem: MutableStateFlow<MediaItem?>,
) {
    private var durationUpdateJob: Job? = null

    private var timer = 0L

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    init {
        coroutineScope.launch {
            currentMediaItem.collect {
                // this is a workaround
                // in case a new media item is just being played, make sure seeking to 0 first,
                // because the browser still keeps the current position of the old song
                if (browser.playerState.value.controllerState !is ControllerState.None) {
                    browser.seekTo(0)
                }
            }
        }
    }

    private fun startDurationUpdates() {
        durationUpdateJob?.cancel()
        durationUpdateJob = coroutineScope.launch {
            durationUpdateFlow()
                .collect { _ ->
                    timer += DURATION_UPDATE_INTERVAL
                    if (timer > DURATION_TO_START_SEEKING_20X) {
                        // After 5000ms, the speed is increased to 20x
                        browser.updatePlayerState {
                            when (it.controllerState) {
                                is FastForward4X -> it.copy(controllerState = FastForward20X)
                                is Rewind4X -> it.copy(controllerState = Rewind20X)
                                else -> it
                            }
                        }
                    }
                    doSeekingInternal()
                }
        }
    }

    private fun durationUpdateFlow(): Flow<Unit> = flow {
        while (true) {
            emit(Unit)
            delay(DURATION_UPDATE_INTERVAL.milliseconds)
        }
    }


    fun fastForward() {
        if (browser.playerState.value.controllerState is ControllerState.None) {
            browser.updatePlayerState { it.copy(controllerState = FastForward4X) }
            startDurationUpdates()
        } else {
            cancel()
        }
    }

    fun rewind() {
        if (browser.playerState.value.controllerState is ControllerState.None) {
            browser.updatePlayerState { it.copy(controllerState = Rewind4X) }
            startDurationUpdates()
        } else {
            cancel()
        }
    }

    private fun cancel() {
        browser.updatePlayerState { it.copy(controllerState = ControllerState.None) }
        durationUpdateJob?.cancel()
        timer = 0L
    }


    private fun doSeekingInternal() {
        val delta = DURATION_UPDATE_INTERVAL * browser.playerState.value.controllerState.factor
        browser.seekTo(
            (browser.browser.currentPosition + delta).let {
                if (it < 0) 0L.also { browser.skipToPrevious() }
                else if (it > browser.browser.duration) browser.browser.duration
                    .also { browser.skipToNext() }
                else it
            },
        )
    }

    companion object {
        const val DURATION_UPDATE_INTERVAL = 100L
        const val DURATION_TO_START_SEEKING_20X = 5000L
    }
}
