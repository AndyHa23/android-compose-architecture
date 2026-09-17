package com.andyha.musiccommonresource.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.andyha.musiccommonresource.model.MediaRowUi
import com.andyha.musiccommonresource.utils.Constants.appHorizontalPadding
import com.andyha.musiccommonresource.utils.MediaThumbnail

@Composable
fun BrowsableItem(row: MediaRowUi) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(appHorizontalPadding),
        ) {
            MediaThumbnail(row, 48.dp)
            Text(
                style = MaterialTheme.typography.titleMedium,
                text = row.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 16.dp),
            )
        }
        HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(horizontal = appHorizontalPadding))
    }
}
