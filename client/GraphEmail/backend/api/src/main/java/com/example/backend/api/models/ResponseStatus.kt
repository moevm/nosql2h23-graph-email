package com.example.backend.api.models

sealed class ResponseStatus<out T : Any> {
    data class Success<out T : Any>(
        val data: T?,
        val code: Int
    ) : ResponseStatus<T>() {
        override fun <K : Any> map(mapper: (oldValue: T?) -> K?): ResponseStatus<K> {
            return Success(mapper.invoke(data), code)
        }
    }

    data class Error<out T : Any>(
        val exception: Exception,
    ): ResponseStatus<T>() {
        override fun <K : Any> map(mapper: (oldValue: T?) -> K?): ResponseStatus<K> {
            return Error(exception)
        }
    }

    abstract fun <K : Any> map(mapper: (oldValue: T?) -> K?): ResponseStatus<K>
}