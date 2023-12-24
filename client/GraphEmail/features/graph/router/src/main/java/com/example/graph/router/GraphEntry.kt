package com.example.graph.router

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.common.router.ComposableFeatureEntry

abstract class GraphEntry : ComposableFeatureEntry {

    final override val featureRoute =
        "graphScreen?$START_DATE={$START_DATE}&$END_DATE={$END_DATE}&$SENDER={$SENDER}&$RECEIVER={$RECEIVER}&$SUBJECT={$SUBJECT}"

    final override val arguments = listOf(
        navArgument(START_DATE) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        },
        navArgument(END_DATE) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        },
        navArgument(SENDER) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        },
        navArgument(RECEIVER) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        },
        navArgument(SUBJECT) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        },
    )

    fun destination(
        startDate: String?,
        endDate: String?,
        sender: String?,
        receiver: String?,
        subject: String?
    ): String = "graphScreen?$START_DATE=$startDate&$END_DATE=$endDate&$SENDER=$sender&$RECEIVER=$receiver&$SUBJECT=$subject"

    protected companion object {
        const val START_DATE = "startDate"
        const val END_DATE = "endDate"
        const val SENDER = "sender"
        const val RECEIVER = "receiver"
        const val SUBJECT = "subject"
    }

}