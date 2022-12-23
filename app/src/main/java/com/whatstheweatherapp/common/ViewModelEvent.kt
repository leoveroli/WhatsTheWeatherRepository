package com.whatstheweatherapp.common

import java.lang.Exception

sealed class ViewModelEvent<out T> {
    object Loading : ViewModelEvent<Nothing>()
    class Done<out T>(val data: T) : ViewModelEvent<T>()
    class Error(val exception: Exception) : ViewModelEvent<Nothing>()
}
