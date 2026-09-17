package com.andyha.musiccommonresource.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.andyha.musiccommonresource.model.MediaRowUi
import com.andyha.musiccommonresource.model.toMediaRowUi


@Composable
fun MediaThumbnail(row: MediaRowUi, size: Dp) {
    val context = LocalContext.current
    val imageRequest = remember(context, row.artworkUri) {
        ImageRequest.Builder(context)
            .data(row.artworkUri)
            .crossfade(true)
            .build()
    }
    val placeholder = rememberVectorPainter(mediaThumbnailPlaceholder(row.mediaType))

    AsyncImage(
        model = imageRequest,
        contentDescription = "Thumbnail",
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp)),
        contentScale = ContentScale.Crop,
        placeholder = placeholder,
        fallback = placeholder,
        error = placeholder,
    )
}

/**
 * Convenience overload for the single-item player surfaces, where row skipping isn't a
 * concern. List rows should pass a [MediaRowUi] directly.
 */
@Composable
fun MediaThumbnail(mediaItem: MediaItem, size: Dp) {
    MediaThumbnail(remember(mediaItem) { mediaItem.toMediaRowUi() }, size)
}

fun mediaThumbnailPlaceholder(mediaType: Int): ImageVector {
    return when (mediaType) {
        MediaMetadata.MEDIA_TYPE_ALBUM -> Icons.Filled.Album
        MediaMetadata.MEDIA_TYPE_ARTIST -> Icons.Filled.Person
        else -> Icons.Filled.MusicNote
    }
}