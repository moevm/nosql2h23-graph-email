package com.example.graph.presentation.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.domain.Entity
import com.example.graph.domain.GraphInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class GraphViewModel @Inject constructor(
    private val interactor: GraphInteractor
) : ViewModel() {
    private val _graphEntity = MutableStateFlow("")
    val graphEntity = _graphEntity.asStateFlow()

    fun getGraphWithFilter(
        startDate: String?,
        endDate: String?,
        sender: String?,
        receiver: String?,
        subject: String?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = interactor.getGraphWithFilter(
                startDate,
                endDate,
                sender,
                receiver,
                subject
            )) {
                is Entity.Success -> {
                    Log.d("Graph", "Success")
                    _graphEntity.value = response.data
                }

                is Entity.Error -> {
                    _graphEntity.value = ""
                    Log.d("Graph", "response.message ${response.message}")
                }
            }
        }
    }

    fun buildGraphUrl(
        baseUrl: String,
        startDate: String?,
        endDate: String?,
        sender: String?,
        receiver: String?,
        subject: String?
    ): String {

        if (startDate.isNullOrEmpty() && endDate.isNullOrEmpty() && sender.isNullOrEmpty() && receiver.isNullOrEmpty() && subject.isNullOrEmpty()) {
            return baseUrl
        }

        val urlBuilder = StringBuilder(baseUrl)

        if (!startDate.isNullOrEmpty()) {
            urlBuilder.append("&start_date=").append(startDate)
        }

        if (!endDate.isNullOrEmpty()) {
            urlBuilder.append("&end_date=").append(endDate)
        }

        if (!sender.isNullOrEmpty()) {
            urlBuilder.append("&email_sender=").append(sender)
        }

        if (!receiver.isNullOrEmpty()) {
            urlBuilder.append("&email_deliver=").append(receiver)
        }

        if (!subject.isNullOrEmpty()) {
            urlBuilder.append("&subject=").append(subject)
        }

        return urlBuilder.toString()
    }
}