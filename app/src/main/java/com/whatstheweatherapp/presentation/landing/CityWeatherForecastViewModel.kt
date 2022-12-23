package com.whatstheweatherapp.presentation.landing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whatstheweatherapp.BuildConfig
import com.whatstheweatherapp.R
import com.whatstheweatherapp.common.Result
import com.whatstheweatherapp.common.ViewModelEvent
import com.whatstheweatherapp.common.isNotNullAndNotEmpty
import com.whatstheweatherapp.domain.DispatcherProvider
import com.whatstheweatherapp.domain.models.DailyWeatherForecast
import com.whatstheweatherapp.domain.usecase.WeatherForecastUseCase
import com.whatstheweatherapp.presentation.model.WeatherForecastWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CityWeatherForecastViewModel @Inject constructor(
    private val mWeatherForecastUseCase: WeatherForecastUseCase,
    private val mDispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val mForecastWrapperLiveData: MutableLiveData<ViewModelEvent<WeatherForecastWrapper>> =
        MutableLiveData()
    val forecastObserver: LiveData<ViewModelEvent<WeatherForecastWrapper>> =
        mForecastWrapperLiveData

    private val mDailyForecastLiveData: MutableLiveData<ArrayList<DailyWeatherForecast>> =
        MutableLiveData()
    val dailyForecastObserver: LiveData<ArrayList<DailyWeatherForecast>> =
        mDailyForecastLiveData


    private val mFirstCalendar = Calendar.getInstance()
    private val mSecondCalendar = Calendar.getInstance()
    private lateinit var mForecastWrapper: WeatherForecastWrapper
    private val mSimpleDateFormatter = SimpleDateFormat("EEE dd/MM", Locale.getDefault())

    fun retrieveCityWeatherForecast(
        latitude: String? = null,
        longitude: String? = null
    ) {
        mForecastWrapperLiveData.value = ViewModelEvent.Loading

        CoroutineScope(mDispatcherProvider.main()).launch {
            val response = if (latitude.isNotNullAndNotEmpty() && latitude.isNotNullAndNotEmpty()) {
                mWeatherForecastUseCase.retrieveCityWeatherForecast(
                    latitude = latitude!!,
                    longitude = longitude!!,
                    appId = BuildConfig.APP_ID
                )
            } else {
                Result.Failure(Exception("Missing parameters to perform network call with"))
            }

            when (response) {
                is Result.Success -> {
                    val forecastMap: MutableMap<String, ArrayList<DailyWeatherForecast>> =
                        mutableMapOf()

                    //From the forecast list let's build a map with this structure:
                    //key: Sat 21/12 value: all the forecasts for 21/12
                    response.data.list.forEach { firstItem ->
                        if (firstItem.date != null) {
                            val formattedDate = mSimpleDateFormatter.format(Date(firstItem.date))
                            if (!forecastMap.containsKey(formattedDate)) {
                                mFirstCalendar.time = Date(firstItem.date)
                                val sameDayForecastList = response.data.list.filter { secondItem ->
                                    if (secondItem.date != null) {
                                        mSecondCalendar.time = Date(secondItem.date)
                                        isSameDay(mFirstCalendar, mSecondCalendar)
                                    } else {
                                        false
                                    }
                                }.toCollection(ArrayList())

                                forecastMap[formattedDate] = sameDayForecastList
                            }
                        }
                    }

                    mForecastWrapper = WeatherForecastWrapper(
                        forecastMap = forecastMap,
                        city = response.data.city
                    )
                    mForecastWrapperLiveData.value = ViewModelEvent.Done(mForecastWrapper)
                }

                is Result.Failure -> {
                    mForecastWrapperLiveData.value = ViewModelEvent.Error(response.e)
                }
            }
        }
    }

    private fun isSameDay(firstCalendar: Calendar, secondCalendar: Calendar): Boolean {
        return firstCalendar.get(Calendar.DAY_OF_YEAR) == secondCalendar.get(Calendar.DAY_OF_YEAR) &&
                firstCalendar.get(Calendar.YEAR) == secondCalendar.get(Calendar.YEAR)
    }

    //Retrieve forecast list for the selected day
    fun onDaySelected(day: String) {
        mDailyForecastLiveData.value = if (mForecastWrapper.forecastMap.containsKey(day)) {
            mForecastWrapper.forecastMap[day]
        } else {
            arrayListOf()
        }
    }

    //Retrieve the proper animation to display according to the "current" (first forecast of the first day)
    fun getCurrentWeatherProperAnimation(): Int? {
        val currentDayForecastList = mForecastWrapper.forecastMap.values.first()
        return if (currentDayForecastList.first().weatherCondition.isNotEmpty()) {
            val currentWeather = currentDayForecastList.first().weatherCondition.first()
            when (currentWeather.id) {
                //Rain
                in 500..531 -> {
                    R.raw.rain_animation
                }

                //Snow
                in 600..622 -> {
                    R.raw.snow_animation
                }

                //Clear
                800 -> {
                    if (currentWeather.iconId != null &&
                        currentWeather.iconId.endsWith("n", true)
                    ) {
                        R.raw.moon_animation
                    } else {
                        R.raw.sunny_animation
                    }
                }

                in 801..804 -> {
                    R.raw.cloudy_animation
                }

                else -> null
            }
        } else {
            null
        }
    }
}