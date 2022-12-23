package com.whatstheweatherapp.data.networkmanager

import com.whatstheweatherapp.data.api.WeatherForecastApiService
import retrofit2.Retrofit

class CoreNetworkManager(
    private val retrofitClient: Retrofit.Builder
) : NetworkManager {

    override fun getWeatherClient(): WeatherForecastApiService {
        return retrofitClient.baseUrl("https://api.openweathermap.org")
            .build().create(WeatherForecastApiService::class.java)
    }
}