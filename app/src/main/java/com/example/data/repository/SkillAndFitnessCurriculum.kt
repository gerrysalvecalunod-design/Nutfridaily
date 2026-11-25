package com.example.data.repository

import com.example.data.model.FootballExercise
import com.example.data.model.LessonSection
import com.example.data.model.PitchDiagramType
import com.example.data.model.SkillLevel
import com.example.data.model.TrainingCategory

internal fun drill(
    id: String,
    name: String,
    category: TrainingCategory,
    description: String,
    instructions: List<String>,
    durationMinutes: Int,
    timerWorkSeconds: Int,
    difficulty: SkillLevel,
    recommendedReps: String,
    restTimeSeconds: Int,
    tips: List<String>,
    safety: List<String> = emptyList(),
    diagramType: PitchDiagramType
): FootballExercise = FootballExercise(
    id = id,
    name = name,
    section = LessonSection.CATEGORY,
    categoryKey = category.name,
    categoryLabel = category.displayName,
    description = description,
    instructions = instructions,
    durationMinutes = durationMinutes,
    timerWorkSeconds = timerWorkSeconds,
    difficulty = difficulty,
    recommendedReps = recommendedReps,
    restTimeSeconds = restTimeSeconds,
    trainingTips = tips,
    safetyInstructions = safety,
    diagramType = diagramType
)

object SkillAndFitnessCurriculum {

    // Categories A (8), B (7), C (7), D (8) = 30 technical drills
    val attackAndBallExercises: List<FootballExercise> = listOf(
        // A. Ball Mastery (8)
        drill(
            id = "bm_toe_taps",
            name = "Toe Taps",
            category = TrainingCategory.BALL_MASTERY,
            description = "Alternating rapid sole taps on top of the ball to build ankle speed and rhythm.",
            instructions = listOf(
                "Stand balanced with knees slightly bent behind a stationary ball.",
                "Tap the top of the ball lightly with the ball of your right foot, then switch rapidly to your left.",
                "Stay on the balls of your feet and glance upward every 5 touches."
            ),
            durationMinutes = 8,
            timerWorkSeconds = 45,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "4 sets x 45 sec",
            restTimeSeconds = 20,
            tips = listOf("Keep touches quiet and soft so the ball stays completely still."),
            diagramType = PitchDiagramType.SLALOM_DRIBBLING
        ),
        drill(
            id = "bm_foundations",
            name = "Foundations",
            category = TrainingCategory.BALL_MASTERY,
            description = "Pendulum inside-foot touches back and forth between both feet for clean ball control.",
            instructions = listOf(
                "Position the ball centered between both feet shoulder-width apart.",
                "Tap the ball gently side-to-side using only the inside arches of left and right feet.",
                "Gradually increase speed while keeping your hips low."
            ),
            durationMinutes = 8,
            timerWorkSeconds = 45,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "4 sets x 45 sec",
            restTimeSeconds = 20,
            tips = listOf("Point toes slightly upward and keep knees soft like springs."),
            diagramType = PitchDiagramType.SLALOM_DRIBBLING
        ),
        drill(
            id = "bm_sole_rolls",
            name = "Sole Rolls",
            category = TrainingCategory.BALL_MASTERY,
            description = "Lateral and forward sole drags to manipulate the ball cleanly under tight pressure.",
            instructions = listOf(
                "Place the sole of your right foot on top of the ball and roll it cleanly across your body.",
                "Stop it softly with the inside or sole of your left foot, then roll back the other way.",
                "Add a gentle hop on your standing foot for fluid rhythm."
            ),
            durationMinutes = 10,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "4 sets x 60 sec",
            restTimeSeconds = 20,
            tips = listOf("Use the front half of your sole, never your heel."),
            diagramType = PitchDiagramType.SLALOM_DRIBBLING
        ),
        drill(
            id = "bm_inside_outside",
            name = "Inside-Outside Touches",
            category = TrainingCategory.BALL_MASTERY,
            description = "Quick alternating inside and outside foot shifts with each foot to unbalance markers.",
            instructions = listOf(
                "Touch the ball sideways with the outside of your right foot, then immediately tap back with the inside.",
                "Perform 30 seconds right foot only, then 30 seconds left foot only.",
                "Keep your upper body loose to sell lateral feints."
            ),
            durationMinutes = 10,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 60 sec",
            restTimeSeconds = 25,
            tips = listOf("Move your hips with the ball rather than reaching with stiff legs."),
            diagramType = PitchDiagramType.SLALOM_DRIBBLING
        ),
        drill(
            id = "bm_v_turns",
            name = "V-Turns",
            category = TrainingCategory.BALL_MASTERY,
            description = "Pull the ball back with the sole and push diagonally forward in a sharp V shape.",
            instructions = listOf(
                "Pull the ball backward using your right sole as your left foot pivots 45 degrees.",
                "Push the ball diagonally forward with the inside or instep of your right foot.",
                "Alternate legs continuously in a 2m x 2m box."
            ),
            durationMinutes = 10,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 16 V-turns",
            restTimeSeconds = 25,
            tips = listOf("Snap your standing ankle as you pivot so the exit angle stays sharp."),
            diagramType = PitchDiagramType.SLALOM_DRIBBLING
        ),
        drill(
            id = "bm_pull_push",
            name = "Pull-Push",
            category = TrainingCategory.BALL_MASTERY,
            description = "Sole pull-back followed immediately by an instep or laces forward push.",
            instructions = listOf(
                "Pull the ball toward yourself with the sole of your dominant foot.",
                "Before it stops, tap it forward cleanly with your laces or inside foot.",
                "Repeat alternating feet while shuffling lightly on your toes."
            ),
            durationMinutes = 10,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "4 sets x 50 sec",
            restTimeSeconds = 20,
            tips = listOf("Keep the ball moving smoothly — avoid stomping down hard."),
            diagramType = PitchDiagramType.SLALOM_DRIBBLING
        ),
        drill(
            id = "bm_ball_control",
            name = "Ball Control",
            category = TrainingCategory.BALL_MASTERY,
            description = "Toss-and-cushion aerial traps using instep, thigh, and chest into open space.",
            instructions = listOf(
                "Toss the ball 4 meters high above yourself.",
                "Cushion the drop smoothly with your instep, withdrawing your foot downward on contact.",
                "Control within 1 meter, turn 90 degrees, and accelerate 3 steps."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 75,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 12 controls",
            restTimeSeconds = 25,
            tips = listOf("Relax your ankle on impact like catching an egg."),
            diagramType = PitchDiagramType.PASSING_TRIANGLE
        ),
        drill(
            id = "bm_close_mastery",
            name = "Close Ball Mastery",
            category = TrainingCategory.BALL_MASTERY,
            description = "High-tempo 1m x 1m box freestyle combining toe taps, rolls, L-turns, and step-overs.",
            instructions = listOf(
                "Mark a tight 1m x 1m square with 4 cones.",
                "Chain sole rolls, foundations, and behind-leg L-turns nonstop without touching a cone.",
                "Keep your head up scanning every 3 touches."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "5 sets x 60 sec",
            restTimeSeconds = 25,
            tips = listOf("Small touches keep defenders guessing and protect possession in crowds."),
            diagramType = PitchDiagramType.SLALOM_DRIBBLING
        ),

        // B. Dribbling (7)
        drill(
            id = "drib_cone",
            name = "Cone Dribbling",
            category = TrainingCategory.DRIBBLING,
            description = "Weave tightly through 8 inline cones using inside and outside touches.",
            instructions = listOf(
                "Place 8 cones 1.2 meters apart in a straight line.",
                "Weave cleanly with right foot only, left foot only, then alternating both feet.",
                "Sprint 5 meters past the last cone on exit."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "6 slalom runs",
            restTimeSeconds = 25,
            tips = listOf("One soft touch per stride keeps the ball glued near your feet."),
            diagramType = PitchDiagramType.SLALOM_DRIBBLING
        ),
        drill(
            id = "drib_speed",
            name = "Speed Dribbling",
            category = TrainingCategory.DRIBBLING,
            description = "Push the ball cleanly into stride using laces/pinky-toe at near top sprint speed.",
            instructions = listOf(
                "Set two cones 25 meters apart.",
                "Take controlled forward touches with your laces every 2–3 running strides.",
                "Decelerate smoothly in 2 steps around the end cone and drive back."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 45,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "6 x 25m carries",
            restTimeSeconds = 35,
            tips = listOf("Look ahead at space so you push the ball into your natural running lane."),
            diagramType = PitchDiagramType.SLALOM_DRIBBLING
        ),
        drill(
            id = "drib_1v1",
            name = "1v1 Dribbling",
            category = TrainingCategory.DRIBBLING,
            description = "Attack a central marker cone with a sharp feint and explosive directional exit.",
            instructions = listOf(
                "Dribble straight toward a defender cone 10 meters ahead.",
                "Drop your shoulder left 1.5m before the cone, then burst right with your outside foot.",
                "Alternate attacking left and right lanes."
            ),
            durationMinutes = 14,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 8 beats",
            restTimeSeconds = 30,
            tips = listOf("Sell the fake with hips and shoulders, then accelerate hard past the defender."),
            diagramType = PitchDiagramType.SLALOM_DRIBBLING
        ),
        drill(
            id = "drib_cod",
            name = "Change of Direction",
            category = TrainingCategory.DRIBBLING,
            description = "Execute sharp 180-degree Cruyff turns, inside cuts, and sole drag-backs at pace.",
            instructions = listOf(
                "Dribble 10 meters fast toward a turn gate.",
                "Execute a sharp inside-foot hook or Cruyff turn behind your standing heel.",
                "Accelerate back in the opposite direction immediately."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "4 sets x 8 turns",
            restTimeSeconds = 25,
            tips = listOf("Bend your knees low into every cut so your exit stride is instant."),
            diagramType = PitchDiagramType.SLALOM_DRIBBLING
        ),
        drill(
            id = "drib_zigzag",
            name = "Zigzag Dribbling",
            category = TrainingCategory.DRIBBLING,
            description = "Diagonal sprint carries and sharp outside-foot cuts between staggered cones.",
            instructions = listOf(
                "Place 6 cones in a wide zigzag pattern 4 meters apart diagonally.",
                "Drive fast at each cone, cut sharply around the outside edge, and sprint to the next.",
                "Use left foot cuts on left cones and right foot cuts on right cones."
            ),
            durationMinutes = 14,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "6 zigzag runs",
            restTimeSeconds = 30,
            tips = listOf("Plant wide outside the cone and push off aggressively into the new angle."),
            diagramType = PitchDiagramType.SLALOM_DRIBBLING
        ),
        drill(
            id = "drib_tight",
            name = "Tight-Space Dribbling",
            category = TrainingCategory.DRIBBLING,
            description = "Keep possession inside a crowded 4m x 4m box using sole rolls and quick shields.",
            instructions = listOf(
                "Set 4 cones in a 4m x 4m square with 2 obstacle cones inside.",
                "Dribble fast inside the grid using rapid sole turns and inside-outside taps.",
                "Change speed every 4 seconds without letting the ball leave the square."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "4 sets x 60 sec",
            restTimeSeconds = 25,
            tips = listOf("Use your arms softly for balance and always keep your body between pressure and ball."),
            diagramType = PitchDiagramType.SLALOM_DRIBBLING
        ),
        drill(
            id = "drib_skill_moves",
            name = "Skill Moves",
            category = TrainingCategory.DRIBBLING,
            description = "Practice Step-Overs, La Croqueta, Elastico, and Body Feints cleanly at match speed.",
            instructions = listOf(
                "Approach a cone defender at 70% tempo.",
                "Perform a double step-over or La Croqueta lateral glide right before reaching the cone.",
                "Push cleanly into space and finish with a crisp pass."
            ),
            durationMinutes = 14,
            timerWorkSeconds = 75,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "4 sets x 10 skill combos",
            restTimeSeconds = 30,
            tips = listOf("Timing matters more than flashiness — execute 1.5 meters from the defender."),
            diagramType = PitchDiagramType.SLALOM_DRIBBLING
        ),

        // C. Passing (7)
        drill(
            id = "pass_short",
            name = "Short Passing",
            category = TrainingCategory.PASSING,
            description = "Accurate 5–10m inside-foot ground passes with locked ankle and clean follow-through.",
            instructions = listOf(
                "Set a 1-meter target gate 6 meters ahead or use a wall mark.",
                "Plant your non-kicking foot beside the ball aimed straight at the target.",
                "Strike through the ball's equator with your arch."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "4 sets x 20 passes",
            restTimeSeconds = 20,
            tips = listOf("Lock your ankle at 90 degrees so the ball glides flat across the turf."),
            diagramType = PitchDiagramType.PASSING_TRIANGLE
        ),
        drill(
            id = "pass_wall",
            name = "Wall Passing",
            category = TrainingCategory.PASSING,
            description = "High-tempo rebound wall rhythm building weak-foot confidence and ankle firmness.",
            instructions = listOf(
                "Stand 4 meters from a solid rebound wall.",
                "Strike firm ground passes alternating right and left feet.",
                "Take a quick check over your shoulder between every pass."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "5 sets x 60 sec",
            restTimeSeconds = 25,
            tips = listOf("Stay on your toes and meet the ball early rather than waiting flat-footed."),
            diagramType = PitchDiagramType.PASSING_TRIANGLE
        ),
        drill(
            id = "pass_one_touch",
            name = "One-Touch Passing",
            category = TrainingCategory.PASSING,
            description = "Instant first-time returns off a wall or partner to sharpen reaction and rhythm.",
            instructions = listOf(
                "Stand 3.5 meters from a rebound wall.",
                "Play nonstop one-touch passes without trapping, alternating feet every strike.",
                "Count consecutive clean rebounds up to 30+."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "5 sets x 45 sec",
            restTimeSeconds = 25,
            tips = listOf("Set your standing foot before the rebound arrives so your strike stays crisp."),
            diagramType = PitchDiagramType.PASSING_TRIANGLE
        ),
        drill(
            id = "pass_two_touch",
            name = "Two-Touch Passing",
            category = TrainingCategory.PASSING,
            description = "Receive across your body with touch #1 and immediately drive pass #2 into space.",
            instructions = listOf(
                "Receive a return pass on your back foot, pushing the ball cleanly across a 1m cone gate.",
                "Step into touch #2 and fire a crisp ground pass back off the wall.",
                "Alternate exiting left and right."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 75,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 16 reps",
            restTimeSeconds = 25,
            tips = listOf("Never trap dead under your feet — push touch #1 forward into your passing stride."),
            diagramType = PitchDiagramType.PASSING_TRIANGLE
        ),
        drill(
            id = "pass_long",
            name = "Long Passing",
            category = TrainingCategory.PASSING,
            description = "Driven 25–35m diagonal switches and lofted backspin passes to wide channels.",
            instructions = listOf(
                "Place a 3m x 3m target zone 25–30 meters away.",
                "Approach at a 35-degree angle and strike low under the ball's center with your instep.",
                "Follow through across your body for clean backspin and control."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "4 sets x 10 switches",
            restTimeSeconds = 30,
            tips = listOf("Lean slightly back and sweep across the ball rather than poking straight up."),
            diagramType = PitchDiagramType.PASSING_TRIANGLE
        ),
        drill(
            id = "pass_accuracy",
            name = "Passing Accuracy",
            category = TrainingCategory.PASSING,
            description = "Thread firm passes cleanly through narrow 0.8m cone gates from 8–15 meters.",
            instructions = listOf(
                "Set up three 0.8-meter cone gates at 8m, 12m, and 15m.",
                "Pick your gate, check shoulders, and thread a firm ground pass cleanly through.",
                "Track clean gate hits out of 15 attempts."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 75,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 15 passes",
            restTimeSeconds = 25,
            tips = listOf("Pick a spot on the grass inside the gate to lock your aim before swinging."),
            diagramType = PitchDiagramType.PASSING_TRIANGLE
        ),
        drill(
            id = "pass_first_touch",
            name = "First-Touch Passing",
            category = TrainingCategory.PASSING,
            description = "Scan behind, cushion passes out of pressure, and immediately release into open lanes.",
            instructions = listOf(
                "Pass against a wall, scan over your shoulder while the ball travels.",
                "Take an angled first touch past a defender cone and thread a quick pass into a side gate.",
                "Alternate left and right scanning turns."
            ),
            durationMinutes = 14,
            timerWorkSeconds = 75,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "4 sets x 12 combos",
            restTimeSeconds = 25,
            tips = listOf("Open hips early so you can see both the ball and your forward target."),
            diagramType = PitchDiagramType.PASSING_TRIANGLE
        ),

        // D. Shooting (8)
        drill(
            id = "shoot_power",
            name = "Power Shooting",
            category = TrainingCategory.SHOOTING,
            description = "Drive clean low power strikes with your laces from 18–22 yards.",
            instructions = listOf(
                "Take a positive prep touch angled 30 degrees ahead.",
                "Plant beside the ball, point kicking toes down, and drive cleanly through center with laces.",
                "Land forward on your kicking foot to maximize power transfer."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "4 sets x 8 shots",
            restTimeSeconds = 35,
            tips = listOf("Keep chest slightly over the ball so low drives stay under the crossbar."),
            diagramType = PitchDiagramType.FINISHING_ZONES
        ),
        drill(
            id = "shoot_finesse",
            name = "Finesse Shooting",
            category = TrainingCategory.SHOOTING,
            description = "Wrap curled inside-foot shots around defenders into the far corner.",
            instructions = listOf(
                "Dribble toward the corner of the penalty arc and cut inside.",
                "Strike the outer lower quadrant of the ball with your inside arch.",
                "Follow through across your body aiming outside the far post to curl in."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 8 curls",
            restTimeSeconds = 35,
            tips = listOf("Smooth whip speed creates bend — don't try to smash it flat."),
            diagramType = PitchDiagramType.FINISHING_ZONES
        ),
        drill(
            id = "shoot_instep",
            name = "Instep Shooting",
            category = TrainingCategory.SHOOTING,
            description = "Pure sweet-spot instep mechanics for flat, dipping strikes with minimal spin.",
            instructions = listOf(
                "Set the ball stationary at 18 yards.",
                "Strike dead-center with the hard top bone of your instep with locked ankle.",
                "Focus on crisp pop sound and flat trajectory."
            ),
            durationMinutes = 14,
            timerWorkSeconds = 75,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "4 sets x 8 strikes",
            restTimeSeconds = 30,
            tips = listOf("Eyes locked on the center of the ball until follow-through finishes."),
            diagramType = PitchDiagramType.FINISHING_ZONES
        ),
        drill(
            id = "shoot_first_time",
            name = "First-Time Shooting",
            category = TrainingCategory.SHOOTING,
            description = "Strike rolling wall rebounds or lay-offs cleanly on the first touch inside the box.",
            instructions = listOf(
                "Roll or bounce a pass into the edge of the penalty box.",
                "Adjust feet rapidly, open hips toward target, and strike first-time without trapping.",
                "Target low corners away from the goalkeeper."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "4 sets x 8 first-time hits",
            restTimeSeconds = 35,
            tips = listOf("Shorten your backswing so you make clean contact with moving passes."),
            diagramType = PitchDiagramType.FINISHING_ZONES
        ),
        drill(
            id = "shoot_volleys",
            name = "Volleys",
            category = TrainingCategory.SHOOTING,
            description = "Time airborne drops cleanly on the full volley and half-volley on target.",
            instructions = listOf(
                "Toss the ball softly to waist height 14 yards out.",
                "Rotate hips sideways, keep knee over the ball, and strike cleanly through the middle.",
                "Mix full volleys and half-volleys off one bounce."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "4 sets x 8 volleys",
            restTimeSeconds = 35,
            tips = listOf("Smooth timing beats brute force — watch the ball all the way onto your boot."),
            diagramType = PitchDiagramType.FINISHING_ZONES
        ),
        drill(
            id = "shoot_weak_foot",
            name = "Weak-Foot Shooting",
            category = TrainingCategory.SHOOTING,
            description = "Build two-footed box finishing confidence from tight and central angles.",
            instructions = listOf(
                "Take 100% of strikes in this set with your non-dominant foot.",
                "Start close at 12 yards focusing on clean inside-foot placement, then move to 16 yards.",
                "Keep your balance arm out wide for clean hip rotation."
            ),
            durationMinutes = 14,
            timerWorkSeconds = 75,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 8 weak-foot shots",
            restTimeSeconds = 30,
            tips = listOf("Plant your dominant leg slightly wider if needed to keep your swing path clean."),
            diagramType = PitchDiagramType.FINISHING_ZONES
        ),
        drill(
            id = "shoot_penalty",
            name = "Penalty Training",
            category = TrainingCategory.SHOOTING,
            description = "Repeatable breathing, run-up rhythm, and side-netting placement from 11 meters.",
            instructions = listOf(
                "Place the ball firmly on the 11m penalty spot.",
                "Pick top-left, bottom-left, top-right, or bottom-right BEFORE starting your run-up.",
                "Take one calm breath and strike cleanly inside the side netting."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 75,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "3 sets x 6 penalties",
            restTimeSeconds = 25,
            tips = listOf("Never change your target mid-run-up; commit 100% to your corner."),
            diagramType = PitchDiagramType.PENALTY_SET_PIECE
        ),
        drill(
            id = "shoot_accuracy",
            name = "Shooting Accuracy",
            category = TrainingCategory.SHOOTING,
            description = "Target corner cone gates inside the goalposts under calm composure.",
            instructions = listOf(
                "Place cones 1.5m inside both left and right goalposts.",
                "Dribble 5m to the penalty arc and place accurate shots inside the corner zones.",
                "Score 1 point per clean corner hit out of 10."
            ),
            durationMinutes = 14,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 10 target shots",
            restTimeSeconds = 30,
            tips = listOf("Aim for the side netting — keepers rarely reach firm shots hugging the post."),
            diagramType = PitchDiagramType.FINISHING_ZONES
        )
    )
}
