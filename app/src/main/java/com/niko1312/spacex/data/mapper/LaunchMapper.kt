package com.niko1312.spacex.data.mapper

import com.niko1312.spacex.data.remote.dto.LaunchDto
import com.niko1312.spacex.domain.model.Launch

fun LaunchDto.toLaunch(): Launch = Launch(
    id = id,
    name = name,
    net = net,
    imageUrl = image,
    videoUrl = vidURLs.firstOrNull()?.url,
    details = mission?.description,
)
