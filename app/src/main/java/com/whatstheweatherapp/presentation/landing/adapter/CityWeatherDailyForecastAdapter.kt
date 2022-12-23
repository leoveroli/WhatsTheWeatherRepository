package com.whatstheweatherapp.presentation.landing.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.whatstheweatherapp.R
import com.whatstheweatherapp.common.AdapterRowWrapper
import com.whatstheweatherapp.common.BindingViewHolder
import com.whatstheweatherapp.databinding.WeatherAppEmptyItemBinding
import com.whatstheweatherapp.databinding.WeatherAppHourForecastItemBinding
import com.whatstheweatherapp.domain.models.DailyWeatherForecast
import java.text.SimpleDateFormat
import java.util.*

class CityWeatherDailyForecastAdapter(
    private val mContext: Context,
    private val interactionListener: ForecastInteractionListener
) : RecyclerView.Adapter<BindingViewHolder>() {
    private val mItems = arrayListOf<AdapterRowWrapper<*>>()

    private val FORECAST_ITEM = 230
    private val mSimpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return mItems[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = when (viewType) {
            FORECAST_ITEM -> {
                WeatherAppHourForecastItemBinding.inflate(inflater, parent, false)
            }

            else -> {
                WeatherAppEmptyItemBinding.inflate(inflater, parent, false)
            }
        }

        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        when (holder.binding) {
            is WeatherAppHourForecastItemBinding -> {
                val forecast = mItems[position].data as DailyWeatherForecast
                with(holder.binding) {
                    timeTV.text = mSimpleDateFormat.format(forecast.date)
                    weatherDescription.text = forecast.weatherCondition.first().weatherDescription

                    forecast.weatherInfo.perceivedTemperature?.let { percTemp ->
                        perceivedTemperatureTV.isVisible = true
                        perceivedTemperatureTV.text = String.format(
                            "%s: %.2f°",
                            mContext.getString(
                                R.string.weather_app_temperature_short
                            ),
                            percTemp
                        )
                    } ?: kotlin.run {
                        perceivedTemperatureTV.isVisible = false
                    }

                    Glide.with(mContext)
                        .load("https://openweathermap.org/img/wn/${forecast.weatherCondition.first().iconId}@2x.png")
                        .into(timeWeatherIcon)
                }

                holder.itemView.setOnClickListener {
                    interactionListener.onForecastSelected(forecast)
                }
            }
        }
    }

    fun setDailyForecastList(dailyForecastList: ArrayList<DailyWeatherForecast>) {
        mItems.clear()
        mItems.addAll(
            dailyForecastList.map {
                AdapterRowWrapper(it, FORECAST_ITEM)
            }
        )

        notifyDataSetChanged()
    }

    interface ForecastInteractionListener {
        fun onForecastSelected(forecast: DailyWeatherForecast)
    }
}