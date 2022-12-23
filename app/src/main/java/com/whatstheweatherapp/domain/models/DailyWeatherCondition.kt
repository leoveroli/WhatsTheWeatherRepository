package com.whatstheweatherapp.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DailyWeatherCondition(
    val id: Int?,
    val weatherCondition: String?,
    val weatherDescription: String?,
    val iconId: String?
) : Parcelable
