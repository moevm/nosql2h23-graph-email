package com.example.filter.presentation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.example.common.router.Destinations
import com.example.core.dependency.findDependencies
import com.example.core.dependency.injectedViewModel
import com.example.filter.presentation.di.DaggerFilterComponent
import com.example.filter.presentation.ui.FilterScreen
import com.example.filter.router.FilterEntry
import javax.inject.Inject

class FilterEntryImpl @Inject constructor() : FilterEntry(){

    @Composable
    override fun Composable(
        navController: NavHostController,
        destinations: Destinations,
        backStackEntry: NavBackStackEntry
    ) {
        val context = LocalContext.current
        val viewModel = injectedViewModel {
            DaggerFilterComponent.builder()
                .filterDeps((context as Activity).findDependencies())
                .build()
                .filterViewModel
        }
        FilterScreen(
            //viewModel = viewModel,
        )
    }
}