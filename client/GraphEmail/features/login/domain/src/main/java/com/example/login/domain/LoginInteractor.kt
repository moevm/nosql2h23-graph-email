package com.example.login.domain

import com.example.common.domain.Entity

interface LoginInteractor {
    suspend fun login(
        value: LoginEntity
    ) : Entity<Boolean>
}