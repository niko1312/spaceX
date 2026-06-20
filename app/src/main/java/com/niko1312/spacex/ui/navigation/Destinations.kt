package com.niko1312.spacex.ui.navigation

object Destinations {
    const val LAUNCHES = "launches"
    const val LAUNCH_DETAILS = "launch_details"
    const val ARG_LAUNCH_ID = "launchId"

    fun launchDetails(launchId: String) = "$LAUNCH_DETAILS/$launchId"
}
