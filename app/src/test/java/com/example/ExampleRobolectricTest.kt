package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.BadmintonDatabase
import com.example.data.local.DatabaseInitializer
import com.example.data.local.DateUtils
import com.example.data.local.SettingsDataStore
import com.example.data.model.AppSettings
import com.example.data.model.CompletedSessionEntity
import com.example.data.model.PlayerProfileEntity
import com.example.data.model.ProgramModuleCompletionEntity
import com.example.data.model.SavedTrainingCheckpointEntity
import com.example.data.model.SkillLevel
import com.example.data.model.TrainingCategory
import com.example.data.repository.BadmintonRepository
import com.example.data.repository.CategoriesAndFitnessCurriculum
import com.example.data.repository.ProgramsAndRulesCurriculum
import com.example.viewmodel.BadmintonViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class ExampleRobolectricTest {

    @Test
    fun `app name matches Badminton Pro`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Badminton Pro", appName)
    }

    @Test
    fun `offline sqlite persists profile history checkpoint and resets cleanly`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(context, BadmintonDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        val settingsStore = SettingsDataStore(context)
        val repo = BadmintonRepository(db, settingsStore)

        DatabaseInitializer.populateIfEmpty(db)

        // 1. Save player name, skill level, duration, goals automatically
        repo.upsertProfile(
            PlayerProfileEntity(
                id = 1,
                playerName = "Jordan Reyes",
                skillLevel = SkillLevel.INTERMEDIATE.name,
                trainingGoals = "Master net hairpins and jump smash recovery",
                preferredDurationMinutes = 45,
                onboardingCompleted = true
            )
        )
        val savedProfile = repo.getProfileOnce()
        assertNotNull(savedProfile)
        assertEquals("Jordan Reyes", savedProfile!!.playerName)
        assertEquals(SkillLevel.INTERMEDIATE.name, savedProfile.skillLevel)
        assertEquals(45, savedProfile.preferredDurationMinutes)
        assertTrue(savedProfile.onboardingCompleted)

        // 2. Record training history with drills & results
        repo.insertCompletedSession(
            CompletedSessionEntity(
                exerciseId = "cat_smash_full",
                dateKey = DateUtils.todayKey(),
                categoryKey = TrainingCategory.SMASH.name,
                categoryLabel = "Smash Training",
                title = "Full-Court Rear Jump Smash",
                durationMinutes = 25,
                drillsSummary = "4 sets x 15 smashes",
                resultSummary = "Timer Completed (180s) • 100%"
            )
        )
        val sessions = repo.completedSessions.first()
        assertEquals(1, sessions.size)
        assertEquals("4 sets x 15 smashes", sessions.first().drillsSummary)
        assertEquals("Timer Completed (180s) • 100%", sessions.first().resultSummary)

        // 3. Save continue-training checkpoint
        repo.upsertCheckpoint(
            SavedTrainingCheckpointEntity(
                id = 1,
                queueIdsSerialized = "fit_warmup,cat_smash_full",
                customTitle = "Smash Session",
                currentIndex = 1,
                phase = "WORK",
                totalPhaseSeconds = 120,
                remainingSeconds = 74,
                completedInSessionCount = 1,
                isActive = true
            )
        )
        val checkpoint = repo.getCheckpointOnce()
        assertNotNull(checkpoint)
        assertEquals(74, checkpoint!!.remainingSeconds)
        assertTrue(checkpoint.isActive)

        // 4. Reset all saved information cleanly
        repo.resetAllProgress()
        val resetProfile = repo.getProfileOnce()
        assertNotNull(resetProfile)
        assertEquals("", resetProfile!!.playerName)
        assertEquals(false, resetProfile.onboardingCompleted)
        assertTrue(repo.completedSessions.first().isEmpty())
        assertNull(repo.getCheckpointOnce())

        db.close()
    }

    @Test
    fun `dashboard summary computes streak minutes and hours accurately`() {
        val today = DateUtils.todayKey()
        val yesterday = DateUtils.daysAgoKey(1)
        val sessions = listOf(
            CompletedSessionEntity(
                exerciseId = "cat_foot_6corner",
                dateKey = today,
                categoryKey = TrainingCategory.FOOTWORK.name,
                categoryLabel = "Footwork Training",
                title = "6-Corner Shadow Footwork",
                durationMinutes = 30,
                drillsSummary = "6 sets",
                resultSummary = "Completed"
            ),
            CompletedSessionEntity(
                exerciseId = "cat_serve_low",
                dateKey = yesterday,
                categoryKey = TrainingCategory.SERVING.name,
                categoryLabel = "Serving Training",
                title = "Backhand Low Short Serve",
                durationMinutes = 30,
                drillsSummary = "4 sets",
                resultSummary = "Completed"
            )
        )
        val progDone = listOf(
            ProgramModuleCompletionEntity("prog_beginner", "beg_rules", true, today)
        )
        val summary = BadmintonViewModel.computeSummary(
            sessions = sessions,
            progDone = progDone,
            currentSettings = AppSettings(),
            categoryExercises = CategoriesAndFitnessCurriculum.categoryExercises,
            programs = ProgramsAndRulesCurriculum.programs
        )
        assertEquals(2, summary.totalSessions)
        assertEquals(60, summary.totalMinutes)
        assertEquals("1.0", summary.totalHoursFormatted)
        assertEquals(2, summary.streakDays)
    }
}
