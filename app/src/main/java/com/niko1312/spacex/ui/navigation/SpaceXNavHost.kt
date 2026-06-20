package com.niko1312.spacex.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.niko1312.spacex.ui.details.DetailsScreen
import com.niko1312.spacex.ui.launches.LaunchesScreen

@Composable
fun SpaceXNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destinations.LAUNCHES,
    ) {
        composable(Destinations.LAUNCHES) {
            LaunchesScreen(
                onLaunchClick = { launchId ->
                    navController.navigate(Destinations.launchDetails(launchId))
                },
            )
        }

        composable(
            route = "${Destinations.LAUNCH_DETAILS}/{${Destinations.ARG_LAUNCH_ID}}",
            arguments = listOf(
                navArgument(Destinations.ARG_LAUNCH_ID) { type = NavType.StringType },
            ),
        ) {
            DetailsScreen(
                onBack = { navController.popBackStack() },
            )
        }
    }
}
