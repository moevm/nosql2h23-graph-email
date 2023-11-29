package com.example.common.router

import androidx.navigation.NavHostController

fun NavHostController.navigateAndClean(route: String) {
    this.backQueue.clear()
    navigate(route = route) {
        popUpTo(graph.startDestinationId) { inclusive = true }
    }
    graph.setStartDestination(route)
}

fun NavHostController.navigateLogout(route: String){
    navigate(route = route) {
        popUpTo(graph.startDestinationId) {
            inclusive = true
            saveState = false
        }
    }
}