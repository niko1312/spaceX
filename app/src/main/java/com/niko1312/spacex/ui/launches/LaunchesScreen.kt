package com.niko1312.spacex.ui.launches

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.niko1312.spacex.R
import com.niko1312.spacex.core.formatLaunchDate
import com.niko1312.spacex.domain.model.Launch
import com.niko1312.spacex.ui.common.EmptyState
import com.niko1312.spacex.ui.common.ErrorState
import com.niko1312.spacex.ui.common.LoadingState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchesScreen(
    onLaunchClick: (String) -> Unit,
    viewModel: LaunchesViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LaunchesEffect.NavigateToDetails -> onLaunchClick(effect.id)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.launches_title)) })
        },
    ) { innerPadding ->
        LaunchesContent(
            state = state,
            onLaunchClick = { viewModel.onIntent(LaunchesIntent.LaunchClicked(it)) },
            onRetry = { viewModel.onIntent(LaunchesIntent.Retry) },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        )
    }
}

@Composable
private fun LaunchesContent(
    state: LaunchesState,
    onLaunchClick: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        state.isLoading -> LoadingState(modifier)
        state.errorMessage != null -> ErrorState(
            message = state.errorMessage,
            onRetry = onRetry,
            modifier = modifier,
        )
        state.isEmpty -> EmptyState(stringResource(R.string.empty_launches), modifier)
        else -> LazyColumn(modifier = modifier) {
            items(items = state.launches, key = { it.id }) { launch ->
                LaunchRow(launch = launch, onClick = { onLaunchClick(launch.id) })
                HorizontalDivider()
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun LaunchRow(
    launch: Launch,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        GlideImage(
            model = launch.imageUrl,
            contentDescription = stringResource(R.string.patch_content_description),
            contentScale = ContentScale.Fit,
            loading = placeholder(R.drawable.ic_rocket_placeholder),
            failure = placeholder(R.drawable.ic_rocket_placeholder),
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp)),
        )
        Spacer(Modifier.width(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = launch.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = formatLaunchDate(launch.net),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private val previewLaunches = listOf(
    Launch(
        id = "1",
        name = "Falcon 9 Block 5 | Starlink Group 6-1",
        net = "2023-06-04T08:20:00Z",
        imageUrl = null,
        videoUrl = "https://www.youtube.com/watch?v=abc12345678",
        details = null,
    ),
    Launch(
        id = "2",
        name = "Falcon 9 | CRS-21 Dragon Resupply",
        net = "2020-12-06T16:17:00Z",
        imageUrl = null,
        videoUrl = null,
        details = null,
    ),
)

@Preview(name = "Launches – list", showBackground = true)
@Composable
private fun LaunchesContentListPreview() {
    MaterialTheme {
        LaunchesContent(
            state = LaunchesState(launches = previewLaunches),
            onLaunchClick = {},
            onRetry = {},
        )
    }
}

@Preview(name = "Launches – loading", showBackground = true)
@Composable
private fun LaunchesContentLoadingPreview() {
    MaterialTheme {
        LaunchesContent(
            state = LaunchesState(isLoading = true),
            onLaunchClick = {},
            onRetry = {},
        )
    }
}

@Preview(name = "Launches – error", showBackground = true)
@Composable
private fun LaunchesContentErrorPreview() {
    MaterialTheme {
        LaunchesContent(
            state = LaunchesState(errorMessage = "No internet connection. Please check your network."),
            onLaunchClick = {},
            onRetry = {},
        )
    }
}

@Preview(name = "Launches – empty", showBackground = true)
@Composable
private fun LaunchesContentEmptyPreview() {
    MaterialTheme {
        LaunchesContent(
            state = LaunchesState(launches = emptyList()),
            onLaunchClick = {},
            onRetry = {},
        )
    }
}

@Preview(name = "Launch row", showBackground = true)
@Composable
private fun LaunchRowPreview() {
    MaterialTheme {
        LaunchRow(launch = previewLaunches.first(), onClick = {})
    }
}
