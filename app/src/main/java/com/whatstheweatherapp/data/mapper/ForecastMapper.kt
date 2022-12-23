package com.whatstheweatherapp.data.mapper

interface ForecastMapper<I, O> {
    fun map(input: I): O
}