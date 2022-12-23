package com.whatstheweatherapp.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DailyWeatherForecast(
    val date: Long?,
    val formattedDate: String,
    val weatherInfo: DailyWeatherInfo,
    val weatherCondition: ArrayList<DailyWeatherCondition>,
    val clouds: DailyClouds,
    val wind: DailyWind,
    val visibility: Int?,
    val precipitationPercentage: Double?
): Parcelable