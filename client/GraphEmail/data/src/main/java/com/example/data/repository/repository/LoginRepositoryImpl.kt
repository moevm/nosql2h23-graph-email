package com.example.data.repository.repository

import android.util.Log
import com.example.backend.api.api.ApiService
import com.example.backend.api.models.ResponseStatus
import com.example.common.domain.Entity
import com.example.data.repository.base.BaseRepository
import com.example.data.repository.mappers.asRequest
import com.example.login.domain.LoginEntity
import com.example.login.domain.LoginRepository

class LoginRepositoryImpl(
    private val api: ApiService
) : BaseRepository(), LoginRepository {
    override suspend fun login(value: LoginEntity): Entity<Boolean> {
        return when(
            val response = safeApiSuspendResult {
                api.login(value.asRequest())
            }
        ){
            is ResponseStatus.Success -> {
                Log.d("Login Repo", "response.data ${response.data}")
                Log.d("Login Repo", "response.code ${response.code}")
                return Entity.Success(true)
            }
            is ResponseStatus.Error -> {
                Log.d("Login Repo", "response.exception ${response.exception}")
                Entity.Error(
                    response.exception.message ?: ""
                )
            }
        }
    }
}