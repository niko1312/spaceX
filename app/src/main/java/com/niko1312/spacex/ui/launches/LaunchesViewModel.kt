package com.niko1312.spacex.ui.launches

import androidx.lifecycle.viewModelScope
import com.niko1312.spacex.core.Resource
import com.niko1312.spacex.domain.repository.LaunchRepository
import com.niko1312.spacex.ui.common.SpaceXViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LaunchesViewModel @Inject constructor(
    private val repository: LaunchRepository,
) : SpaceXViewModel<LaunchesState, LaunchesIntent, LaunchesEffect>() {

    init {
        onIntent(LaunchesIntent.Load)
    }

    override fun initialState() = LaunchesState(isLoading = true)

    override fun onIntent(intent: LaunchesIntent) {
        when (intent) {
            LaunchesIntent.Load, LaunchesIntent.Retry -> loadLaunches()
            is LaunchesIntent.LaunchClicked ->
                sendEffect(LaunchesEffect.NavigateToDetails(intent.id))
        }
    }

    private fun loadLaunches() {
        repository.getLaunches()
            .onEach { resource ->
                when (resource) {
                    Resource.Loading -> setState {
                        copy(isLoading = launches.isEmpty(), errorMessage = null)
                    }

                    is Resource.Success -> setState {
                        copy(isLoading = false, launches = resource.data, errorMessage = null)
                    }

                    is Resource.Error -> setState {
                        copy(isLoading = false, errorMessage = resource.message)
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}
