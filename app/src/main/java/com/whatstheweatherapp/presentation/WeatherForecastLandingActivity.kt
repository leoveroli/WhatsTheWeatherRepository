package com.whatstheweatherapp.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.location.LocationServices
import com.whatstheweatherapp.R
import com.whatstheweatherapp.databinding.WeatherLandingActivityLayoutBinding
import com.whatstheweatherapp.presentation.landing.CityWeatherForecastFragmentArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherForecastLandingActivity : AppCompatActivity() {

    private lateinit var mBinding: WeatherLandingActivityLayoutBinding
    private lateinit var mNavigationController: NavController
    private val mRequestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            retrieveUserLocation()
        } else {
            initializeGraph(R.id.searchCityFragment)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = WeatherLandingActivityLayoutBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        when (resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

            Configuration.UI_MODE_NIGHT_UNDEFINED,
            Configuration.UI_MODE_NIGHT_NO -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            retrieveUserLocation()
        } else {
            mRequestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    @SuppressLint("MissingPermission")
    private fun retrieveUserLocation() {
        val locationProvider = LocationServices.getFusedLocationProviderClient(this)
        locationProvider.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                Log.d(
                    "LandingActivity",
                    "LAT: ${location.latitude} LONG: ${location.longitude}"
                )

                val argsBundle = CityWeatherForecastFragmentArgs(
                    latitude = location.latitude.toString(),
                    longitude = location.longitude.toString(),
                    fromSearch = false
                ).toBundle()

                initializeGraph(
                    R.id.cityWeatherForecastFragment,
                    argsBundle
                )
            } else {
                initializeGraph(R.id.searchCityFragment)
            }
        }

        locationProvider.lastLocation.addOnFailureListener {
            initializeGraph(R.id.searchCityFragment)
        }
    }

    private fun initializeGraph(@IdRes startDestinationId: Int, destinationArgs: Bundle? = null) {
        (supportFragmentManager.findFragmentById(mBinding.fragmentContainer.id) as? NavHostFragment)?.let {
            mNavigationController = it.navController

            val graph =
                it.navController.navInflater.inflate(R.navigation.weather_forecast_nav_graph)
            graph.setStartDestination(startDestinationId)
            it.navController.setGraph(graph, destinationArgs)
        } ?: finish()
    }
}