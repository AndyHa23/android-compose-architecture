package com.andyha.musicdata.repository

import android.net.Uri
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.andyha.corenetwork.di.IoDispatcher
import com.andyha.musicdata.datasource.remote.MusicDataSource
import com.andyha.musicdomain.constants.Constants
import com.andyha.musicdomain.model.DataState
import com.andyha.musicdomain.model.Music
import com.andyha.musicdomain.repository.MediaTreeRepository
import com.google.common.collect.ImmutableList
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


class MediaTreeRepositoryImpl @Inject constructor(
    private val musicDatasource: MusicDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : MediaTreeRepository {

    private val coroutineScope = CoroutineScope(
        ioDispatcher + SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
            Timber.e(throwable, "MediaTreeRepository scope failure")
        }
    )

    private var treeNodes: MutableMap<String, MediaItemNode> = mutableMapOf()

    private var titleMap: MutableMap<String, MediaItemNode> = mutableMapOf()

    private val _dataState = MutableStateFlow<DataState>(DataState.Loading)
    override val dataState = _dataState.asStateFlow()

    private var loadJob: Job? = null

    inner class MediaItemNode(val item: MediaItem) {
        val searchTitle = normalizeSearchText(item.mediaMetadata.title)
        val searchText =
            StringBuilder()
                .append(searchTitle)
                .append(" ")
                .append(normalizeSearchText(item.mediaMetadata.artist))
                .append(" ")
                .append(normalizeSearchText(item.mediaMetadata.albumArtist))
                .append(" ")
                .append(normalizeSearchText(item.mediaMetadata.albumTitle))
                .toString()

        private val children: MutableSet<MediaItem> = mutableSetOf()

        fun addChild(childID: String) {
            treeNodes[childID]?.let { this.children.add(it.item) }
        }

        fun getChildren(): List<MediaItem> {
            return ImmutableList.copyOf(children)
        }
    }

    init {
        refresh()
    }

    override fun refresh() {
        loadJob?.cancel()
        loadJob = coroutineScope.launch {
            _dataState.value = DataState.Loading
            var receivedCatalog = false

            runCatching {
                musicDatasource.getRemoteMusic().collect { musicList ->
                    receivedCatalog = true
                    Timber.d("collect MediaList: ${musicList.size} items")
                    reset()
                    initializeRoot()
                    musicList.forEach { item -> addNodeToTree(item) }
                    _dataState.emit(DataState.Ready)
                }
            }.onFailure { error ->
                if (error is CancellationException) throw error
                // Never leave a partially built tree behind: the UI would show
                // an inconsistent list instead of the error state.
                Timber.e(error, "Failed to load media tree")
                reset()
                _dataState.emit(DataState.Failed(error))
            }.onSuccess {
                // The source completed without ever producing a catalog. Without this
                // the UI would sit on the loading state forever with nothing to retry.
                if (!receivedCatalog) {
                    Timber.e("Music catalog source completed without emitting")
                    reset()
                    _dataState.emit(DataState.Failed(null))
                }
            }
        }
    }

    private fun initializeRoot() {
        // create root and folders for songs/albums/artists

        treeNodes[Constants.ROOT_ID] =
            MediaItemNode(
                buildMediaItem(
                    title = "Root",
                    mediaId = Constants.ROOT_ID,
                    isPlayable = false,
                    isBrowsable = true,
                    mediaType = MediaMetadata.MEDIA_TYPE_FOLDER_MIXED,
                ),
            )

        treeNodes[Constants.ALBUM_ID] =
            MediaItemNode(
                buildMediaItem(
                    title = "Album",
                    mediaId = Constants.ALBUM_ID,
                    isPlayable = false,
                    isBrowsable = true,
                    mediaType = MediaMetadata.MEDIA_TYPE_FOLDER_ALBUMS,
                ),
            )
        treeNodes[Constants.ARTIST_ID] =
            MediaItemNode(
                buildMediaItem(
                    title = "Artist",
                    mediaId = Constants.ARTIST_ID,
                    isPlayable = false,
                    isBrowsable = true,
                    mediaType = MediaMetadata.MEDIA_TYPE_FOLDER_ARTISTS,
                ),
            )
        treeNodes[Constants.GENRE_ID] =
            MediaItemNode(
                buildMediaItem(
                    title = "Genre",
                    mediaId = Constants.GENRE_ID,
                    isPlayable = false,
                    isBrowsable = true,
                    mediaType = MediaMetadata.MEDIA_TYPE_FOLDER_GENRES,
                ),
            )

        // Add root nodes
        treeNodes[Constants.ROOT_ID]?.addChild(Constants.ALBUM_ID)
        treeNodes[Constants.ROOT_ID]?.addChild(Constants.ARTIST_ID)
        treeNodes[Constants.ROOT_ID]?.addChild(Constants.GENRE_ID)
    }

    private fun buildMediaItem(
        title: String,
        mediaId: String,
        isPlayable: Boolean,
        isBrowsable: Boolean,
        mediaType: @MediaMetadata.MediaType Int,
        album: String? = null,
        artist: String? = null,
        genre: String? = null,
        sourceUri: Uri? = null,
        imageUri: Uri? = null,
    ): MediaItem {
        val metadata = MediaMetadata.Builder()
            .setAlbumTitle(album)
            .setTitle(title)
            .setArtist(artist)
            .setGenre(genre)
            .setIsBrowsable(isBrowsable)
            .setIsPlayable(isPlayable)
            .setArtworkUri(imageUri)
            .setMediaType(mediaType)
            .build()

        return MediaItem.Builder()
            .setMediaId(mediaId)
            .setMediaMetadata(metadata)
            .setUri(sourceUri)
            .build()
    }

    private fun addNodeToTree(item: Music) {
        val id = item.id
        val album = item.album
        val title = item.title
        val artist = item.artist
        val genre = item.genre
        val sourceUri = item.source.toUri()

        // key of such items in tree
        val songIdInTree = id
        val albumIdInTree = "${Constants.ALBUM_PREFIX}$album"
        val artistIdInTree = "${Constants.ARTIST_PREFIX}$artist"
        val genreIdInTree = "${Constants.GENRE_ID}$genre"

        // Build song node
        treeNodes[songIdInTree] =
            MediaItemNode(
                buildMediaItem(
                    title = title,
                    mediaId = songIdInTree,
                    isPlayable = true,
                    isBrowsable = false,
                    mediaType = MediaMetadata.MEDIA_TYPE_MUSIC,
                    album = album,
                    artist = artist,
                    genre = genre,
                    sourceUri = sourceUri,
                    imageUri = item.image.toUri()
                ),
            )

        treeNodes[songIdInTree]?.also { titleMap[title.lowercase()] = it }

        // Build album node
        if (!treeNodes.containsKey(albumIdInTree)) {
            treeNodes[albumIdInTree] =
                MediaItemNode(
                    buildMediaItem(
                        title = album,
                        mediaId = albumIdInTree,
                        isPlayable = false,
                        isBrowsable = true,
                        mediaType = MediaMetadata.MEDIA_TYPE_ALBUM,
                        album = null,
                        artist = null,
                        genre = null,
                        sourceUri = null,
                        imageUri = item.image.toUri()
                    ),
                )
            treeNodes[Constants.ALBUM_ID]?.addChild(albumIdInTree)
        }
        treeNodes[albumIdInTree]?.addChild(songIdInTree)

        // Build artist node
        if (!treeNodes.containsKey(artistIdInTree)) {
            treeNodes[artistIdInTree] =
                MediaItemNode(
                    buildMediaItem(
                        title = artist,
                        mediaId = artistIdInTree,
                        isPlayable = false,
                        isBrowsable = true,
                        mediaType = MediaMetadata.MEDIA_TYPE_ARTIST,
                        imageUri = item.image.toUri()
                    ),
                )
            treeNodes[Constants.ARTIST_ID]?.addChild(artistIdInTree)
        }

        // Build genre node
        if (!treeNodes.containsKey(genreIdInTree)) {
            treeNodes[genreIdInTree] =
                MediaItemNode(
                    buildMediaItem(
                        title = genre,
                        mediaId = genreIdInTree,
                        isPlayable = false,
                        isBrowsable = true,
                        mediaType = MediaMetadata.MEDIA_TYPE_GENRE
                    ),
                )
            treeNodes[Constants.GENRE_ID]?.addChild(genreIdInTree)
        }

        //Add the current song as a child of this artist
        treeNodes[artistIdInTree]?.addChild(songIdInTree)

        //Add the current album as a child of this artist
        treeNodes[artistIdInTree]?.addChild(albumIdInTree)

        //Add the current song as a child of this genre
        treeNodes[genreIdInTree]?.addChild(songIdInTree)
    }

    private fun reset() {
        treeNodes = mutableMapOf()
        titleMap = mutableMapOf()
    }

    override fun getItem(id: String): MediaItem? {
        return treeNodes[id]?.item
    }


    /**
     * Returns the media ID of the parent of the given media ID, or null if the media ID wasn't found.
     *
     * @param mediaId The media ID of which to search the parent.
     * @Param parentId The media ID of the media item to start the search from, or undefined to search
     *   from the top most node.
     */
    override fun getParentId(mediaId: String, parentId: String): String? {
        treeNodes[parentId]?.getChildren()?.let { children ->
            for (child in children) {
                if (child.mediaId == mediaId) {
                    return parentId
                } else if (child.mediaMetadata.isBrowsable == true) {
                    val nextParentId = getParentId(mediaId, child.mediaId)
                    if (nextParentId != null) {
                        return nextParentId
                    }
                }
            }
        }

        return null
    }

    /**
     * Tokenizes the query into a list of words with at least two letters and searches in the search
     * text of the [MediaItemNode].
     */
    override fun search(query: String): List<MediaItem> {
        val matches: MutableList<MediaItem> = mutableListOf()
        val titleMatches: MutableList<MediaItem> = mutableListOf()
        val words: List<String> = if (query.contains(" ")) {
            query.split(" ").map { it.trim().lowercase() }.filter { it.length > 1 }
        } else {
            listOf(query)
        }
        titleMap.keys.forEach { title ->
            titleMap[title]?.let { mediaItemNode ->
                for (word in words) {
                    if (mediaItemNode.searchText.contains(word)) {
                        if (mediaItemNode.searchTitle.contains(query.lowercase())) {
                            titleMatches.add(mediaItemNode.item)
                        } else {
                            matches.add(mediaItemNode.item)
                        }
                        break
                    }
                }
            }
        }
        titleMatches.addAll(matches)
        return titleMatches
    }

    override fun getRootItem(): MediaItem? {
        return treeNodes[Constants.ROOT_ID]?.item
    }

    override fun getChildren(id: String): List<MediaItem> {
        if (id == Constants.ALL_SONGS) {
            return search("")
        }
        return treeNodes[id]?.getChildren() ?: listOf()
    }

    private fun normalizeSearchText(text: CharSequence?): String {
        if (text.isNullOrEmpty() || text.trim().length == 1) {
            return ""
        }
        return "$text".trim().lowercase()
    }
}