package com.niko1312.spacex.ui

import app.cash.turbine.test
import com.niko1312.spacex.core.Resource
import com.niko1312.spacex.domain.model.Launch
import com.niko1312.spacex.domain.repository.LaunchRepository
import com.niko1312.spacex.ui.launches.LaunchesEffect
import com.niko1312.spacex.ui.launches.LaunchesIntent
import com.niko1312.spacex.ui.launches.LaunchesViewModel
import com.niko1312.spacex.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class LaunchesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: LaunchRepository = mock()

    private val sampleLaunches = listOf(
        Launch(
            id = "1",
            name = "Falcon 1 | FalconSat",
            net = "2006-03-24T22:30:00Z",
            imageUrl = "https://example.com/patch.png",
            videoUrl = "https://www.youtube.com/watch?v=abc12345678",
            details = null,
        ),
    )

    @Test
    fun `given the repository returns launches when loading then state holds the launches`() = runTest {
        whenever(repository.getLaunches())
            .doReturn(flowOf(Resource.Loading, Resource.Success(sampleLaunches)))

        val viewModel = LaunchesViewModel(repository)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(sampleLaunches, state.launches)
        assertEquals(null, state.errorMessage)
    }

    @Test
    fun `given the repository returns an error when loading then state holds the error message`() = runTest {
        whenever(repository.getLaunches())
            .doReturn(flowOf(Resource.Loading, Resource.Error("Boom")))

        val viewModel = LaunchesViewModel(repository)
        advanceUntilIdle()

        assertEquals("Boom", viewModel.state.value.errorMessage)
    }

    @Test
    fun `given launches are loaded when a launch is clicked then a navigate effect is emitted`() = runTest {
        whenever(repository.getLaunches()).doReturn(flowOf(Resource.Success(sampleLaunches)))
        val viewModel = LaunchesViewModel(repository)
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onIntent(LaunchesIntent.LaunchClicked("1"))
            assertEquals(LaunchesEffect.NavigateToDetails("1"), awaitItem())
        }
    }
}
