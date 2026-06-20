package com.niko1312.spacex.ui.details

import com.niko1312.spacex.domain.model.Launch

data class DetailsState(
    val isLoading: Boolean = false,
    val launch: Launch? = null,
    val errorMessage: String? = null,
)

sealed interface DetailsIntent {
    data object Load : DetailsIntent
    data object Retry : DetailsIntent
    data object WatchWebcastClicked : DetailsIntent
}

sealed interface DetailsEffect {
    data class OpenUrl(val url: String) : DetailsEffect
}
