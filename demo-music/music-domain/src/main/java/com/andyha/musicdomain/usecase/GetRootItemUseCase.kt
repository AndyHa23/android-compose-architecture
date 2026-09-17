package com.andyha.musicdomain.usecase

import androidx.media3.common.MediaItem
import com.andyha.coredata.base.NoInputUseCase
import com.andyha.musicdomain.repository.MediaTreeRepository
import javax.inject.Inject


class GetRootItemUseCaseImpl @Inject constructor(
    private val mediaTreeRepository: MediaTreeRepository
) : GetRootItemUseCase {
    override fun invoke(): MediaItem? {
        return mediaTreeRepository.getRootItem()
    }
}

interface GetRootItemUseCase : NoInputUseCase<MediaItem?>