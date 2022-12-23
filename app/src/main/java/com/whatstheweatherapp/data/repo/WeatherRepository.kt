package com.whatstheweatherapp.data.repo

import com.whatstheweatherapp.common.Result
import com.whatstheweatherapp.domain.models.City
import com.whatstheweatherapp.domain.models.WeatherForecast

interface WeatherRepository {
    suspend fun getWeatherForecast(
        latitude: String,
        longitude: String,
        appId: String
    ): Result<WeatherForecast>

    suspend fun getCitiesList(
        query: String,
        appId: String
    ): Result<ArrayList<City>>
}