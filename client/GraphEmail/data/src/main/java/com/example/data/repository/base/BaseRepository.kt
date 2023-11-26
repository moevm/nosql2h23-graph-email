package com.example.data.repository.base

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
import java.net.UnknownHostException

open class BaseRepository(
    private val repository: String = TAG,
) {

    protected suspend fun <K : Any> safeApiSuspendResult(call: suspend () -> ServerResponse<K>?): ResponseStatus<K> {
        val response: ServerResponse<K>?
        return try {
            response = call.invoke()
            when (response?.code) {
                in SUCCESS_CODES -> {
                    ResponseStatus.Success(
                        response?.data,
                        response?.code ?: ErrorCode.NO_CODE.value
                    )
                }
                in SERVER_ERROR_CODE -> {
                    ResponseStatus.Error(
                        NetworkException(
                            response?.message,
                            Throwable(repository),
                        )
                    )
                }
                AUTHENTICATION_ERROR_CODE -> {
                    ResponseStatus.Error(
                        AuthException(
                            response.message,
                            Throwable(repository),
                        )
                    )
                }
                else -> {
                    ResponseStatus.Error(
                        NetworkException(
                            response?.message,
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
                            e.message,
                            Throwable(repository),
                        )
                    )
                }
                is HttpException -> {
                    if (e.code() == 401) {
                        ResponseStatus.Error(
                            AuthException(
                                "Время сессии истекло",
                                Throwable(repository),
                            )
                        )
                    } else {
                        ResponseStatus.Error(
                            NetworkException(
                                e.message,
                                Throwable(repository),
                            )
                        )
                    }

                }
                else -> {
                    ResponseStatus.Error(
                        NetworkException(
                            e.message,
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
