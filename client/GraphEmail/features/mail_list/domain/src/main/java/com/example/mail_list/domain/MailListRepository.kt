package com.example.mail_list.domain

import com.example.common.domain.Entity

interface MailListRepository {
    suspend fun getMails() : Entity<MailListEntity>

    suspend fun getMailsWithFilter(
        startDate: String?,
        endDate: String?,
        sender: String?,
        receiver: String?,
        subject: String?
    ): Entity<MailListEntity>

    suspend fun export() : Entity<Boolean>
}