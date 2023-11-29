package com.example.mail_list.presentation.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.domain.Entity
import com.example.mail_list.domain.MailListEntity
import com.example.mail_list.domain.MailListInteractor
import com.example.mail_list.presentation.ui.model.Mail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class MailListViewModel @Inject constructor(
    val interactor: MailListInteractor
) : ViewModel(){

    private val _mailList = MutableStateFlow<MailListEntity?>(null)
    val mailList = _mailList.asStateFlow()

    private val _mailListCards = MutableStateFlow<List<Mail>?>(null)
    val mailListCards = _mailListCards.asStateFlow()

    init {
        getMails()
    }

    fun getMails(){
        viewModelScope.launch(Dispatchers.IO) {
            when(val response = interactor.getMails()){
                is Entity.Success -> {
                    Log.d("Mail List", "Success")
                    _mailList.value = response.data
                    _mailListCards.value = createMailList(response.data)
                    //Log.d("createMailList", "${createMailList(response.data)}")
                }
                is Entity.Error -> {
                    _mailList.value = null
                    Log.d("Mail List", "response.message ${response.message}")
                }
            }
        }
    }

    fun createMailList(mailListEntity: MailListEntity): List<Mail> {
        val nodesPersonsMap = mailListEntity.nodesPerson?.associateBy { it.id ?: "" } ?: emptyMap()
        val nodesLettersMap = mailListEntity.nodesLetter?.associateBy { it.id ?: "" } ?: emptyMap()
        val mainPersonMap = mailListEntity.mainPerson?.let { mapOf(it.id to it) } ?: emptyMap()

        return mailListEntity.links?.flatMap { link ->
            val sourcePerson = nodesPersonsMap[link.source ?: ""]
            val targetPerson = nodesPersonsMap[link.target ?: ""]
            val letter = nodesLettersMap[link.source ?: ""]
            val mainPersonSource = mainPersonMap[link.source ?: ""]
            val mainPersonTarget = mainPersonMap[link.target ?: ""]

            val mails = mutableListOf<Mail>()

            if (sourcePerson != null && letter != null) {
                mails.add(
                    Mail(
                        subject = letter.subject,
                        author = sourcePerson.name,
                        date = formatDateString(link.date.toString())
                    )
                )
            }

            if (targetPerson != null && letter != null) {
                mails.add(
                    Mail(
                        subject = letter.subject,
                        author = targetPerson.name,
                        date = formatDateString(link.date.toString())
                    )
                )
            }

            if (mainPersonSource != null && letter != null) {
                mails.add(
                    Mail(
                        subject = letter.subject,
                        author = mainPersonSource.name,
                        date = formatDateString(link.date.toString())
                    )
                )
            }

            if (mainPersonTarget != null && letter != null) {
                mails.add(
                    Mail(
                        subject = letter.subject,
                        author = mainPersonTarget.name,
                        date = formatDateString(link.date.toString())
                    )
                )
            }

            mails
        } ?: emptyList()
    }

    private fun formatDateString(input: String): String? {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        try {
            val date = inputFormat.parse(input)
            return date?.let { outputFormat.format(it) }
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return ""
    }
}