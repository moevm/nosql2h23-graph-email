package com.example.data.repository.repository

import android.util.Log
import com.example.backend.api.api.ApiService
import com.example.backend.api.models.ResponseStatus
import com.example.common.domain.Entity
import com.example.data.repository.base.BaseRepository
import com.example.data.repository.mappers.asDomain
import com.example.mail_list.domain.MailListEntity
import com.example.mail_list.domain.MailListRepository

class MailListRepositoryImpl(
    private val api: ApiService
) : BaseRepository(), MailListRepository {

    override suspend fun getMails(): Entity<MailListEntity> {
        return when(
            val response = safeApiSuspendResult {
                api.getMails(export = false, onlyLetters = false,1000)
            }
        ){
            is ResponseStatus.Success -> {
                response.data?.let { data ->
                    map {
                        data.asDomain()
                    }
                } ?: run {
                    Entity.Error(
                        "Произошла ошибка"
                    )
                }
            }
            is ResponseStatus.Error -> {
                Log.d("MailList Repo", "response.exception ${response.exception.message}")
                Entity.Error(
                    response.exception.message ?: ""
                )
            }
        }
    }

    override suspend fun getMailsWithFilter(
        startDate: String?,
        endDate: String?,
        sender: String?,
        receiver: String?,
        subject: String?
    ): Entity<MailListEntity> {
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

                api.getMailsWithFilter(export = false, onlyLetters = false,1000, queryFilters)
            }
        ){
            is ResponseStatus.Success -> {
                response.data?.let { data ->
                    map {
                        data.asDomain()
                    }
                } ?: run {
                    Entity.Error(
                        "Произошла ошибка"
                    )
                }
            }
            is ResponseStatus.Error -> {
                Log.d("MailList Repo", "response.exception ${response.exception.message}")
                Entity.Error(
                    response.exception.message ?: ""
                )
            }
        }
    }
}