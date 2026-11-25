package com.example.data.repository

import com.example.data.model.FitnessArea
import com.example.data.model.FootballExercise
import com.example.data.model.LessonSection
import com.example.data.model.PitchDiagramType
import com.example.data.model.SkillLevel
import com.example.data.model.TrainingCategory

object SkillAndFitnessCurriculum {

    val categoryExercises: List<FootballExercise> = listOf(
        // 1. DRIBBLING & BALL MASTERY (3)
        FootballExercise(
            id = "cat_drib_slalom",
            name = "10-Cone Tight Slalom Weave",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.DRIBBLING.name,
            categoryLabel = TrainingCategory.DRIBBLING.displayName,
            description = "Rapid inside-outside foot weaving through narrow cones without breaking stride.",
            instructions = listOf(
                "Set 10 cones in a line spaced 1 meter apart.",
                "Weave cleanly using inside and outside touches of your dominant foot, then weak foot.",
                "Sprint 5 meters beyond the final cone with controlled acceleration."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "6 slalom runs",
            restTimeSeconds = 25,
            trainingTips = listOf(
                "Keep your hips low and take one touch per step.",
                "Look slightly ahead of the ball, not straight down at your laces."
            ),
            diagramType = PitchDiagramType.SLALOM_DRIBBLING
        ),
        FootballExercise(
            id = "cat_drib_sole",
            name = "V-Pull, Roll & L-Turn Mastery",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.DRIBBLING.name,
            categoryLabel = TrainingCategory.DRIBBLING.displayName,
            description = "Master tight-space sole rolls, V-cuts, and L-turns behind the standing leg.",
            instructions = listOf(
                "Pull the ball back with your sole and push it diagonally forward with your instep (V-cut).",
                "Alternate left and right foot for 45 seconds nonstop.",
                "Finish with 10 behind-the-leg L-turns in a 2m x 2m box."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 60 sec",
            restTimeSeconds = 25,
            trainingTips = listOf(
                "Hop lightly on your standing foot as you pull with your sole.",
                "Protect the ball with your upper body every time you turn."
            ),
            diagramType = PitchDiagramType.SLALOM_DRIBBLING
        ),
        FootballExercise(
            id = "cat_drib_feint",
            name = "1v1 La Croqueta & Elastico Attack",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.DRIBBLING.name,
            categoryLabel = TrainingCategory.DRIBBLING.displayName,
            description = "Beat tight defenders using rapid double-touch glides and shoulder feints.",
            instructions = listOf(
                "Approach a cone defender squarely at medium speed.",
                "Shift the ball sideways from inside-right to inside-left in one crisp motion (La Croqueta).",
                "Accelerate past the cone and repeat with an outside-inside Elastico."
            ),
            durationMinutes = 18,
            timerWorkSeconds = 75,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "4 sets x 10 beats",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Keep the ball glued to your feet during the lateral shift.",
                "Explode out of the skill with 3 quick acceleration strides."
            ),
            diagramType = PitchDiagramType.SLALOM_DRIBBLING
        ),

        // 2. PASSING & FIRST TOUCH (3)
        FootballExercise(
            id = "cat_pass_wall",
            name = "Rapid 1-Touch Rebound Wall",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.PASSING.name,
            categoryLabel = TrainingCategory.PASSING.displayName,
            description = "Develop automatic rhythm and clean ankle lock with alternating 1-touch passes.",
            instructions = listOf(
                "Stand 4 meters from a flat wall and strike cleanly with the inside of your right foot.",
                "Return the rebound immediately with your left foot without trapping.",
                "Build up to 40 consecutive clean 1-touch passes without losing rhythm."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "5 sets x 60 sec",
            restTimeSeconds = 25,
            trainingTips = listOf(
                "Stay light on your toes between every strike.",
                "Strike the horizontal middle of the ball so rebounds stay smooth."
            ),
            diagramType = PitchDiagramType.PASSING_TRIANGLE
        ),
        FootballExercise(
            id = "cat_pass_cushion",
            name = "Aerial Drop & Soft Cushion Control",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.PASSING.name,
            categoryLabel = TrainingCategory.PASSING.displayName,
            description = "Kill high lofted balls dead on your instep, thigh, or chest into space.",
            instructions = listOf(
                "Toss the ball 4–5 meters high above your head.",
                "Meet the falling ball with a relaxed instep, withdrawing your foot downward on contact.",
                "Kill the bounce within 1 meter, then immediately play a crisp ground pass."
            ),
            durationMinutes = 16,
            timerWorkSeconds = 75,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 15 drops",
            restTimeSeconds = 25,
            trainingTips = listOf(
                "Think of catching a water balloon — soft ankle, zero tension.",
                "Direct your first touch away from imaginary pressure."
            ),
            diagramType = PitchDiagramType.PASSING_TRIANGLE
        ),
        FootballExercise(
            id = "cat_pass_through",
            name = "Disguised Reverse Through-Ball",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.PASSING.name,
            categoryLabel = TrainingCategory.PASSING.displayName,
            description = "Shape your hips for a wide pass, then slice a reverse ball between defender cones.",
            instructions = listOf(
                "Set two cones 2 meters apart to represent a center-back/full-back channel.",
                "Open your hips toward the touchline as if passing wide.",
                "Wrap your inside foot at the last moment to thread the ball cleanly between the cones."
            ),
            durationMinutes = 18,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "4 sets x 12 passes",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Look at the decoy runner first, then slice the pass into the central gap.",
                "Weight the pass so the striker runs onto it without slowing down."
            ),
            diagramType = PitchDiagramType.PASSING_TRIANGLE
        ),

        // 3. SHOOTING & FINISHING (3)
        FootballExercise(
            id = "cat_shoot_laces",
            name = "Power Instep Box Drive",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.SHOOTING.name,
            categoryLabel = TrainingCategory.SHOOTING.displayName,
            description = "Generate clean, unstoppable low power drives from 18 yards.",
            instructions = listOf(
                "Place the ball near the edge of the penalty arc.",
                "Take one angled touch forward, plant firmly beside the ball, and drive through with your laces.",
                "Aim for the lower corners of the goal with both left and right feet."
            ),
            durationMinutes = 18,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "4 sets x 8 shots",
            restTimeSeconds = 35,
            trainingTips = listOf(
                "Lock your ankle tight and strike with the top bone of your boot.",
                "Keep your knee over the ball at impact to prevent skying shots."
            ),
            diagramType = PitchDiagramType.FINISHING_ZONES
        ),
        FootballExercise(
            id = "cat_shoot_finesse",
            name = "Cut-Inside Whipped Finesse",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.SHOOTING.name,
            categoryLabel = TrainingCategory.SHOOTING.displayName,
            description = "Bend curled strikes around imaginary defenders into the far top corner.",
            instructions = listOf(
                "Dribble down the wide corner of the penalty box.",
                "Cut sharply inside onto your shooting foot with one positive touch.",
                "Wrap your inside foot around the side of the ball aiming outside the far post to curl in."
            ),
            durationMinutes = 18,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 8 curls",
            restTimeSeconds = 35,
            trainingTips = listOf(
                "Follow through across your body to maximize curl spin.",
                "Aim just outside the post and let the bend bring it inside the frame."
            ),
            diagramType = PitchDiagramType.FINISHING_ZONES
        ),
        FootballExercise(
            id = "cat_shoot_volley",
            name = "Self-Toss Half-Volley Clinical Finish",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.SHOOTING.name,
            categoryLabel = TrainingCategory.SHOOTING.displayName,
            description = "Time bouncing deliveries cleanly and strike low controlled volleys on target.",
            instructions = listOf(
                "Toss the ball diagonally into the penalty box and sprint onto the bounce.",
                "Keep your chest over the ball and strike right after the first bounce on the half-volley.",
                "Alternate left and right foot finishes."
            ),
            durationMinutes = 20,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "4 sets x 8 volleys",
            restTimeSeconds = 35,
            trainingTips = listOf(
                "Timing beats brute strength — focus on clean sweet-spot contact.",
                "Watch the ball all the way onto your boot before looking at the net."
            ),
            diagramType = PitchDiagramType.FINISHING_ZONES
        ),

        // 4. DEFENDING & 1V1 (3)
        FootballExercise(
            id = "cat_def_jockey",
            name = "Zig-Zag Shadow Jockey Shuffle",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.DEFENDING.name,
            categoryLabel = TrainingCategory.DEFENDING.displayName,
            description = "Build rapid hip-flip footwork while backpedaling against quick wingers.",
            instructions = listOf(
                "Set 4 cones in a zig-zag channel spanning 15 meters.",
                "Backpedal side-on from cone 1 to 2, drop-step flip your hips, and shuffle to cone 3.",
                "Sprint forward 5 meters at the final cone to simulate winning a loose ball."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "5 sets x 60 sec",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Keep your center of gravity low and heels off the turf.",
                "Never cross your feet during lateral transitions."
            ),
            diagramType = PitchDiagramType.DEFENSE_SHADOW
        ),
        FootballExercise(
            id = "cat_def_recovery",
            name = "Goal-Side Recovery & Interception",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.DEFENDING.name,
            categoryLabel = TrainingCategory.DEFENDING.displayName,
            description = "Sprint diagonally to recover goal-side position before stepping into the lane.",
            instructions = listOf(
                "Start wide at the halfway line and sprint diagonally toward the penalty spot.",
                "Brake smoothly into a low jockey stance without over-running the ball carrier.",
                "Step forward explosively to intercept or poke-tackle on cue."
            ),
            durationMinutes = 16,
            timerWorkSeconds = 75,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "6 recovery sprints",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Always take the shortest diagonal line back toward your own goal.",
                "Check behind you for late trailing midfield runs."
            ),
            diagramType = PitchDiagramType.DEFENSE_SHADOW
        ),
        FootballExercise(
            id = "cat_def_aerial",
            name = "Box Clearance & Weak-Foot Exit Pass",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.DEFENDING.name,
            categoryLabel = TrainingCategory.DEFENDING.displayName,
            description = "Win defensive clearances under pressure and immediately launch a calm outlet pass.",
            instructions = listOf(
                "Toss a high ball toward the 6-yard line, attack it at peak height.",
                "Land in balance, take one sharp defensive touch away from pressure.",
                "Play an accurate 15-meter outlet pass into wide channel cones."
            ),
            durationMinutes = 18,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "4 sets x 8 clearances",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Clear high and wide away from the central penalty arc.",
                "Defending only finishes when your team secures the next pass."
            ),
            diagramType = PitchDiagramType.DEFENSE_SHADOW
        ),

        // 5. SPEED & AGILITY (3)
        FootballExercise(
            id = "cat_spd_tshuttle",
            name = "T-Cone Explosive Direction Change",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.SPEED_AGILITY.name,
            categoryLabel = TrainingCategory.SPEED_AGILITY.displayName,
            description = "Accelerate, brake cleanly, shuffle laterally, and sprint out of sharp turns.",
            instructions = listOf(
                "Place 4 cones in a T-shape (10m stem, 5m left and right wings).",
                "Sprint 10m forward, side-shuffle left 5m, side-shuffle right 10m, back to center, and backpedal home.",
                "Rest fully between reps so every cut stays explosive."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 45,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "6 all-out T-runs",
            restTimeSeconds = 40,
            trainingTips = listOf(
                "Drop your hips before every plant to absorb braking force safely.",
                "Drive your arms violently on the forward acceleration."
            ),
            diagramType = PitchDiagramType.AGILITY_LADDER
        ),
        FootballExercise(
            id = "cat_spd_5105",
            name = "5-10-5 Pro Match Shuttle",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.SPEED_AGILITY.name,
            categoryLabel = TrainingCategory.SPEED_AGILITY.displayName,
            description = "Simulate high-speed pressing transitions and 180-degree sprints.",
            instructions = listOf(
                "Start straddling the center cone.",
                "Sprint 5m right and touch the cone, sprint 10m left and touch, sprint 5m back through center.",
                "Push off your outside foot aggressively on both 180-degree turns."
            ),
            durationMinutes = 16,
            timerWorkSeconds = 45,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "6 shuttles",
            restTimeSeconds = 45,
            trainingTips = listOf(
                "Stay low through each turn — tall posture wastes valuable tenths of a second.",
                "Alternate starting left and right every rep."
            ),
            diagramType = PitchDiagramType.AGILITY_LADDER
        ),
        FootballExercise(
            id = "cat_spd_ballburst",
            name = "15m Sprint + Ball Carry Deceleration",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.SPEED_AGILITY.name,
            categoryLabel = TrainingCategory.SPEED_AGILITY.displayName,
            description = "Transition seamlessly from top sprint speed straight into composed ball control.",
            instructions = listOf(
                "Sprint 15m without the ball to meet a stationary ball at a cone.",
                "Decelerate in 2 quick steps, cushion a controlled outside-foot touch around the cone.",
                "Finish with a composed low pass into a target gate."
            ),
            durationMinutes = 18,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "6 sprint-to-control reps",
            restTimeSeconds = 40,
            trainingTips = listOf(
                "Shorten your final 3 steps before the ball so your touch stays soft.",
                "Match-winning wingers master braking as well as accelerating."
            ),
            diagramType = PitchDiagramType.AGILITY_LADDER
        ),

        // 6. STAMINA & ENDURANCE (3)
        FootballExercise(
            id = "cat_end_shuttle",
            name = "Box-to-Box 20/20 Interval Shuttle",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.ENDURANCE.name,
            categoryLabel = TrainingCategory.ENDURANCE.displayName,
            description = "Build 90-minute match stamina with repeated high-intensity runs and active jogging.",
            instructions = listOf(
                "Mark a 40-meter pitch channel between two cones.",
                "Run hard at 85% effort for 20 seconds, then jog lightly for 20 seconds.",
                "Repeat continuously for 8 cycles without stopping."
            ),
            durationMinutes = 20,
            timerWorkSeconds = 120,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "3 blocks x 8 intervals",
            restTimeSeconds = 60,
            trainingTips = listOf(
                "Focus on smooth nasal-to-mouth breathing during the 20-second recovery jog.",
                "Keep consistent lap speed from rep 1 through rep 8."
            ),
            diagramType = PitchDiagramType.AGILITY_LADDER
        ),
        FootballExercise(
            id = "cat_end_dribble",
            name = "Continuous 3-Minute Ball-Control Loop",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.ENDURANCE.name,
            categoryLabel = TrainingCategory.ENDURANCE.displayName,
            description = "Train technical precision while tired using non-stop dribbling and turns.",
            instructions = listOf(
                "Set a 20m x 15m rectangle with cones.",
                "Dribble continuously around the loop mixing inside cuts, sole rolls, and pace surges.",
                "Maintain tight control even as heart rate climbs in minute 3."
            ),
            durationMinutes = 18,
            timerWorkSeconds = 180,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "3 sets x 3 min",
            restTimeSeconds = 45,
            trainingTips = listOf(
                "Fatigue tests your technique — keep touches short and frequent.",
                "Scan upward every 4 seconds like in late-game situations."
            ),
            diagramType = PitchDiagramType.SLALOM_DRIBBLING
        ),
        FootballExercise(
            id = "cat_end_yoyo",
            name = "Match-Tempo Shuttle + Finish Under Fatigue",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.ENDURANCE.name,
            categoryLabel = TrainingCategory.ENDURANCE.displayName,
            description = "Execute sharp passes and shots immediately after high-heart-rate shuttle runs.",
            instructions = listOf(
                "Complete 4 x 20-meter shuttles at high tempo.",
                "Collect a ball at the top of the box and immediately strike on target.",
                "Walk back calmly for 40 seconds before starting the next round."
            ),
            durationMinutes = 22,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "5 fatigue-finish rounds",
            restTimeSeconds = 45,
            trainingTips = listOf(
                "Exhale right before striking to steady your core and balance.",
                "Late goals in the 85th minute come from calm technique under fatigue."
            ),
            diagramType = PitchDiagramType.FINISHING_ZONES
        ),

        // 7. SET PIECES & CROSSING (3)
        FootballExercise(
            id = "cat_set_freekick",
            name = "Curled Free Kick Over the Wall",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.SET_PIECES.name,
            categoryLabel = TrainingCategory.SET_PIECES.displayName,
            description = "Master whip, dip, and target placement from 20–24 yards.",
            instructions = listOf(
                "Place cones 9.15m ahead as an imaginary 3-player defensive wall.",
                "Stand 4 steps back and 2 steps wide of the ball.",
                "Strike across the lower inside quadrant with rapid follow-through for dip and spin."
            ),
            durationMinutes = 18,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 6 free kicks",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Lean slightly away from the ball as you swing to create natural upward lift.",
                "Follow through upward and across rather than leaning backward."
            ),
            diagramType = PitchDiagramType.PENALTY_SET_PIECE
        ),
        FootballExercise(
            id = "cat_set_penalty",
            name = "High-Composure Penalty Routine",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.SET_PIECES.name,
            categoryLabel = TrainingCategory.SET_PIECES.displayName,
            description = "Build a repeatable breathing, run-up, and corner-placement penalty habit.",
            instructions = listOf(
                "Place the ball on the 11m penalty spot and step back smoothly.",
                "Take one deep calming breath, lock eyes on your chosen corner, and walk up cleanly.",
                "Strike firmly inside the side netting using either inside-foot precision or laces drive."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 75,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "3 sets x 6 penalties",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Never change your corner choice mid-run-up.",
                "Firm side-foot strikes into the side netting beat soft center shots."
            ),
            diagramType = PitchDiagramType.PENALTY_SET_PIECE
        ),
        FootballExercise(
            id = "cat_set_corner",
            name = "Inswinging & Outswinging Corner Delivery",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.SET_PIECES.name,
            categoryLabel = TrainingCategory.SET_PIECES.displayName,
            description = "Drop consistent corners onto the 6-yard edge and penalty spot target zones.",
            instructions = listOf(
                "Place 2 target cones inside the box: one at the near post, one at the penalty spot.",
                "Deliver whipped corners aiming to clear the first defender cleanly.",
                "Track how many out of 10 land inside the danger corridor."
            ),
            durationMinutes = 18,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "3 sets x 10 corners",
            restTimeSeconds = 35,
            trainingTips = listOf(
                "Drive across the lower side of the ball for pace — avoid slow, floaty balls.",
                "Check the wind before deciding how wide to aim."
            ),
            diagramType = PitchDiagramType.PENALTY_SET_PIECE
        ),

        // 8. REACTION & SCANNING (3)
        FootballExercise(
            id = "cat_react_scan",
            name = "Shoulder-Scan Color Cue Turn",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.REACTION_VISION.name,
            categoryLabel = TrainingCategory.REACTION_VISION.displayName,
            description = "Train automatic pre-touch scanning and instant directional turns.",
            instructions = listOf(
                "Place two different colored cones 4 meters behind you.",
                "Pass against a wall, scan behind before the rebound arrives, and choose a color mentally.",
                "Take touch #1 smoothly into the chosen color gate without stopping."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "4 sets x 14 scans",
            restTimeSeconds = 25,
            trainingTips = listOf(
                "Keep your head level on the swivel so you maintain depth perception.",
                "Scan while the ball is in transit — never while it's touching your foot."
            ),
            diagramType = PitchDiagramType.PASSING_TRIANGLE
        ),
        FootballExercise(
            id = "cat_react_bounce",
            name = "Quick-Reflex Wall Volley Reactions",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.REACTION_VISION.name,
            categoryLabel = TrainingCategory.REACTION_VISION.displayName,
            description = "Sharpen ankle reflexes and split-step readiness against unpredictable rebounds.",
            instructions = listOf(
                "Stand 2.5 meters from a rebound surface.",
                "Strike firm half-volleys alternating feet as fast as possible.",
                "Adjust immediately to awkward bounces with chest, thigh, or outside-foot saves."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "5 sets x 45 sec",
            restTimeSeconds = 25,
            trainingTips = listOf(
                "Keep knees bent and arms ready for rapid balance shifts.",
                "Quick little adjustment steps beat big lunges."
            ),
            diagramType = PitchDiagramType.GOALKEEPER_ANGLES
        ),
        FootballExercise(
            id = "cat_react_sprint",
            name = "Whistle-Cue Transition Burst",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.REACTION_VISION.name,
            categoryLabel = TrainingCategory.REACTION_VISION.displayName,
            description = "React from slow ball mastery to a 10m counter-attack sprint on audio cue.",
            instructions = listOf(
                "Perform slow toe-taps in a central 2m square.",
                "When the timer cue sounds or every 10 seconds, push the ball 4m ahead and sprint 10m.",
                "Cut sharply at the end cone and jog back smoothly."
            ),
            durationMinutes = 18,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "6 transition bursts",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "React with your hips first — lean forward into the sprint immediately.",
                "Big first touch into open space lets you hit top speed faster."
            ),
            diagramType = PitchDiagramType.AGILITY_LADDER
        ),

        // 9. MATCH TACTICS (3)
        FootballExercise(
            id = "cat_tact_433",
            name = "4-3-3 Half-Space Pocket Movement",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.MATCH_TACTICS.name,
            categoryLabel = TrainingCategory.MATCH_TACTICS.displayName,
            description = "Find dangerous pockets between center-backs and full-backs to receive forward passes.",
            instructions = listOf(
                "Mark a wide wing zone and an inside half-space channel with cones.",
                "Start wide to stretch the defender, then dart diagonally inside into the half-space pocket.",
                "Receive on the half-turn and play forward toward goal."
            ),
            durationMinutes = 18,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 8 pocket runs",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Wait until your teammate has their head up before making the inside dart.",
                "Occupy spaces between two defenders so neither knows who should mark you."
            ),
            diagramType = PitchDiagramType.FORMATION_433
        ),
        FootballExercise(
            id = "cat_tact_width",
            name = "3rd-Man Support & Give-and-Go",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.MATCH_TACTICS.name,
            categoryLabel = TrainingCategory.MATCH_TACTICS.displayName,
            description = "Unlock compact defenses using rapid wall-pass combinations and forward support runs.",
            instructions = listOf(
                "Play a firm pass into a target wall or cone triangle.",
                "Sprint forward past the line immediately as the ball leaves your foot.",
                "Finish the return pass cleanly into a corner mini-goal zone."
            ),
            durationMinutes = 18,
            timerWorkSeconds = 75,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "4 sets x 10 combos",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Pass and MOVE — never admire your own pass.",
                "Angle your run away from the defender's field of vision."
            ),
            diagramType = PitchDiagramType.PASSING_TRIANGLE
        ),
        FootballExercise(
            id = "cat_tact_transition",
            name = "5-Second Counter-Press Transition",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.MATCH_TACTICS.name,
            categoryLabel = TrainingCategory.MATCH_TACTICS.displayName,
            description = "Simulate instant defensive pressure immediately after losing possession in attack.",
            instructions = listOf(
                "Execute a controlled strike or pass inside the attacking third.",
                "Immediately trigger a 5-second high-intensity press sprint toward two side cones.",
                "Drop smoothly into compact midfield shape if the 5-second press is bypassed."
            ),
            durationMinutes = 20,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "6 transition cycles",
            restTimeSeconds = 35,
            trainingTips = listOf(
                "The first 5 seconds after losing the ball are the easiest time to win it back.",
                "Curve your press run to block the opponent's central outlet."
            ),
            diagramType = PitchDiagramType.FORMATION_433
        )
    )

    val fitnessExercises: List<FootballExercise> = listOf(
        FootballExercise(
            id = "fit_warmup",
            name = "FIFA-Style Dynamic Pitch Warm-Up",
            section = LessonSection.FITNESS,
            categoryKey = FitnessArea.WARMUP.name,
            categoryLabel = FitnessArea.WARMUP.displayName,
            description = "Activate hips, hamstrings, ankles, and nervous system before touching the ball.",
            instructions = listOf(
                "Light forward and backward jog for 90 seconds.",
                "Perform 10 open-the-gate and 10 close-the-gate hip circles.",
                "Complete 2 progressive 15m build-up runs at 60% and 75% speed."
            ),
            durationMinutes = 8,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "1 complete warm-up flow",
            restTimeSeconds = 15,
            trainingTips = listOf("Warm muscles produce faster sprints and prevent hamstring pulls."),
            safetyInstructions = listOf(
                "Never start explosive sprinting or hard shooting cold.",
                "Increase range of motion gradually on every repetition."
            ),
            diagramType = PitchDiagramType.AGILITY_LADDER
        ),
        FootballExercise(
            id = "fit_stretch",
            name = "Groin, Hip Flexor & Hamstring Mobility",
            section = LessonSection.FITNESS,
            categoryKey = FitnessArea.STRETCHING.name,
            categoryLabel = FitnessArea.STRETCHING.displayName,
            description = "Protect kicking muscles and improve stride length for long passes and strikes.",
            instructions = listOf(
                "Perform World's Greatest Stretch lunges with thoracic rotation (5 each side).",
                "Complete dynamic leg swings front-to-back and side-to-side (12 per leg).",
                "Finish with 30-second gentle seated butterfly adductor holds."
            ),
            durationMinutes = 8,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "2 controlled rounds",
            restTimeSeconds = 15,
            trainingTips = listOf("Exhale slowly as you sink gently into each hip opener."),
            safetyInstructions = listOf(
                "Stretch to mild tension only — never bounce aggressively or force painful joints."
            ),
            diagramType = PitchDiagramType.AGILITY_LADDER
        ),
        FootballExercise(
            id = "fit_sprints",
            name = "10m & 20m Flying Sprint Bursts",
            section = LessonSection.FITNESS,
            categoryKey = FitnessArea.SPRINTS.name,
            categoryLabel = FitnessArea.SPRINTS.displayName,
            description = "Develop explosive first-step separation to beat full-backs and chase through-balls.",
            instructions = listOf(
                "Start in a low 2-point sprinter stance behind the baseline cone.",
                "Drive hard for 10 meters staying low for the first 4 steps.",
                "Walk back slowly for full recovery between reps."
            ),
            durationMinutes = 14,
            timerWorkSeconds = 30,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "6 explosive sprints",
            restTimeSeconds = 45,
            trainingTips = listOf("Drive your elbows backward and knees high through the first 5 strides."),
            safetyInstructions = listOf(
                "Allow full 45-second rest between sprints so technique never breaks down.",
                "Stop immediately if you feel sharp hamstring tightness."
            ),
            diagramType = PitchDiagramType.AGILITY_LADDER
        ),
        FootballExercise(
            id = "fit_ladder",
            name = "Quick-Feet Cone & Ladder Footwork",
            section = LessonSection.FITNESS,
            categoryKey = FitnessArea.LADDER_AGILITY.name,
            categoryLabel = FitnessArea.LADDER_AGILITY.displayName,
            description = "Sharpen ground contact speed and balance for rapid 1v1 changes of direction.",
            instructions = listOf(
                "Place 6 flat cones or ladder rungs 45cm apart.",
                "Run 2-feet-in-each-box forward, then lateral Icky Shuffle steps.",
                "Stay light on the balls of your feet with quick arm pumping."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 45,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "5 sets x 45 sec",
            restTimeSeconds = 25,
            trainingTips = listOf("Light, quiet steps mean faster neural reaction time."),
            safetyInstructions = listOf(
                "Keep eyes forward once you learn the pattern so you don't trip on markers."
            ),
            diagramType = PitchDiagramType.AGILITY_LADDER
        ),
        FootballExercise(
            id = "fit_squats",
            name = "Explosive Jump Squats & Single-Leg Balance",
            section = LessonSection.FITNESS,
            categoryKey = FitnessArea.SQUATS.name,
            categoryLabel = FitnessArea.SQUATS.displayName,
            description = "Build quad and glute power for stronger shots, shielding, and aerial duels.",
            instructions = listOf(
                "Perform 12 controlled bodyweight squats with chest proud and weight through mid-foot.",
                "Add 6 soft-landing tuck/jump squats focusing on silent landings.",
                "Finish with 20-second single-leg balance holds per leg."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "3 sets x 12 squats",
            restTimeSeconds = 35,
            trainingTips = listOf("Keep knees aligned over your middle toes — never let them cave inward."),
            safetyInstructions = listOf(
                "Land softly with bent knees to protect ankles and knee ligaments."
            ),
            diagramType = PitchDiagramType.AGILITY_LADDER
        ),
        FootballExercise(
            id = "fit_lunges",
            name = "Multi-Directional Football Lunges",
            section = LessonSection.FITNESS,
            categoryKey = FitnessArea.LUNGES.name,
            categoryLabel = FitnessArea.LUNGES.displayName,
            description = "Strengthen deceleration muscles for sharp cuts, shooting plants, and tackles.",
            instructions = listOf(
                "Step forward into a 90/90 lunge and drive sharply back to standing.",
                "Step sideways into a lateral groin lunge to protect cutting muscles.",
                "Complete 10 reps per leg with steady upright posture."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "3 sets x 10 per leg",
            restTimeSeconds = 30,
            trainingTips = listOf("Control the lowering phase for 2 seconds before driving back up."),
            safetyInstructions = listOf(
                "Keep your front knee behind or directly above your toes without twisting."
            ),
            diagramType = PitchDiagramType.DEFENSE_SHADOW
        ),
        FootballExercise(
            id = "fit_planks",
            name = "Anti-Rotation Core & Plank Circuit",
            section = LessonSection.FITNESS,
            categoryKey = FitnessArea.CORE_PLANKS.name,
            categoryLabel = FitnessArea.CORE_PLANKS.displayName,
            description = "Stabilize your torso for shoulder-to-shoulder duels and clean shooting balance.",
            instructions = listOf(
                "Hold a straight-line forearm plank with glutes and abs braced for 30 seconds.",
                "Transition to side planks for 20 seconds per side.",
                "Finish with 12 slow dead-bug core extensions."
            ),
            durationMinutes = 10,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "3 core circuits",
            restTimeSeconds = 25,
            trainingTips = listOf("Breathe steadily while keeping ribs tucked and hips level."),
            safetyInstructions = listOf(
                "Stop and reset if your lower back begins to sag toward the ground."
            ),
            diagramType = PitchDiagramType.PITCH_DIMENSIONS
        ),
        FootballExercise(
            id = "fit_cooldown",
            name = "Post-Pitch Recovery & Cool-Down",
            section = LessonSection.FITNESS,
            categoryKey = FitnessArea.COOLDOWN.name,
            categoryLabel = FitnessArea.COOLDOWN.displayName,
            description = "Lower heart rate gradually and flush calf, quad, and hamstring tension after play.",
            instructions = listOf(
                "Walk slowly around the pitch perimeter for 2 minutes while breathing deeply.",
                "Hold standing quad, calf, and seated hamstring stretches for 30 seconds per side.",
                "Hydrate thoroughly and review today's logged session notes."
            ),
            durationMinutes = 8,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "1 full cool-down",
            restTimeSeconds = 10,
            trainingTips = listOf("A consistent cool-down reduces next-day muscle soreness dramatically."),
            safetyInstructions = listOf(
                "Hold static stretches gently without bouncing while muscles are still warm."
            ),
            diagramType = PitchDiagramType.PITCH_DIMENSIONS
        )
    )
}
