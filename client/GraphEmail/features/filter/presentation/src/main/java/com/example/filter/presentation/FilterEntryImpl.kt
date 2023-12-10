package com.example.filter.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.example.common.router.Destinations
import com.example.common.router.find
import com.example.filter.presentation.ui.FilterScreen
import com.example.filter.router.FilterEntry
import com.example.mail_list.router.MailListEntry
import javax.inject.Inject

class FilterEntryImpl @Inject constructor() : FilterEntry() {

    @Composable
    override fun Composable(
        navController: NavHostController,
        destinations: Destinations,
        backStackEntry: NavBackStackEntry
    ) {
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
                navController.navigate(destination)
            }

        FilterScreen(
            navigateToMailList = navigateToMailList
        )
    }
}