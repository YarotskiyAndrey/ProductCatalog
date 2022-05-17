package com.task.productcatalog.util

sealed class Resource<T>(open val value: T?) {
    data class Success<T>(override val value: T) : Resource<T>(value)
    data class Failure<T>(val exception: Exception) : Resource<T>(null)
}