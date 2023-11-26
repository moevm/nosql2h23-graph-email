package com.example.graphemail

import android.app.Activity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.core.dependency.destinationsProvider
import com.example.core.dependency.findDestinations
import com.example.login.router.LoginEntry
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation() {
    val navController = rememberAnimatedNavController()
    val context = (LocalContext.current as Activity)
    val loginScreen = context.findDestinations<LoginEntry>()

    Box(Modifier.fillMaxSize()) {
        AnimatedNavHost(
            navController,
            startDestination = loginScreen.destination()
        ) {
            with(loginScreen) {
                animatedComposable(
                    navController,
                    context.destinationsProvider,
                )
            }
        }
    }
}
