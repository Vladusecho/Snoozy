package com.wem.snoozy.data.local

import android.content.Context
import androidx.core.text.isDigitsOnly
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserPreferencesManager(
    private val context: Context
) {

    private val appContext = context.applicationContext

    companion object {

        val SLEEP_START_TIME = stringPreferencesKey("sleep_start_time")
        val CYCLE_LENGTH = stringPreferencesKey("cycle_length")
        val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
    }

    val sleepStartTimeFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[SLEEP_START_TIME] ?: "0"
        }

    val cycleLengthFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[CYCLE_LENGTH] ?: "90"
        }

    val darkThemeFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[DARK_THEME_KEY] ?: true
        }

    suspend fun saveSleepStartTime(time: String) {
        var newTime = time
        if (!newTime.isDigitsOnly()) {
            newTime = "0"
        } else {
            if (newTime.toInt() < 0) {
                newTime = "0"
            } else if (newTime.toInt() > 60) {
                newTime = "60"
            }
        }
        context.dataStore.edit { preferences ->
            preferences[SLEEP_START_TIME] = newTime
        }
    }

    suspend fun saveCycleLength(value: String) {
        var newValue = value
        if (!newValue.isDigitsOnly()) {
            newValue = "90"
        } else {
            if (newValue.toInt() < 50) {
                newValue = "50"
            } else if (newValue.toInt() > 150) {
                newValue = "150"
            }
        }
        context.dataStore.edit { preferences ->
            preferences[CYCLE_LENGTH] = newValue
        }
    }

    suspend fun saveTheme(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_THEME_KEY] = isDark
        }
    }

    suspend fun clearAll() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}