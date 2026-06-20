package com.niko1312.spacex.ui.launches

import com.niko1312.spacex.domain.model.Launch

data class LaunchesState(
    val isLoading: Boolean = false,
    val launches: List<Launch> = emptyList(),
    val errorMessage: String? = null,
) {
    val isEmpty: Boolean get() = !isLoading && errorMessage == null && launches.isEmpty()
}

sealed interface LaunchesIntent {
    data object Load : LaunchesIntent
    data object Retry : LaunchesIntent
    data class LaunchClicked(val id: String) : LaunchesIntent
}

sealed interface LaunchesEffect {
    data class NavigateToDetails(val id: String) : LaunchesEffect
}
