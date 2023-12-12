package com.example.data.repository.repository

import android.util.Log
import com.example.backend.api.api.ApiService
import com.example.backend.api.models.ResponseStatus
import com.example.common.domain.Entity
import com.example.data.repository.base.BaseRepository
import com.example.graph.domain.GraphRepository

class GraphRepositoryImpl(
    private val api: ApiService
) : BaseRepository(), GraphRepository {

    override suspend fun getGraphWithFilter(
        startDate: String?,
        endDate: String?,
        sender: String?,
        receiver: String?,
        subject: String?
    ): Entity<String> {
        val queryFilters = mutableMapOf<String, String>()
        if (!startDate.isNullOrEmpty()){
            queryFilters["start_date"] = startDate
        }
        if (!endDate.isNullOrEmpty()){
            queryFilters["end_date"] = endDate
        }
        if (!sender.isNullOrEmpty()){
            queryFilters["email_sender"] = sender
        }
        if (!receiver.isNullOrEmpty()){
            queryFilters["email_deliver"] = receiver
        }
        if (!subject.isNullOrEmpty()){
            queryFilters["subject"] = subject
        }
        return when(
            val response = safeApiSuspendResult {

                api.getGraphWithFilter( queryFilters )
            }
        ){
            is ResponseStatus.Success -> {
                Entity.Success(response.data.toString())
            }
            is ResponseStatus.Error -> {
                Log.d("Graph Repo", "response.exception ${response}")
                Entity.Error(
                    response.exception.message ?: ""
                )
            }
        }
    }
}