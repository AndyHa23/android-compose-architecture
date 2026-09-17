package com.andyha.feature.musicplayer.ui.playlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andyha.coreresource.R
import com.andyha.musiccommonresource.model.MediaRowUi
import com.andyha.musiccommonresource.widgets.BrowsableItem
import com.andyha.musiccommonresource.widgets.LoadingIndicator
import com.andyha.musiccommonresource.widgets.PlayableItem


@Composable
fun PlayListScreen(
    viewModel: PlayListViewModel = hiltViewModel<PlayListViewModel>(),
) {
    val mediaRows = viewModel.playListRows.collectAsStateWithLifecycle()
    val currentMediaId = viewModel.currentMediaId.collectAsStateWithLifecycle()
    val uiState = viewModel.mediaListUiState.collectAsStateWithLifecycle()

    val onRowClick: (MediaRowUi) -> Unit = remember(viewModel) {
        { row -> viewModel.seekAndPlay(row.mediaId) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        when (uiState.value) {
            is PlayListUiState.NotReady -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    LoadingIndicator()
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = stringResource(id = R.string.common_loading),
                        fontSize = TextUnit(18f, TextUnitType.Sp),
                    )
                }
            }

            else -> {
                LazyColumn {
                    items(mediaRows.value, key = { it.mediaId }) { row ->
                        MediaItemRow(
                            row = row,
                            isPlaying = currentMediaId.value == row.mediaId,
                            onClick = onRowClick,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MediaItemRow(row: MediaRowUi, isPlaying: Boolean, onClick: (MediaRowUi) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(onClick = { onClick(row) })
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        if (row.isBrowsable) {
            BrowsableItem(row)
        } else {
            PlayableItem(row, Modifier, isPlaying)
        }
    }
}
