package com.andyha.musicdomain.model

import android.net.Uri
import android.os.Parcelable
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import androidx.core.net.toUri


@OptIn(UnstableApi::class)
@Parcelize
data class SavedMusicItem(
    @SerializedName("id")
    val id: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("isPlayable")
    val isPlayable: Boolean?,

    @SerializedName("isBrowsable")
    val isBrowsable: Boolean?,

    @SerializedName("mediaType")
    val mediaType: Int?,

    @SerializedName("album")
    val album: String? = null,

    @SerializedName("artist")
    val artist: String? = null,

    @SerializedName("genre")
    val genre: String? = null,

    @SerializedName("sourceUri")
    val sourceUri: String? = null,

    @SerializedName("imageUri")
    val imageUri: String? = null,
): Parcelable {

    constructor(mediaItem: MediaItem): this(
        mediaItem.mediaId,
        mediaItem.mediaMetadata.title.toString(),
        mediaItem.mediaMetadata.isPlayable,
        mediaItem.mediaMetadata.isBrowsable,
        mediaItem.mediaMetadata.mediaType,
        mediaItem.mediaMetadata.albumTitle.toString(),
        mediaItem.mediaMetadata.artist.toString(),
        mediaItem.mediaMetadata.genre.toString(),
        mediaItem.localConfiguration?.uri.toString(),
        mediaItem.mediaMetadata.artworkUri.toString(),
    )

    fun toAndroidMediaItem(): MediaItem {
        val metadata =
            MediaMetadata.Builder()
                .setAlbumTitle(album)
                .setTitle(title)
                .setArtist(artist)
                .setGenre(genre)
                .setIsBrowsable(isBrowsable)
                .setIsPlayable(isPlayable)
                .setArtworkUri(imageUri?.toUri())
                .setMediaType(mediaType)
                .build()
        return MediaItem.Builder()
            .setMediaId(id)
            .setMediaMetadata(metadata)
            .setUri(sourceUri?.toUri())
            .build()
    }
}
