package com.example.domain

import com.example.common.domain.Entity
import com.example.login.domain.LoginEntity
import com.example.login.domain.LoginInteractor
import com.example.login.domain.LoginRepository

class LoginInteractorImpl(
    private val repository: LoginRepository
) : LoginInteractor {
    override suspend fun login(value: LoginEntity): Entity<Boolean> {
        return repository.login(value)
    }
}