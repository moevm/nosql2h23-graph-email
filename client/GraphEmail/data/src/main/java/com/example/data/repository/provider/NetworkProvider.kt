package com.example.data.repository.provider

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val HOST = "http://192.168.1.216:5000"
class NetworkProvider {

    private val client = OkHttpClient.Builder().apply {
        readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
        connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)

        addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )

    }.build()

    fun <T> provideRetrofit(
        clazz: Class<T>,
    ): T {
        return Retrofit.Builder()
            .baseUrl(HOST)
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().create()
                )
            )
            .build()
            .create(clazz)
    }

    companion object {
        private const val CONNECT_TIME_OUT: Long = 120
        private const val READ_TIME_OUT: Long = 120
    }
}
