package com.whatstheweatherapp.data.repo

import android.util.Log
import com.whatstheweatherapp.common.Result
import com.whatstheweatherapp.data.mapper.ForecastMapper
import com.whatstheweatherapp.data.models.NetworkCityItem
import com.whatstheweatherapp.data.models.NetworkWeatherForecastResponse
import com.whatstheweatherapp.data.networkmanager.NetworkManager
import com.whatstheweatherapp.domain.DispatcherProvider
import com.whatstheweatherapp.domain.models.City
import com.whatstheweatherapp.domain.models.WeatherForecast
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CoreWeatherRepository @Inject constructor(
    private val mNetworkManager: NetworkManager,
    private val mWeatherResponseMapper: ForecastMapper<NetworkWeatherForecastResponse, WeatherForecast>,
    private val mCitiesListMapper: ForecastMapper<ArrayList<NetworkCityItem>, ArrayList<City>>,
    private val mDispatcherProvider: DispatcherProvider
) : WeatherRepository {

    override suspend fun getWeatherForecast(
        latitude: String,
        longitude: String,
        appId: String
    ): Result<WeatherForecast> = withContext(mDispatcherProvider.io()) {
        try {
            val result = mNetworkManager.getWeatherClient().getCityWeatherForecast(
                latitude = latitude,
                longitude = longitude,
                appId = appId,
                language = "IT",
                units = "metric"
            )

            Result.Success(mWeatherResponseMapper.map(result))
        } catch (e: Exception) {
            Log.e("WeatherRepo", e.message, e)
            Result.Failure(e)
        }
    }

    override suspend fun getCitiesList(
        query: String,
        appId: String
    ): Result<ArrayList<City>> = withContext(mDispatcherProvider.io()) {
        try {
            val result = mNetworkManager.getWeatherClient().getCityList(
                cityName = query,
                limit = 5,
                appId = appId,
                language = "IT"
            )

            Result.Success(mCitiesListMapper.map(result))
        } catch (e: Exception) {
            Log.e("WeatherRepo", e.message, e)
            Result.Failure(e)
        }
    }
}