package com.andyha.musicservice.browser.state


sealed class ControllerState(
    val soundMuted: Boolean = false,
    val factor: Int = 1,
    val description: String = "",
) {
    data object None : ControllerState()
    data object SeekbarDragging : ControllerState(true)
    data object FastForward4X : ControllerState(true, FAST_FORWARD_4X_FACTOR, "Fast Forward 4x")
    data object FastForward20X : ControllerState(true, FAST_FORWARD_20X_FACTOR, "Fast Forward 20x")
    data object Rewind4X : ControllerState(true, REWIND_4X_FACTOR, "Rewind 4x")
    data object Rewind20X : ControllerState(true, REWIND_20X_FACTOR, "Rewind 20x")

    companion object {
        const val FAST_FORWARD_4X_FACTOR = 4
        const val FAST_FORWARD_20X_FACTOR = 20
        const val REWIND_4X_FACTOR = -4
        const val REWIND_20X_FACTOR = -20
    }
}
