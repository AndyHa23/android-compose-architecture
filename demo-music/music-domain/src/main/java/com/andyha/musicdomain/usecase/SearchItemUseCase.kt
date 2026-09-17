package com.andyha.musicdomain.usecase

import androidx.media3.common.MediaItem
import com.andyha.coredata.base.BaseUseCase
import com.andyha.musicdomain.repository.MediaTreeRepository
import javax.inject.Inject

class SearchItemUseCaseImpl @Inject constructor(
    private val mediaTreeRepository: MediaTreeRepository
): SearchItemUseCase{
    override fun invoke(params: String): List<MediaItem> {
        return mediaTreeRepository.search(params)
    }
}

interface SearchItemUseCase: BaseUseCase<String, List<MediaItem>>