package com.andyha.musicservice.browser.state


import androidx.compose.runtime.Immutable

/**
 * Immutable snapshot of the player. Updates go through [MutableStateFlow.update] so that
 * [StateFlow] can conflate identical values, and so Compose can skip consumers that did not change.
 */
@Immutable
data class PlayerState(
    val playbackPosition: PlaybackPosition = PlaybackPosition.zero,
    val playbackState: PlaybackState = PlaybackState.Initial,
    val controllerState: ControllerState = ControllerState.None,
    val repeatMode: RepeatMode = RepeatMode.Off,
    val shuffleMode: ShuffleMode = ShuffleMode.Off,
) {
    fun canPlay(): Boolean {
        return playbackState == PlaybackState.Ready || playbackState == PlaybackState.Pause
    }
}
