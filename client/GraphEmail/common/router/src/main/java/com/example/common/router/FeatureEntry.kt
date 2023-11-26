package com.example.common.router

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.*
import com.google.accompanist.navigation.animation.composable as composableAnimated


typealias Destinations = Map<Class<out FeatureEntry>, @JvmSuppressWildcards FeatureEntry>

interface FeatureEntry {

    val featureRoute: String

    val arguments: List<NamedNavArgument>
        get() = emptyList()

    val deepLinks: List<NavDeepLink>
        get() = emptyList()
}

interface ComposableFeatureEntry : FeatureEntry {
    @OptIn(ExperimentalAnimationApi::class)
    fun NavGraphBuilder.animatedComposable(
        navController: NavHostController,
        destinations: Destinations,
        enterTransition: (() -> EnterTransition?)? = null,
        exitTransition: (() -> ExitTransition?)? = null,
        popEnterTransition: (() -> EnterTransition?)? = null,
        popExitTransition: (() -> ExitTransition?)? = null
    ) {
        composableAnimated(
            featureRoute,
            arguments,
            deepLinks,
            enterTransition = { enterTransition?.invoke() },
            exitTransition = { exitTransition?.invoke() },
            popEnterTransition = { popEnterTransition?.invoke() },
            popExitTransition = { popExitTransition?.invoke() }
        ) { backStackEntry ->
            Composable(navController, destinations, backStackEntry)
        }
    }

    @Composable
    fun Composable(
        navController: NavHostController,
        destinations: Destinations,
        backStackEntry: NavBackStackEntry
    )
}

inline fun <reified T : FeatureEntry> Destinations.find(): T =
    findOrNull() ?: error("Unable to find '${T::class.java}' destination.")

inline fun <reified T : FeatureEntry> Destinations.findOrNull(): T? =
    this[T::class.java] as? T

@Composable
fun NavBackStackEntry.rememberBackStackEntry(
    navController: NavHostController,
    route: String
): NavBackStackEntry {
    return remember(this) { navController.getBackStackEntry(route) }
}