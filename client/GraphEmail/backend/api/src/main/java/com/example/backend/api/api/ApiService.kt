package com.example.backend.api.api

import com.example.backend.api.dto.LoginRequest
import com.example.backend.api.utils.dto.ServerResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/login")
    suspend fun login(
        @Body body: LoginRequest
    ) : ServerResponse<Boolean>
}