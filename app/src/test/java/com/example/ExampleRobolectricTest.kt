package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.DatabaseInitializer
import com.example.data.local.DateUtils
import com.example.data.local.FootballDatabase
import com.example.data.local.SettingsDataStore
import com.example.data.model.AppSettings
import com.example.data.model.CompletedSessionEntity
import com.example.data.model.PlayerPosition
import com.example.data.model.PlayerProfileEntity
import com.example.data.model.ProgramModuleCompletionEntity
import com.example.data.model.SavedTrainingCheckpointEntity
import com.example.data.model.SkillLevel
import com.example.data.model.TrainingCategory
import com.example.data.repository.FootballRepository
import com.example.data.repository.ProgramsAndTacticsCurriculum
import com.example.data.repository.SkillAndFitnessCurriculum
import com.example.viewmodel.FootballViewModel
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
    fun `app name matches Football Pro`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Football Pro", appName)
    }

    @Test
    fun `offline sqlite persists profile history checkpoint and resets cleanly`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(context, FootballDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        val settingsStore = SettingsDataStore(context)
        val repo = FootballRepository(db, settingsStore)

        DatabaseInitializer.populateIfEmpty(db)

        // 1. Save player name, skill level, position, duration, goals automatically
        repo.upsertProfile(
            PlayerProfileEntity(
                id = 1,
                playerName = "Marcus Silva",
                skillLevel = SkillLevel.INTERMEDIATE.name,
                playerPosition = PlayerPosition.MIDFIELDER.name,
                trainingGoals = "Master half-turn scanning and box finishing",
                preferredDurationMinutes = 45,
                onboardingCompleted = true
            )
        )
        val savedProfile = repo.getProfileOnce()
        assertNotNull(savedProfile)
        assertEquals("Marcus Silva", savedProfile!!.playerName)
        assertEquals(SkillLevel.INTERMEDIATE.name, savedProfile.skillLevel)
        assertEquals(PlayerPosition.MIDFIELDER.name, savedProfile.playerPosition)
        assertEquals(45, savedProfile.preferredDurationMinutes)
        assertTrue(savedProfile.onboardingCompleted)

        // 2. Record training history with drills & results
        repo.insertCompletedSession(
            CompletedSessionEntity(
                exerciseId = "cat_shoot_finesse",
                dateKey = DateUtils.todayKey(),
                categoryKey = TrainingCategory.SHOOTING.name,
                categoryLabel = "Shooting & Finishing",
                title = "Cut-Inside Whipped Finesse",
                durationMinutes = 18,
                drillsSummary = "4 sets x 8 curls",
                resultSummary = "Timer Completed (90s) • 100%"
            )
        )
        val sessions = repo.completedSessions.first()
        assertEquals(1, sessions.size)
        assertEquals("4 sets x 8 curls", sessions.first().drillsSummary)
        assertEquals("Timer Completed (90s) • 100%", sessions.first().resultSummary)

        // 3. Save continue-training checkpoint
        repo.upsertCheckpoint(
            SavedTrainingCheckpointEntity(
                id = 1,
                queueIdsSerialized = "fit_warmup,cat_shoot_finesse",
                customTitle = "Finishing Session",
                currentIndex = 1,
                phase = "WORK",
                totalPhaseSeconds = 90,
                remainingSeconds = 52,
                completedInSessionCount = 1,
                isActive = true
            )
        )
        val checkpoint = repo.getCheckpointOnce()
        assertNotNull(checkpoint)
        assertEquals(52, checkpoint!!.remainingSeconds)
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
                exerciseId = "cat_drib_slalom",
                dateKey = today,
                categoryKey = TrainingCategory.DRIBBLING.name,
                categoryLabel = "Dribbling & Mastery",
                title = "10-Cone Tight Slalom Weave",
                durationMinutes = 30,
                drillsSummary = "6 slalom runs",
                resultSummary = "Completed"
            ),
            CompletedSessionEntity(
                exerciseId = "cat_pass_wall",
                dateKey = yesterday,
                categoryKey = TrainingCategory.PASSING.name,
                categoryLabel = "Passing & First Touch",
                title = "Rapid 1-Touch Rebound Wall",
                durationMinutes = 30,
                drillsSummary = "5 sets x 60 sec",
                resultSummary = "Completed"
            )
        )
        val progDone = listOf(
            ProgramModuleCompletionEntity("prog_beginner", "beg_rules", true, today)
        )
        val summary = FootballViewModel.computeSummary(
            sessions = sessions,
            progDone = progDone,
            currentSettings = AppSettings(),
            categoryExercises = SkillAndFitnessCurriculum.categoryExercises,
            programs = ProgramsAndTacticsCurriculum.programs
        )
        assertEquals(2, summary.totalSessions)
        assertEquals(60, summary.totalMinutes)
        assertEquals("1.0", summary.totalHoursFormatted)
        assertEquals(2, summary.streakDays)
    }
}
