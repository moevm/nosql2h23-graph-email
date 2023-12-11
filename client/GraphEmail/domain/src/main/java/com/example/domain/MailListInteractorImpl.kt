package com.example.domain

import com.example.common.domain.Entity
import com.example.mail_list.domain.MailListEntity
import com.example.mail_list.domain.MailListInteractor
import com.example.mail_list.domain.MailListRepository

class MailListInteractorImpl(
    private val mailListRepository: MailListRepository
) : MailListInteractor {
    override suspend fun getMails(): Entity<MailListEntity> {
        return mailListRepository.getMails()
    }

    override suspend fun getMailsWithFilter(
        startDate: String?,
        endDate: String?,
        sender: String?,
        receiver: String?,
        subject: String?
    ): Entity<MailListEntity> {
        return mailListRepository.getMailsWithFilter(
            startDate, endDate, sender, receiver, subject
        )
    }
}