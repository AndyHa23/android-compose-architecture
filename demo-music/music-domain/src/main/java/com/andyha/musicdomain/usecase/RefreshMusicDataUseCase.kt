package com.andyha.musicdomain.usecase

import com.andyha.coredata.base.NoInputUseCase
import com.andyha.musicdomain.repository.MediaTreeRepository
import javax.inject.Inject

class RefreshMusicDataUseCaseImpl @Inject constructor(
    private val repository: MediaTreeRepository
) : RefreshMusicDataUseCase {

    override fun invoke() = repository.refresh()
}

interface RefreshMusicDataUseCase : NoInputUseCase<Unit>
