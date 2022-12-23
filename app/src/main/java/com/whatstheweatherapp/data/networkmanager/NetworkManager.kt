package com.whatstheweatherapp.data.networkmanager

import com.whatstheweatherapp.data.api.WeatherForecastApiService

interface NetworkManager {
    fun getWeatherClient(): WeatherForecastApiService
}