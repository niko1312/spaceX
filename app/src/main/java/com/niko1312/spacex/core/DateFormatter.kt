package com.niko1312.spacex.core

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

private const val UNKNOWN = "Date unknown"

fun formatLaunchDate(net: String?): String {
    if (net.isNullOrBlank()) return UNKNOWN
    val date = parseNet(net) ?: return UNKNOWN
    return SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
}

private fun parseNet(net: String): java.util.Date? {
    for (pattern in listOf("yyyy-MM-dd'T'HH:mm:ss'Z'", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")) {
        try {
            val parser = SimpleDateFormat(pattern, Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            return parser.parse(net)
        } catch (_: ParseException) {
        }
    }
    return null
}
