package com.whatstheweatherapp.data.mapper

import com.whatstheweatherapp.data.models.NetworkCityItem
import com.whatstheweatherapp.data.models.NetworkForecastCity
import com.whatstheweatherapp.domain.models.City

class CoreCitiesListMapper : ForecastMapper<ArrayList<NetworkCityItem>, ArrayList<City>> {
    override fun map(input: ArrayList<NetworkCityItem>): ArrayList<City> {
        return input.map {
            City(
                name = it.name,
                country = it.country,
                state = it.state,
                latitude = it.latitude,
                longitude = it.longitude
            )
        }.toCollection(ArrayList())
    }
}