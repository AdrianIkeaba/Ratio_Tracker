package com.ghostdev.tracker.cal.ai.data.repo.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepo(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val USER_EXISTS_KEY = booleanPreferencesKey("user_exists")
    }

    fun isUserExist(): Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[USER_EXISTS_KEY] ?: false
        }

    suspend fun setUserExists(exists: Boolean) {
        dataStore.edit { preferences ->
            preferences[USER_EXISTS_KEY] = exists
        }
    }
}