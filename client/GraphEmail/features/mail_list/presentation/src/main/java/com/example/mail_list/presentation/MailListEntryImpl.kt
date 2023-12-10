package com.example.mail_list.presentation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.example.common.router.Destinations
import com.example.common.router.find
import com.example.core.dependency.findDependencies
import com.example.core.dependency.injectedViewModel
import com.example.filter.router.FilterEntry
import com.example.login.router.LoginEntry
import com.example.mail_list.presentation.di.DaggerMailListComponent
import com.example.mail_list.presentation.ui.MailListScreen
import com.example.mail_list.router.MailListEntry
import javax.inject.Inject

class MailListEntryImpl @Inject constructor() : MailListEntry(){

    @Composable
    override fun Composable(
        navController: NavHostController,
        destinations: Destinations,
        backStackEntry: NavBackStackEntry
    ) {
        val context = LocalContext.current

        val startDate = backStackEntry.arguments?.getString(START_DATE)
        val endDate = backStackEntry.arguments?.getString(END_DATE)
        val sender = backStackEntry.arguments?.getString(SENDER)
        val receiver = backStackEntry.arguments?.getString(RECEIVER)
        val subject = backStackEntry.arguments?.getString(SUBJECT)

        val viewModel = injectedViewModel {
            DaggerMailListComponent.builder()
                .mailListDeps((context as Activity).findDependencies())
                .build()
                .mailListViewModel
        }
        MailListScreen(
            viewModel = viewModel,
            navigateToFilter = {
                val destination = destinations
                    .find<FilterEntry>()
                    .destination()
                navController.navigate(destination)
            },
            navigateToLogin = {
                val destination = destinations
                    .find<LoginEntry>()
                    .destination()
                navController.navigate(destination)
            },
            startDate =  startDate ?: "",
            endDate = endDate ?: "",
            sender = sender ?: "",
            receiver = receiver ?: "",
            subject = subject ?: ""
        )
    }
}