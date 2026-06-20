package com.niko1312.spacex.data.repository

import com.niko1312.spacex.core.Resource
import com.niko1312.spacex.data.local.LaunchCache
import com.niko1312.spacex.data.mapper.toLaunch
import com.niko1312.spacex.data.remote.SpaceXApi
import com.niko1312.spacex.domain.model.Launch
import com.niko1312.spacex.domain.repository.LaunchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LaunchRepositoryImpl @Inject constructor(
    private val api: SpaceXApi,
    private val cache: LaunchCache,
) : LaunchRepository {

    override fun getLaunches(): Flow<Resource<List<Launch>>> = flow {
        emit(Resource.Loading)

        val cached = cache.getLaunches()
        if (cached.isNotEmpty()) {
            emit(Resource.Success(cached))
        }

        try {
            val remote = api.getLaunches().results
                .map { it.toLaunch() }
                .sortedByDescending { it.net.orEmpty() }
            cache.saveLaunches(remote)
            emit(Resource.Success(remote))
        } catch (e: Exception) {
            if (cached.isEmpty()) {
                emit(Resource.Error(e.toMessage(), e))
            }
        }
    }

    override suspend fun getLaunch(id: String): Resource<Launch> {
        return try {
            Resource.Success(api.getLaunch(id).toLaunch())
        } catch (e: Exception) {
            val cached = cache.getLaunches().firstOrNull { it.id == id }
            if (cached != null) Resource.Success(cached) else Resource.Error(e.toMessage(), e)
        }
    }

    private fun Exception.toMessage(): String = when (this) {
        is IOException -> "No internet connection. Please check your network."
        else -> message ?: "Unexpected error."
    }
}
