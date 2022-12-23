package com.whatstheweatherapp.presentation.forecastdetail

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.whatstheweatherapp.R
import com.whatstheweatherapp.databinding.WeatherAppForecastDetailFragmentLayoutBinding
import com.whatstheweatherapp.domain.models.DailyWeatherForecast

class ForecastDetailFragment : BottomSheetDialogFragment() {

    companion object {
        private const val FORECAST_ARG_KEY = "FORECAST_ARG_KEY"

        fun newInstance(forecast: DailyWeatherForecast) = ForecastDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(FORECAST_ARG_KEY, forecast)
            }
        }
    }

    override fun getTheme() = R.style.WeatherAppRoundedCornersDialog

    private var binding: WeatherAppForecastDetailFragmentLayoutBinding? = null
    private val mFragmentBinding get() = binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.skipCollapsed = true
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WeatherAppForecastDetailFragmentLayoutBinding.inflate(inflater, container, false)
        return mFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable<DailyWeatherForecast>(FORECAST_ARG_KEY)?.let { forecast ->
            with(mFragmentBinding) {
                //Min temperature
                forecast.weatherInfo.minTemperature?.let { minTemp ->
                    minTempLabel.isVisible = true
                    minTempTV.isVisible = true
                    minTempTV.text = String.format("%.2f°", minTemp)
                }

                //Max temperature
                forecast.weatherInfo.maxTemperature?.let { maxTemp ->
                    maxTempLabel.isVisible = true
                    maxTempTV.isVisible = true
                    maxTempTV.text = String.format("%.2f°", maxTemp)
                }

                forecast.weatherInfo.perceivedTemperature?.let { percTemp ->
                    perceivedTemperatureLabel.isVisible = true
                    perceivedTemperatureTV.isVisible = true
                    perceivedTemperatureTV.text = String.format("%.2f°", percTemp)
                }

                //Visibility
                forecast.visibility?.let { range ->
                    visibilityLabel.isVisible = true
                    visibilityTV.isVisible = true
                    visibilityTV.text = if (range > 1000) {
                        "${range / 1000} Km"
                    } else {
                        "$range m"
                    }
                }

                //Precipitation percentage
                forecast.precipitationPercentage?.let { percentage ->
                    probabilityLabel.isVisible = true
                    probabilityPercentageTV.isVisible = true
                    probabilityPercentageTV.text = if (percentage == 0.0) {
                        getString(R.string.weather_app_absent)
                    } else {
                        String.format("%.0f%%", percentage * 100)
                    }
                }

                //Wind Speed
                forecast.wind.windSpeed?.let { windSpeed ->
                    windLabel.isVisible = true
                    windSpeedTV.isVisible = true
                    windSpeedTV.text = String.format("%.2f m/s", windSpeed)
                }

                //Humidity
                forecast.weatherInfo.humidity?.let { humidity ->
                    humidityLabel.isVisible = true
                    humidityPercentageTV.isVisible = true
                    humidityPercentageTV.text = String.format("%.0f%%", humidity)
                }

                //Cloudiness
                forecast.clouds.cloudsPercentage?.let { percentage ->
                    cloudinessLabel.isVisible = true
                    cloudsPercentageTV.isVisible = true
                    cloudsPercentageTV.text = String.format("%d%%", percentage)
                }

                //Close button
                closeButton.setOnClickListener {
                    dismiss()
                }
            }
        } ?: kotlin.run {
            dismiss()
        }
    }
}