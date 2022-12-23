package com.whatstheweatherapp.data.models

import com.google.gson.annotations.SerializedName

data class NetworkDailyWind(
    @SerializedName("speed") val windSpeed: Double? = null,
    @SerializedName("deg") val windDirection: Double? = null
)
