package com.whatstheweatherapp.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DailyWeatherInfo(
    val temperature: Double?,
    val minTemperature: Double?,
    val maxTemperature: Double?,
    val perceivedTemperature: Double?,
    val humidity: Double?
) : Parcelable
