package com.lahmudin3031.assesment2.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "laundry_settings")

class SettingsDataStore(private val context: Context) {
    companion object {
        val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
        val IS_GRID_LAYOUT = booleanPreferencesKey("is_grid_layout")
    }
    val isDarkTheme: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_DARK_THEME] ?: false
    }
    val isGridLayout: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_GRID_LAYOUT] ?: false
    }
    suspend fun saveThemePreference(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_DARK_THEME] = isDark
        }
    }
    suspend fun saveLayoutPreference(isGrid: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_GRID_LAYOUT] = isGrid
        }
    }
}