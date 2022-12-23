package com.whatstheweatherapp.data.models

import com.google.gson.annotations.SerializedName

data class NetworkDailyWeatherCondition(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("main") val weatherCondition: String? = null,
    @SerializedName("description") val weatherDescription: String? = null,
    @SerializedName("icon") val iconId: String? = null
)
