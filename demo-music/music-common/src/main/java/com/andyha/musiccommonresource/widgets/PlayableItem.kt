package com.andyha.musiccommonresource.widgets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaMetadata
import com.andyha.musiccommonresource.model.MediaRowUi
import com.andyha.musiccommonresource.utils.Constants.appHorizontalPadding
import com.andyha.musiccommonresource.utils.MediaThumbnail

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayableItem(
    row: MediaRowUi,
    modifier: Modifier,
    isPlaying: Boolean = true,
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(appHorizontalPadding),
        ) {
            MediaThumbnail(row, 48.dp)

            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    style = if (isPlaying)
                        MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    else
                        MaterialTheme.typography.titleMedium,
                    modifier = if (isPlaying) Modifier.basicMarquee() else Modifier,
                    color = if (isPlaying) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    maxLines = 1,
                    overflow = if (isPlaying) TextOverflow.Clip else TextOverflow.Ellipsis,
                    text = row.title,
                )
                Text(
                    modifier = if (isPlaying) Modifier.basicMarquee() else Modifier,
                    maxLines = 1,
                    overflow = if (isPlaying) TextOverflow.Clip else TextOverflow.Ellipsis,
                    color = if (isPlaying)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface,
                    text = row.subtitle,
                )
            }
        }
        HorizontalDivider(
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = appHorizontalPadding)
        )
    }
}

@Preview
@Composable
fun PlayableMediaItemPreview() {
    PlayableItem(
        row = MediaRowUi(
            mediaId = "mediaId",
            title = "Title",
            subtitle = "Artist",
            artworkUri = null,
            isBrowsable = false,
            mediaType = MediaMetadata.MEDIA_TYPE_MUSIC,
        ),
        modifier = Modifier,
        isPlaying = false,
    )
}
