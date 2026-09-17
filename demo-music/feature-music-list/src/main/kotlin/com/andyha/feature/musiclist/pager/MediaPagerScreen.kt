package com.andyha.feature.musiclist.pager

import androidx.annotation.StringRes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.AppBarDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import com.andyha.coreui.base.context.BaseContext
import com.andyha.feature.musiclist.R
import com.andyha.feature.musiclist.list.MediaListScreen
import com.andyha.feature.musiclist.pager.MediaListPageType.Album
import com.andyha.feature.musiclist.pager.MediaListPageType.Artist
import com.andyha.feature.musiclist.pager.MediaListPageType.Genre
import com.andyha.feature.musiclist.pager.MediaListPageType.Song
import com.andyha.musiccommonresource.utils.Constants.bottomNavigationBarHeight
import com.andyha.musicdomain.constants.Constants.ALBUM_ID
import com.andyha.musicdomain.constants.Constants.ALL_SONGS
import com.andyha.musicdomain.constants.Constants.ARTIST_ID
import com.andyha.musicdomain.constants.Constants.GENRE_ID
import kotlinx.coroutines.launch


enum class MediaListPageType(val id: String, @StringRes val title: Int, val mediaType: Int = -1) {
    Song(ALL_SONGS, R.string.song_title, MediaMetadata.MEDIA_TYPE_MUSIC),
    Album(ALBUM_ID, R.string.album_title, MediaMetadata.MEDIA_TYPE_ALBUM),
    Artist(ARTIST_ID, R.string.artist_title, MediaMetadata.MEDIA_TYPE_ARTIST),
    Genre(GENRE_ID, R.string.genre_title, MediaMetadata.MEDIA_TYPE_GENRE),
    Settings("", R.string.settings_title),
}

@UnstableApi
@Composable
fun MediaListPagerScreen(
    context: BaseContext,
    settingsPage: @Composable () -> Unit,
) {
    Surface(
        modifier = Modifier.navigationBarsPadding()
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { _ ->
            MediaListPagerContent(context, settingsPage)
        }
    }
}

private const val TAB_SWITCH_ANIM_DURATION = 300

@UnstableApi
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaListPagerContent(
    context: BaseContext,
    settingsPage: @Composable () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val mediaCollectionTypeValues = MediaListPageType.entries.toTypedArray()
    val pagerState = rememberPagerState(initialPage = Song.ordinal) {
        mediaCollectionTypeValues.size
    }
    val language by context.app.configurations.language.collectAsState()

    key(language) {
        Scaffold(
            bottomBar = {
                MediaTabBar(
                    pagerState.currentPage,
                    onTabSelected = { newTab ->
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(
                                newTab.ordinal,
                                animationSpec = tween(TAB_SWITCH_ANIM_DURATION),
                            )
                        }
                    },
                )
            },
        ) { paddingValues ->
            HorizontalPager(
                state = pagerState,
                beyondBoundsPageCount = 1
            ) { page ->
                when (val pageType = mediaCollectionTypeValues[page]) {
                    Song, Album, Artist, Genre -> {
                        MediaListScreen(
                            context,
                            paddingValues,
                            pageType.id,
                            pageType.mediaType
                        )
                    }

                    MediaListPageType.Settings -> {
                        settingsPage()
                    }
                }
            }
        }
    }
}

@Composable
fun MediaTabBar(
    tabSelected: Int,
    onTabSelected: (MediaListPageType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colorScheme.surface)
    ) {
        MediaTab(
            modifier = Modifier
                .height(bottomNavigationBarHeight)
                .align(Alignment.CenterVertically),
            items = MediaListPageType.entries,
            tabSelected = tabSelected,
            onTabSelected = { newTab -> onTabSelected(MediaListPageType.entries[newTab.ordinal]) },
        )
    }
}

@Composable
fun MediaTab(
    modifier: Modifier,
    items: List<MediaListPageType>,
    tabSelected: Int,
    onTabSelected: (MediaListPageType) -> Unit,
) {
    TabRow(
        selectedTabIndex = tabSelected,
        modifier = modifier.shadow(elevation = AppBarDefaults.BottomAppBarElevation),
        containerColor = colorScheme.background,
        contentColor = colorScheme.onBackground,
        divider = { },
    ) {
        items.forEachIndexed { index, item ->
            val selected = index == tabSelected

            Tab(
                modifier = Modifier.height(bottomNavigationBarHeight),
                selected = selected,
                onClick = { onTabSelected(item) },
            ) {
                Text(
                    modifier = Modifier
                        .wrapContentHeight(align = Alignment.CenterVertically)
                        .align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.labelMedium,
                    color = if (selected) colorScheme.primary else colorScheme.onBackground,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                    text = stringResource(item.title),
                )
            }
        }
    }
}
