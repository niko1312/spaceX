package com.niko1312.spacex.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.niko1312.spacex.domain.model.Launch
import kotlinx.coroutines.flow.first
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LaunchCache @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val json: Json,
) {
    suspend fun getLaunches(): List<Launch> {
        val raw = dataStore.data.first()[KEY_LAUNCHES] ?: return emptyList()
        return runCatching { json.decodeFromString<List<Launch>>(raw) }.getOrDefault(emptyList())
    }

    suspend fun saveLaunches(launches: List<Launch>) {
        val raw = json.encodeToString(launches)
        dataStore.edit { it[KEY_LAUNCHES] = raw }
    }

    private companion object {
        val KEY_LAUNCHES = stringPreferencesKey("cached_launches")
    }
}
