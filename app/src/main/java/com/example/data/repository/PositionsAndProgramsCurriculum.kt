package com.example.data.repository

import com.example.data.model.DifficultyLevel
import com.example.data.model.DrillDiagramType
import com.example.data.model.LessonGroup
import com.example.data.model.PlayerPosition
import com.example.data.model.ProgramDayPlan
import com.example.data.model.TrainingLesson
import com.example.data.model.TrainingProgram

object PositionsAndProgramsCurriculum {

    val positionLessons: List<TrainingLesson> = listOf(
        // GOALKEEPER
        TrainingLesson(
            id = "pos_gk_1",
            title = "GK: Set-Position Hop & Near-Post Angle",
            group = LessonGroup.POSITION,
            categoryKey = PlayerPosition.GOALKEEPER.name,
            categoryLabel = "Goalkeeper",
            description = "Synchronize your tiny split-step hop with striker contact while sealing the near post.",
            instructions = listOf(
                "Start near the center of goal; shuffle toward the angled cone as a pass goes wide.",
                "Pause squarely on your arc just before the imaginary shot, weight balanced on balls of feet.",
                "Collapse quickly onto low near-post angles and secure cleanly."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "4 sets × 8 angle shifts + dives",
            durationMinutes = 20,
            equipment = "2 footballs, goal, 3 cones",
            coachingTips = listOf("Stay upright until shot intent is clear; avoid guessing early."),
            diagramType = DrillDiagramType.GOALKEEPER_ARCS
        ),
        TrainingLesson(
            id = "pos_gk_2",
            title = "GK: High Claim & Sweeper Clearance",
            group = LessonGroup.POSITION,
            categoryKey = PlayerPosition.GOALKEEPER.name,
            categoryLabel = "Goalkeeper",
            description = "Command aerial crosses at the highest point and sweep through-balls behind high lines.",
            instructions = listOf(
                "Toss the ball high above penalty box height, sprint 4 steps, jump off one leg raising opposite knee for protection.",
                "Catch cleanly at max arm extension and yell 'KEEPER!' decisively.",
                "Reset and sprint 15m out of the box to clear an imaginary through ball first-time."
            ),
            difficulty = DifficultyLevel.ADVANCED,
            recommendedReps = "3 sets × 8 claims + 6 sweeper clearances",
            durationMinutes = 20,
            equipment = "2 footballs, cones",
            coachingTips = listOf("Meet the ball at its peak before attackers can challenge."),
            diagramType = DrillDiagramType.GOALKEEPER_ARCS
        ),

        // CENTER BACK
        TrainingLesson(
            id = "pos_cb_1",
            title = "CB: Aerial Clearance & Line Step-Up",
            group = LessonGroup.POSITION,
            categoryKey = PlayerPosition.CENTER_BACK.name,
            categoryLabel = "Center Back",
            description = "Win central headers cleanly with distance and squeeze space upward right after clearing.",
            instructions = listOf(
                "Toss ball up, attack from 3 steps back, leap powerfully to direct a firm forehead clearance wide.",
                "Immediately sprint 6 meters forward calling 'LINE UP!' to squeeze the pitch."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "4 sets × 8 clearances + line squeezes",
            durationMinutes = 18,
            equipment = "1 football, 4 cones",
            coachingTips = listOf("Clear high and wide toward the touchline, never back down the middle."),
            diagramType = DrillDiagramType.DEFENSIVE_CHANNEL
        ),
        TrainingLesson(
            id = "pos_cb_2",
            title = "CB: Diagonal Line-Breaking Build-Up Pass",
            group = LessonGroup.POSITION,
            categoryKey = PlayerPosition.CENTER_BACK.name,
            categoryLabel = "Center Back",
            description = "Beat the first press by disguising eyes wide and firing crisp passes into midfield pockets.",
            instructions = listOf(
                "Dribble forward 3 meters toward a pressing cone to invite pressure.",
                "Show hips wide toward the full-back gate, then snap a disguised diagonal pass through the central gate."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "4 sets × 10 passes per foot",
            durationMinutes = 18,
            equipment = "2 footballs, 6 cones",
            coachingTips = listOf("Drive through the ball firmly so midfielders get extra time to turn."),
            diagramType = DrillDiagramType.TRIANGLE_PASSING
        ),

        // FULL BACK
        TrainingLesson(
            id = "pos_fb_1",
            title = "FB: Overlap Sprint & Whipped First-Time Cross",
            group = LessonGroup.POSITION,
            categoryKey = PlayerPosition.FULL_BACK.name,
            categoryLabel = "Full Back",
            description = "Time lung-busting overlap sprints down the touchline and deliver dangerous early crosses.",
            instructions = listOf(
                "Play a firm pass inside to an imaginary winger, immediately sprint 25m around the wide cone.",
                "Take one clean forward touch along the touchline and whip a driven low cross into the 6-yard corridor."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "8 overlapping runs + crosses (rest 45s)",
            durationMinutes = 20,
            equipment = "3 footballs, 4 cones",
            coachingTips = listOf("Scan the box quickly before your final stride so you pick out runners vs cut-back."),
            diagramType = DrillDiagramType.SPRINT_LANES
        ),
        TrainingLesson(
            id = "pos_fb_2",
            title = "FB: Touchline 1v1 Wing Isolation",
            group = LessonGroup.POSITION,
            categoryKey = PlayerPosition.FULL_BACK.name,
            categoryLabel = "Full Back",
            description = "Shut down tricky wingers by blocking the inside lane and forcing them onto the outside line.",
            instructions = listOf(
                "Close down a wide cone swiftly, brake low at 1.5m distance angled inside-to-out.",
                "Shuffle backward 6m blocking the cut-inside lane, then burst 10m to block the line cross."
            ),
            difficulty = DifficultyLevel.ADVANCED,
            recommendedReps = "6 × 1v1 shadow lockdowns per side",
            durationMinutes = 18,
            equipment = "4 cones",
            coachingTips = listOf("Never let a winger cut cleanly inside onto their strong shooting foot."),
            diagramType = DrillDiagramType.DEFENSIVE_CHANNEL
        ),

        // DEFENSIVE MIDFIELDER
        TrainingLesson(
            id = "pos_dm_1",
            title = "CDM: 360° Scan & Half-Turn Pivot Escape",
            group = LessonGroup.POSITION,
            categoryKey = PlayerPosition.DEFENSIVE_MID.name,
            categoryLabel = "Defensive Midfielder",
            description = "Receive under pressure from center-backs and escape presses cleanly with one smooth half-turn.",
            instructions = listOf(
                "Play pass against rebound wall, scan left & right over shoulders before ball arrives.",
                "Let the ball run across your back foot into open space past the central marker cone.",
                "Switch play crisply into a wide gate."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "4 sets × 12 press escapes",
            durationMinutes = 20,
            equipment = "1 football, wall, 5 cones",
            coachingTips = listOf("Your head check 1 second BEFORE receiving gives you 2 extra seconds on the ball."),
            diagramType = DrillDiagramType.TRIANGLE_PASSING
        ),
        TrainingLesson(
            id = "pos_dm_2",
            title = "CDM: Screen the 10-Pocket & Counter-Press",
            group = LessonGroup.POSITION,
            categoryKey = PlayerPosition.DEFENSIVE_MID.name,
            categoryLabel = "Defensive Midfielder",
            description = "Break up central counter-attacks by shifting across Zone 14 and winning second balls.",
            instructions = listOf(
                "Shuffle across a 12m central corridor blocking passing lanes to 2 attacker cones.",
                "On cue, sprint forward 5m to press an imaginary turnover and play a 1-touch forward pass."
            ),
            difficulty = DifficultyLevel.ADVANCED,
            recommendedReps = "6 × 40-second high-focus rounds",
            durationMinutes = 18,
            equipment = "1 football, 6 cones",
            coachingTips = listOf("Stay compact with your center-backs—never chase wildly out of the center."),
            diagramType = DrillDiagramType.DEFENSIVE_CHANNEL
        ),

        // CENTRAL MIDFIELDER
        TrainingLesson(
            id = "pos_cm_1",
            title = "CM: Box-to-Box Link Play & Late Box Arrival",
            group = LessonGroup.POSITION,
            categoryKey = PlayerPosition.CENTRAL_MID.name,
            categoryLabel = "Central Midfielder",
            description = "Dictate tempo from deep, combine quickly, and arrive late at the penalty arc to finish.",
            instructions = listOf(
                "Play a 1-2 combination off the wall near halfway, sprint 20m forward toward the penalty arc.",
                "Receive a laid-off ball smoothly and strike a placed low finish on your second touch."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "4 sets × 6 box-to-box finishing runs",
            durationMinutes = 22,
            equipment = "3 footballs, wall/cones, goal",
            coachingTips = listOf("Time your run so you strike the ball at pace without breaking stride."),
            diagramType = DrillDiagramType.SHOOTING_ARCS
        ),
        TrainingLesson(
            id = "pos_cm_2",
            title = "CM: Rondo Rapid 1-2 Touch Tempo",
            group = LessonGroup.POSITION,
            categoryKey = PlayerPosition.CENTRAL_MID.name,
            categoryLabel = "Central Midfielder",
            description = "Keep possession tight under swarm pressure using open body angles and disguised passes.",
            instructions = listOf(
                "Inside a 3x3m wall corner, alternate rapid 1-touch passes off left and right walls.",
                "Add a sole roll disguise before every 4th pass."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "5 × 60-second tempo blocks",
            durationMinutes = 18,
            equipment = "1 football, corner wall",
            coachingTips = listOf("Soften touch velocity only when resetting; zip passes forward."),
            diagramType = DrillDiagramType.TRIANGLE_PASSING
        ),

        // ATTACKING MIDFIELDER
        TrainingLesson(
            id = "pos_am_1",
            title = "CAM: Half-Space Pocket Turn & Through Ball",
            group = LessonGroup.POSITION,
            categoryKey = PlayerPosition.ATTACKING_MID.name,
            categoryLabel = "Attacking Midfielder",
            description = "Receive between defensive lines and thread perfectly weighted reverse passes behind full-backs.",
            instructions = listOf(
                "Check into the pocket between 2 defender cones.",
                "Receive on the half-turn with your far foot, take 1 quick touch forward, and slide a reverse pass through a 1.5m gate."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "4 sets × 10 turns + weighted passes",
            durationMinutes = 20,
            equipment = "2 footballs, 6 cones",
            coachingTips = listOf("Weight the through ball so the striker runs onto it without slowing down."),
            diagramType = DrillDiagramType.TRIANGLE_PASSING
        ),
        TrainingLesson(
            id = "pos_am_2",
            title = "CAM: Edge-of-Box Quick-Release Curled Shot",
            group = LessonGroup.POSITION,
            categoryKey = PlayerPosition.ATTACKING_MID.name,
            categoryLabel = "Attacking Midfielder",
            description = "Shift the ball half a yard past a closing center-back and whip it into the top or bottom corner.",
            instructions = listOf(
                "Dribble toward a central cone 18m from goal.",
                "Use a quick sole-roll or La Croqueta shift half a yard laterally and strike immediately around the cone into the far corner."
            ),
            difficulty = DifficultyLevel.ADVANCED,
            recommendedReps = "3 sets × 8 shots per side",
            durationMinutes = 20,
            equipment = "3 footballs, 2 cones, goal",
            coachingTips = listOf("Shoot through the space you just created before the defender recovers."),
            diagramType = DrillDiagramType.SHOOTING_ARCS
        ),

        // WINGER
        TrainingLesson(
            id = "pos_wg_1",
            title = "Winger: Touchline Isolation & Cut-Inside Strike",
            group = LessonGroup.POSITION,
            categoryKey = PlayerPosition.WINGER.name,
            categoryLabel = "Winger",
            description = "Beat your full-back 1v1 out wide and bend shots cleanly into the far side-netting.",
            instructions = listOf(
                "Receive wide near the touchline, attack the defender cone with speed.",
                "Perform a quick step-over fake outside, chop inside at pace, and curl toward the far post."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "10 cuts from left wing + 10 from right wing",
            durationMinutes = 20,
            equipment = "3 footballs, 3 cones, goal",
            coachingTips = listOf("Change pace violently right after the feint—slow to fast beats defenders."),
            diagramType = DrillDiagramType.SHOOTING_ARCS
        ),
        TrainingLesson(
            id = "pos_wg_2",
            title = "Winger: Byline Drive & Pull-Back Assist",
            group = LessonGroup.POSITION,
            categoryKey = PlayerPosition.WINGER.name,
            categoryLabel = "Winger",
            description = "Attack the byline outside the defender and pick out late runners at the penalty spot.",
            instructions = listOf(
                "Beat the cone on the outside with a sharp outside-foot burst.",
                "Lift your head near the byline and fizz a controlled ground cut-back pass onto the penalty spot."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "12 byline drives + cut-backs",
            durationMinutes = 18,
            equipment = "3 footballs, 4 cones",
            coachingTips = listOf("Pass backward at a 45-degree angle rather than blindly crossing into the keeper."),
            diagramType = DrillDiagramType.SPRINT_LANES
        ),

        // STRIKER
        TrainingLesson(
            id = "pos_st_1",
            title = "ST: Blindside Curve Run & 1-Touch Finish",
            group = LessonGroup.POSITION,
            categoryKey = PlayerPosition.STRIKER.name,
            categoryLabel = "Striker",
            description = "Peel off the center-back's blind shoulder and bury low one-touch finishes cleanly.",
            instructions = listOf(
                "Start between two defender cones, make a sharp dummy step toward the ball then dart diagonally behind.",
                "Meet a moving ball cleanly with a composed one-touch finish low to either corner."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "4 sets × 6 runs + finishes",
            durationMinutes = 22,
            equipment = "3 footballs, 3 cones, goal",
            coachingTips = listOf("Angle your run across the defender's back shoulder so you stay onside."),
            diagramType = DrillDiagramType.SHOOTING_ARCS
        ),
        TrainingLesson(
            id = "pos_st_2",
            title = "ST: Hold-Up Shield, Lay-Off & Spin In Behind",
            group = LessonGroup.POSITION,
            categoryKey = PlayerPosition.STRIKER.name,
            categoryLabel = "Striker",
            description = "Pin defenders securely with your back to goal, cushion lay-offs, and spin into the box.",
            instructions = listOf(
                "Pass hard into wall from 12m, receive back-to-goal with wide balanced stance protecting the ball.",
                "Bounce a clean 1-touch lay-off off the side cone, spin immediately into space, and finish."
            ),
            difficulty = DifficultyLevel.ADVANCED,
            recommendedReps = "3 sets × 8 hold-up & spin combos",
            durationMinutes = 20,
            equipment = "2 footballs, wall, 3 cones, goal",
            coachingTips = listOf("Feel the defender's pressure with your arm and spin the opposite way."),
            diagramType = DrillDiagramType.SHOOTING_ARCS
        )
    )

    private fun build7DayProgram(): TrainingProgram {
        val days = listOf(
            ProgramDayPlan(1, "Touch & Ball Mastery Ignition", false, 30, "Ball Control & Warm-up", listOf("FIFA 11+ Dynamic Warm-up (10m)", "First Touch Cushion (10m)", "Ball Mastery 1000-Touch Matrix (10m)")),
            ProgramDayPlan(2, "Explosive Dribbling & Acceleration", false, 35, "Dribbling & Speed", listOf("10m–20m Acceleration Starts (15m)", "Cone Dribbling Slalom (10m)", "1v1 Step-Over Feints (10m)")),
            ProgramDayPlan(3, "Crisp Passing & Core Shielding", false, 35, "Passing & Strength", listOf("Crisp Inside-Foot Short Passing (15m)", "One-Touch Triangle Passing (10m)", "Footballer Bodyweight Foundation (10m)")),
            ProgramDayPlan(4, "Active Recovery & Mobility Reset", true, 15, "Rest & Cooldown", listOf("15-min Easy Walk + Deep Stretch", "90-90 Hip Mobility Flow", "Hydration & Sleep Focus")),
            ProgramDayPlan(5, "Clinical Finishing & Weak Foot", false, 40, "Shooting & Power", listOf("Dynamic Ball Warm-up (10m)", "Laces Power Shooting (15m)", "Weak-Foot Strike Symmetry (15m)")),
            ProgramDayPlan(6, "Defending & Match Stamina HIIT", false, 40, "Defending & Stamina", listOf("Jockey & Block Tackling (12m)", "1v1 Channeling & Recovery (13m)", "30/30 Football Match HIIT (15m)")),
            ProgramDayPlan(7, "Total Player Challenge Showcase", false, 35, "Full-Game Integration", listOf("Progressive Juggling Ladder PB Attempt", "Curled Corner Placement Shots", "Post-Session Recovery Stretch"))
        )
        return TrainingProgram(
            id = "program_7",
            title = "7-Day Football Challenge",
            totalDays = 7,
            badge = "SPRINT WEEK",
            description = "A rapid high-focus 7-day blueprint upgrading first touch, 1v1 dribbling, passing accuracy, shooting power, and match conditioning.",
            weeklyStructureSummary = "6 Active Training Days + 1 Structured Active Recovery Day",
            days = days
        )
    }

    private fun build30DayProgram(): TrainingProgram {
        val weeklyTemplates = listOf(
            "Ball Control & First Touch Foundation" to listOf("First Touch Cushion", "Ball Mastery Matrix", "Dynamic Warm-up"),
            "Dribbling & 1v1 Speed" to listOf("Cone Dribbling Slalom", "Explosive Speed Dribbling", "5-10-5 Pro Agility"),
            "Passing Precision & Scanning" to listOf("Crisp Short Passing", "Two-Touch Scan & Release", "30/30 Stamina HIIT"),
            "Clinical Finishing & Weak Foot" to listOf("Laces Power Shooting", "Curled Corner Placement", "Weak-Foot Strike Symmetry"),
            "Defending & Physical Duels" to listOf("Jockey & Block Tackling", "Tight Marking & Interceptions", "Single-Leg Strength"),
            "Complete Match Intensity Circuit" to listOf("Position Specialization Drill", "Repeat Sprint Ability", "Cooldown & Stretch"),
            "Rest & Active Recovery Day" to listOf("Light 15-min Walk", "Foam Roll / Mobility Flow", "Tactics Board Review")
        )
        val days = (1..30).map { day ->
            val mod = (day - 1) % 7
            val isRest = mod == 6 || day == 14 || day == 21 || day == 28
            val week = ((day - 1) / 7) + 1
            val tpl = weeklyTemplates[mod]
            ProgramDayPlan(
                dayNumber = day,
                title = if (isRest) "Week $week Recovery & Reset" else "Week $week • ${tpl.first}",
                isRestDay = isRest,
                durationMinutes = if (isRest) 15 else 30 + (week * 4),
                focusArea = if (isRest) "Active Recovery" else tpl.first,
                exercises = tpl.second
            )
        }
        return TrainingProgram(
            id = "program_30",
            title = "30-Day Football Improvement",
            totalDays = 30,
            badge = "4-WEEK BUILD",
            description = "Progressive 4-week cycle compounding technical touch quality, weak-foot confidence, sprint speed, and 90-minute aerobic stamina.",
            weeklyStructureSummary = "4 Progressive Micro-Cycles • 6 Days On / 1 Recovery Day per week",
            days = days
        )
    }

    private fun build90DayProgram(): TrainingProgram {
        val days = (1..90).map { day ->
            val phase = when {
                day <= 30 -> "Phase 1: Technical Mastery"
                day <= 60 -> "Phase 2: Speed & Tactical Power"
                else -> "Phase 3: Match-Ready Peak"
            }
            val mod = (day - 1) % 7
            val isRest = mod == 6
            val (focus, drills, mins) = when (mod) {
                0 -> Triple("Ball Control & Juggling", listOf("Ball Mastery 1000-Touch", "Juggling Ladder", "Tight Box Close Control"), 35)
                1 -> Triple("Speed, Agility & 1v1 Dribbling", listOf("Flying 30m Sprints", "T-Drill Agility", "1v1 Step-Overs & Cuts"), 45)
                2 -> Triple("Passing Tempo & Vision", listOf("One-Touch Triangle Passing", "Lofted Switches", "Core Stability"), 40)
                3 -> Triple("Finishing & Shot Power", listOf("Laces Power Drive", "Box 1v1 Finishing", "Side-Foot Volleys"), 45)
                4 -> Triple("Defending & Position Mastery", listOf("Position Specialized Drill", "Interceptions & Recovery Sprints"), 40)
                5 -> Triple("Match Stamina & Plyometrics", listOf("4x4 Endurance / RSA Sprints", "Vertical Header Jumps", "Post-Match Mobility"), 50)
                else -> Triple("Mandatory Rest & Recovery", listOf("20-min Walk", "Hip & Ankle Mobility", "Match Video/Tactics Study"), 15)
            }
            ProgramDayPlan(
                dayNumber = day,
                title = "$phase — Day $day",
                isRestDay = isRest,
                durationMinutes = mins,
                focusArea = focus,
                exercises = drills
            )
        }
        return TrainingProgram(
            id = "program_90",
            title = "90-Day Football Training Program",
            totalDays = 90,
            badge = "PRO 90-DAY",
            description = "Full 13-week elite training curriculum structured across Technical Foundation, Speed & Tactical Power, and Match-Ready Peak.",
            weeklyStructureSummary = "3 Phases (Days 1–30, 31–60, 61–90) • Weekly Recovery Days Built-In",
            days = days
        )
    }

    val programs: List<TrainingProgram> = listOf(
        build7DayProgram(),
        build30DayProgram(),
        build90DayProgram()
    )
}
