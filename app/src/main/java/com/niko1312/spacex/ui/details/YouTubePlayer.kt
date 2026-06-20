package com.niko1312.spacex.ui.details

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun YouTubePlayer(
    videoId: String,
    modifier: Modifier = Modifier,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                settings.javaScriptEnabled = true
                settings.mediaPlaybackRequiresUserGesture = true
                settings.domStorageEnabled = true
                webChromeClient = WebChromeClient()
                tag = videoId
                loadVideo(videoId)
            }
        },
        update = { webView ->
            if (webView.tag != videoId) {
                webView.tag = videoId
                webView.loadVideo(videoId)
            }
        },
    )
}

private fun WebView.loadVideo(videoId: String) {
    loadDataWithBaseURL(
        "https://www.youtube.com",
        buildEmbedHtml(videoId),
        "text/html",
        "utf-8",
        null,
    )
}

private fun buildEmbedHtml(videoId: String): String = """
    <!DOCTYPE html>
    <html>
      <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <style>
          html, body { margin: 0; padding: 0; background: #000; height: 100%; }
          .wrap { position: relative; width: 100%; padding-bottom: 56.25%; }
          iframe { position: absolute; top: 0; left: 0; width: 100%; height: 100%; border: 0; }
        </style>
      </head>
      <body>
        <div class="wrap">
          <iframe
            src="https://www.youtube.com/embed/$videoId?playsinline=1"
            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
            allowfullscreen>
          </iframe>
        </div>
      </body>
    </html>
""".trimIndent()
