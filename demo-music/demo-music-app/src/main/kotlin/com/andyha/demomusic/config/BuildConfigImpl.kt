package com.andyha.demomusic.config

import android.content.Context
import com.andyha.coreconfig.buildConfig.BuildConfig
import com.andyha.demomusic.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import com.andyha.demomusic.BuildConfig as GeneratedBuildConfig


class BuildConfigImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : BuildConfig {

    private val isProductionBuild =
        GeneratedBuildConfig.FLAVOR == PRODUCTION_FLAVOR && !GeneratedBuildConfig.DEBUG

    override var isStorageEncrypted =
        context.resources.getBoolean(R.bool.isStorageEncrypted) || isProductionBuild
    override var isLoggingEnabled =
        context.resources.getBoolean(R.bool.isLoggingEnabled) && !isProductionBuild
    override var isCrashlyticsEnabled = context.resources.getBoolean(R.bool.isCrashlyticsEnabled)
    override var isAnalyticsEnabled = context.resources.getBoolean(R.bool.isAnalyticsEnabled)
    override var isProductionRelease =
        context.resources.getBoolean(R.bool.isProductionRelease) || isProductionBuild

    companion object {
        private const val PRODUCTION_FLAVOR = "production"
    }
}