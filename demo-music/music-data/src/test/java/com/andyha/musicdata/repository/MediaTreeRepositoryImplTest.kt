package com.andyha.musicdata.repository

import android.net.Uri
import com.andyha.musicdomain.constants.Constants
import com.andyha.musicdomain.model.Music
import com.andyha.musicdata.datasource.remote.MusicDataSource
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MediaTreeRepositoryImplTest {

    private val testDispatcher = StandardTestDispatcher()
    private val musicDataSource = mockk<MusicDataSource>()
    private lateinit var repository: MediaTreeRepositoryImpl

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Uri::class)
        every { Uri.parse(any()) } returns mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(Uri::class)
    }

    @Test
    fun `addNodeToTree should add song to genre node`() = runTest(testDispatcher) {
        val music = Music(
            id = "song1",
            title = "Song 1",
            album = "Album 1",
            artist = "Artist 1",
            genre = "Pop",
            source = "https://example.com/song1.mp3",
            image = "https://example.com/image1.jpg",
            trackNumber = 1,
            totalTrackCount = 10,
            duration = 300,
            site = ""
        )
        
        every { musicDataSource.getRemoteMusic() } returns flowOf(listOf(music))
        
        repository = MediaTreeRepositoryImpl(musicDataSource, testDispatcher)
        
        // Wait for init block to complete
        advanceUntilIdle()
        
        val genreId = "${Constants.GENRE_ID}${music.genre}"
        val children = repository.getChildren(genreId)
        
        assertTrue("Genre node should contain the song", children.any { it.mediaId == music.id })
    }
}
