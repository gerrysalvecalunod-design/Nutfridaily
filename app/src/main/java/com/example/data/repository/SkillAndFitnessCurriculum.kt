package com.example.data.repository

import com.example.data.model.DifficultyLevel
import com.example.data.model.DrillDiagramType
import com.example.data.model.FitnessCategory
import com.example.data.model.LessonGroup
import com.example.data.model.SkillCategory
import com.example.data.model.TrainingLesson

object SkillAndFitnessCurriculum {

    val skillLessons: List<TrainingLesson> = listOf(
        // A. BALL CONTROL (5 lessons)
        TrainingLesson(
            id = "bc_first_touch",
            title = "First Touch Cushion",
            group = LessonGroup.SKILL_MODULE,
            categoryKey = SkillCategory.BALL_CONTROL.name,
            categoryLabel = "Ball Control",
            description = "Absorb driven ground and lofted passes into your next playing space with one clean touch.",
            instructions = listOf(
                "Stand 3 meters from a rebound wall or partner.",
                "Strike a firm pass against the wall with your inside foot.",
                "As the ball returns, watch it onto your foot and relax your ankle right at contact.",
                "Direct your first touch 45 degrees left or right past an imaginary defender.",
                "Alternate left and right feet every repetition."
            ),
            difficulty = DifficultyLevel.BEGINNER,
            recommendedReps = "4 sets × 20 touches per foot",
            durationMinutes = 15,
            equipment = "1 football, wall or partner, 2 cones",
            coachingTips = listOf(
                "Check over your shoulder before the ball arrives.",
                "Withdraw your receiving foot slightly like catching an egg."
            ),
            diagramType = DrillDiagramType.TRIANGLE_PASSING
        ),
        TrainingLesson(
            id = "bc_ball_mastery",
            title = "Ball Mastery 1000-Touch Matrix",
            group = LessonGroup.SKILL_MODULE,
            categoryKey = SkillCategory.BALL_CONTROL.name,
            categoryLabel = "Ball Control",
            description = "Build effortless comfort with sole rolls, toe taps, foundations, and inside-outside taps.",
            instructions = listOf(
                "Perform 45 seconds of rapid sole toe taps on top of the ball.",
                "Switch directly to inside-to-inside pendulum bells between both feet.",
                "Execute sole drags across your body followed by an outside push.",
                "Finish each round with V-pulls (pull with sole, push out with inside)."
            ),
            difficulty = DifficultyLevel.BEGINNER,
            recommendedReps = "3 rounds × 4 drills (45s work / 15s rest)",
            durationMinutes = 15,
            equipment = "1 football, 2x2m flat space",
            coachingTips = listOf(
                "Stay on the balls of your feet with knees slightly bent.",
                "Keep your eyes glancing forward, not glued to the ball."
            ),
            diagramType = DrillDiagramType.JUGGLE_CIRCLE
        ),
        TrainingLesson(
            id = "bc_juggling",
            title = "Progressive Juggling Ladder",
            group = LessonGroup.SKILL_MODULE,
            categoryKey = SkillCategory.BALL_CONTROL.name,
            categoryLabel = "Ball Control",
            description = "Master aerial ball control, ankle lock, and weak-foot symmetry through structured juggling targets.",
            instructions = listOf(
                "Start from hands: drop the ball, let it bounce once, juggle with laces, catch.",
                "Progress to continuous alternating right-left laces juggles below waist height.",
                "Add a 2-foot-1-thigh sequence to build rhythm across surfaces.",
                "Attempt to beat your personal best streak without spin."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "10 minutes continuous ladder + 3 max-streak attempts",
            durationMinutes = 15,
            equipment = "1 football",
            coachingTips = listOf(
                "Lock your ankle slightly upward so the ball rises cleanly with minimal backspin.",
                "Make contact with the flat shoelace zone, not your toes."
            ),
            diagramType = DrillDiagramType.JUGGLE_CIRCLE
        ),
        TrainingLesson(
            id = "bc_foot_coordination",
            title = "Quick-Feet Coordination & Pull-Tucks",
            group = LessonGroup.SKILL_MODULE,
            categoryKey = SkillCategory.BALL_CONTROL.name,
            categoryLabel = "Ball Control",
            description = "Combine fast neuromuscular footwork with instant ball manipulation in tight pockets.",
            instructions = listOf(
                "Set 4 cones in a 1.5m square box.",
                "Perform fast L-turns inside the box using sole pull behind standing leg.",
                "Follow immediately with 4 quick inside-outside touches with your weak foot.",
                "Accelerate out of the square for 3 explosive steps."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "5 sets × 6 sequences per side",
            durationMinutes = 15,
            equipment = "1 football, 4 cones",
            coachingTips = listOf(
                "Use your arms for balance during rapid weight shifts.",
                "Stay low so your center of gravity changes quickly."
            ),
            diagramType = DrillDiagramType.CONES_SLALOM
        ),
        TrainingLesson(
            id = "bc_close_control",
            title = "Tight-Space Close Control Box",
            group = LessonGroup.SKILL_MODULE,
            categoryKey = SkillCategory.BALL_CONTROL.name,
            categoryLabel = "Ball Control",
            description = "Keep the ball glued to your boots under high-tempo directional changes inside a crowded grid.",
            instructions = listOf(
                "Mark a 4x4m grid with 4 cones and place 2 obstacle cones inside.",
                "Dribble at brisk tempo taking a touch every single stride.",
                "Every time you approach an interior cone, execute a sole roll or Cruyff turn.",
                "Escape through one of the outer gates on a mental whistle cue."
            ),
            difficulty = DifficultyLevel.ADVANCED,
            recommendedReps = "6 × 60-second high-tempo intervals",
            durationMinutes = 20,
            equipment = "1 football, 6 cones",
            coachingTips = listOf(
                "Keep touches within arm's length of your body.",
                "Use small ankle adjustments rather than big leg swings."
            ),
            diagramType = DrillDiagramType.CONES_SLALOM
        ),

        // B. DRIBBLING (5 lessons)
        TrainingLesson(
            id = "dr_cone_slalom",
            title = "Cone Dribbling Slalom",
            group = LessonGroup.SKILL_MODULE,
            categoryKey = SkillCategory.DRIBBLING.name,
            categoryLabel = "Dribbling",
            description = "Weave sharply through tight cones using both feet and rapid cadence changes.",
            instructions = listOf(
                "Line up 6 cones spaced 1 meter apart in a straight line.",
                "Weave through using only the right foot (inside + outside) on Run 1.",
                "Use only the left foot on Run 2, then alternate both feet on Run 3.",
                "Sprint 5 meters past the final cone after exiting cleanly."
            ),
            difficulty = DifficultyLevel.BEGINNER,
            recommendedReps = "6 reps right, 6 reps left, 8 reps alternating",
            durationMinutes = 15,
            equipment = "1 football, 6 cones",
            coachingTips = listOf(
                "Brush past the base of each cone without swinging wide.",
                "Accelerate out of the final cone to build match-realistic bursts."
            ),
            diagramType = DrillDiagramType.CONES_SLALOM
        ),
        TrainingLesson(
            id = "dr_inside_outside",
            title = "Inside-Outside Rhythm Touches",
            group = LessonGroup.SKILL_MODULE,
            categoryKey = SkillCategory.DRIBBLING.name,
            categoryLabel = "Dribbling",
            description = "Develop hypnotic dribbling flow with single-foot inside/outside cuts to unbalance markers.",
            instructions = listOf(
                "Set two cones 12 meters apart.",
                "Push the ball diagonally with your outside pinky-toe zone, then chop back inside.",
                "Travel the full 12m channel taking small rhythmic outside-inside touches.",
                "Jog back slowly and switch to the opposite foot."
            ),
            difficulty = DifficultyLevel.BEGINNER,
            recommendedReps = "8 lengths per foot",
            durationMinutes = 15,
            equipment = "1 football, 2 cones",
            coachingTips = listOf(
                "Drop your shoulder toward each cut to sell the body fake.",
                "Keep your standing knee bent for instant lateral push-off."
            ),
            diagramType = DrillDiagramType.CONES_SLALOM
        ),
        TrainingLesson(
            id = "dr_speed_dribbling",
            title = "Explosive Speed Dribbling",
            group = LessonGroup.SKILL_MODULE,
            categoryKey = SkillCategory.DRIBBLING.name,
            categoryLabel = "Dribbling",
            description = "Attack open space at maximum sprint speed using controlled pinky-toe pushes out in front.",
            instructions = listOf(
                "Place two gates 25 meters apart.",
                "Push the ball 1.5–2 meters ahead using the outer laces/pinky-toe area.",
                "Reach full sprint stride between touches without breaking running mechanics.",
                "Decelerate smoothly with a sole trap inside the finish gate."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "8 × 25m sprints (45s rest between sprints)",
            durationMinutes = 20,
            equipment = "1 football, 4 cones",
            coachingTips = listOf(
                "Pump your arms naturally just like a track sprint.",
                "Never kick the ball so far ahead that a goalkeeper could sweep it."
            ),
            diagramType = DrillDiagramType.SPRINT_LANES
        ),
        TrainingLesson(
            id = "dr_1v1_moves",
            title = "1v1 Step-Over & Body Feint Mastery",
            group = LessonGroup.SKILL_MODULE,
            categoryKey = SkillCategory.DRIBBLING.name,
            categoryLabel = "Dribbling",
            description = "Freeze defenders with convincing hip drops, step-overs, and La Croqueta bursts.",
            instructions = listOf(
                "Place a mannequin or tall cone 8 meters ahead to represent a defender.",
                "Dribble directly at the defender at medium speed.",
                "2 meters before contact, drop your shoulder + circle foot around the ball (step-over).",
                "Explode past the opposite side with the outside of your other foot."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "10 reps left exit + 10 reps right exit",
            durationMinutes = 20,
            equipment = "1 football, 3 cones",
            coachingTips = listOf(
                "Timing matters more than speed—initiate the feint just outside tackle range.",
                "Your exit touch must go past the defender's back shoulder."
            ),
            diagramType = DrillDiagramType.CONES_SLALOM
        ),
        TrainingLesson(
            id = "dr_change_direction",
            title = "Sharp Cut & Change of Direction",
            group = LessonGroup.SKILL_MODULE,
            categoryKey = SkillCategory.DRIBBLING.name,
            categoryLabel = "Dribbling",
            description = "Break pressing traps using Cruyff turns, L-cuts, and outside-hook reversals.",
            instructions = listOf(
                "Set 3 cones in a triangle (5m sides).",
                "Sprint-dribble to the top cone, perform a sharp Cruyff turn behind your plant foot.",
                "Accelerate toward the bottom-left cone and execute an outside-hook cut.",
                "Finish with a snappy acceleration back through the start gate."
            ),
            difficulty = DifficultyLevel.ADVANCED,
            recommendedReps = "4 sets × 4 full triangle circuits",
            durationMinutes = 20,
            equipment = "1 football, 3 cones",
            coachingTips = listOf(
                "Plant your non-kicking foot beside the ball and bend low to absorb momentum.",
                "Fake a pass or shot before hooking the ball to freeze defenders."
            ),
            diagramType = DrillDiagramType.TRIANGLE_PASSING
        ),

        // C. PASSING (5 lessons)
        TrainingLesson(
            id = "pa_short_pass",
            title = "Crisp Inside-Foot Short Passing",
            group = LessonGroup.SKILL_MODULE,
            categoryKey = SkillCategory.PASSING.name,
            categoryLabel = "Passing",
            description = "Deliver firm, flat ground passes that arrive smoothly on your teammate's front foot.",
            instructions = listOf(
                "Set a 1-meter target gate against a wall 6 meters away.",
                "Place plant foot alongside the ball pointing directly at the target.",
                "Lock your ankle at 90 degrees and strike through the ball's equator.",
                "Follow through toward the target; receive cleanly on return."
            ),
            difficulty = DifficultyLevel.BEGINNER,
            recommendedReps = "50 passes right foot + 50 passes left foot",
            durationMinutes = 15,
            equipment = "1 football, wall, 2 cones",
            coachingTips = listOf(
                "Keep your chest slightly over the ball so it stays flat on the grass.",
                "Listen for a crisp 'thud'—no bouncing or wobbling."
            ),
            diagramType = DrillDiagramType.TRIANGLE_PASSING
        ),
        TrainingLesson(
            id = "pa_long_pass",
            title = "Driven & Lofted Long Passing",
            group = LessonGroup.SKILL_MODULE,
            categoryKey = SkillCategory.PASSING.name,
            categoryLabel = "Passing",
            description = "Switch the point of attack 25–40 meters accurately with clean backspin.",
            instructions = listOf(
                "Mark a 3x3m landing zone 25 meters away with 4 cones.",
                "Approach the ball at a 35-degree angle.",
                "Plant foot slightly back and wide; lean torso slightly away from the ball.",
                "Slice across the lower third with your inner instep to create true backspin."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "3 sets × 10 driven switches per foot",
            durationMinutes = 20,
            equipment = "2 footballs, 4 cones",
            coachingTips = listOf(
                "Keep your kicking ankle rigid through impact and follow through low.",
                "Pick out a visual landmark behind your target zone."
            ),
            diagramType = DrillDiagramType.TRIANGLE_PASSING
        ),
        TrainingLesson(
            id = "pa_one_touch",
            title = "One-Touch Triangle Passing",
            group = LessonGroup.SKILL_MODULE,
            categoryKey = SkillCategory.PASSING.name,
            categoryLabel = "Passing",
            description = "Accelerate ball tempo and off-ball movement with rapid single-touch wall combinations.",
            instructions = listOf(
                "Stand 3.5m from a wall with two cones placed 2m apart.",
                "Play a firm pass against the wall from Cone A, immediately shuffle to Cone B.",
                "Return the rebound on the first touch back into the target zone.",
                "Maintain continuous rhythm without letting the ball stop."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "4 sets × 45 seconds continuous",
            durationMinutes = 15,
            equipment = "1 football, wall, 2 cones",
            coachingTips = listOf(
                "Pre-shape your hips and shoulders before the ball arrives.",
                "Stay light on your toes so you can adjust to awkward bounces."
            ),
            diagramType = DrillDiagramType.TRIANGLE_PASSING
        ),
        TrainingLesson(
            id = "pa_two_touch",
            title = "Two-Touch Scan & Release",
            group = LessonGroup.SKILL_MODULE,
            categoryKey = SkillCategory.PASSING.name,
            categoryLabel = "Passing",
            description = "Simulate real midfield build-up: scan, open body touch across cones, and crisp release.",
            instructions = listOf(
                "Place 1 central defender cone 4m from a rebound wall.",
                "Pass to the wall, check over your shoulder before it returns.",
                "Take your first touch across your body past the cone with your back foot.",
                "Strike a clean second-touch pass back into the wall."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "4 sets × 16 sequences",
            durationMinutes = 20,
            equipment = "1 football, wall, 3 cones",
            coachingTips = listOf(
                "Always receive with your back foot (furthest from the wall) to stay open.",
                "Keep touch 1 and touch 2 under 1.5 seconds total."
            ),
            diagramType = DrillDiagramType.TRIANGLE_PASSING
        ),
        TrainingLesson(
            id = "pa_wall_passing",
            title = "High-Tempo Wall Passing Gauntlet",
            group = LessonGroup.SKILL_MODULE,
            categoryKey = SkillCategory.PASSING.name,
            categoryLabel = "Passing",
            description = "Sharpen reaction speed and weak-foot passing reliability under match fatigue.",
            instructions = listOf(
                "Stand 2.5m from a hard wall.",
                "Alternate right foot, left foot one-touch passes as fast as possible for 30s.",
                "After 30s, step back to 6m and execute 10 driven two-touch passes.",
                "Track how many clean passes you complete in 60 seconds."
            ),
            difficulty = DifficultyLevel.ADVANCED,
            recommendedReps = "5 × 60-second timed rounds",
            durationMinutes = 20,
            equipment = "1 football, solid wall",
            coachingTips = listOf(
                "Strike the exact center of the ball to prevent side spin.",
                "Keep knees athletic and shoulders relaxed."
            ),
            diagramType = DrillDiagramType.TRIANGLE_PASSING
        ),

        // D. SHOOTING (5 lessons)
        TrainingLesson(
            id = "sh_power_drive",
            title = "Laces Power Shooting",
            group = LessonGroup.SKILL_MODULE,
            categoryKey = SkillCategory.SHOOTING.name,
            categoryLabel = "Shooting",
            description = "Generate blistering shot velocity by locking the ankle and landing on your striking foot.",
            instructions = listOf(
                "Place the ball 18 meters from goal (just outside the penalty arc).",
                "Touch the ball slightly forward at a slight angle, take 2–3 athletic strides.",
                "Plant your support foot level with the ball; point toes down and lock ankle.",
                "Strike dead center with the hard bone of your shoelaces and land forward on your kicking foot."
            ),
            difficulty = DifficultyLevel.BEGINNER,
            recommendedReps = "3 sets × 8 shots (rest 60s per set)",
            durationMinutes = 20,
            equipment = "3–5 footballs, goal or target wall",
            coachingTips = listOf(
                "Keep your head down through contact and chest over the ball.",
                "Swing from the hip and snap the knee cleanly—don't lean backward."
            ),
            diagramType = DrillDiagramType.SHOOTING_ARCS
        ),
        TrainingLesson(
            id = "sh_placement",
            title = "Curled Corner Placement Shot",
            group = LessonGroup.SKILL_MODULE,
            categoryKey = SkillCategory.SHOOTING.name,
            categoryLabel = "Shooting",
            description = "Bend precision shots around defenders into the far side-netting using inside-instep whip.",
            instructions = listOf(
                "Set cones near the left and right corners inside the goal.",
                "Dribble across the top of the box toward the center.",
                "Open your hips, plant wide, and wrap the inside-arch of your boot around the side of the ball.",
                "Aim 1 meter outside the post so curl brings the ball inside the side-netting."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "12 shots from left half-space + 12 from right",
            durationMinutes = 20,
            equipment = "3 footballs, 2 corner target cones",
            coachingTips = listOf(
                "Follow through across your body toward your opposite shoulder.",
                "Glance at the goalkeeper's positioning right before your final touch."
            ),
            diagramType = DrillDiagramType.SHOOTING_ARCS
        ),
        TrainingLesson(
            id = "sh_finishing",
            title = "Box Composure 1v1 Finishing",
            group = LessonGroup.SKILL_MODULE,
            categoryKey = SkillCategory.SHOOTING.name,
            categoryLabel = "Shooting",
            description = "Convert high-percentage box chances with quick release, low driven finishes, and dinks.",
            instructions = listOf(
                "Place 4 balls around the penalty spot and 12m arc.",
                "Pass or toss the ball slightly ahead, sprint onto it, and shoot within 2 strides.",
                "Alternate low driven shots to bottom corners and subtle chipped finishes.",
                "Reset quickly to keep heart rate realistic."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "4 rounds × 6 rapid finishes",
            durationMinutes = 20,
            equipment = "4 footballs, goal",
            coachingTips = listOf(
                "Side-foot precision beats wild power inside 12 yards.",
                "Disguise your body shape until the last split second."
            ),
            diagramType = DrillDiagramType.SHOOTING_ARCS
        ),
        TrainingLesson(
            id = "sh_volleys",
            title = "Side-Foot & Instep Volleys",
            group = LessonGroup.SKILL_MODULE,
            categoryKey = SkillCategory.SHOOTING.name,
            categoryLabel = "Shooting",
            description = "Time airborne strikes cleanly and keep volleys under the crossbar.",
            instructions = listOf(
                "Self-toss the ball gently 1.5m into the air.",
                "Adjust your feet quickly so your hip aligns horizontally with the ball.",
                "Keep knee above the ball at contact and strike cleanly with laces or inside.",
                "Progress to bounce half-volleys from a wall rebound."
            ),
            difficulty = DifficultyLevel.ADVANCED,
            recommendedReps = "3 sets × 10 volleys (5 right, 5 left)",
            durationMinutes = 20,
            equipment = "2–3 footballs, goal or rebound wall",
            coachingTips = listOf(
                "Focus on smooth timing at 70% power before swinging harder.",
                "Keep your eye fixed on the center seam of the ball until after impact."
            ),
            diagramType = DrillDiagramType.SHOOTING_ARCS
        ),
        TrainingLesson(
            id = "sh_weak_foot",
            title = "Weak-Foot Strike Symmetry",
            group = LessonGroup.SKILL_MODULE,
            categoryKey = SkillCategory.SHOOTING.name,
            categoryLabel = "Shooting",
            description = "Eliminate predictability by building confident shooting power and accuracy with your weaker foot.",
            instructions = listOf(
                "Start 8 meters from goal: strike 10 clean inside-foot placed finishes with your weak foot.",
                "Step back to 14 meters: practice slow-motion plant + laces swing mechanics.",
                "Add a 2-touch prep dribble on your weak side and strike low across goal.",
                "Compare mechanics against your strong foot and mirror your plant distance."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "25 weak-foot strikes per session",
            durationMinutes = 20,
            equipment = "3 footballs, goal/wall",
            coachingTips = listOf(
                "Most weak-foot mishits happen because the plant foot is too close to the ball.",
                "Use your non-kicking arm fully for balance and rotational torque."
            ),
            diagramType = DrillDiagramType.SHOOTING_ARCS
        ),

        // E. DEFENDING (5 lessons)
        TrainingLesson(
            id = "df_tackling",
            title = "Jockey & Clean Block Tackling",
            group = LessonGroup.SKILL_MODULE,
            categoryKey = SkillCategory.DEFENDING.name,
            categoryLabel = "Defending",
            description = "Time tackles patiently and win possession cleanly without diving in.",
            instructions = listOf(
                "Adopt a low athletic side-on stance with knees bent and weight on toes.",
                "Drop back smoothly 2 meters while watching the ball (not the attacker's shoulders).",
                "Wait for a heavy touch, then step in decisively with a locked ankle inside-foot block.",
                "Follow through and push the ball into your own control."
            ),
            difficulty = DifficultyLevel.BEGINNER,
            recommendedReps = "4 sets × 8 shadow jockeys + tackle lunges",
            durationMinutes = 15,
            equipment = "1 football, 4 cones",
            coachingTips = listOf(
                "Never cross your legs while jockeying backward.",
                "Keep arm's-length distance so you cannot be nutmegged."
            ),
            diagramType = DrillDiagramType.DEFENSIVE_CHANNEL
        ),
        TrainingLesson(
            id = "df_positioning",
            title = "Goal-Side Defensive Positioning",
            group = LessonGroup.SKILL_MODULE,
            categoryKey = SkillCategory.DEFENDING.name,
            categoryLabel = "Defending",
            description = "Hold ideal angles between ball, attacker, and goal while shifting with the backline.",
            instructions = listOf(
                "Set 3 cones across a 15m line representing forward attackers and 1 goal behind you.",
                "Start on the center mark, check over both shoulders.",
                "Sprint-shuffle laterally to shut down the imaginary wide pass while staying goal-side.",
                "Drop and compact quickly whenever the imaginary ball moves backward."
            ),
            difficulty = DifficultyLevel.BEGINNER,
            recommendedReps = "5 × 45-second defensive line shifts",
            durationMinutes = 15,
            equipment = "5 cones",
            coachingTips = listOf(
                "Position yourself slightly inside the attacker to force play toward the touchline.",
                "Communicate line drops aloud ('Step!', 'Drop!') to build habit."
            ),
            diagramType = DrillDiagramType.DEFENSIVE_CHANNEL
        ),
        TrainingLesson(
            id = "df_marking",
            title = "Tight Touch-Tight Marking & Body Shield",
            group = LessonGroup.SKILL_MODULE,
            categoryKey = SkillCategory.DEFENDING.name,
            categoryLabel = "Defending",
            description = "Deny turns at the source by closing space as the ball travels and using balanced contact.",
            instructions = listOf(
                "Place a cone 10m away representing an attacker with back to goal.",
                "Wait for a bounce pass; sprint out while the ball is travelling and arrive as it lands.",
                "Decelerate 1 meter short with low hips and wide base so the attacker cannot spin.",
                "Guide play away from central danger."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "3 sets × 8 close-down sprints",
            durationMinutes = 20,
            equipment = "1 football, 4 cones",
            coachingTips = listOf(
                "Travel fast while the ball is moving; arrive under control before impact.",
                "Use forearms gently for spatial awareness without grabbing."
            ),
            diagramType = DrillDiagramType.DEFENSIVE_CHANNEL
        ),
        TrainingLesson(
            id = "df_interceptions",
            title = "Passing-Lane Anticipation & Interceptions",
            group = LessonGroup.SKILL_MODULE,
            categoryKey = SkillCategory.DEFENDING.name,
            categoryLabel = "Defending",
            description = "Read body cues early and step into passing lanes cleanly to launch instant counter-attacks.",
            instructions = listOf(
                "Pass firmly against a rebound wall at an angle.",
                "Anticipate the rebound angle, explode forward 2 meters, and intercept before the cone line.",
                "Take a positive first touch forward on the move.",
                "Deliver an immediate forward pass into a target gate."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "4 sets × 10 forward interceptions",
            durationMinutes = 20,
            equipment = "1 football, rebound wall, 4 cones",
            coachingTips = listOf(
                "Watch the passer's plant foot and hips—they reveal pass direction early.",
                "Commit only when you are 100% sure you will win the ball cleanly."
            ),
            diagramType = DrillDiagramType.DEFENSIVE_CHANNEL
        ),
        TrainingLesson(
            id = "df_1v1_defending",
            title = "1v1 Channeling & Recovery Sprint",
            group = LessonGroup.SKILL_MODULE,
            categoryKey = SkillCategory.DEFENDING.name,
            categoryLabel = "Defending",
            description = "Force wingers onto their weaker foot and execute angled recovery runs when beaten.",
            instructions = listOf(
                "Build a 10x15m narrow defending channel with cones.",
                "Angle your hips at 45 degrees to show the attacker ONLY toward the outside line.",
                "Jockey back 5 meters, then execute a sharp turn and 10m recovery sprint toward goal.",
                "Recover goal-side before the finish cone."
            ),
            difficulty = DifficultyLevel.ADVANCED,
            recommendedReps = "6 × angled channel + recovery sprints",
            durationMinutes = 20,
            equipment = "6 cones",
            coachingTips = listOf(
                "Always recover toward the penalty spot, not straight after the ball carrier.",
                "Stay patient—the touchline acts as an extra defender."
            ),
            diagramType = DrillDiagramType.DEFENSIVE_CHANNEL
        ),

        // F. GOALKEEPER (5 lessons)
        TrainingLesson(
            id = "gk_diving",
            title = "Low & Mid Collapse Diving",
            group = LessonGroup.SKILL_MODULE,
            categoryKey = SkillCategory.GOALKEEPER.name,
            categoryLabel = "Goalkeeper",
            description = "Master safe, explosive lateral diving mechanics landing smoothly on thigh-hip-shoulder.",
            instructions = listOf(
                "Start in kneeling set position with a ball 1.5m to your side.",
                "Lead with hands first toward the ball, extend forward at a slight angle, and absorb on side-thigh/hip.",
                "Progress to standing set stance: take a small positive power step and dive onto a soft mat/grass.",
                "Tuck the ball securely into your chest upon landing."
            ),
            difficulty = DifficultyLevel.BEGINNER,
            recommendedReps = "3 sets × 6 dives left + 6 dives right",
            durationMinutes = 20,
            equipment = "2 footballs, soft grass/turf",
            coachingTips = listOf(
                "Always attack forward toward the ball angle rather than diving backward.",
                "Keep thumbs close together behind the ball."
            ),
            diagramType = DrillDiagramType.GOALKEEPER_ARCS
        ),
        TrainingLesson(
            id = "gk_catching",
            title = "Contour W-Grip & Scoop Catching",
            group = LessonGroup.SKILL_MODULE,
            categoryKey = SkillCategory.GOALKEEPER.name,
            categoryLabel = "Goalkeeper",
            description = "Hold stinging chest and low shots cleanly with zero rebounds.",
            instructions = listOf(
                "Stand 3m from a rebound wall and throw/volley firm chest-height passes.",
                "Form a W-shape with thumbs and index fingers behind the ball.",
                "For low ground shots, get one knee nearly down as a backup barrier and scoop into your chest.",
                "Hold every catch cleanly for 1 full second before resetting."
            ),
            difficulty = DifficultyLevel.BEGINNER,
            recommendedReps = "4 sets × 15 clean catches",
            durationMinutes = 15,
            equipment = "1–2 footballs, wall",
            coachingTips = listOf(
                "Let soft elbows act as shock absorbers on hard strikes.",
                "Watch the ball all the way into your gloves."
            ),
            diagramType = DrillDiagramType.GOALKEEPER_ARCS
        ),
        TrainingLesson(
            id = "gk_reflex",
            title = "Close-Range Reflex Reaction Saves",
            group = LessonGroup.SKILL_MODULE,
            categoryKey = SkillCategory.GOALKEEPER.name,
            categoryLabel = "Goalkeeper",
            description = "Sharpen split-second set position timing and explosive parry reflexes.",
            instructions = listOf(
                "Stand 2.5m from a wall facing away or eyes up.",
                "Throw a tennis ball or football hard off the wall at varying angles.",
                "Hop lightly into your balanced set stance just before impact.",
                "React cleanly—catch or parry wide away from the danger zone."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "5 × 30-second high-intensity reflex rounds",
            durationMinutes = 15,
            equipment = "1 football (or tennis ball), solid wall",
            coachingTips = listOf(
                "Keep hands slightly forward in front of your torso at waist-chest height.",
                "Deflect unstoppable shots wide toward the corner flag, never back centrally."
            ),
            diagramType = DrillDiagramType.GOALKEEPER_ARCS
        ),
        TrainingLesson(
            id = "gk_positioning",
            title = "Goal-Arc Angle Positioning",
            group = LessonGroup.SKILL_MODULE,
            categoryKey = SkillCategory.GOALKEEPER.name,
            categoryLabel = "Goalkeeper",
            description = "Cut down shooting angles geometrically using fast footwork along the goalkeeper arc.",
            instructions = listOf(
                "Place 4 shooting cones around the penalty box (wide left, left half-space, central, wide right).",
                "Shuffle along an imaginary arc 2 meters off your goal line as you scan each cone.",
                "Pause squarely on the line bisecting the ball and center of the goal at each station.",
                "Check near-post distance with quick shoulder glances."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "4 × 4-station angle footwork circuits",
            durationMinutes = 15,
            equipment = "4 cones, goal",
            coachingTips = listOf(
                "Feet must be stationary and balanced the instant the striker's foot swings.",
                "Do not hug the goal line—narrowing the angle by 2 meters shrinks the goal massively."
            ),
            diagramType = DrillDiagramType.GOALKEEPER_ARCS
        ),
        TrainingLesson(
            id = "gk_distribution",
            title = "Accurate Side-Volley & Roll Distribution",
            group = LessonGroup.SKILL_MODULE,
            categoryKey = SkillCategory.GOALKEEPER.name,
            categoryLabel = "Goalkeeper",
            description = "Launch fast counter-attacks with bowling rolls, javelin throws, and crisp side-volleys.",
            instructions = listOf(
                "Place two target cones 15m and 35m downfield.",
                "Practice 10 smooth underarm bowling rolls through a 2m cone gate.",
                "Follow with 10 overarm slingshot throws to midfield wing channels.",
                "Finish with 10 low-trajectory side-volleys aiming for the 35m target zone."
            ),
            difficulty = DifficultyLevel.ADVANCED,
            recommendedReps = "10 rolls + 10 slingshot throws + 10 side-volleys",
            durationMinutes = 20,
            equipment = "3 footballs, 4 cones",
            coachingTips = listOf(
                "Drop the ball out in front and strike through sideways for flat, fast side-volleys.",
                "Target your teammate's front foot so they can break forward at speed."
            ),
            diagramType = DrillDiagramType.GOALKEEPER_ARCS
        )
    )

    val fitnessLessons: List<TrainingLesson> = listOf(
        // SPEED
        TrainingLesson(
            id = "fit_speed_beg",
            title = "10m–20m Acceleration Starts",
            group = LessonGroup.FITNESS,
            categoryKey = FitnessCategory.SPEED.name,
            categoryLabel = "Speed",
            description = "Build explosive first-step acceleration out of split-stance and falling starts.",
            instructions = listOf(
                "Mark 10m and 20m sprint gates.",
                "Lean forward until you naturally fall into your first explosive step.",
                "Drive knees forward and pump arms violently for 20 meters.",
                "Walk back slowly for full recovery (60s)."
            ),
            difficulty = DifficultyLevel.BEGINNER,
            recommendedReps = "6 × 20m sprints (60s rest)",
            durationMinutes = 18,
            equipment = "4 cones",
            coachingTips = listOf("Stay low for the first 5 steps before rising tall."),
            diagramType = DrillDiagramType.SPRINT_LANES
        ),
        TrainingLesson(
            id = "fit_speed_int",
            title = "Flying 30m Top-Speed Sprints",
            group = LessonGroup.FITNESS,
            categoryKey = FitnessCategory.SPEED.name,
            categoryLabel = "Speed",
            description = "Increase maximal sprint velocity with a 10m build-up into a 20m max-effort zone.",
            instructions = listOf(
                "Build up smoothly over the first 10m.",
                "Hit 100% top speed through the middle 20m gate.",
                "Coast smoothly for 10m to protect hamstrings."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "6 × 30m sprints (90s recovery)",
            durationMinutes = 20,
            equipment = "4 cones",
            coachingTips = listOf("Relax your jaw and shoulders at top speed."),
            diagramType = DrillDiagramType.SPRINT_LANES
        ),
        TrainingLesson(
            id = "fit_speed_adv",
            title = "Resisted & Curved Overlap Sprints",
            group = LessonGroup.FITNESS,
            categoryKey = FitnessCategory.SPEED.name,
            categoryLabel = "Speed",
            description = "Master high-speed curved runs used by full-backs and wingers breaking behind defenses.",
            instructions = listOf(
                "Set a 30m curved arc around 4 cones.",
                "Explode out of a defensive jockey stance into a curved sprint.",
                "Finish with a quick deceleration stop at the final cone."
            ),
            difficulty = DifficultyLevel.ADVANCED,
            recommendedReps = "8 × 30m curved bursts (4 left, 4 right)",
            durationMinutes = 25,
            equipment = "4 cones",
            coachingTips = listOf("Lean into the curve and drive your outside arm across."),
            diagramType = DrillDiagramType.SPRINT_LANES
        ),

        // AGILITY
        TrainingLesson(
            id = "fit_agility_beg",
            title = "5-10-5 Pro Agility Shuttle",
            group = LessonGroup.FITNESS,
            categoryKey = FitnessCategory.AGILITY.name,
            categoryLabel = "Agility",
            description = "Sharpen lateral deceleration and rapid re-acceleration in tight zones.",
            instructions = listOf(
                "Place 3 cones 5m apart in a line. Start at the center cone.",
                "Sprint 5m right, touch line, sprint 10m left, touch line, sprint 5m back through center."
            ),
            difficulty = DifficultyLevel.BEGINNER,
            recommendedReps = "6 reps (45s rest)",
            durationMinutes = 15,
            equipment = "3 cones",
            coachingTips = listOf("Drop your hips low before every direction plant."),
            diagramType = DrillDiagramType.CONES_SLALOM
        ),
        TrainingLesson(
            id = "fit_agility_int",
            title = "T-Drill Multi-Directional Footwork",
            group = LessonGroup.FITNESS,
            categoryKey = FitnessCategory.AGILITY.name,
            categoryLabel = "Agility",
            description = "Combine forward sprints, lateral defensive shuffles, and clean backpedals.",
            instructions = listOf(
                "Arrange 4 cones in a T shape (10m stem, 5m wings).",
                "Sprint forward 10m, side-shuffle left 5m, shuffle right 10m, shuffle left 5m, backpedal 10m."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "6 × T-Drill circuits (45s rest)",
            durationMinutes = 20,
            equipment = "4 cones",
            coachingTips = listOf("Never click heels together when shuffling laterally."),
            diagramType = DrillDiagramType.CONES_SLALOM
        ),
        TrainingLesson(
            id = "fit_agility_adv",
            title = "Reactive Mirror Cut Drills",
            group = LessonGroup.FITNESS,
            categoryKey = FitnessCategory.AGILITY.name,
            categoryLabel = "Agility",
            description = "Develop explosive reactive agility with sharp 90° and 180° cuts on mental/timer cues.",
            instructions = listOf(
                "Set 4 colored/numbered cones in a 6x6m square.",
                "Sprint toward the front cone, react sharply on every 4-second beep to pivot toward a new corner."
            ),
            difficulty = DifficultyLevel.ADVANCED,
            recommendedReps = "6 × 25-second reactive sets",
            durationMinutes = 20,
            equipment = "4 cones",
            coachingTips = listOf("Keep chest slightly upright so transitions happen instantly."),
            diagramType = DrillDiagramType.CONES_SLALOM
        ),

        // STAMINA
        TrainingLesson(
            id = "fit_stamina_beg",
            title = "Box-to-Box Tempo Shuttles",
            group = LessonGroup.FITNESS,
            categoryKey = FitnessCategory.STAMINA.name,
            categoryLabel = "Stamina",
            description = "Condition match recovery with structured 15s run / 15s walk aerobic intervals.",
            instructions = listOf(
                "Set cones 40m apart.",
                "Run briskly for 15 seconds, walk/jog for 15 seconds continuously for 6 minutes."
            ),
            difficulty = DifficultyLevel.BEGINNER,
            recommendedReps = "2 blocks × 6 minutes (2m rest)",
            durationMinutes = 16,
            equipment = "2 cones, timer",
            coachingTips = listOf("Control your nasal breathing during the 15s recovery walk."),
            diagramType = DrillDiagramType.SPRINT_LANES
        ),
        TrainingLesson(
            id = "fit_stamina_int",
            title = "30/30 Football Match HIIT",
            group = LessonGroup.FITNESS,
            categoryKey = FitnessCategory.STAMINA.name,
            categoryLabel = "Stamina",
            description = "Raise VO2max and lactate tolerance to stay sharp in the final 20 minutes of matches.",
            instructions = listOf(
                "Run at 85–90% intensity for 30 seconds covering 110–130m.",
                "Active light jog for 30 seconds. Repeat continuously."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "2 sets × 8 rounds (3m rest between sets)",
            durationMinutes = 22,
            equipment = "Timer, flat pitch/track",
            coachingTips = listOf("Hit consistent distance every rep rather than burning out early."),
            diagramType = DrillDiagramType.SPRINT_LANES
        ),
        TrainingLesson(
            id = "fit_stamina_adv",
            title = "Repeat Sprint Ability (RSA) 6×40m",
            group = LessonGroup.FITNESS,
            categoryKey = FitnessCategory.STAMINA.name,
            categoryLabel = "Stamina",
            description = "Train elite repeated high-speed counter-attack and recovery sprint capacity.",
            instructions = listOf(
                "Sprint 40 meters at 95–100% effort.",
                "Jog back to start within 20 seconds and immediately launch the next sprint."
            ),
            difficulty = DifficultyLevel.ADVANCED,
            recommendedReps = "3 sets × 6 sprints (3m full rest between sets)",
            durationMinutes = 25,
            equipment = "2 cones, interval timer",
            coachingTips = listOf("Focus on maintaining running form even in sprint #5 and #6."),
            diagramType = DrillDiagramType.SPRINT_LANES
        ),

        // ENDURANCE
        TrainingLesson(
            id = "fit_endurance_beg",
            title = "Aerobic Base 20-Min Pitch Run",
            group = LessonGroup.FITNESS,
            categoryKey = FitnessCategory.ENDURANCE.name,
            categoryLabel = "Endurance",
            description = "Build foundational capillary and heart endurance at a conversational aerobic pace.",
            instructions = listOf(
                "Jog steadily around the perimeter of a pitch or park for 20 minutes.",
                "Keep effort at 65–70% where you could still speak short sentences."
            ),
            difficulty = DifficultyLevel.BEGINNER,
            recommendedReps = "1 × 20 minutes steady",
            durationMinutes = 20,
            equipment = "Running shoes/turf boots",
            coachingTips = listOf("Avoid sprinting; build steady aerobic efficiency."),
            diagramType = DrillDiagramType.SPRINT_LANES
        ),
        TrainingLesson(
            id = "fit_endurance_int",
            title = "Fartlek Match-Pace Simulation",
            group = LessonGroup.FITNESS,
            categoryKey = FitnessCategory.ENDURANCE.name,
            categoryLabel = "Endurance",
            description = "Vary speeds organically (jog, stride, sprint, walk) just like a real 90-minute match.",
            instructions = listOf(
                "Jog 2 minutes -> Stride fast 30s -> Walk 30s -> Sprint 15s -> Jog 90s.",
                "Repeat the Fartlek cycle 5 times."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "5 × 5-minute cycles",
            durationMinutes = 25,
            equipment = "Timer",
            coachingTips = listOf("Use pitch lines to trigger your pace transitions."),
            diagramType = DrillDiagramType.SPRINT_LANES
        ),
        TrainingLesson(
            id = "fit_endurance_adv",
            title = "4×4-Min Norwegian Endurance Intervals",
            group = LessonGroup.FITNESS,
            categoryKey = FitnessCategory.ENDURANCE.name,
            categoryLabel = "Endurance",
            description = "Proven aerobic capacity builder for tireless box-to-box 90-minute engine.",
            instructions = listOf(
                "Run 4 minutes at strong 85% effort.",
                "Jog lightly for 3 minutes active recovery.",
                "Complete 4 total rounds."
            ),
            difficulty = DifficultyLevel.ADVANCED,
            recommendedReps = "4 rounds (4m hard / 3m easy)",
            durationMinutes = 28,
            equipment = "Timer",
            coachingTips = listOf("Round 4 should feel tough but controlled at the same pace as Round 1."),
            diagramType = DrillDiagramType.SPRINT_LANES
        ),

        // STRENGTH
        TrainingLesson(
            id = "fit_strength_beg",
            title = "Footballer Bodyweight Foundation",
            group = LessonGroup.FITNESS,
            categoryKey = FitnessCategory.STRENGTH.name,
            categoryLabel = "Strength",
            description = "Strengthen quads, glutes, hamstrings, and core for balance in physical duels.",
            instructions = listOf(
                "Bodyweight Squats (15 reps) -> Reverse Lunges (10/leg) -> Push-ups (12) -> Forearm Plank (40s).",
                "Rest 60 seconds between circuits."
            ),
            difficulty = DifficultyLevel.BEGINNER,
            recommendedReps = "3 rounds",
            durationMinutes = 20,
            equipment = "Mat / pitch grass",
            coachingTips = listOf("Control the lowering phase for 2 seconds on every squat and lunge."),
            diagramType = DrillDiagramType.JUGGLE_CIRCLE
        ),
        TrainingLesson(
            id = "fit_strength_int",
            title = "Single-Leg Stability & Core Shielding",
            group = LessonGroup.FITNESS,
            categoryKey = FitnessCategory.STRENGTH.name,
            categoryLabel = "Strength",
            description = "Prevent knee/ankle injuries and generate stronger single-leg shooting power.",
            instructions = listOf(
                "Bulgarian Split Squats (10/leg) -> Single-Leg Glute Bridges (12/leg) -> Side Plank Hip Dips (12/side) -> Nordic Hamstring Negatives (6)."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "4 rounds (45s rest)",
            durationMinutes = 25,
            equipment = "Bench/step",
            coachingTips = listOf("Keep your front knee tracking straight over your middle toes."),
            diagramType = DrillDiagramType.JUGGLE_CIRCLE
        ),
        TrainingLesson(
            id = "fit_strength_adv",
            title = "Explosive Power & Core Anti-Rotation",
            group = LessonGroup.FITNESS,
            categoryKey = FitnessCategory.STRENGTH.name,
            categoryLabel = "Strength",
            description = "Transfer lower-body force through a rock-solid core during shoulder-to-shoulder battles.",
            instructions = listOf(
                "Pistol Squat progressions (6/leg) -> Explosive Split Jumps (12) -> Decline Plank Shoulder Taps (20) -> Single-Leg Romanian Deadlifts (10/leg)."
            ),
            difficulty = DifficultyLevel.ADVANCED,
            recommendedReps = "4 rounds (60s rest)",
            durationMinutes = 25,
            equipment = "Bodyweight / optional light ball",
            coachingTips = listOf("Brace your abs tightly during every single-leg hinge."),
            diagramType = DrillDiagramType.JUGGLE_CIRCLE
        ),

        // JUMP TRAINING
        TrainingLesson(
            id = "fit_jump_beg",
            title = "Vertical Header & Landing Mechanics",
            group = LessonGroup.FITNESS,
            categoryKey = FitnessCategory.JUMP.name,
            categoryLabel = "Jump Training",
            description = "Learn safe double-leg takeoffs and soft athletic landings to win aerial duels.",
            instructions = listOf(
                "Snap arms overhead, bend hips back, explode vertically for max height.",
                "Land softly on balls of feet with knees quiet and bent."
            ),
            difficulty = DifficultyLevel.BEGINNER,
            recommendedReps = "4 sets × 6 max-height jumps (60s rest)",
            durationMinutes = 15,
            equipment = "Flat turf",
            coachingTips = listOf("Stick every landing silently for 1 full second."),
            diagramType = DrillDiagramType.JUGGLE_CIRCLE
        ),
        TrainingLesson(
            id = "fit_jump_int",
            title = "Broad Jumps & Lateral Skater Bounds",
            group = LessonGroup.FITNESS,
            categoryKey = FitnessCategory.JUMP.name,
            categoryLabel = "Jump Training",
            description = "Build horizontal sprint power and lateral push-off force for rapid cuts.",
            instructions = listOf(
                "Perform 5 standing broad jumps for max forward distance.",
                "Follow with 12 explosive single-leg lateral skater hops sticking each landing."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "4 sets (60s rest)",
            durationMinutes = 18,
            equipment = "Cones for distance markers",
            coachingTips = listOf("Drive your arms forward aggressively on broad jumps."),
            diagramType = DrillDiagramType.SPRINT_LANES
        ),
        TrainingLesson(
            id = "fit_jump_adv",
            title = "Single-Leg Approach Header Hops",
            group = LessonGroup.FITNESS,
            categoryKey = FitnessCategory.JUMP.name,
            categoryLabel = "Jump Training",
            description = "Dominate set pieces with single-leg takeoff height and reactive pogo elasticity.",
            instructions = listOf(
                "Take 3 quick approach strides and explode vertically off one leg mimicking a contested header.",
                "Pair with 20 stiff-ankle pogo hops for tendon bounce."
            ),
            difficulty = DifficultyLevel.ADVANCED,
            recommendedReps = "4 sets × 5 per takeoff leg + 20 pogo hops",
            durationMinutes = 20,
            equipment = "Turf",
            coachingTips = listOf("Drive your free knee sharply upward toward your chest at takeoff."),
            diagramType = DrillDiagramType.SPRINT_LANES
        ),

        // WARM-UP
        TrainingLesson(
            id = "fit_warmup_beg",
            title = "FIFA 11+ Dynamic Pre-Session Warm-Up",
            group = LessonGroup.FITNESS,
            categoryKey = FitnessCategory.WARMUP.name,
            categoryLabel = "Warm-up",
            description = "Activate hips, hamstrings, calves, and nervous system before touching the ball.",
            instructions = listOf(
                "2 min easy jog -> Hip openers ('open & close the gate') -> Leg swings front & side -> High knees & butt kicks -> 2 progressive 75% build-up strides."
            ),
            difficulty = DifficultyLevel.BEGINNER,
            recommendedReps = "10 minutes continuous flow",
            durationMinutes = 10,
            equipment = "2 cones",
            coachingTips = listOf("Never do long static holds before explosive sprinting."),
            diagramType = DrillDiagramType.SPRINT_LANES
        ),
        TrainingLesson(
            id = "fit_warmup_int",
            title = "Neuromuscular Ball-Included Warm-Up",
            group = LessonGroup.FITNESS,
            categoryKey = FitnessCategory.WARMUP.name,
            categoryLabel = "Warm-up",
            description = "Prime balance, quick feet, and first-touch sharpness in 12 minutes.",
            instructions = listOf(
                "Jog + carioca side-steps -> World's Greatest Stretch (5/side) -> Rapid toe taps & sole rolls (90s) -> 3 short 10m reactive accelerations."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "12 minutes",
            durationMinutes = 12,
            equipment = "1 football, 2 cones",
            coachingTips = listOf("Increase movement tempo gradually every 3 minutes."),
            diagramType = DrillDiagramType.JUGGLE_CIRCLE
        ),
        TrainingLesson(
            id = "fit_warmup_adv",
            title = "Match-Day High-Readiness Activation",
            group = LessonGroup.FITNESS,
            categoryKey = FitnessCategory.WARMUP.name,
            categoryLabel = "Warm-up",
            description = "Full neural ignition combining mobility, pogo hops, sharp cuts, and crisp passing.",
            instructions = listOf(
                "Dynamic lunges with twist -> Single-leg hop & stick -> 10 rapid wall passes -> 4 × 15m match-intensity bursts."
            ),
            difficulty = DifficultyLevel.ADVANCED,
            recommendedReps = "15 minutes",
            durationMinutes = 15,
            equipment = "1 football, 4 cones",
            coachingTips = listOf("Finish feeling warm and springy—never fatigued."),
            diagramType = DrillDiagramType.CONES_SLALOM
        ),

        // COOLDOWN & STRETCHING
        TrainingLesson(
            id = "fit_cooldown_beg",
            title = "Post-Session Recovery & Static Stretch",
            group = LessonGroup.FITNESS,
            categoryKey = FitnessCategory.COOLDOWN.name,
            categoryLabel = "Cooldown & Stretch",
            description = "Lower heart rate calmly and restore length to calves, quads, hamstrings, and hip flexors.",
            instructions = listOf(
                "3 min slow walk with deep breaths.",
                "Hold each stretch 30 seconds per side: Standing Quad, Seated Hamstring, Couch Hip Flexor, Butterfly Groin, Calf Wall Stretch."
            ),
            difficulty = DifficultyLevel.BEGINNER,
            recommendedReps = "30s hold × 2 per muscle group",
            durationMinutes = 10,
            equipment = "Flat mat/turf",
            coachingTips = listOf("Stretch to gentle tension, never sharp pain."),
            diagramType = DrillDiagramType.JUGGLE_CIRCLE
        ),
        TrainingLesson(
            id = "fit_cooldown_int",
            title = "90-90 Hip Mobility & Posterior Reset",
            group = LessonGroup.FITNESS,
            categoryKey = FitnessCategory.COOLDOWN.name,
            categoryLabel = "Cooldown & Stretch",
            description = "Unlock tight soccer hips and lower back stiffness after heavy shooting or sprinting.",
            instructions = listOf(
                "90-90 Hip Switches (12 slow reps) -> Pigeon Pose (45s/side) -> Child's Pose Thoracic Reach (45s) -> Supine Hamstring Floss (12/leg)."
            ),
            difficulty = DifficultyLevel.INTERMEDIATE,
            recommendedReps = "12 minutes calm breathing flow",
            durationMinutes = 12,
            equipment = "Exercise mat",
            coachingTips = listOf("Exhale slowly for 6 seconds to drop muscle tension."),
            diagramType = DrillDiagramType.JUGGLE_CIRCLE
        ),
        TrainingLesson(
            id = "fit_cooldown_adv",
            title = "Deep Fascia & Ankle Dorsiflexion Recovery",
            group = LessonGroup.FITNESS,
            categoryKey = FitnessCategory.COOLDOWN.name,
            categoryLabel = "Cooldown & Stretch",
            description = "Maintain supple ankle mobility and hamstring resilience across intense multi-day schedules.",
            instructions = listOf(
                "Knee-to-Wall Ankle Rocks (15/side) -> Adductor Rock-Backs (12/side) -> Frog Stretch (60s) -> Legs-Up-Wall Parasympathetic Reset (3m)."
            ),
            difficulty = DifficultyLevel.ADVANCED,
            recommendedReps = "15 minutes full recovery",
            durationMinutes = 15,
            equipment = "Wall + mat",
            coachingTips = listOf("Consistent post-training mobility keeps groin and hamstring pulls away."),
            diagramType = DrillDiagramType.JUGGLE_CIRCLE
        )
    )
}
