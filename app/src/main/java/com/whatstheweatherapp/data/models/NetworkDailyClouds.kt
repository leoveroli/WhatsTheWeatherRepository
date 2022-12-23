package com.whatstheweatherapp.data.models

import com.google.gson.annotations.SerializedName

data class NetworkDailyClouds(
    @SerializedName("all") val cloudsPercentage: Int? = null
)
