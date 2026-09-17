package com.andyha.musiccommonresource.utils

sealed class Route(val route: String) {
    data object Main : Route(MAIN_ROUTE)
    data object MediaDetails : Route(MEDIA_DETAILS_ROUTE)
    data object NowPlaying : Route(NOW_PLAYING)

    companion object {
        const val MAIN_ROUTE = "main"
        const val MEDIA_DETAILS_ROUTE = "media-details"
        const val NOW_PLAYING = "now-playing"
    }
}