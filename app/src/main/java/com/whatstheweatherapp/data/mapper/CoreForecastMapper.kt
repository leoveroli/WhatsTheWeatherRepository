package com.whatstheweatherapp.data.mapper

import com.whatstheweatherapp.data.models.NetworkWeatherForecastResponse
import com.whatstheweatherapp.domain.models.*

class CoreForecastMapper :
    ForecastMapper<NetworkWeatherForecastResponse, WeatherForecast> {
    override fun map(input: NetworkWeatherForecastResponse): WeatherForecast {
        val forecastList = input.list.map {
            DailyWeatherForecast(
                date = if (it.date != null) {
                    it.date * 1000
                } else {
                    null
                },
                formattedDate = it.formattedDate.orEmpty(),
                weatherCondition =
                it.weatherCondition?.map { condition ->
                    DailyWeatherCondition(
                        id = condition.id,
                        weatherCondition = condition.weatherCondition,
                        weatherDescription = condition.weatherDescription,
                        iconId = condition.iconId
                    )
                }?.toCollection(ArrayList()) ?: arrayListOf(),
                weatherInfo = DailyWeatherInfo(
                    temperature = it.weatherInfo?.temperature,
                    minTemperature = it.weatherInfo?.minTemperature,
                    maxTemperature = it.weatherInfo?.maxTemperature,
                    perceivedTemperature = it.weatherInfo?.perceivedTemperature,
                    humidity = it.weatherInfo?.humidity
                ),
                clouds = DailyClouds(
                    cloudsPercentage = it.clouds?.cloudsPercentage
                ),
                wind = DailyWind(
                    windSpeed = it.wind?.windSpeed,
                    windDirection = it.wind?.windDirection
                ),
                visibility = it.visibility,
                precipitationPercentage = it.precipitationPercentage
            )
        }

        val city = City(
            name = input.city.name,
            country = input.city.country,
            state = input.city.state,
            latitude = null,
            longitude = null
        )

        return WeatherForecast(forecastList.toCollection(ArrayList()), city)
    }
}