package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.model.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.badmintonSettingsStore: DataStore<Preferences> by preferencesDataStore(
    name = "badminton_pro_settings"
)

class SettingsDataStore(private val context: Context) {
    private object Keys {
        val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val SOUND_TONE_INDEX = intPreferencesKey("sound_tone_index")
        val PREFERRED_DURATION = intPreferencesKey("preferred_duration_minutes")
        val DAILY_MINUTES_GOAL = intPreferencesKey("daily_minutes_goal")
        val WEEKLY_SESSIONS_GOAL = intPreferencesKey("weekly_sessions_goal")
        val ACTIVE_PROGRAM_ID = stringPreferencesKey("active_program_id")
    }

    val settingsFlow: Flow<AppSettings> = context.badmintonSettingsStore.data.map { prefs ->
        AppSettings(
            isDarkTheme = prefs[Keys.IS_DARK_THEME] ?: false,
            soundEnabled = prefs[Keys.SOUND_ENABLED] ?: true,
            soundToneIndex = prefs[Keys.SOUND_TONE_INDEX] ?: 0,
            preferredDurationMinutes = prefs[Keys.PREFERRED_DURATION] ?: 30,
            dailyMinutesGoal = prefs[Keys.DAILY_MINUTES_GOAL] ?: 30,
            weeklySessionsGoal = prefs[Keys.WEEKLY_SESSIONS_GOAL] ?: 5,
            activeProgramId = prefs[Keys.ACTIVE_PROGRAM_ID] ?: "prog_beginner"
        )
    }

    suspend fun setDarkTheme(enabled: Boolean) {
        context.badmintonSettingsStore.edit { it[Keys.IS_DARK_THEME] = enabled }
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        context.badmintonSettingsStore.edit { it[Keys.SOUND_ENABLED] = enabled }
    }

    suspend fun setSoundToneIndex(index: Int) {
        context.badmintonSettingsStore.edit { it[Keys.SOUND_TONE_INDEX] = index.coerceIn(0, 2) }
    }

    suspend fun setPreferredDuration(minutes: Int) {
        context.badmintonSettingsStore.edit { it[Keys.PREFERRED_DURATION] = minutes.coerceIn(10, 120) }
    }

    suspend fun setDailyMinutesGoal(minutes: Int) {
        context.badmintonSettingsStore.edit { it[Keys.DAILY_MINUTES_GOAL] = minutes.coerceIn(10, 180) }
    }

    suspend fun setWeeklySessionsGoal(sessions: Int) {
        context.badmintonSettingsStore.edit { it[Keys.WEEKLY_SESSIONS_GOAL] = sessions.coerceIn(1, 14) }
    }

    suspend fun setActiveProgramId(programId: String) {
        context.badmintonSettingsStore.edit { it[Keys.ACTIVE_PROGRAM_ID] = programId }
    }

    suspend fun resetAllSettings() {
        context.badmintonSettingsStore.edit { it.clear() }
    }
}
