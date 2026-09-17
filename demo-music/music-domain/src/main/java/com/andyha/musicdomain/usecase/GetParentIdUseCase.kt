package com.andyha.musicdomain.usecase

import com.andyha.coredata.base.BaseUseCase
import com.andyha.musicdomain.repository.MediaTreeRepository
import javax.inject.Inject

class GetParentIdUseCaseImpl @Inject constructor(
    private val mediaTreeRepository: MediaTreeRepository
) : GetParentIdUseCase {
    override fun invoke(params: String): String? {
        return mediaTreeRepository.getParentId(params)
    }
}

interface GetParentIdUseCase : BaseUseCase<String, String?>