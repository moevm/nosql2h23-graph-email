package com.example.mail_list.domain

import com.example.common.domain.Entity

interface MailListRepository {
    suspend fun getMails() : Entity<MailListEntity>
}