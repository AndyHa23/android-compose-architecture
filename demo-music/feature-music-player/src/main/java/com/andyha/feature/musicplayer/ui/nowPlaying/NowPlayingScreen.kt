package com.andyha.feature.musicplayer.ui.nowPlaying

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andyha.musiccommonresource.utils.Constants.appHorizontalPadding
import com.andyha.musiccommonresource.utils.MediaThumbnail
import com.andyha.musiccommonresource.widgets.LoadingIndicator


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NowPlayingScreen(
    viewModel: NowPlayingViewModel = hiltViewModel(),
    onClickBack: () -> Unit,
) {
    val currentItem by viewModel.currentItem.collectAsStateWithLifecycle()
    val uiState by viewModel.nowPlayingUiState.collectAsStateWithLifecycle()

    Surface {
        when (uiState) {
            is NowPlayingUiState.Ready -> {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            modifier = Modifier.fillMaxWidth(),
                            title = {},
                            navigationIcon = {
                                IconButton(onClick = onClickBack, modifier = Modifier.size(36.dp)) {
                                    Icon(
                                        modifier = Modifier.size(24.dp),
                                        imageVector = Icons.Filled.KeyboardArrowDown,
                                        contentDescription = "hide NowPlayingScreen",
                                        tint = MaterialTheme.colorScheme.onSurface,
                                    )
                                }
                            })
                    },
                    bottomBar = { NowPlayingController() },
                ) { padding ->
                    Column(
                        modifier = Modifier.padding(padding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween,
                        ) {
                            currentItem?.let { MediaThumbnail(it, 200.dp) }
                            Text(
                                text = currentItem?.mediaMetadata?.title.toString(),
                                style = MaterialTheme.typography.titleLarge,
                                maxLines = 1,
                                modifier = Modifier
                                    .basicMarquee()
                                    .padding(top = 24.dp),
                            )
                            Text(
                                text = currentItem?.mediaMetadata?.artist.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                modifier = Modifier
                                    .basicMarquee()
                                    .padding(top = 24.dp),

                                )
                        }
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = appHorizontalPadding),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            NowPlayingSeekBar()
                        }
                    }
                }
            }

            else -> {
                LoadingIndicator()
            }
        }
    }
}
