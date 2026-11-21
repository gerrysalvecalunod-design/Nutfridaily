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

private val Context.footballSettingsStore: DataStore<Preferences> by preferencesDataStore(
    name = "footballpro_settings"
)

class SettingsDataStore(private val context: Context) {
    private object Keys {
        val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val REMINDER_ENABLED = booleanPreferencesKey("reminder_enabled")
        val REMINDER_TIME = stringPreferencesKey("reminder_time")
        val ACTIVE_PROGRAM_ID = stringPreferencesKey("active_program_id")
        val APP_LANGUAGE = stringPreferencesKey("app_language")
        val DAILY_MINUTES_GOAL = intPreferencesKey("daily_minutes_goal")
        val WEEKLY_SESSIONS_GOAL = intPreferencesKey("weekly_sessions_goal")
    }

    val settingsFlow: Flow<AppSettings> = context.footballSettingsStore.data.map { prefs ->
        AppSettings(
            isDarkTheme = prefs[Keys.IS_DARK_THEME] ?: true,
            soundEnabled = prefs[Keys.SOUND_ENABLED] ?: true,
            reminderEnabled = prefs[Keys.REMINDER_ENABLED] ?: true,
            reminderTimeLabel = prefs[Keys.REMINDER_TIME] ?: "05:30 PM",
            activeProgramId = prefs[Keys.ACTIVE_PROGRAM_ID] ?: "program_7",
            appLanguage = prefs[Keys.APP_LANGUAGE] ?: "en",
            dailyMinutesGoal = prefs[Keys.DAILY_MINUTES_GOAL] ?: 20,
            weeklySessionsGoal = prefs[Keys.WEEKLY_SESSIONS_GOAL] ?: 5
        )
    }

    suspend fun setDarkTheme(enabled: Boolean) {
        context.footballSettingsStore.edit { it[Keys.IS_DARK_THEME] = enabled }
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        context.footballSettingsStore.edit { it[Keys.SOUND_ENABLED] = enabled }
    }

    suspend fun setReminderEnabled(enabled: Boolean) {
        context.footballSettingsStore.edit { it[Keys.REMINDER_ENABLED] = enabled }
    }

    suspend fun setReminderTime(timeLabel: String) {
        context.footballSettingsStore.edit { it[Keys.REMINDER_TIME] = timeLabel }
    }

    suspend fun setActiveProgramId(programId: String) {
        context.footballSettingsStore.edit { it[Keys.ACTIVE_PROGRAM_ID] = programId }
    }

    suspend fun setAppLanguage(languageCode: String) {
        context.footballSettingsStore.edit { it[Keys.APP_LANGUAGE] = languageCode }
    }

    suspend fun setDailyMinutesGoal(minutes: Int) {
        context.footballSettingsStore.edit { it[Keys.DAILY_MINUTES_GOAL] = minutes.coerceIn(5, 240) }
    }

    suspend fun setWeeklySessionsGoal(sessions: Int) {
        context.footballSettingsStore.edit { it[Keys.WEEKLY_SESSIONS_GOAL] = sessions.coerceIn(1, 21) }
    }

    suspend fun resetAllSettings() {
        context.footballSettingsStore.edit { it.clear() }
    }
}
