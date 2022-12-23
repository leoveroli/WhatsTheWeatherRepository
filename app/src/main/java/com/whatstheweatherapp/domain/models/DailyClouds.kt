package com.whatstheweatherapp.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DailyClouds(
    val cloudsPercentage: Int?
) : Parcelable
