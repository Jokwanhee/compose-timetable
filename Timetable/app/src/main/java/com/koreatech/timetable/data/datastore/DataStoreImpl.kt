package com.koreatech.timetable.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreImpl(
    private val context: Context,
) {
    fun getString(key: String): Flow<String> =
        context.dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(key)] ?: ""
        }

    fun getColorString(): Flow<String> =
        context.dataStore.data.map { preferences ->
            preferences[stringPreferencesKey("colors")] ?: ""
        }


    suspend fun putSemesterString(value: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey("semester")] = value
        }
    }

    suspend fun putColorsString(value: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey("colors")] = value
        }
    }

    suspend fun putString(key: String, value: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }
}