package com.whatstheweatherapp.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class CoreDispatcherProvider : DispatcherProvider {
    override fun io(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    override fun main(): CoroutineDispatcher {
        return Dispatchers.Main
    }
}