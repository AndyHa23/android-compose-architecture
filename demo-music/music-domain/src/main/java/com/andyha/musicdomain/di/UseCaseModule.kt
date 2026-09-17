package com.andyha.musicdomain.di

import com.andyha.musicdomain.usecase.GetChildrenItemsUseCase
import com.andyha.musicdomain.usecase.GetChildrenItemsUseCaseImpl
import com.andyha.musicdomain.usecase.GetMusicDataStateUseCase
import com.andyha.musicdomain.usecase.GetMusicDataStateUseCaseImpl
import com.andyha.musicdomain.usecase.GetMusicItemUseCase
import com.andyha.musicdomain.usecase.GetMusicItemUseCaseImpl
import com.andyha.musicdomain.usecase.GetParentIdUseCase
import com.andyha.musicdomain.usecase.GetParentIdUseCaseImpl
import com.andyha.musicdomain.usecase.GetRootItemUseCase
import com.andyha.musicdomain.usecase.GetRootItemUseCaseImpl
import com.andyha.musicdomain.usecase.RefreshMusicDataUseCase
import com.andyha.musicdomain.usecase.RefreshMusicDataUseCaseImpl
import com.andyha.musicdomain.usecase.SearchItemUseCase
import com.andyha.musicdomain.usecase.SearchItemUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    @Singleton
    abstract fun bindGetMusicDataStateUseCase(impl: GetMusicDataStateUseCaseImpl): GetMusicDataStateUseCase

    @Binds
    @Singleton
    abstract fun bindGetChildrenItemsUseCaseImpl(impl: GetChildrenItemsUseCaseImpl): GetChildrenItemsUseCase

    @Binds
    @Singleton
    abstract fun bindGetMusicItemUseCase(impl: GetMusicItemUseCaseImpl): GetMusicItemUseCase

    @Binds
    @Singleton
    abstract fun bindGetParentIdUseCase(impl: GetParentIdUseCaseImpl): GetParentIdUseCase

    @Binds
    @Singleton
    abstract fun bindGetRootItemUseCase(impl: GetRootItemUseCaseImpl): GetRootItemUseCase

    @Binds
    @Singleton
    abstract fun bindSearchItemUseCase(impl: SearchItemUseCaseImpl): SearchItemUseCase

    @Binds
    @Singleton
    abstract fun bindRefreshMusicDataUseCase(impl: RefreshMusicDataUseCaseImpl): RefreshMusicDataUseCase
}