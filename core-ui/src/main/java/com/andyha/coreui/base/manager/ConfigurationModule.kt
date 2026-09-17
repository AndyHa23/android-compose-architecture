package com.andyha.coreui.base.manager

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * [ConfigurationManager] is app agnostic, so it is bound here instead of being repeated in
 * every app's own manager module.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ConfigurationModule {

    @Binds
    @Singleton
    abstract fun bindConfigurationManager(impl: ConfigurationManagerImpl): ConfigurationManager
}
