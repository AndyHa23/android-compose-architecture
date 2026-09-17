package com.andyha.musicdomain.usecase

import com.andyha.coredata.base.NoInputUseCase
import com.andyha.musicdomain.model.DataState
import com.andyha.musicdomain.repository.MediaTreeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMusicDataStateUseCaseImpl @Inject constructor(
    private val repository: MediaTreeRepository
) : GetMusicDataStateUseCase {

    override fun invoke(): Flow<DataState> {
        return repository.dataState
    }
}

interface GetMusicDataStateUseCase : NoInputUseCase<Flow<DataState>>