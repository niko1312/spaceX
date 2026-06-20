package com.niko1312.spacex.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.niko1312.spacex.core.Resource
import com.niko1312.spacex.domain.repository.LaunchRepository
import com.niko1312.spacex.ui.common.SpaceXViewModel
import com.niko1312.spacex.ui.navigation.Destinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: LaunchRepository,
    savedStateHandle: SavedStateHandle,
) : SpaceXViewModel<DetailsState, DetailsIntent, DetailsEffect>() {

    private val launchId: String =
        checkNotNull(savedStateHandle[Destinations.ARG_LAUNCH_ID]) { "launchId is required" }

    init {
        onIntent(DetailsIntent.Load)
    }

    override fun initialState() = DetailsState(isLoading = true)

    override fun onIntent(intent: DetailsIntent) {
        when (intent) {
            DetailsIntent.Load, DetailsIntent.Retry -> loadLaunch()
            DetailsIntent.WatchWebcastClicked ->
                currentState.launch?.videoUrl?.let { sendEffect(DetailsEffect.OpenUrl(it)) }
        }
    }

    private fun loadLaunch() {
        setState { copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            when (val result = repository.getLaunch(launchId)) {
                is Resource.Success -> setState {
                    copy(isLoading = false, launch = result.data, errorMessage = null)
                }

                is Resource.Error -> setState {
                    copy(isLoading = false, errorMessage = result.message)
                }

                Resource.Loading -> Unit
            }
        }
    }
}
