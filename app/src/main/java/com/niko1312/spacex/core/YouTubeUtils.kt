package com.niko1312.spacex.core

fun extractYouTubeVideoId(url: String?): String? {
    if (url.isNullOrBlank()) return null
    val patterns = listOf(
        Regex("""youtu\.be/([A-Za-z0-9_-]{11})"""),
        Regex("""[?&]v=([A-Za-z0-9_-]{11})"""),
        Regex("""youtube\.com/embed/([A-Za-z0-9_-]{11})"""),
        Regex("""youtube\.com/live/([A-Za-z0-9_-]{11})"""),
    )
    for (pattern in patterns) {
        val match = pattern.find(url)
        if (match != null) return match.groupValues[1]
    }
    return null
}
