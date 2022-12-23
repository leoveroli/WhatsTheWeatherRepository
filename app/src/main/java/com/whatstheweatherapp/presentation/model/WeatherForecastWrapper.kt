package com.whatstheweatherapp.presentation.model

import com.whatstheweatherapp.domain.models.City
import com.whatstheweatherapp.domain.models.DailyWeatherForecast

data class WeatherForecastWrapper(
    val forecastMap: MutableMap<String, ArrayList<DailyWeatherForecast>>,
    val city: City
)
