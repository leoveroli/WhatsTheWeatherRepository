package com.whatstheweatherapp.data.models

import com.google.gson.annotations.SerializedName

data class NetworkCityItem(
    @SerializedName("name") val name: String? = null,
    @SerializedName("country") val country: String? = null,
    @SerializedName("state") val state: String? = null,
    @SerializedName("lat") val latitude: Double? = null,
    @SerializedName("lon") val longitude: Double? = null
)
