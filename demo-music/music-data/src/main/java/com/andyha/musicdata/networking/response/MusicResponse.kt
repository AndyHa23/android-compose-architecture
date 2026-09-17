package com.andyha.musicdata.networking.response

import com.google.gson.annotations.SerializedName


class MusicResponse {

    @SerializedName("music")
    var music: List<MusicDto>? = null
}