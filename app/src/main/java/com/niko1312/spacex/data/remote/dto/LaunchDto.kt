package com.niko1312.spacex.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LaunchListResponse(
    val count: Int = 0,
    val results: List<LaunchDto> = emptyList(),
)

@Serializable
data class LaunchDto(
    val id: String,
    val name: String,
    val net: String? = null,
    val image: String? = null,
    val mission: MissionDto? = null,
    val vidURLs: List<VidUrlDto> = emptyList(),
)

@Serializable
data class MissionDto(
    val description: String? = null,
)

@Serializable
data class VidUrlDto(
    val url: String? = null,
)
