package com.example.data.repository.repository

import android.os.Environment
import com.example.backend.api.api.ApiService
import com.example.backend.api.models.ResponseStatus
import com.example.common.domain.Entity
import com.example.data.repository.base.BaseRepository
import com.example.data.repository.mappers.asRequest
import com.example.login.domain.LoginEntity
import com.example.login.domain.LoginRepository
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.nio.charset.StandardCharsets

class LoginRepositoryImpl(
    private val api: ApiService
) : BaseRepository(), LoginRepository {
    override suspend fun login(value: LoginEntity): Entity<Boolean> {
        return when (
            val response = safeApiSuspendResult {
                api.login(value.asRequest())
            }
        ) {
            is ResponseStatus.Success -> {
                Entity.Success(true)
            }

            is ResponseStatus.Error -> {
                Entity.Error(
                    response.exception.message ?: ""
                )
            }
        }
    }

    override suspend fun import(): Entity<Boolean> {
        try {
            val directory = File(Environment.getExternalStorageDirectory(), "Download")
            val jsonFile = File(directory, "graph.json")
            val requestBody = getRequestBodyFromJson(jsonFile)

            return when (
                val response = safeApiSuspendResult {
                    api.import(requestBody)
                }
            ) {
                is ResponseStatus.Success -> {
                    Entity.Success(true)
                }

                is ResponseStatus.Error -> {
                    Entity.Error(
                        response.exception.message ?: ""
                    )
                }
            }
        } catch (e: Exception) {
            return Entity.Error()
        }
    }

    private fun getRequestBodyFromJson(file: File): RequestBody {
        val jsonString = file.readText(StandardCharsets.UTF_8)
        return object : RequestBody() {
            override fun contentType(): MediaType? {
                return "application/json".toMediaTypeOrNull()
            }

            override fun writeTo(sink: BufferedSink) {
                sink.writeUtf8(jsonString)
            }
        }
    }
}