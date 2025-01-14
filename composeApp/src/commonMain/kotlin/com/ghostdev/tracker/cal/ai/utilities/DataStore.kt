package com.ghostdev.tracker.cal.ai.utilities

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

private var dataStoreInstance: DataStore<Preferences>? = null

internal const val dataStoreFileName = "ratio.preferences_pb"

fun createDataStore(producePath: () -> String): DataStore<Preferences> {
    return dataStoreInstance ?: PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    ).also { dataStoreInstance = it }
}