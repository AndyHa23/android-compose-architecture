package com.andyha.featuremusiclistui.details

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata.MEDIA_TYPE_ARTIST
import androidx.media3.common.util.UnstableApi
import com.andyha.coreui.base.context.BaseContext
import com.andyha.feature.musiclist.list.MediaListScreen
import com.andyha.feature.musiclist.pager.MediaListPageType
import com.andyha.feature.musiclist.pager.MediaTab
import com.andyha.musiccommonresource.utils.Constants.bottomNavigationBarHeight
import kotlinx.coroutines.launch
import timber.log.Timber


@UnstableApi
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MusicDetailsScreen(context: BaseContext, mediaItem: MediaItem) {
    val coroutineScope = rememberCoroutineScope()
    val pagerItems = when (mediaItem.mediaMetadata.mediaType) {
        MEDIA_TYPE_ARTIST -> listOf(MediaListPageType.Song, MediaListPageType.Album)
        else -> listOf(MediaListPageType.Song)
    }
    val pagerState = rememberPagerState(initialPage = 0) { pagerItems.size }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(AppBarDefaults.TopAppBarElevation),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
                    .copy(containerColor = MaterialTheme.colorScheme.background),
                title = {
                    Text(
                        text = mediaItem.mediaMetadata.title.toString(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.basicMarquee(),
                        maxLines = 1,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { context.navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIosNew,
                            modifier = Modifier.size(24.dp),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        Column {
            Timber.d("pagerItems.size=${pagerItems.size}")
            if (pagerItems.size > 1) {
                MediaDetailsTabBar(
                    modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
                    pagerState.currentPage,
                    onTabSelected = { newTab ->
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(newTab, animationSpec = tween(300))
                        }
                    },
                    pagerItems = pagerItems,
                )
            }

            HorizontalPager(state = pagerState) { page ->
                MediaListScreen(
                    context,
                    PaddingValues(top = 0.dp, bottom = paddingValues.calculateBottomPadding()),
                    mediaItem.mediaId,
                    pagerItems[page].mediaType,
                    "$page"
                )
            }
        }
    }
}

@Composable
fun MediaDetailsTabBar(
    modifier: Modifier,
    tabSelected: Int,
    onTabSelected: (Int) -> Unit,
    pagerItems: List<MediaListPageType>,
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        MediaTab(
            modifier = Modifier
                .height(bottomNavigationBarHeight)
                .align(Alignment.CenterVertically),
            items = pagerItems,
            tabSelected = tabSelected,
            onTabSelected = { newTab -> onTabSelected(pagerItems.indexOf(newTab)) },
        )
    }
}