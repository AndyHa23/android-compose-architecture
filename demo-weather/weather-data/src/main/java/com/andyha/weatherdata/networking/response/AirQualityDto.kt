package com.andyha.weatherdata.networking.response

import com.google.gson.annotations.SerializedName

class AirQualityDto {
    @SerializedName("us-epa-index")
    var quality: Int? = null
}