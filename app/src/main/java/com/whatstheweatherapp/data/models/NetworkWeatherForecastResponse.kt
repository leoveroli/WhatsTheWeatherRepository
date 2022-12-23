package com.whatstheweatherapp.data.models

import com.google.gson.annotations.SerializedName

data class NetworkWeatherForecastResponse(
    @SerializedName("list") val list: ArrayList<NetworkDailyWeatherForecast>,
    @SerializedName("city") val city: NetworkForecastCity
)