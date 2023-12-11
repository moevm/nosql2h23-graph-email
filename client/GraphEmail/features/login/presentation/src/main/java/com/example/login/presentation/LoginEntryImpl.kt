package com.example.login.presentation

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
import com.example.login.presentation.di.DaggerLoginComponent
import com.example.login.presentation.ui.LoginScreen
import com.example.login.router.LoginEntry
import com.example.mail_list.router.MailListEntry
import javax.inject.Inject

class LoginEntryImpl @Inject constructor() : LoginEntry() {

    @Composable
    override fun Composable(
        navController: NavHostController,
        destinations: Destinations,
        backStackEntry: NavBackStackEntry
    ) {
        val context = LocalContext.current
        val viewModel = injectedViewModel {
            DaggerLoginComponent.builder()
                .loginDeps((context as Activity).findDependencies())
                .build()
                .loginViewModel
        }
        LoginScreen(
            viewModel = viewModel,
            navigateToMailList = {
                val destination = destinations
                    .find<MailListEntry>()
                    .destination( "","","","","" )
                navController.navigateAndClean(destination)
            }
        )
    }
}