package com.whatstheweatherapp.data.models

import com.google.gson.annotations.SerializedName

data class NetworkForecastCity(
    @SerializedName("name") val name: String? = null,
    @SerializedName("country") val country: String? = null,
    @SerializedName("state") val state: String? = null,
    @SerializedName("population") val population: String? = null,
    @SerializedName("sunrise") val sunriseTime: Long? = null,
    @SerializedName("sunset") val sunsetTime: Long? = null

)
