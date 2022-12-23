package com.whatstheweatherapp.common

import java.lang.Exception

sealed class Result<out T> {
    class Success<out T>(val data: T) : Result<T>()
    class Failure(val e: Exception) : Result<Nothing>()
}