package com.example.mail_list.domain

import com.example.common.domain.Entity

interface MailListInteractor {
    suspend fun getMails() : Entity<MailListEntity>
}