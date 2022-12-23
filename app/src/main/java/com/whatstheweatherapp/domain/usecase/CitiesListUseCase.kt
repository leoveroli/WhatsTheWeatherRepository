package com.whatstheweatherapp.domain.usecase

import com.whatstheweatherapp.common.Result
import com.whatstheweatherapp.data.repo.WeatherRepository
import com.whatstheweatherapp.domain.models.City
import javax.inject.Inject

class CitiesListUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {

    suspend fun retrieveCitiesList(query: String, appId: String): Result<ArrayList<City>> {
        return weatherRepository.getCitiesList(query, appId)
    }
}