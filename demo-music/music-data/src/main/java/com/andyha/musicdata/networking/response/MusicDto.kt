package com.andyha.musicdata.networking.response

import com.google.gson.annotations.SerializedName


class MusicDto {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("album")
    var album: String? = null

    @SerializedName("artist")
    var artist: String? = null

    @SerializedName("genre")
    var genre: String? = null

    @SerializedName("source")
    var source: String? = null

    @SerializedName("image")
    var image: String? = null

    @SerializedName("trackNumber")
    var trackNumber: Int? = null

    @SerializedName("totalTrackCount")
    var totalTrackCount: Int? = null

    @SerializedName("duration")
    var duration: Int? = null

    @SerializedName("site")
    var site: String? = null
}