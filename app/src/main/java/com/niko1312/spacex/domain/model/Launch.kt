package com.niko1312.spacex.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Launch(
    val id: String,
    val name: String,
    val net: String?,
    val imageUrl: String?,
    val videoUrl: String?,
    val details: String?,
) {
    val hasWebcast: Boolean get() = !videoUrl.isNullOrBlank()
}
