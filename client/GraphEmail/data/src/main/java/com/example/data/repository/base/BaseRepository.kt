package com.example.data.repository.base

import android.util.Log
import com.example.backend.api.utils.dto.ServerResponse
import com.example.backend.api.utils.exception.AuthException
import com.example.backend.api.utils.exception.ErrorCode
import com.example.backend.api.utils.exception.NetworkException
import com.example.backend.api.utils.exception.NoNetworkException
import com.example.common.domain.Entity
import com.example.data.repository.base.ResponseCodes.AUTHENTICATION_ERROR_CODE
import com.example.data.repository.base.ResponseCodes.SERVER_ERROR_CODE
import com.example.data.repository.base.ResponseCodes.SUCCESS_CODES
import retrofit2.HttpException
import com.example.backend.api.models.ResponseStatus
import com.example.backend.api.utils.exception.NetworkError
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Response
import java.net.UnknownHostException

open class BaseRepository(
    private val repository: String = TAG,
) {

    /*protected suspend fun <K : Any> safeApiSuspendResultNoResponse(call: suspend () -> Response<K>?): ResponseStatus<K> {
        val response: Response<K>?
        try {
            response = call.invoke()
            if (response != null && response.isSuccessful) {
                return ResponseStatus.Success(response.body(), response.code())
            }
            val errorBody: Error? = response?.errorBody()?.parseError()
            return ResponseStatus.Error(
                NetworkException(
                    errorBody?.message,
                    Throwable(repository)
                )
            )
        } catch (e: Exception) {
            Log.e("BaseRepository: ", e.message.toString())
            when (e) {
                is UnknownHostException -> return ResponseStatus.Error(
                    NoNetworkException(
                        e.message,
                        Throwable(repository)
                    )
                )
                is JsonSyntaxException -> {
                    Log.e("BaseRepository: ", e.message.toString())
                    return ResponseStatus.Error(
                        NetworkException(
                            e.message,
                            Throwable(repository)
                        )
                    )
                }
                is HttpException -> return ResponseStatus.Error(
                    NoNetworkException(
                        e.message,
                        Throwable(repository)
                    )
                )
                else -> return ResponseStatus.Error(
                    NetworkException(
                    e.message,
                    Throwable(repository)
                ))
            }
        }
    }*/

    protected suspend fun <K : Any> safeApiSuspendResult(call: suspend () -> Response<K>?): ResponseStatus<K> {
        val response: Response<K>?
        return try {
            response = call.invoke()
            when (response?.code()) {
                in SUCCESS_CODES -> {
                    ResponseStatus.Success(
                        response?.body(),
                        response?.code() ?: ErrorCode.NO_CODE.value
                    )
                }
                in SERVER_ERROR_CODE -> {
                    ResponseStatus.Error(
                        NetworkException(
                            response?.message(), // TODO доделать прокидывание текстов
                            Throwable(repository),
                        )
                    )
                }
                AUTHENTICATION_ERROR_CODE -> {
                    ResponseStatus.Error(
                        AuthException(
                            response.message(), // TODO доделать прокидывание текстов
                            Throwable(repository),
                        )
                    )
                }
                else -> {
                    ResponseStatus.Error(
                        NetworkException(
                            response?.message(), // TODO доделать прокидывание текстов
                            Throwable(repository),
                        )
                    )
                }
            }
        } catch (e: Exception) {
            when (e) {
                is UnknownHostException -> {
                    ResponseStatus.Error(
                        NoNetworkException(
                            e.message, // TODO доделать прокидывание текстов
                            Throwable(repository),
                        )
                    )
                }
                is HttpException -> {
                    if (e.code() == 401) {
                        ResponseStatus.Error(
                            AuthException(
                                "Время сессии истекло", // TODO доделать прокидывание текстов
                                Throwable(repository),
                            )
                        )
                    } else {
                        ResponseStatus.Error(
                            NetworkException(
                                e.message, // TODO доделать прокидывание текстов
                                Throwable(repository),
                            )
                        )
                    }

                }
                else -> {
                    ResponseStatus.Error(
                        NetworkException(
                            e.message, // TODO доделать прокидывание текстов
                            Throwable(repository),
                        )
                    )
                }
            }
        }
    }

    protected fun <G : Any> map(call: () -> G): Entity<G> {
        return try {
            Entity.Success(
                call.invoke()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Entity.Error("Произошла ошибка")
        }
    }

    protected fun <G : Any> mapSocket(call: () -> G?): G? {
        return try {
            call.invoke()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    companion object {
        private const val TAG = "Repository"
    }
}

fun ResponseBody.parseError(): Error {
    return try {
        val gson = Gson()
        val type = object : TypeToken<Error>() {}.type
        gson.fromJson(charStream(), type)
    } catch (e: JsonSyntaxException) {
        Error("", Throwable(e.message))
    }
}