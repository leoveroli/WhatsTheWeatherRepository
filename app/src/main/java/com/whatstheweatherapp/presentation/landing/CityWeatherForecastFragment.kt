package com.whatstheweatherapp.presentation.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.whatstheweatherapp.R
import com.whatstheweatherapp.common.ViewModelEvent
import com.whatstheweatherapp.databinding.WeatherAppCityForecastFragmentLayoutBinding
import com.whatstheweatherapp.domain.models.City
import com.whatstheweatherapp.domain.models.DailyWeatherForecast
import com.whatstheweatherapp.domain.models.DailyWeatherInfo
import com.whatstheweatherapp.presentation.forecastdetail.ForecastDetailFragment
import com.whatstheweatherapp.presentation.landing.adapter.CityWeatherDailyForecastAdapter
import com.whatstheweatherapp.presentation.landing.adapter.CityWeatherDaysAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CityWeatherForecastFragment : Fragment(), CityWeatherDaysAdapter.DaysInteractionListener,
    CityWeatherDailyForecastAdapter.ForecastInteractionListener {

    private var _binding: WeatherAppCityForecastFragmentLayoutBinding? = null
    private val fragmentBinding: WeatherAppCityForecastFragmentLayoutBinding
        get() = _binding!!

    private val mViewModel: CityWeatherForecastViewModel by viewModels()
    private val mDaysAdapter = CityWeatherDaysAdapter(this)
    private var mDailyForecastAdapter: CityWeatherDailyForecastAdapter? = null
    private var mSelectedCity: City? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WeatherAppCityForecastFragmentLayoutBinding.inflate(inflater, container, false)
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val cityLatitude = CityWeatherForecastFragmentArgs.fromBundle(it).latitude
            val cityLongitude = CityWeatherForecastFragmentArgs.fromBundle(it).longitude
            val selectedCity = CityWeatherForecastFragmentArgs.fromBundle(it).selectedCity
            val fromSearch = CityWeatherForecastFragmentArgs.fromBundle(it).fromSearch

            if (selectedCity?.latitude != null && selectedCity.longitude != null) {
                mSelectedCity = selectedCity

                mViewModel.retrieveCityWeatherForecast(
                    latitude = selectedCity.latitude.toString(),
                    longitude = selectedCity.longitude.toString()
                )

                populateUI(fromSearch)
                observeViewModel()
            } else if (cityLatitude != null && cityLongitude != null) {
                mViewModel.retrieveCityWeatherForecast(
                    latitude = cityLatitude,
                    longitude = cityLongitude
                )

                populateUI(fromSearch)
                observeViewModel()
            } else {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.weather_app_attention)
                    .setMessage(R.string.weather_app_technical_error)
                    .setNeutralButton(
                        android.R.string.ok
                    ) { _, _ -> onDialogButtonSelected() }.show()
            }
        } ?: kotlin.run {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.weather_app_attention)
                .setMessage(R.string.weather_app_technical_error)
                .setNeutralButton(
                    android.R.string.ok
                ) { _, _ -> onDialogButtonSelected() }.show()
        }
    }

    private fun onDialogButtonSelected() {
        val hasNavigated = findNavController().navigateUp()
        if (!hasNavigated) {
            activity?.finish()
        }
    }

    private fun populateUI(comingFromSearch: Boolean) = with(fragmentBinding.cityInfoLayout) {
        if (!comingFromSearch) {
            closeButton.isVisible = false
            searchButton.isVisible = true
            searchButton.setOnClickListener {
                val action = CityWeatherForecastFragmentDirections.openSearch()
                findNavController().navigate(action)
            }
        } else {
            searchButton.isVisible = false
            closeButton.isVisible = true
            closeButton.setOnClickListener {
                findNavController().navigateUp()
            }
        }

        daysRecyclerView.adapter = mDaysAdapter
        forecastRecyclerView.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.weather_app_divider_gray
                )?.let { drawable ->
                    setDrawable(drawable)
                }
            }
        )

        context?.let {
            mDailyForecastAdapter =
                CityWeatherDailyForecastAdapter(it, this@CityWeatherForecastFragment)
            forecastRecyclerView.adapter = mDailyForecastAdapter
        }
    }

    private fun observeViewModel() {
        with(mViewModel) {
            forecastObserver.observe(viewLifecycleOwner) { event ->
                with(fragmentBinding) {
                    when (event) {
                        is ViewModelEvent.Loading -> {
                            cityInfoLayout.root.isVisible = false
                            cityInfoLoadingLayout.root.isVisible = true
                            cityInfoLoadingLayout.loadingAnimationLottieView.setAnimation(R.raw.weather_app_loading)
                            cityInfoLoadingLayout.loadingAnimationLottieView.playAnimation()
                        }

                        is ViewModelEvent.Done -> {
                            cityInfoLayout.root.isVisible = true
                            cityInfoLoadingLayout.root.isVisible = false

                            //Populate city info with data retrieved from search list because
                            //some cities returned by forecast network call have empty name
                            populateCityData(mSelectedCity ?: event.data.city)

                            val forecastMap = event.data.forecastMap
                            if (forecastMap.isNotEmpty()) {
                                //Display temperature info from the first forecast of the first day
                                val firstDayForecastList = forecastMap.values.first()
                                populateTemperature(firstDayForecastList.first().weatherInfo)

                                //Populate RecyclerViews with proper data
                                mDaysAdapter.setDays(forecastMap.keys.toCollection(ArrayList()))
                                mViewModel.onDaySelected(forecastMap.keys.first())

                                //Load correct animation in lottie background
                                mViewModel.getCurrentWeatherProperAnimation()?.let {
                                    cityInfoLayout.lottieView.setAnimation(it)
                                    cityInfoLayout.lottieView.playAnimation()
                                }
                            }
                        }

                        is ViewModelEvent.Error -> {
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle(R.string.weather_app_attention)
                                .setMessage(R.string.weather_app_network_error_message)
                                .setNeutralButton(
                                    android.R.string.ok
                                ) { _, _ -> onDialogButtonSelected() }.show()
                        }
                    }
                }
            }

            dailyForecastObserver.observe(viewLifecycleOwner) {
                mDailyForecastAdapter?.setDailyForecastList(it)
            }
        }
    }

    private fun populateCityData(city: City) = with(fragmentBinding.cityInfoLayout) {
        cityNameTV.text = city.name
        if (city.country.isNullOrEmpty()) {
            cityContryTV.isVisible = false
        } else {
            cityContryTV.text = city.country
        }
    }

    private fun populateTemperature(temperature: DailyWeatherInfo) =
        with(fragmentBinding.cityInfoLayout) {
            if (temperature.perceivedTemperature != null) {
                val perceivedTemperature = temperature.perceivedTemperature.toString() + "°"
                perceivedTemperatureTV.text = perceivedTemperature
            } else {
                perceivedTemperatureTV.isVisible = false
            }

            if (temperature.minTemperature == null && temperature.maxTemperature == null) {
                minMaxTemperatureTV.isVisible = false
            } else {
                val temperatureMessage =
                    if (temperature.minTemperature != null && temperature.maxTemperature != null) {
                        String.format(
                            "(%s %.2f° - %s %.2f°)",
                            getString(R.string.weather_app_min_temperature_short),
                            temperature.minTemperature,
                            getString(R.string.weather_app_max_temperature_short),
                            temperature.maxTemperature
                        )
                    } else if (temperature.minTemperature != null) {
                        String.format(
                            "(%s %.2f°)",
                            getString(R.string.weather_app_min_temperature_short),
                            temperature.minTemperature
                        )
                    } else {
                        String.format(
                            "(%s %.2f°)",
                            getString(R.string.weather_app_max_temperature_short),
                            temperature.maxTemperature
                        )
                    }

                minMaxTemperatureTV.text = temperatureMessage
            }
        }

    override fun onDaySelected(day: String) {
        mViewModel.onDaySelected(day)
    }

    override fun onForecastSelected(forecast: DailyWeatherForecast) {
        ForecastDetailFragment.newInstance(forecast).show(childFragmentManager, "TAG")
    }
}