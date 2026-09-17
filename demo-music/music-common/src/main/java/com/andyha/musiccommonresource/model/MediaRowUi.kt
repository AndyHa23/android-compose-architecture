package com.andyha.musiccommonresource.model

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata

/**
 * Stable snapshot of the [MediaItem] fields a list row renders.
 *
 * media3's [MediaItem] is an external type that the Compose compiler must treat as
 * unstable, which stops every row in a list from ever skipping recomposition. Rows take
 * this model instead so they can skip when their content hasn't changed.
 */
@Immutable
data class MediaRowUi(
    val mediaId: String,
    val title: String,
    val subtitle: String,
    val artworkUri: Uri?,
    val isBrowsable: Boolean,
    val mediaType: Int,
)

fun MediaItem.toMediaRowUi(): MediaRowUi = MediaRowUi(
    mediaId = mediaId,
    title = mediaMetadata.title?.toString().orEmpty(),
    subtitle = mediaMetadata.artist?.toString().orEmpty(),
    artworkUri = mediaMetadata.artworkUri,
    isBrowsable = mediaMetadata.isBrowsable == true,
    mediaType = mediaMetadata.mediaType ?: MediaMetadata.MEDIA_TYPE_MIXED,
)
