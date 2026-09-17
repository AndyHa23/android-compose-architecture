package com.andyha.feature.musiclist.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import com.andyha.coreresource.R as CoreR
import com.andyha.coreui.base.context.BaseContext
import com.andyha.feature.musiclist.R
import com.andyha.musiccommonresource.model.MediaRowUi
import com.andyha.musiccommonresource.utils.Route.MediaDetails
import com.andyha.musiccommonresource.widgets.BrowsableItem
import com.andyha.musiccommonresource.widgets.LoadingIndicator
import com.andyha.musiccommonresource.widgets.PlayableItem

@UnstableApi
@Composable
fun MediaListScreen(
    context: BaseContext,
    paddingValues: PaddingValues,
    parentId: String,
    mediaType: Int,
    key: String = "",
    viewModel: MediaListViewModel = hiltViewModel<MediaListViewModel>(key = "$parentId-$key"),
) {
    val mediaRows by viewModel.mediaRows.collectAsStateWithLifecycle()
    val currentMediaId by viewModel.currentMediaId.collectAsStateWithLifecycle()
    val uiState by viewModel.mediaListUiState.collectAsStateWithLifecycle()

    val navController = context.navController

    // Hoisted so the row callback keeps a stable identity across recompositions;
    // an inline lambda capturing the view model would defeat row skipping.
    val onRowClick: (MediaRowUi) -> Unit = remember(viewModel, navController) {
        { row ->
            if (row.isBrowsable) {
                val destination = navController.graph.findNode(MediaDetails.route)
                val mediaItem = viewModel.mediaItemFor(row.mediaId)
                if (destination != null && mediaItem != null) {
                    navController.navigate(destination.id, mediaItem.toBundle())
                }
            } else {
                viewModel.seekAndPlay(row.mediaId)
            }
        }
    }
    val onRetry: () -> Unit = remember(viewModel) { viewModel::retry }

    LaunchedEffect(uiState, parentId, mediaType) {
        if (uiState is MediaListUiState.Ready) {
            viewModel.loadInitialMediaItems(parentId, mediaType)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        when (uiState) {
            is MediaListUiState.NotReady -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    LoadingIndicator()
                    Text(
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp),
                        text = stringResource(id = CoreR.string.common_loading),
                    )
                }
            }

            is MediaListUiState.Failed -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                ) {
                    Text(
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground,
                        text = stringResource(id = R.string.media_list_load_failed),
                    )
                    Button(
                        modifier = Modifier.padding(top = 16.dp),
                        onClick = onRetry,
                    ) {
                        Text(
                            style = MaterialTheme.typography.labelLarge,
                            text = stringResource(id = CoreR.string.common_try_again_connection),
                        )
                    }
                }
            }

            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding()
                                + if (currentMediaId != null) 60.dp else 0.dp
                    )
                ) {
                    items(mediaRows, key = { it.mediaId }) { row ->
                        MediaItemRow(
                            row = row,
                            isPlaying = currentMediaId == row.mediaId,
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
        modifier =
            Modifier
                .clickable(onClick = { onClick(row) })
                .fillMaxWidth()
    ) {
        if (row.isBrowsable) {
            BrowsableItem(row)
        } else {
            PlayableItem(row, Modifier, isPlaying)
        }
    }
}
