package com.andyha.musicdomain.usecase

import androidx.media3.common.MediaItem
import com.andyha.coredata.base.BaseUseCase
import com.andyha.musicdomain.repository.MediaTreeRepository
import javax.inject.Inject

class GetChildrenItemsUseCaseImpl @Inject constructor(
    private val mediaTreeRepository: MediaTreeRepository
): GetChildrenItemsUseCase{
    override fun invoke(parentId: String): List<MediaItem> {
        return mediaTreeRepository.getChildren(parentId)
    }
}

interface GetChildrenItemsUseCase: BaseUseCase<String, List<MediaItem>>