package com.whatstheweatherapp.data.models

import com.google.gson.annotations.SerializedName

data class NetworkDailyWeatherForecast(
    @SerializedName("dt") val date: Long? = null,
    @SerializedName("main") val weatherInfo: NetworkDailyWeatherInfo? = null,
    @SerializedName("weather") val weatherCondition: ArrayList<NetworkDailyWeatherCondition>? = null,
    @SerializedName("clouds") val clouds: NetworkDailyClouds? = null,
    @SerializedName("wind") val wind: NetworkDailyWind? = null,
    @SerializedName("visibility") val visibility: Int? = null,
    @SerializedName("pop") val precipitationPercentage: Double? = null,
    @SerializedName("dt_txt") val formattedDate: String? = null
)