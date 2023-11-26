package com.example.login.domain

import com.example.common.domain.Entity

interface LoginRepository {
    suspend fun login(
        value: LoginEntity
    ): Entity<Boolean>
}