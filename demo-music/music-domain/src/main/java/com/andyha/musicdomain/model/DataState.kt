package com.andyha.musicdomain.model

sealed class DataState {
    // Data has not been fetched from remote server
    data object Loading : DataState()

    // Data has been fetched and ready to use
    data object Ready : DataState()

    // Data could not be fetched or the media tree could not be built.
    // [cause] is null when the source completed without ever producing a catalog.
    data class Failed(val cause: Throwable? = null) : DataState()
}
