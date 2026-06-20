package com.niko1312.spacex.data

import com.niko1312.spacex.data.mapper.toLaunch
import com.niko1312.spacex.data.remote.dto.LaunchDto
import com.niko1312.spacex.data.remote.dto.MissionDto
import com.niko1312.spacex.data.remote.dto.VidUrlDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LaunchMapperTest {

    @Test
    fun `given a fully populated dto when mapping to domain then all fields are copied`() {
        val dto = LaunchDto(
            id = "abc-1",
            name = "Falcon 1 | FalconSat",
            net = "2006-03-24T22:30:00Z",
            image = "https://example.com/patch.png",
            mission = MissionDto(description = "Engine failure"),
            vidURLs = listOf(
                VidUrlDto(url = "https://www.youtube.com/watch?v=abc12345678"),
                VidUrlDto(url = "https://www.youtube.com/watch?v=second00000"),
            ),
        )

        val launch = dto.toLaunch()

        assertEquals("abc-1", launch.id)
        assertEquals("Falcon 1 | FalconSat", launch.name)
        assertEquals("2006-03-24T22:30:00Z", launch.net)
        assertEquals("https://example.com/patch.png", launch.imageUrl)
        assertEquals("https://www.youtube.com/watch?v=abc12345678", launch.videoUrl)
        assertEquals("Engine failure", launch.details)
    }

    @Test
    fun `given a dto without media when mapping to domain then media fields are null`() {
        val dto = LaunchDto(id = "abc-2", name = "NoMedia")

        val launch = dto.toLaunch()

        assertNull(launch.imageUrl)
        assertNull(launch.videoUrl)
        assertNull(launch.details)
        assertEquals(false, launch.hasWebcast)
    }
}
