package com.example.domain

import com.example.common.domain.Entity
import com.example.graph.domain.GraphInteractor
import com.example.graph.domain.GraphRepository

class GraphInteractorImpl(
    private val graphRepository: GraphRepository
) : GraphInteractor {

    override suspend fun getGraphWithFilter(
        startDate: String?,
        endDate: String?,
        sender: String?,
        receiver: String?,
        subject: String?
    ): Entity<String> {
        return graphRepository.getGraphWithFilter(
            startDate, endDate, sender, receiver, subject
        )
    }
}