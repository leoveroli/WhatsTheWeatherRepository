package com.whatstheweatherapp.data.api

import com.whatstheweatherapp.data.models.NetworkCityItem
import com.whatstheweatherapp.data.models.NetworkWeatherForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherForecastApiService {
    @GET("/data/2.5/forecast")
    suspend fun getCityWeatherForecast(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("appid") appId: String,
        @Query("lang") language: String,
        @Query("units") units: String
    ): NetworkWeatherForecastResponse

    @GET("/data/2.5/forecast")
    suspend fun getCityWeatherForecast(
        @Query("q") query: String,
        @Query("appid") appId: String,
        @Query("units") units: String,
        @Query("lang") language: String,
    ): NetworkWeatherForecastResponse

    @GET("/geo/1.0/direct")
    suspend fun getCityList(
        @Query("q") cityName: String,
        @Query("limit") limit: Int,
        @Query("appid") appId: String,
        @Query("lang") language: String
    ): ArrayList<NetworkCityItem>

}