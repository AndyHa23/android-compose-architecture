package com.andyha.demomusic.di

import com.andyha.coredata.manager.InAppUpdateManager
import com.andyha.coredata.manager.NetworkConnectionManager
import com.andyha.coredata.manager.SessionManager
import com.andyha.corenetwork.config.TokenRefresher
import com.andyha.coreui.manager.ApiErrorHandler
import com.andyha.demomusic.manager.ApiErrorHandlerImpl
import com.andyha.demomusic.manager.InAppUpdateManagerImpl
import com.andyha.demomusic.manager.NetworkConnectionManagerImpl
import com.andyha.demomusic.manager.SessionManagerImpl
import com.andyha.demomusic.manager.TokenRefresherImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class ManagerModule {

    @Binds
    @Singleton
    abstract fun bindNetworkManager(networkManagerImpl: NetworkConnectionManagerImpl): NetworkConnectionManager

    @Binds
    @Singleton
    abstract fun bindApiErrorManager(apiErrorManagerImpl: ApiErrorHandlerImpl): ApiErrorHandler

    @Binds
    @Singleton
    abstract fun bindInAppUpdateManager(appUpdateManagerImpl: InAppUpdateManagerImpl): InAppUpdateManager

    @Binds
    @Singleton
    abstract fun bindSessionManager(sessionManager: SessionManagerImpl): SessionManager

    @Binds
    @Singleton
    abstract fun bindTokenRefresher(tokenRefresher: TokenRefresherImpl): TokenRefresher
}