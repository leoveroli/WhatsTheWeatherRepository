package com.whatstheweatherapp.domain.usecase

import com.whatstheweatherapp.common.Result
import com.whatstheweatherapp.data.repo.WeatherRepository
import com.whatstheweatherapp.domain.models.WeatherForecast
import javax.inject.Inject

class WeatherForecastUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {

    suspend fun retrieveCityWeatherForecast(
        latitude: String,
        longitude: String,
        appId: String
    ): Result<WeatherForecast> {
        return weatherRepository.getWeatherForecast(latitude, longitude, appId)
    }
}