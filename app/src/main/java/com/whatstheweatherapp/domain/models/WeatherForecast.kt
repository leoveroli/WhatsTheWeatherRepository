package com.whatstheweatherapp.domain.models

data class WeatherForecast(
    val list: ArrayList<DailyWeatherForecast>,
    val city: City
)