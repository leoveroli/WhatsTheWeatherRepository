package com.whatstheweatherapp.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(
    val name: String?,
    val country: String?,
    val state: String?,
    val latitude: Double?,
    val longitude: Double?
) : Parcelable
