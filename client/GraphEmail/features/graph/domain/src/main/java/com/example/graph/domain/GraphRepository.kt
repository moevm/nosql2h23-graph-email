package com.example.graph.domain

import com.example.common.domain.Entity

interface GraphRepository {
    suspend fun getGraphWithFilter(
        startDate: String?,
        endDate: String?,
        sender: String?,
        receiver: String?,
        subject: String?
    ): Entity<String>
}