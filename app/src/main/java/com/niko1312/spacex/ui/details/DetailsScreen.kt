package com.niko1312.spacex.ui.details

import android.content.ActivityNotFoundException
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.niko1312.spacex.R
import com.niko1312.spacex.core.extractYouTubeVideoId
import com.niko1312.spacex.core.formatLaunchDate
import com.niko1312.spacex.domain.model.Launch
import com.niko1312.spacex.ui.common.ErrorState
import com.niko1312.spacex.ui.common.LoadingState
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    onBack: () -> Unit,
    viewModel: DetailsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is DetailsEffect.OpenUrl -> {
                    try {
                        context.startActivity(Intent(Intent.ACTION_VIEW, effect.url.toUri()))
                    } catch (_: ActivityNotFoundException) {
                        Toast.makeText(context, R.string.error_generic, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.launch?.name ?: stringResource(R.string.details_title),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        when {
            state.isLoading -> LoadingState(contentModifier)
            state.errorMessage != null -> ErrorState(
                message = state.errorMessage!!,
                onRetry = { viewModel.onIntent(DetailsIntent.Retry) },
                modifier = contentModifier,
            )
            state.launch != null -> DetailsContent(
                launch = state.launch!!,
                onWatchWebcast = { viewModel.onIntent(DetailsIntent.WatchWebcastClicked) },
                modifier = contentModifier,
            )
        }
    }
}

@Composable
private fun DetailsContent(
    launch: Launch,
    onWatchWebcast: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val videoId = extractYouTubeVideoId(launch.videoUrl)

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = launch.name,
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = stringResource(R.string.launch_date, formatLaunchDate(launch.net)),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        when {
            videoId != null -> YouTubePlayer(
                videoId = videoId,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
            )
            launch.hasWebcast -> OutlinedButton(onClick = onWatchWebcast) {
                Icon(Icons.Filled.PlayArrow, contentDescription = null)
                Text(
                    text = stringResource(R.string.watch_webcast),
                    modifier = Modifier.padding(start = 8.dp),
                )
            }
            else -> Text(
                text = stringResource(R.string.no_webcast),
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        if (!launch.details.isNullOrBlank()) {
            Text(text = launch.details, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Preview(name = "Details – button", showBackground = true)
@Composable
private fun DetailsContentWithWebcastPreview() {
    MaterialTheme {
        DetailsContent(
            launch = Launch(
                id = "1",
                name = "Falcon 9 | CRS-21 Dragon Resupply",
                net = "2020-12-06T16:17:00Z",
                imageUrl = null,
                videoUrl = "https://example.com/livestream",
                details = "Dragon delivers cargo to the ISS and returns science back to Earth.",
            ),
            onWatchWebcast = {},
        )
    }
}

@Preview(name = "Details – no webcast", showBackground = true)
@Composable
private fun DetailsContentNoWebcastPreview() {
    MaterialTheme {
        DetailsContent(
            launch = Launch(
                id = "2",
                name = "Falcon 1 | FalconSat",
                net = "2006-03-24T22:30:00Z",
                imageUrl = null,
                videoUrl = null,
                details = null,
            ),
            onWatchWebcast = {},
        )
    }
}
