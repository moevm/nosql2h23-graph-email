package com.example.backend.api.api

import com.example.backend.api.dto.LoginRequest
import com.example.backend.api.dto.MailListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {
    @POST("api/login")
    suspend fun login(
        @Body body: LoginRequest
    ) : Response<Boolean>

    @GET("api/graph/graph_data")
    suspend fun getMails(
        @Query("export") export: Boolean,
        @Query("only_letters") onlyLetters: Boolean,
        @Query("limit") limit: Int,
    ) : Response<MailListResponse>

    @GET("api/graph/graph_data")
    suspend fun getMailsWithFilter(
        @Query("export") export: Boolean,
        @Query("only_letters") onlyLetters: Boolean,
        @Query("limit") limit: Int,
        @QueryMap queryFilters: Map<String, String>
    ) : Response<MailListResponse>
}