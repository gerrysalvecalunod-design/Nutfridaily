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
import com.example.data.repository.ConditioningAndDefenseCurriculum
import com.example.data.repository.DailyPlanGenerator
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
    fun `app name and tagline match FOOTBALL PRO branding`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        assertEquals("FOOTBALL PRO", context.getString(R.string.app_name))
        assertEquals("Train Hard. Play Smart. Become Better.", context.getString(R.string.app_tagline))
    }

    @Test
    fun `offline sqlite persists profile daily plan history checkpoint and resets cleanly`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(context, FootballDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        val settingsStore = SettingsDataStore(context)
        val repo = FootballRepository(db, settingsStore)

        DatabaseInitializer.populateIfEmpty(db)

        // 1. Save player name, age, skill level, position, duration, goals automatically
        val profile = PlayerProfileEntity(
            id = 1,
            playerName = "Cristiano Silva",
            age = 21,
            skillLevel = SkillLevel.INTERMEDIATE.name,
            playerPosition = PlayerPosition.ATTACKING_MIDFIELDER.name,
            trainingGoals = "Master half-turn scanning and box finishing",
            preferredDurationMinutes = 45,
            onboardingCompleted = true
        )
        repo.upsertProfile(profile)
        val savedProfile = repo.getProfileOnce()
        assertNotNull(savedProfile)
        assertEquals("Cristiano Silva", savedProfile!!.playerName)
        assertEquals(21, savedProfile.age)
        assertEquals(SkillLevel.INTERMEDIATE.name, savedProfile.skillLevel)
        assertEquals(PlayerPosition.ATTACKING_MIDFIELDER.name, savedProfile.playerPosition)
        assertEquals(45, savedProfile.preferredDurationMinutes)
        assertTrue(savedProfile.onboardingCompleted)

        // 2. Persist personalized Daily Training Plan & completion toggle
        val generatedPlan = DailyPlanGenerator.generatePlanItems(
            profile = savedProfile,
            allCategoryExercises = repo.categoryExercises,
            fitnessExercises = repo.fitnessExercises,
            dateKey = DateUtils.todayKey()
        )
        repo.replaceDailyPlan(generatedPlan)
        val storedPlan = repo.getDailyPlanOnce()
        assertTrue(storedPlan.isNotEmpty())
        val firstStep = storedPlan.first()
        repo.updateDailyPlanItem(firstStep.copy(isCompleted = true))
        assertTrue(repo.getDailyPlanOnce().first().isCompleted)

        // 3. Record training history with drills count & results
        repo.insertCompletedSession(
            CompletedSessionEntity(
                exerciseId = "shoot_finesse",
                dateKey = DateUtils.todayKey(),
                categoryKey = TrainingCategory.SHOOTING.name,
                categoryLabel = TrainingCategory.SHOOTING.displayName,
                title = "Finesse Shooting",
                durationMinutes = 18,
                completedDrillsCount = 2,
                drillsSummary = "4 sets x 8 curls",
                resultSummary = "Timer Completed (90s) • 100%"
            )
        )
        val sessions = repo.completedSessions.first()
        assertEquals(1, sessions.size)
        assertEquals(2, sessions.first().completedDrillsCount)
        assertEquals("4 sets x 8 curls", sessions.first().drillsSummary)
        assertEquals("Timer Completed (90s) • 100%", sessions.first().resultSummary)

        // 4. Save continue-training checkpoint
        repo.upsertCheckpoint(
            SavedTrainingCheckpointEntity(
                id = 1,
                queueIdsSerialized = "fit_warmup,shoot_finesse",
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

        // 5. Verify 9 default achievements exist
        assertEquals(9, repo.getAchievementsOnce().size)

        // 6. Reset all saved information cleanly
        repo.resetAllProgress()
        val resetProfile = repo.getProfileOnce()
        assertNotNull(resetProfile)
        assertEquals("", resetProfile!!.playerName)
        assertEquals(18, resetProfile.age)
        assertEquals(false, resetProfile.onboardingCompleted)
        assertTrue(repo.completedSessions.first().isEmpty())
        assertTrue(repo.getDailyPlanOnce().isEmpty())
        assertNull(repo.getCheckpointOnce())

        db.close()
    }

    @Test
    fun `dashboard summary computes streak minutes and hours accurately`() {
        val allCategories = SkillAndFitnessCurriculum.attackAndBallExercises +
            ConditioningAndDefenseCurriculum.speedDefenseAndGkExercises
        val today = DateUtils.todayKey()
        val yesterday = DateUtils.daysAgoKey(1)
        val sessions = listOf(
            CompletedSessionEntity(
                exerciseId = "drib_cone",
                dateKey = today,
                categoryKey = TrainingCategory.DRIBBLING.name,
                categoryLabel = TrainingCategory.DRIBBLING.displayName,
                title = "Cone Dribbling",
                durationMinutes = 30,
                completedDrillsCount = 1,
                drillsSummary = "6 slalom runs",
                resultSummary = "Completed"
            ),
            CompletedSessionEntity(
                exerciseId = "pass_wall",
                dateKey = yesterday,
                categoryKey = TrainingCategory.PASSING.name,
                categoryLabel = TrainingCategory.PASSING.displayName,
                title = "Wall Passing",
                durationMinutes = 30,
                completedDrillsCount = 1,
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
            categoryExercises = allCategories,
            programs = ProgramsAndTacticsCurriculum.programs,
            achievements = DatabaseInitializer.defaultAchievements()
        )
        assertEquals(2, summary.totalSessions)
        assertEquals(60, summary.totalMinutes)
        assertEquals("1.0", summary.totalHoursFormatted)
        assertEquals(2, summary.streakDays)
        assertEquals(9, summary.totalAchievementsCount)
    }
}
