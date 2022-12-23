package com.whatstheweatherapp.common

fun String?.isNotNullAndNotEmpty(): Boolean {
    return this != null && this.isNotEmpty()
}