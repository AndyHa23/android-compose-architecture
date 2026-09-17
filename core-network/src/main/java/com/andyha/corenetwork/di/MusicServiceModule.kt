package com.andyha.corenetwork.di

import com.andyha.corenetwork.ResponseConverter
import com.andyha.corenetwork.config.NetworkConfig
import com.andyha.corenetwork.config.NetworkConfigConstants
import com.andyha.corenetwork.interceptor.MusicRequestInterceptor
import com.andyha.corenetwork.interceptor.TimeoutInterceptor
import com.andyha.corenetwork.qualifier.ForHostSelection
import com.andyha.corenetwork.qualifier.ForLogging
import com.andyha.corenetwork.qualifier.ForRequestInterceptor
import com.andyha.corenetwork.qualifier.ForTimeout
import com.andyha.corenetwork.qualifier.MusicService
import com.andyha.corenetwork.qualifier.Service
import com.andyha.corenetwork.remoteConfig.RemoteConfigManager
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class MusicServiceModule {

    @Singleton
    @Provides
    @MusicService
    fun provideMusicBaseUrl(networkConfig: NetworkConfig) = networkConfig.musicBaseUrl

    @Singleton
    @Provides
    @MusicService
    fun provideRetrofit(
        @MusicService baseUrl: String,
        gson: Gson,
        @MusicService okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(ResponseConverter(gson))
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    @MusicService
    fun provideHttpClient(
        @ForRequestInterceptor(Service.MUSIC) requestInterceptor: Interceptor,
        @ForTimeout timeoutInterceptor: Interceptor,
        @ForLogging logging: Interceptor?,
        @ForHostSelection(Service.MUSIC) hostSelection: Interceptor?,
        autoAuthenticator: okhttp3.Authenticator
    ): OkHttpClient {
        val httpClient = OkHttpClient.Builder().apply {
            connectTimeout(NetworkConfigConstants.DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(NetworkConfigConstants.DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(NetworkConfigConstants.DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
            addInterceptor(requestInterceptor)
            addInterceptor(timeoutInterceptor)
            hostSelection?.let { addInterceptor(it) }
            logging?.let { addInterceptor(it) }
            authenticator(autoAuthenticator)
        }
        return httpClient.build()
    }

    @Singleton
    @Provides
    @ForRequestInterceptor(Service.MUSIC)
    fun provideRequestInterceptor(networkConfig: NetworkConfig): Interceptor =
        MusicRequestInterceptor(networkConfig)

    @Singleton
    @Provides
    @ForHostSelection(Service.MUSIC)
    fun provideHostSelectionInterceptor(remoteConfigManager: RemoteConfigManager): Interceptor? {
        // return HostSelectionInterceptor(remoteConfigManager.weatherBaseUrl)
        // Disable for now as this demo is not using remote config to configure api base url
        return null
    }

    @Singleton
    @Provides
    @ForTimeout
    fun provideTimeoutInterceptor(): Interceptor = TimeoutInterceptor()

}