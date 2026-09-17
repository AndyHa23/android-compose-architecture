package com.andyha.musicdomain.usecase

import androidx.media3.common.MediaItem
import com.andyha.coredata.base.BaseUseCase
import com.andyha.musicdomain.repository.MediaTreeRepository
import javax.inject.Inject


class GetMusicItemUseCaseImpl @Inject constructor(
    private val mediaTreeRepository: MediaTreeRepository
) : GetMusicItemUseCase {
    override fun invoke(mediaId: String): MediaItem? {
        return mediaTreeRepository.getItem(mediaId)
    }
}

interface GetMusicItemUseCase : BaseUseCase<String, MediaItem?>