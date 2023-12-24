package com.example.data.repository.repository

import android.os.Environment
import android.util.Log
import com.example.backend.api.api.ApiService
import com.example.backend.api.models.ResponseStatus
import com.example.common.domain.Entity
import com.example.data.repository.base.BaseRepository
import com.example.data.repository.mappers.asDomain
import com.example.mail_list.domain.MailListEntity
import com.example.mail_list.domain.MailListRepository
import java.io.File
import java.io.FileOutputStream

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

    override suspend fun export(): Entity<Boolean> {
        return when(
            val response = safeApiSuspendResult {
                api.export(export = true)
            }
        ){
            is ResponseStatus.Success -> {
                response.data?.let { responseBody ->
                    val json = responseBody.string()
                    Log.d("MailList Repo", json)
                    saveJsonToFile(json)
                }

                Entity.Success(true)
            }
            is ResponseStatus.Error -> {
                Log.d("MailList Repo", "response.exception ${response.exception.message}")
                Entity.Error(
                    response.exception.message ?: ""
                )
            }
        }
    }

    private fun saveJsonToFile(json: String) {
        try {
            val directory = File(Environment.getExternalStorageDirectory(), "Download")
            if (!directory.exists()) {
                directory.mkdirs()
            }
            val file = File(directory, "graph.json")
            file.createNewFile()
            FileOutputStream(file).use { fos ->
                fos.write(json.toByteArray())
            }
            Log.d("MailListRepository", "JSON saved to file: graph.json")
        } catch (e: Exception) {
            Log.e("MailListRepository", "Error saving JSON to file: $e")
        }
    }
}