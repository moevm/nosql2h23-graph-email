package com.example.common.domain

sealed class Entity<out T : Any> {
    data class Success<out T : Any>(
        val data: T
    ) : Entity<T>()

    data class Error<out T : Any>(
        val message: String = ""
    ) : Entity<T>()
}
