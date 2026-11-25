package com.example.data.repository

import com.example.data.model.FitnessArea
import com.example.data.model.FootballExercise
import com.example.data.model.LessonSection
import com.example.data.model.PitchDiagramType
import com.example.data.model.SkillLevel
import com.example.data.model.TrainingCategory

object ConditioningAndDefenseCurriculum {

    // Categories E (8), F (9), G (7), H (7) = 31 drills (Total A-H = 61 drills!)
    val speedDefenseAndGkExercises: List<FootballExercise> = listOf(
        // E. Speed and Agility (8)
        drill(
            id = "spd_10m",
            name = "10-Meter Sprint",
            category = TrainingCategory.SPEED_AGILITY,
            description = "Explosive first-step burst over 10 meters to win 50/50 balls and beat full-backs.",
            instructions = listOf(
                "Set two cones 10 meters apart and take a low 2-point sprinter stance.",
                "Drive hard through the ground staying low for the first 4 strides.",
                "Walk back calmly for full 40-second recovery before the next sprint."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 30,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "6 x 10m sprints",
            restTimeSeconds = 40,
            tips = listOf("Pump your elbows violently backward on your first 3 steps."),
            safety = listOf("Complete a dynamic warm-up before max-effort sprints."),
            diagramType = PitchDiagramType.AGILITY_LADDER
        ),
        drill(
            id = "spd_20m",
            name = "20-Meter Sprint",
            category = TrainingCategory.SPEED_AGILITY,
            description = "Build top-end sprint mechanics and stride power over a 20m pitch channel.",
            instructions = listOf(
                "Mark a 20-meter straight lane.",
                "Accelerate smoothly over the first 10m, then lift knees tall to hit top speed through 20m.",
                "Decelerate gradually over 5 extra meters."
            ),
            durationMinutes = 14,
            timerWorkSeconds = 35,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "6 x 20m sprints",
            restTimeSeconds = 45,
            tips = listOf("Keep shoulders relaxed and chin level while hitting top speed."),
            safety = listOf("Stop immediately if you feel any hamstring tightness."),
            diagramType = PitchDiagramType.AGILITY_LADDER
        ),
        drill(
            id = "spd_acceleration",
            name = "Acceleration",
            category = TrainingCategory.SPEED_AGILITY,
            description = "Rolling-jog into explosive 15-meter gear-shift bursts mirroring match transitions.",
            instructions = listOf(
                "Jog lightly for 5 meters.",
                "At the trigger cone, drop your chest and explode into full sprint for 12 meters.",
                "Reset smoothly after 35 seconds rest."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 35,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "6 gear-shift bursts",
            restTimeSeconds = 35,
            tips = listOf("Push the ground behind you rather than reaching your front foot far ahead."),
            safety = listOf("Use proper turf/grass studs to avoid slipping during acceleration."),
            diagramType = PitchDiagramType.AGILITY_LADDER
        ),
        drill(
            id = "spd_shuttle",
            name = "Shuttle Runs",
            category = TrainingCategory.SPEED_AGILITY,
            description = "5-10-5 pro agility shuttles training rapid braking and 180-degree re-acceleration.",
            instructions = listOf(
                "Set 3 cones spaced 5 meters apart in a line (center, left, right).",
                "Start at center, sprint 5m right, touch, sprint 10m left, touch, sprint 5m back through center.",
                "Alternate starting left and right."
            ),
            durationMinutes = 14,
            timerWorkSeconds = 45,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "6 shuttles",
            restTimeSeconds = 40,
            tips = listOf("Drop your hips low before touching each cone to coil for the turn."),
            safety = listOf("Keep ankles stable and plant with knees aligned over toes."),
            diagramType = PitchDiagramType.AGILITY_LADDER
        ),
        drill(
            id = "spd_agility",
            name = "Agility Drills",
            category = TrainingCategory.SPEED_AGILITY,
            description = "Quick-feet ladder and cone footwork for rapid balance and multi-angle steps.",
            instructions = listOf(
                "Place 6 flat cones 45cm apart.",
                "Run 2-feet-in-each-gap forward, then lateral side-shuffle patterns.",
                "Finish every set with a 5-meter forward burst."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 45,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "5 sets x 45 sec",
            restTimeSeconds = 25,
            tips = listOf("Light, quiet ground contacts equal faster directional reactions."),
            diagramType = PitchDiagramType.AGILITY_LADDER
        ),
        drill(
            id = "spd_cone_sprint",
            name = "Cone Sprint",
            category = TrainingCategory.SPEED_AGILITY,
            description = "T-cone sprint-shuffle-backpedal circuit for all-direction pitch speed.",
            instructions = listOf(
                "Arrange 4 cones in a T-shape (10m stem, 5m wings).",
                "Sprint forward 10m, side-shuffle left 5m, side-shuffle right 10m, back to middle, and backpedal home.",
                "Keep your eyes facing forward throughout."
            ),
            durationMinutes = 14,
            timerWorkSeconds = 45,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "6 T-sprints",
            restTimeSeconds = 40,
            tips = listOf("Never cross your feet during the lateral shuffles."),
            safety = listOf("Stay low while backpedaling to avoid tripping backward."),
            diagramType = PitchDiagramType.AGILITY_LADDER
        ),
        drill(
            id = "spd_reaction",
            name = "Reaction Training",
            category = TrainingCategory.SPEED_AGILITY,
            description = "Split-step readiness and instant directional sprints on audio timer cues.",
            instructions = listOf(
                "Bounce lightly on your toes in a central cone square.",
                "On every 8-second mental or audio cue, burst diagonally to the left or right gate cone.",
                "Jog back calmly and reset athletic stance."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 45,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "6 reaction sets",
            restTimeSeconds = 30,
            tips = listOf("Lean into the turn with your hips before your feet move."),
            diagramType = PitchDiagramType.AGILITY_LADDER
        ),
        drill(
            id = "spd_cod",
            name = "Change of Direction",
            category = TrainingCategory.SPEED_AGILITY,
            description = "Sharp 45-degree and 90-degree deceleration cuts at match speed.",
            instructions = listOf(
                "Set 5 cones in a wide zig-zag channel 5 meters apart.",
                "Sprint into each cone, brake in 2 quick steps, plant outside foot, and cut 90 degrees.",
                "Maintain crisp posture out of every exit."
            ),
            durationMinutes = 14,
            timerWorkSeconds = 45,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "6 full runs",
            restTimeSeconds = 35,
            tips = listOf("Chop your steps right before planting to protect your knees and cut sharper."),
            safety = listOf("Avoid planting with a straight locked knee on sharp turns."),
            diagramType = PitchDiagramType.AGILITY_LADDER
        ),

        // F. Physical Conditioning (9)
        drill(
            id = "cond_endurance",
            name = "Endurance",
            category = TrainingCategory.PHYSICAL_CONDITIONING,
            description = "Steady aerobic 40m box-to-box tempo runs building 90-minute match lungs.",
            instructions = listOf(
                "Mark a 40-meter pitch lane.",
                "Run smoothly at 75% pace for 45 seconds, then walk/jog 25 seconds.",
                "Maintain steady nasal-mouth breathing rhythm throughout."
            ),
            durationMinutes = 18,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "4 sets x 90 sec",
            restTimeSeconds = 35,
            tips = listOf("Keep your stride relaxed and shoulders loose."),
            safety = listOf("Hydrate between blocks and stop if dizzy."),
            diagramType = PitchDiagramType.PITCH_DIMENSIONS
        ),
        drill(
            id = "cond_stamina",
            name = "Stamina",
            category = TrainingCategory.PHYSICAL_CONDITIONING,
            description = "High-intensity 20/20 repeated shuttle intervals for late-game recovery speed.",
            instructions = listOf(
                "Run hard across a 35-meter channel for 20 seconds.",
                "Jog lightly for 20 seconds active recovery.",
                "Complete 6 continuous cycles per block."
            ),
            durationMinutes = 16,
            timerWorkSeconds = 120,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "3 blocks x 2 min",
            restTimeSeconds = 50,
            tips = listOf("Hold consistent shuttle speed on rep 6 just like rep 1."),
            safety = listOf("Take full 50-second rest between blocks."),
            diagramType = PitchDiagramType.AGILITY_LADDER
        ),
        drill(
            id = "cond_core",
            name = "Core Strength",
            category = TrainingCategory.PHYSICAL_CONDITIONING,
            description = "Anti-rotation dead-bugs, Russian twists, and hollow holds for duel stability.",
            instructions = listOf(
                "Perform 12 slow dead-bug extensions keeping lower back pressed to the turf.",
                "Complete 20 controlled Russian twists.",
                "Hold a tight hollow body brace for 20 seconds."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "3 core rounds",
            restTimeSeconds = 25,
            tips = listOf("Exhale smoothly while bracing your abs tight."),
            safety = listOf("Avoid arching your lower back during leg extensions."),
            diagramType = PitchDiagramType.PITCH_DIMENSIONS
        ),
        drill(
            id = "cond_leg_strength",
            name = "Leg Strength",
            category = TrainingCategory.PHYSICAL_CONDITIONING,
            description = "Single-leg Romanian deadlift balance, calf raises, and glute bridges for kick power.",
            instructions = listOf(
                "Complete 10 single-leg hip hinges per side with balanced posture.",
                "Perform 15 single-leg glute bridges per leg.",
                "Finish with 20 controlled calf raises."
            ),
            durationMinutes = 14,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "3 strength sets",
            restTimeSeconds = 30,
            tips = listOf("Strong hamstrings and glutes power faster sprints and protect knees."),
            safety = listOf("Move slowly under control — never jerk your lower back."),
            diagramType = PitchDiagramType.DEFENSE_SHADOW
        ),
        drill(
            id = "cond_squats",
            name = "Squats",
            category = TrainingCategory.PHYSICAL_CONDITIONING,
            description = "Controlled bodyweight squats building quad strength for shielding and acceleration.",
            instructions = listOf(
                "Stand with feet shoulder-width apart and toes slightly turned out.",
                "Lower hips back and down over 2 seconds until thighs are parallel to turf.",
                "Drive cleanly upward through mid-foot."
            ),
            durationMinutes = 10,
            timerWorkSeconds = 45,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "4 sets x 15 squats",
            restTimeSeconds = 30,
            tips = listOf("Keep chest proud and knees tracking over middle toes."),
            safety = listOf("Do not let knees cave inward on ascent."),
            diagramType = PitchDiagramType.AGILITY_LADDER
        ),
        drill(
            id = "cond_lunges",
            name = "Lunges",
            category = TrainingCategory.PHYSICAL_CONDITIONING,
            description = "Forward, reverse, and lateral lunges strengthening cutting and planting muscles.",
            instructions = listOf(
                "Step forward into a 90/90 lunge and push back powerfully.",
                "Step sideways into a lateral groin lunge, keeping trailing leg straight.",
                "Alternate left and right sides evenly."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "3 sets x 10 each leg",
            restTimeSeconds = 30,
            tips = listOf("Keep torso upright and hips square."),
            safety = listOf("Keep front knee aligned over ankle, never twisting inward."),
            diagramType = PitchDiagramType.DEFENSE_SHADOW
        ),
        drill(
            id = "cond_pushups",
            name = "Push-Ups",
            category = TrainingCategory.PHYSICAL_CONDITIONING,
            description = "Upper-body and shoulder-shielding strength for physical 50/50 challenges.",
            instructions = listOf(
                "Set hands slightly wider than shoulder-width in a rigid plank line.",
                "Lower chest cleanly to 3cm above ground with elbows at 45 degrees.",
                "Press powerfully back to full extension."
            ),
            durationMinutes = 10,
            timerWorkSeconds = 45,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "4 sets x 12–15 reps",
            restTimeSeconds = 30,
            tips = listOf("Drop to knees cleanly if needed rather than letting hips sag."),
            safety = listOf("Keep neck neutral and core braced throughout."),
            diagramType = PitchDiagramType.PITCH_DIMENSIONS
        ),
        drill(
            id = "cond_jumps",
            name = "Jump Training",
            category = TrainingCategory.PHYSICAL_CONDITIONING,
            description = "Plyometric tuck jumps and single-leg hops for aerial headers and explosive power.",
            instructions = listOf(
                "Perform 6 counter-movement vertical jumps reaching high as if winning a header.",
                "Add 6 lateral single-leg skater hops sticking each landing silently for 1 second.",
                "Rest fully between sets so every jump stays crisp."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 45,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "4 sets x 10 jumps",
            restTimeSeconds = 40,
            tips = listOf("Land softly on the balls of your feet with bent knees."),
            safety = listOf("Only jump on even grass/turf surfaces and prioritize soft landings."),
            diagramType = PitchDiagramType.AGILITY_LADDER
        ),
        drill(
            id = "cond_plank",
            name = "Plank",
            category = TrainingCategory.PHYSICAL_CONDITIONING,
            description = "Forearm and side plank holds building full-body core rigidity.",
            instructions = listOf(
                "Hold a forearm plank with glutes squeezed and ribs tucked for 40 seconds.",
                "Rotate into left side plank for 25 seconds, then right side plank for 25 seconds.",
                "Breathe steadily without holding your breath."
            ),
            durationMinutes = 10,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "3 sets x 60 sec",
            restTimeSeconds = 25,
            tips = listOf("Keep ears, shoulders, hips, and heels in one straight line."),
            safety = listOf("Stop and reset if your lower back begins to sag."),
            diagramType = PitchDiagramType.PITCH_DIMENSIONS
        ),

        // G. Defending (7)
        drill(
            id = "def_footwork",
            name = "Defensive Footwork",
            category = TrainingCategory.DEFENDING,
            description = "Rapid zig-zag jockey shuffles and hip-flip transitions without crossing feet.",
            instructions = listOf(
                "Set 4 cones in a zig-zag lane spanning 15 meters.",
                "Side-jockey rapidly between cones, dropping your hip cleanly to flip direction.",
                "Keep knees bent and heels light off the turf."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "5 sets x 60 sec",
            restTimeSeconds = 25,
            tips = listOf("Stay low and compact so you never get caught off-balance."),
            diagramType = PitchDiagramType.DEFENSE_SHADOW
        ),
        drill(
            id = "def_1v1",
            name = "1v1 Defending",
            category = TrainingCategory.DEFENDING,
            description = "Close down quickly, decelerate 2m away, and force attackers onto their weak foot.",
            instructions = listOf(
                "Sprint 6 meters toward a cone attacker, chop your steps 2 meters out.",
                "Angle your body side-on to shut off the central lane and invite them wide.",
                "Mirror laterally for 10 seconds before stepping in to win the ball."
            ),
            durationMinutes = 14,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "5 sets x 60 sec",
            restTimeSeconds = 30,
            tips = listOf("Watch the ball — never bite on shoulder feints."),
            diagramType = PitchDiagramType.DEFENSE_SHADOW
        ),
        drill(
            id = "def_interception",
            name = "Interception",
            category = TrainingCategory.DEFENDING,
            description = "Read passing lanes and step forward explosively to cut out through-balls.",
            instructions = listOf(
                "Hold a compact mid-block stance behind two lane cones.",
                "Read the cue, sprint 4 meters diagonally into the passing lane, and control cleanly.",
                "Immediately release a forward counter pass."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 8 interceptions",
            restTimeSeconds = 25,
            tips = listOf("Position half a step behind the lane so you can bait and jump the pass."),
            diagramType = PitchDiagramType.DEFENSE_SHADOW
        ),
        drill(
            id = "def_tackling",
            name = "Tackling Technique",
            category = TrainingCategory.DEFENDING,
            description = "Controlled poke-tackle and block-tackle timing without lunging or fouling.",
            instructions = listOf(
                "Jockey alongside a slow rolling ball.",
                "Wait for a heavy touch, plant beside the ball, and execute a firm inside-foot block.",
                "Keep knees bent and follow straight through the ball."
            ),
            durationMinutes = 14,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 10 blocks",
            restTimeSeconds = 30,
            tips = listOf("Win the ball cleanly on your feet whenever possible — stay upright."),
            safety = listOf("Keep studs low and avoid reckless sliding on dry turf."),
            diagramType = PitchDiagramType.DEFENSE_SHADOW
        ),
        drill(
            id = "def_positioning",
            name = "Positioning",
            category = TrainingCategory.DEFENDING,
            description = "Hold compact goal-side cover angles relative to ball, goal, and blind-side runners.",
            instructions = listOf(
                "Adjust laterally across 3 central cones as imaginary ball possession switches flanks.",
                "Check behind your shoulder every 3 seconds for overlapping runners.",
                "Maintain proper cover depth inside the penalty box width."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 75,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "4 sets x 75 sec",
            restTimeSeconds = 25,
            tips = listOf("Always see both the ball and your closest attacker in one glance."),
            diagramType = PitchDiagramType.FORMATION_433
        ),
        drill(
            id = "def_recovery",
            name = "Recovery Runs",
            category = TrainingCategory.DEFENDING,
            description = "High-speed diagonal recovery sprints back toward your goal post to shut down counters.",
            instructions = listOf(
                "Start high near midfield and sprint diagonally back toward the penalty spot.",
                "Turn your head once mid-sprint to locate the ball without slowing.",
                "Brake smoothly into a low jockey stance at the top of the box."
            ),
            durationMinutes = 14,
            timerWorkSeconds = 45,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "6 recovery sprints",
            restTimeSeconds = 35,
            tips = listOf("Sprint the shortest straight line to the goal post, then step out to engage."),
            diagramType = PitchDiagramType.DEFENSE_SHADOW
        ),
        drill(
            id = "def_awareness",
            name = "Defensive Awareness",
            category = TrainingCategory.DEFENDING,
            description = "360-degree scanning, line communication, and pressing-vs-dropping decisions.",
            instructions = listOf(
                "Shuffle across the back line zone while calling out 'Step!', 'Drop!', or 'Hold!'.",
                "Scan both shoulders rapidly between every lateral shift.",
                "Step up together on backward passes and drop on unpressured long balls."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 75,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "4 tactical rounds",
            restTimeSeconds = 25,
            tips = listOf("Great defending solves danger 3 seconds before the pass is even kicked."),
            diagramType = PitchDiagramType.FORMATION_433
        ),

        // H. Goalkeeper Training (7)
        drill(
            id = "gk_reaction",
            name = "Reaction Training",
            category = TrainingCategory.GOALKEEPER,
            description = "Rapid set-position reflex saves off close-range wall rebounds and deflections.",
            instructions = listOf(
                "Stand 2.5 meters from a wall in a balanced W-hands set stance.",
                "Throw firmly against the wall at varied angles and react cleanly to catch or parry wide.",
                "Reset feet instantly between every rebound."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 45 sec",
            restTimeSeconds = 30,
            tips = listOf("Keep hands slightly ahead of your hips with elbows soft."),
            diagramType = PitchDiagramType.GOALKEEPER_ANGLES
        ),
        drill(
            id = "gk_catching",
            name = "Catching",
            category = TrainingCategory.GOALKEEPER,
            description = "Clean W-contour high catches and basket-cup chest scoops with zero rebounds.",
            instructions = listOf(
                "Toss the ball above head height and attack it at peak reach with thumbs forming a 'W'.",
                "Practice mid-height basket scoops pulling the ball securely into your chest.",
                "Call 'KEEPER!' loudly on every aerial claim."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "4 sets x 15 catches",
            restTimeSeconds = 25,
            tips = listOf("Soft fingers absorb shot pace; bring your knee up on high claims for protection."),
            diagramType = PitchDiagramType.GOALKEEPER_ANGLES
        ),
        drill(
            id = "gk_diving",
            name = "Diving Technique",
            category = TrainingCategory.GOALKEEPER,
            description = "Forward-angled collapse and low-extension dives landing safely on side hips and shoulders.",
            instructions = listOf(
                "Start seated or kneeling on soft grass, then progress to standing low dives.",
                "Step diagonally forward with your near foot toward the ball, never backward.",
                "Land smoothly along your side hip/shoulder while pinning the ball with top hand behind."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "4 sets x 8 dives per side",
            restTimeSeconds = 35,
            tips = listOf("Attack forward at a slight angle to cut the shot trajectory sooner."),
            safety = listOf("Practice dives only on soft grass or padded turf."),
            diagramType = PitchDiagramType.GOALKEEPER_ANGLES
        ),
        drill(
            id = "gk_footwork",
            name = "Footwork",
            category = TrainingCategory.GOALKEEPER,
            description = "Quick side-shuffles, crossover steps, and set-position stops across the goalmouth.",
            instructions = listOf(
                "Start at the near post and side-shuffle rapidly to the center line.",
                "Freeze completely still in your balanced set position for 1 second.",
                "Push cleanly across to the far post without crossing feet early."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "5 sets x 60 sec",
            restTimeSeconds = 25,
            tips = listOf("Be still at the exact moment a striker plants to shoot."),
            diagramType = PitchDiagramType.GOALKEEPER_ANGLES
        ),
        drill(
            id = "gk_distribution",
            name = "Distribution",
            category = TrainingCategory.GOALKEEPER,
            description = "Accurate bowling rolls, side-arm slings, and clipped half-volley goal kicks.",
            instructions = listOf(
                "Set 3 target cones at 15m, 25m, and 40m.",
                "Practice smooth low bowling rolls to full-backs, then overarm slings to midfield.",
                "Finish with clipped side-volleys out of hands into wide targets."
            ),
            durationMinutes = 14,
            timerWorkSeconds = 75,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 10 distributions",
            restTimeSeconds = 25,
            tips = listOf("Release bowling passes smoothly along the grass with zero bounce."),
            diagramType = PitchDiagramType.GOALKEEPER_ANGLES
        ),
        drill(
            id = "gk_shot_stopping",
            name = "Shot Stopping",
            category = TrainingCategory.GOALKEEPER,
            description = "Narrow shooting angles along the ball-to-goal center line and parry shots wide.",
            instructions = listOf(
                "Move forward 2.5m along the imaginary line from the penalty arc to center goal.",
                "Drop into set position as the ball is struck off a rebound surface.",
                "Push shots cleanly wide of the goalpost — never back into the 6-yard penalty spot."
            ),
            durationMinutes = 14,
            timerWorkSeconds = 75,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "4 sets x 10 saves",
            restTimeSeconds = 30,
            tips = listOf("Strong wrists redirect shots out toward the corner flag."),
            diagramType = PitchDiagramType.GOALKEEPER_ANGLES
        ),
        drill(
            id = "gk_handling",
            name = "Handling",
            category = TrainingCategory.GOALKEEPER,
            description = "Consistent glove grip, low ground scoops, and fast transition to counter-distribution.",
            instructions = listOf(
                "Scoop rolling ground shots cleanly with pinkies together and chest behind the ball.",
                "Roll rapidly to feet and scan both flanks within 2 seconds.",
                "Maintain clean finger spread on every ball contact."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "4 sets x 15 scoops",
            restTimeSeconds = 25,
            tips = listOf("Keep your body behind low ground balls as a second safety barrier."),
            diagramType = PitchDiagramType.GOALKEEPER_ANGLES
        )
    )

    val fitnessExercises: List<FootballExercise> = listOf(
        FootballExercise(
            id = "fit_warmup",
            name = "Dynamic Pitch Warm-Up",
            section = LessonSection.FITNESS,
            categoryKey = FitnessArea.WARMUP.name,
            categoryLabel = FitnessArea.WARMUP.displayName,
            description = "Activate hips, hamstrings, ankles, and nervous system before intense drills.",
            instructions = listOf(
                "Light forward and backward jog for 90 seconds.",
                "Perform 10 open-the-gate and 10 close-the-gate hip circles.",
                "Complete 2 progressive 15m build-up runs at 65% and 80% effort."
            ),
            durationMinutes = 5,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "1 complete warm-up flow",
            restTimeSeconds = 15,
            trainingTips = listOf("Warm muscles produce faster sprints and prevent hamstring pulls."),
            safetyInstructions = listOf("Never start max sprints or hard shots cold."),
            diagramType = PitchDiagramType.AGILITY_LADDER
        ),
        FootballExercise(
            id = "fit_stretch",
            name = "Groin, Hip & Hamstring Mobility",
            section = LessonSection.FITNESS,
            categoryKey = FitnessArea.STRETCHING.name,
            categoryLabel = FitnessArea.STRETCHING.displayName,
            description = "Open kicking hips and lengthen hamstrings for effortless passing and striking.",
            instructions = listOf(
                "Perform World's Greatest Stretch lunges with rotation (5 per side).",
                "Dynamic leg swings front-to-back and side-to-side (12 per leg)."
            ),
            durationMinutes = 5,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "2 controlled rounds",
            restTimeSeconds = 15,
            trainingTips = listOf("Exhale slowly as you sink into each mobility hold."),
            safetyInstructions = listOf("Stretch to gentle tension only — never force painful joints."),
            diagramType = PitchDiagramType.AGILITY_LADDER
        ),
        FootballExercise(
            id = "fit_sprints",
            name = "10m & 20m Flying Sprint Bursts",
            section = LessonSection.FITNESS,
            categoryKey = FitnessArea.SPRINTS.name,
            categoryLabel = FitnessArea.SPRINTS.displayName,
            description = "Develop explosive first-step separation to beat defenders.",
            instructions = listOf(
                "Start in a low 2-point sprinter stance behind the cone.",
                "Drive hard for 10m staying low for the first 4 strides, then walk back cleanly."
            ),
            durationMinutes = 10,
            timerWorkSeconds = 30,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "6 explosive sprints",
            restTimeSeconds = 40,
            trainingTips = listOf("Drive your elbows backward and knees high."),
            safetyInstructions = listOf("Allow full 40-second rest between reps."),
            diagramType = PitchDiagramType.AGILITY_LADDER
        ),
        FootballExercise(
            id = "fit_ladder",
            name = "Quick-Feet Cone & Ladder Footwork",
            section = LessonSection.FITNESS,
            categoryKey = FitnessArea.LADDER_AGILITY.name,
            categoryLabel = FitnessArea.LADDER_AGILITY.displayName,
            description = "Sharpen ground contact speed and balance for rapid 1v1 cuts.",
            instructions = listOf(
                "Run 2-feet-in-each-box forward, then lateral Icky Shuffle steps.",
                "Stay light on the balls of your feet with crisp arm swing."
            ),
            durationMinutes = 10,
            timerWorkSeconds = 45,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "5 sets x 45 sec",
            restTimeSeconds = 25,
            trainingTips = listOf("Light, quiet steps mean faster neural reaction time."),
            safetyInstructions = listOf("Keep eyes slightly ahead so you don't trip on markers."),
            diagramType = PitchDiagramType.AGILITY_LADDER
        ),
        FootballExercise(
            id = "fit_squats",
            name = "Jump Squats & Single-Leg Balance",
            section = LessonSection.FITNESS,
            categoryKey = FitnessArea.SQUATS.name,
            categoryLabel = FitnessArea.SQUATS.displayName,
            description = "Build leg power for stronger shots, shielding, and aerial duels.",
            instructions = listOf(
                "Perform 12 controlled bodyweight squats followed by 6 soft-landing jump squats.",
                "Finish with 20-second single-leg balance holds per leg."
            ),
            durationMinutes = 10,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "3 sets x 12 squats",
            restTimeSeconds = 35,
            trainingTips = listOf("Keep knees aligned over middle toes."),
            safetyInstructions = listOf("Land softly with bent knees to protect ankles and knees."),
            diagramType = PitchDiagramType.AGILITY_LADDER
        ),
        FootballExercise(
            id = "fit_lunges",
            name = "Multi-Directional Football Lunges",
            section = LessonSection.FITNESS,
            categoryKey = FitnessArea.LUNGES.name,
            categoryLabel = FitnessArea.LUNGES.displayName,
            description = "Strengthen deceleration muscles for sharp cuts and shooting plants.",
            instructions = listOf(
                "Step forward into a 90/90 lunge and drive sharply back to standing.",
                "Step sideways into a lateral lunge (10 reps per leg)."
            ),
            durationMinutes = 10,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "3 sets x 10 per leg",
            restTimeSeconds = 30,
            trainingTips = listOf("Control the lowering phase before driving upward."),
            safetyInstructions = listOf("Keep your front knee aligned cleanly over your ankle."),
            diagramType = PitchDiagramType.DEFENSE_SHADOW
        ),
        FootballExercise(
            id = "fit_planks",
            name = "Anti-Rotation Core & Plank Circuit",
            section = LessonSection.FITNESS,
            categoryKey = FitnessArea.CORE_PLANKS.name,
            categoryLabel = FitnessArea.CORE_PLANKS.displayName,
            description = "Stabilize your torso for physical duels and balanced finishing.",
            instructions = listOf(
                "Hold a forearm plank for 30 seconds, then side planks for 20 seconds per side.",
                "Finish with 12 slow dead-bug core extensions."
            ),
            durationMinutes = 8,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "3 core circuits",
            restTimeSeconds = 25,
            trainingTips = listOf("Breathe steadily with ribs tucked and hips level."),
            safetyInstructions = listOf("Stop and reset if your lower back begins to sag."),
            diagramType = PitchDiagramType.PITCH_DIMENSIONS
        ),
        FootballExercise(
            id = "fit_cooldown",
            name = "Post-Pitch Recovery & Cool-Down",
            section = LessonSection.FITNESS,
            categoryKey = FitnessArea.COOLDOWN.name,
            categoryLabel = FitnessArea.COOLDOWN.displayName,
            description = "Lower heart rate gradually and flush calf, quad, and hamstring tension.",
            instructions = listOf(
                "Walk slowly around the pitch for 90 seconds while breathing deeply.",
                "Hold standing quad, calf, and seated hamstring stretches for 30 seconds each side.",
                "Hydrate well after finishing your session."
            ),
            durationMinutes = 5,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "1 full cool-down",
            restTimeSeconds = 10,
            trainingTips = listOf("A proper cool-down reduces soreness before tomorrow's session."),
            safetyInstructions = listOf("Hold stretches smoothly without bouncing."),
            diagramType = PitchDiagramType.PITCH_DIMENSIONS
        )
    )
}
