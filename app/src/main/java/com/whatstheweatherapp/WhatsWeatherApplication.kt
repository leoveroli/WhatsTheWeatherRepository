package com.whatstheweatherapp

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WhatsWeatherApplication : Application()