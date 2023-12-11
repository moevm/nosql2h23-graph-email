package com.example.data.repository.mappers

import com.example.graph.domain.GraphEntity

fun String.asResponse(): GraphEntity = GraphEntity(
    body = this
)