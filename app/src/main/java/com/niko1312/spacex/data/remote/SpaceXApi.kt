package com.niko1312.spacex.data.remote

import com.niko1312.spacex.data.remote.dto.LaunchDto
import com.niko1312.spacex.data.remote.dto.LaunchListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpaceXApi {

    @GET("2.0.0/launch/")
    suspend fun getLaunches(
        @Query("lsp__name") provider: String = "SpaceX",
        @Query("ordering") ordering: String = "-net",
    ): LaunchListResponse

    @GET("2.0.0/launch/{id}/")
    suspend fun getLaunch(@Path("id") id: String): LaunchDto

    companion object {
        const val BASE_URL = "https://ll.thespacedevs.com/"
    }
}
