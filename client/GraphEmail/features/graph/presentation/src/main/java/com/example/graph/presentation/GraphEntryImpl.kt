package com.example.graph.presentation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.example.common.router.Destinations
import com.example.common.router.find
import com.example.common.router.navigateAndClean
import com.example.core.dependency.findDependencies
import com.example.core.dependency.injectedViewModel
import com.example.filter.router.FilterEntry
import com.example.graph.presentation.di.DaggerGraphComponent
import com.example.graph.presentation.ui.GraphScreen
import com.example.graph.router.GraphEntry
import com.example.login.router.LoginEntry
import com.example.mail_list.router.MailListEntry
import javax.inject.Inject

class GraphEntryImpl @Inject constructor() : GraphEntry() {

    @Composable
    override fun Composable(
        navController: NavHostController,
        destinations: Destinations,
        backStackEntry: NavBackStackEntry
    ) {
        val context = LocalContext.current

        val startDateArg = backStackEntry.arguments?.getString(START_DATE)
        val endDateArg = backStackEntry.arguments?.getString(END_DATE)
        val senderArg = backStackEntry.arguments?.getString(SENDER)
        val receiverArg = backStackEntry.arguments?.getString(RECEIVER)
        val subjectArg = backStackEntry.arguments?.getString(SUBJECT)

        val viewModel = injectedViewModel {
            DaggerGraphComponent.builder()
                .graphDeps((context as Activity).findDependencies())
                .build()
                .graphViewModel
        }

        val navigateToMailList =
            { startDate: String, endDate: String, sender: String, receiver: String, subject: String ->
                val destination = destinations
                    .find<MailListEntry>()
                    .destination(
                        startDate = startDate,
                        endDate = endDate,
                        sender = sender,
                        receiver = receiver,
                        subject = subject
                    )
                navController.navigateAndClean(destination)
            }

        GraphScreen(
            viewModel,
            startDateArg,
            endDateArg,
            senderArg,
            receiverArg,
            subjectArg,
            navigateToLogin = {
                val destination = destinations
                    .find<LoginEntry>()
                    .destination()
                navController.navigate(destination)
            },
            navigateToFilter = {
                val destination = destinations
                    .find<FilterEntry>()
                    .destination()
                navController.navigate(destination)
            },
            navigateToMailList = navigateToMailList
        )
    }
}