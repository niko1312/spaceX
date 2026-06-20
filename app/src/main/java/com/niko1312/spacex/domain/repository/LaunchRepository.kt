package com.niko1312.spacex.domain.repository

import com.niko1312.spacex.core.Resource
import com.niko1312.spacex.domain.model.Launch
import kotlinx.coroutines.flow.Flow

interface LaunchRepository {

    fun getLaunches(): Flow<Resource<List<Launch>>>

    suspend fun getLaunch(id: String): Resource<Launch>
}
