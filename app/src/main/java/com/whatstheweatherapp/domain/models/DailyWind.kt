package com.whatstheweatherapp.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DailyWind(
    val windSpeed: Double?,
    val windDirection: Double?
) : Parcelable
