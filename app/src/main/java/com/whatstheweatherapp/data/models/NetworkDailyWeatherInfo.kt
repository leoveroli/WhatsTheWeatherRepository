package com.whatstheweatherapp.data.models

import com.google.gson.annotations.SerializedName

data class NetworkDailyWeatherInfo(
    @SerializedName("temp") val temperature: Double? = null,
    @SerializedName("temp_min") val minTemperature: Double? = null,
    @SerializedName("temp_max") val maxTemperature: Double? = null,
    @SerializedName("feels_like") val perceivedTemperature: Double? = null,
    @SerializedName("humidity") val humidity: Double? = null
)
