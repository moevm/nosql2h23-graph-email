package com.example.data.repository.mappers

import com.example.backend.api.dto.LoginRequest
import com.example.login.domain.LoginEntity

fun LoginEntity.asRequest(): LoginRequest = LoginRequest(
    email = email,
    password = password
)