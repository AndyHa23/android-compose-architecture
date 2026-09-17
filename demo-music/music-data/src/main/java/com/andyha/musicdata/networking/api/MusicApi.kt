package com.andyha.musicdata.networking.api

import com.andyha.musicdata.networking.response.MusicResponse
import retrofit2.http.GET


interface MusicApi {

    @GET("/AndyHa23/free-music-api/refs/heads/main/catalog.json")
    suspend fun getMusicList(): MusicResponse?
}