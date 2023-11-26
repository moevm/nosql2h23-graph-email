package com.example.backend.api.utils.dto

import com.google.gson.annotations.SerializedName
import com.example.backend.api.utils.exception.ErrorCode

data class ServerResponse<T>(
    @SerializedName("message") val message: String?,
    @SerializedName("httpCode") val code: Int = ErrorCode.NO_CODE.value,
    @SerializedName("data") val data: T?,
)
