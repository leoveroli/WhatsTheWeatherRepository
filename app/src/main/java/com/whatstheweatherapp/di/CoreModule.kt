package com.whatstheweatherapp.di

import com.whatstheweatherapp.data.mapper.CoreCitiesListMapper
import com.whatstheweatherapp.data.mapper.CoreForecastMapper
import com.whatstheweatherapp.data.mapper.ForecastMapper
import com.whatstheweatherapp.data.models.NetworkCityItem
import com.whatstheweatherapp.data.models.NetworkForecastCity
import com.whatstheweatherapp.data.models.NetworkWeatherForecastResponse
import com.whatstheweatherapp.data.networkmanager.CoreNetworkManager
import com.whatstheweatherapp.data.networkmanager.NetworkManager
import com.whatstheweatherapp.data.repo.CoreWeatherRepository
import com.whatstheweatherapp.data.repo.WeatherRepository
import com.whatstheweatherapp.domain.CoreDispatcherProvider
import com.whatstheweatherapp.domain.DispatcherProvider
import com.whatstheweatherapp.domain.models.City
import com.whatstheweatherapp.domain.models.WeatherForecast
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            ).build()
    }

    @Provides
    @Singleton
    fun provideRetrofitClient(
        okHttpClient: OkHttpClient
    ): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
    }

    @Provides
    @Singleton
    fun provideNetworkManager(
        retrofitBuilder: Retrofit.Builder
    ): NetworkManager {
        return CoreNetworkManager(retrofitBuilder)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(
        networkManager: NetworkManager,
        forecastMapper: ForecastMapper<NetworkWeatherForecastResponse, WeatherForecast>,
        citiesListMapper: ForecastMapper<ArrayList<NetworkCityItem>, ArrayList<City>>,
        dispatcherProvider: DispatcherProvider
    ): WeatherRepository {
        return CoreWeatherRepository(
            networkManager,
            forecastMapper,
            citiesListMapper,
            dispatcherProvider
        )
    }

    @Provides
    @Singleton
    fun provideForecastMapper(): ForecastMapper<NetworkWeatherForecastResponse, WeatherForecast> {
        return CoreForecastMapper()
    }

    @Provides
    @Singleton
    fun provideCitiesListMapper(): ForecastMapper<ArrayList<NetworkCityItem>, ArrayList<City>> {
        return CoreCitiesListMapper()
    }

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider {
        return CoreDispatcherProvider()
    }
}