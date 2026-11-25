package com.example.data.repository

import com.example.data.model.FootballExercise
import com.example.data.model.FootballProgram
import com.example.data.model.LessonSection
import com.example.data.model.PitchDiagramType
import com.example.data.model.SkillLevel

object ProgramsAndTacticsCurriculum {

    val programs: List<FootballProgram> = listOf(
        FootballProgram(
            id = "prog_beginner",
            level = SkillLevel.BEGINNER,
            title = "Beginner Foundations",
            badge = "LEVEL 1 • 6 MODULES",
            description = "Master core pitch laws, ball mastery, first touch, short passing, shooting basics, and athletic balance.",
            focusSummary = "Ball control, clean inside-foot passes & pitch awareness",
            moduleIds = listOf("beg_rules", "beg_touch", "beg_dribble", "beg_passing", "beg_shooting", "beg_defend")
        ),
        FootballProgram(
            id = "prog_intermediate",
            level = SkillLevel.INTERMEDIATE,
            title = "Intermediate Match Ready",
            badge = "LEVEL 2 • 6 MODULES",
            description = "Upgrade to weak-foot control, 1v1 wing beats, driven strikes, whipped crosses, jockey defense & space movement.",
            focusSummary = "1v1 speed, driven strikes & tactical pressing",
            moduleIds = listOf("int_firsttouch", "int_1v1", "int_driven", "int_crossing", "int_jockey", "int_scanning")
        ),
        FootballProgram(
            id = "prog_advanced",
            level = SkillLevel.ADVANCED,
            title = "Advanced Academy Pro",
            badge = "LEVEL 3 • 6 MODULES",
            description = "Train match-speed combinations, half-turn escapes, clinical top-corner finishing, high pressing & transitions.",
            focusSummary = "Match-tempo combinations & tactical dominance",
            moduleIds = listOf("adv_halfturn", "adv_finishing", "adv_rondo", "adv_press", "adv_combos", "adv_goalkeeper")
        )
    )

    val programExercises: List<FootballExercise> = listOf(
        // BEGINNER (6)
        FootballExercise(
            id = "beg_rules",
            name = "Laws of the Game & Pitch Zones",
            section = LessonSection.PROGRAM,
            categoryKey = "PROGRAM",
            categoryLabel = "Beginner Program",
            description = "Learn field markings, kickoff restarts, throw-ins, corners, and offside basics.",
            instructions = listOf(
                "Identify the 18-yard penalty box, 6-yard goal area, halfway line, and corner arcs.",
                "Practice legal two-handed overhead throw-ins keeping both feet grounded on the touchline.",
                "Walk through 5 offside scenarios: you cannot be ahead of the ball and second-last defender when played."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "3 walkthrough rounds",
            restTimeSeconds = 20,
            trainingTips = listOf(
                "Watch the defender's back shoulder to time your forward runs.",
                "You cannot be offside directly from a throw-in, goal kick, or corner kick."
            ),
            diagramType = PitchDiagramType.PITCH_DIMENSIONS,
            motionPhases = listOf(
                "Penalty Box: 16.5m deep attacking zone",
                "Center Circle: 9.15m kickoff clearance",
                "Touchline: Both feet down on throw-in",
                "Offside Line: Stay level with last defender"
            )
        ),
        FootballExercise(
            id = "beg_touch",
            name = "Sole Rolls & Inside-Foot Cushion",
            section = LessonSection.PROGRAM,
            categoryKey = "PROGRAM",
            categoryLabel = "Beginner Program",
            description = "Build soft ball feel using toe taps, sole drags, and inside-foot touches.",
            instructions = listOf(
                "Perform 30 seconds of alternating light toe taps on top of the ball.",
                "Roll the ball sideways with your sole and stop it cleanly with the opposite inside foot.",
                "Keep your knees soft and eyes glancing up between every 3 touches."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "4 sets x 60 sec",
            restTimeSeconds = 25,
            trainingTips = listOf(
                "Stay on the balls of your feet like a boxer — never flat-footed.",
                "Absorb pace by withdrawing your ankle slightly on contact."
            ),
            diagramType = PitchDiagramType.SLALOM_DRIBBLING
        ),
        FootballExercise(
            id = "beg_dribble",
            name = "Pinky-Toe & Inside Cone Dribble",
            section = LessonSection.PROGRAM,
            categoryKey = "PROGRAM",
            categoryLabel = "Beginner Program",
            description = "Keep the ball within one stride while weaving smoothly through 6 cones.",
            instructions = listOf(
                "Place 6 cones 1.5 meters apart in a straight line.",
                "Dribble forward using small pinky-toe touches with your right foot only, then left foot only.",
                "Use the inside of your foot to cut sharply around the end cone and accelerate back."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 75,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "6 slalom runs",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Push the ball softly ahead on every step so you stay balanced.",
                "Use your arms naturally to shield space and keep rhythm."
            ),
            diagramType = PitchDiagramType.SLALOM_DRIBBLING
        ),
        FootballExercise(
            id = "beg_passing",
            name = "Crisp Inside-Foot Wall Passing",
            section = LessonSection.PROGRAM,
            categoryKey = "PROGRAM",
            categoryLabel = "Beginner Program",
            description = "Lock your ankle and strike the ball's center for flat, accurate ground passes.",
            instructions = listOf(
                "Stand 5 meters from a solid rebound wall or training partner.",
                "Plant your non-kicking foot beside the ball pointing straight at the target.",
                "Strike through the ball's equator with your arch, then cushion the return into open space."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "4 sets x 25 passes each foot",
            restTimeSeconds = 25,
            trainingTips = listOf(
                "Lock your ankle at 90 degrees so the ball stays flat on the grass.",
                "Follow through low toward your target instead of stabbing."
            ),
            diagramType = PitchDiagramType.PASSING_TRIANGLE
        ),
        FootballExercise(
            id = "beg_shooting",
            name = "Laces Strike Mechanics",
            section = LessonSection.PROGRAM,
            categoryKey = "PROGRAM",
            categoryLabel = "Beginner Program",
            description = "Learn clean instep power shooting with toes pointed down and chest over the ball.",
            instructions = listOf(
                "Push the ball 1 meter ahead at a slight 30-degree angle.",
                "Plant beside the ball, point your kicking toes down, and strike with the hard bone on your laces.",
                "Land forward on your kicking foot to transfer full body momentum into the shot."
            ),
            durationMinutes = 18,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "3 sets x 10 clean strikes",
            restTimeSeconds = 35,
            trainingTips = listOf(
                "Keep your head down and eyes locked on the center of the ball at impact.",
                "Chest upright leans shots over the bar — lean slightly forward."
            ),
            diagramType = PitchDiagramType.FINISHING_ZONES
        ),
        FootballExercise(
            id = "beg_defend",
            name = "Side-On Jockey & Delay Stance",
            section = LessonSection.PROGRAM,
            categoryKey = "PROGRAM",
            categoryLabel = "Beginner Program",
            description = "Close down attackers smoothly without diving in or getting beaten easily.",
            instructions = listOf(
                "Sprint toward a cone marker, then chop your steps 2 meters away to slow momentum.",
                "Drop into a low side-on stance showing the attacker toward the touchline.",
                "Shuffle backward rapidly while keeping eyes on the ball, not the attacker's shoulders."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "5 sets x 60 sec",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Stay low with knees bent so you can pivot instantly.",
                "Wait for a heavy touch before committing to win possession."
            ),
            diagramType = PitchDiagramType.DEFENSE_SHADOW
        ),

        // INTERMEDIATE (6)
        FootballExercise(
            id = "int_firsttouch",
            name = "Directional First Touch Out of Feet",
            section = LessonSection.PROGRAM,
            categoryKey = "PROGRAM",
            categoryLabel = "Intermediate Program",
            description = "Receive passes across your body to beat pressure on your very first touch.",
            instructions = listOf(
                "Pass sharply against a wall or partner, check both shoulders as the ball travels.",
                "Open your hips and redirect the return pass 45 degrees past a cone marker with your back foot.",
                "Accelerate 5 meters into the new lane immediately after your first touch."
            ),
            durationMinutes = 18,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 16 receptions",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Never stop the ball dead under you unless you want to invite a tackle.",
                "Scan before the ball arrives so you already know where space is open."
            ),
            diagramType = PitchDiagramType.PASSING_TRIANGLE
        ),
        FootballExercise(
            id = "int_1v1",
            name = "Step-Over & Body Feint Beats",
            section = LessonSection.PROGRAM,
            categoryKey = "PROGRAM",
            categoryLabel = "Intermediate Program",
            description = "Unbalance defenders at match speed using sharp shoulder drops and explosive exits.",
            instructions = listOf(
                "Dribble toward a central cone defender at 70% pace.",
                "Drop your left shoulder sharply as if going left, then push the ball hard right with your outside foot.",
                "Explode past the cone for 6 meters before resetting."
            ),
            durationMinutes = 18,
            timerWorkSeconds = 75,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 8 beats per side",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Sell the fake with your eyes and hips, not just your foot.",
                "The change of speed AFTER the skill beats the defender."
            ),
            diagramType = PitchDiagramType.SLALOM_DRIBBLING
        ),
        FootballExercise(
            id = "int_driven",
            name = "Low Driven Shot & Far-Post Curl",
            section = LessonSection.PROGRAM,
            categoryKey = "PROGRAM",
            categoryLabel = "Intermediate Program",
            description = "Pick out bottom corners from the edge of the box with power or whip.",
            instructions = listOf(
                "Take a prep touch at 45 degrees toward the penalty arc.",
                "Alternate between low driven laces strikes across goal and curled inside-foot shots to the far corner.",
                "Target the side netting inside the post where keepers struggle to reach."
            ),
            durationMinutes = 20,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 8 shots",
            restTimeSeconds = 35,
            trainingTips = listOf(
                "Wrap your foot around the side of the ball for bending finesse shots.",
                "Aim 1 meter inside the post rather than directly at the goalkeeper."
            ),
            diagramType = PitchDiagramType.FINISHING_ZONES
        ),
        FootballExercise(
            id = "int_crossing",
            name = "Whipped Wing Crosses & Cutbacks",
            section = LessonSection.PROGRAM,
            categoryKey = "PROGRAM",
            categoryLabel = "Intermediate Program",
            description = "Deliver dangerous balls between the goalkeeper and back line from wide channels.",
            instructions = listOf(
                "Push the ball down the touchline past a marker cone.",
                "Lift your head to spot the penalty spot and near/far post target zones.",
                "Alternate driven low cutbacks to the penalty spot with whipped aerial crosses."
            ),
            durationMinutes = 18,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "3 sets x 10 crosses",
            restTimeSeconds = 35,
            trainingTips = listOf(
                "Approach the ball at an angle so you can whip spin away from the goalkeeper.",
                "Low driven cutbacks are often deadlier than floated high crosses."
            ),
            diagramType = PitchDiagramType.PENALTY_SET_PIECE
        ),
        FootballExercise(
            id = "int_jockey",
            name = "Channel Forcing & Recovery Sprint",
            section = LessonSection.PROGRAM,
            categoryKey = "PROGRAM",
            categoryLabel = "Intermediate Program",
            description = "Force wingers outside onto their weaker foot and sprint to recover central cover.",
            instructions = listOf(
                "Close the wide lane quickly while blocking the inside cutback path.",
                "Mirror rapid lateral shuffles for 15 seconds, then sprint diagonally back to the penalty spot.",
                "Finish by clearing a rolling imaginary through-ball cleanly."
            ),
            durationMinutes = 18,
            timerWorkSeconds = 75,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "5 sets x 60 sec",
            restTimeSeconds = 35,
            trainingTips = listOf(
                "Stay within arm's length so the attacker cannot look up calmly.",
                "Turn your hips quickly when beaten — never cross your feet while jockeying."
            ),
            diagramType = PitchDiagramType.DEFENSE_SHADOW
        ),
        FootballExercise(
            id = "int_scanning",
            name = "2-Touch Scan & Switch Play",
            section = LessonSection.PROGRAM,
            categoryKey = "PROGRAM",
            categoryLabel = "Intermediate Program",
            description = "Check over both shoulders before receiving and switch play smoothly to the opposite flank.",
            instructions = listOf(
                "Check left and right shoulder right before receiving a wall pass.",
                "Take touch #1 across your body toward the opposite side of the pitch.",
                "Strike touch #2 cleanly along the ground or driven low across a 20-meter gate."
            ),
            durationMinutes = 18,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 12 switches",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Great midfielders take a mental snapshot 1 second BEFORE the pass arrives.",
                "Keep your hips open so both flanks remain passing options."
            ),
            diagramType = PitchDiagramType.PASSING_TRIANGLE
        ),

        // ADVANCED (6)
        FootballExercise(
            id = "adv_halfturn",
            name = "Half-Turn Escape Under High Press",
            section = LessonSection.PROGRAM,
            categoryKey = "PROGRAM",
            categoryLabel = "Advanced Program",
            description = "Receive between lines with back-foot disguise and spin past a pressing marker.",
            instructions = listOf(
                "Set two cones behind you representing tight midfield markers.",
                "Check away from the ball, then check back at pace on the half-turn.",
                "Let the ball roll across your back foot or use an inside-outside roll to spin free."
            ),
            durationMinutes = 20,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "5 sets x 10 turns",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Use your off-arm lightly to feel where the defender is positioned.",
                "Disguise your body shape until the very last split second."
            ),
            diagramType = PitchDiagramType.PASSING_TRIANGLE
        ),
        FootballExercise(
            id = "adv_finishing",
            name = "One-Touch Box Volleys & Rebounds",
            section = LessonSection.PROGRAM,
            categoryKey = "PROGRAM",
            categoryLabel = "Advanced Program",
            description = "React instantly to rebounds, half-volleys, and tight-angle finishes inside the 18-yard box.",
            instructions = listOf(
                "Bounce the ball off a wall or toss it to half-height at the edge of the box.",
                "Adjust your feet rapidly with quick stutter steps and strike cleanly on the half-volley.",
                "Follow every strike toward goal for a second-ball tap-in."
            ),
            durationMinutes = 22,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "4 sets x 10 finishes",
            restTimeSeconds = 35,
            trainingTips = listOf(
                "Keep your knee over the ball on volleys to keep strikes down.",
                "Accuracy under fatigue beats wild power every time."
            ),
            diagramType = PitchDiagramType.FINISHING_ZONES
        ),
        FootballExercise(
            id = "adv_rondo",
            name = "High-Tempo Rondo Triangle Rhythm",
            section = LessonSection.PROGRAM,
            categoryKey = "PROGRAM",
            categoryLabel = "Advanced Program",
            description = "Simulate high-speed 1-touch and 2-touch passing lanes with instant support angles.",
            instructions = listOf(
                "Set 3 cones in a tight 6-meter triangle.",
                "Pass sharply off a wall or shuttle between corners, moving to a new support angle after every pass.",
                "Maintain crisp 1-touch tempo for 60 seconds without losing ball control."
            ),
            durationMinutes = 20,
            timerWorkSeconds = 75,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "5 sets x 75 sec",
            restTimeSeconds = 25,
            trainingTips = listOf(
                "Always offer two passing angles on either side of the defender.",
                "Play to the teammate's back foot when pressing is tight."
            ),
            diagramType = PitchDiagramType.PASSING_TRIANGLE
        ),
        FootballExercise(
            id = "adv_press",
            name = "4-3-3 Coordinated Pressing Triggers",
            section = LessonSection.PROGRAM,
            categoryKey = "PROGRAM",
            categoryLabel = "Advanced Program",
            description = "Sprint on pressing cues (bad touch, backward pass) while curved-running to cut passing lanes.",
            instructions = listOf(
                "Start in a compact mid-block shape near the center circle.",
                "On your mental trigger cue, curve your sprint to block the center-back pass while closing the ball.",
                "Recover immediately into compact defensive shape after a 6-second press burst."
            ),
            durationMinutes = 22,
            timerWorkSeconds = 75,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "6 pressing bursts",
            restTimeSeconds = 40,
            trainingTips = listOf(
                "A curved press cuts off half the pitch automatically.",
                "Never press solo — communicate distance with midfielders behind you."
            ),
            diagramType = PitchDiagramType.FORMATION_433
        ),
        FootballExercise(
            id = "adv_combos",
            name = "Wing Overlap + Cutback Combo",
            section = LessonSection.PROGRAM,
            categoryKey = "PROGRAM",
            categoryLabel = "Advanced Program",
            description = "Chain a wall pass, overlapping sprint, touchline cut, and clinical finish in one sequence.",
            instructions = listOf(
                "Play a firm wall pass, sprint wide around a marker cone as the overlapping runner.",
                "Control on the run, cut inside at full speed, and strike low toward the far post.",
                "Alternate left flank and right flank every repetition."
            ),
            durationMinutes = 22,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "4 sets x 6 full sequences",
            restTimeSeconds = 35,
            trainingTips = listOf(
                "Time your overlap sprint so you arrive at full speed as the pass is released.",
                "Finish with balance — slow down slightly on your final planting stride."
            ),
            diagramType = PitchDiagramType.FINISHING_ZONES
        ),
        FootballExercise(
            id = "adv_goalkeeper",
            name = "Goalkeeper Angle Narrowing & Diving Footwork",
            section = LessonSection.PROGRAM,
            categoryKey = "PROGRAM",
            categoryLabel = "Advanced Program",
            description = "Master set-position timing, arc bisecting, and quick recovery footwork.",
            instructions = listOf(
                "Start on the goal line, sprint forward 3 meters along the ball-to-goal center line.",
                "Freeze into a balanced W-hands set position right as the striker plants to shoot.",
                "Shuffle laterally between posts and practice low collapse dives onto soft turf."
            ),
            durationMinutes = 20,
            timerWorkSeconds = 75,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "4 sets x 8 angle sets",
            restTimeSeconds = 35,
            trainingTips = listOf(
                "Be completely still at the moment of the strike so you can push off either foot.",
                "Keep hands forward in front of your torso to parry balls wide of danger."
            ),
            diagramType = PitchDiagramType.GOALKEEPER_ANGLES
        )
    )

    val rulesLessons: List<FootballExercise> = listOf(
        FootballExercise(
            id = "rule_pitch",
            name = "Official Pitch Dimensions & Markings",
            section = LessonSection.RULES,
            categoryKey = "RULES",
            categoryLabel = "Rules & Pitch Guide",
            description = "Understand standard 11v11 pitch measurements, boxes, arcs, and goal size.",
            instructions = listOf(
                "Standard international pitch length: 100–110 meters; width: 64–75 meters.",
                "Goal dimensions: 7.32 meters (8 yards) wide by 2.44 meters (8 feet) high.",
                "Penalty box: 40.3m wide x 16.5m deep; Penalty spot: 11 meters (12 yards) from goal."
            ),
            durationMinutes = 8,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "Study & Pitch Walk",
            restTimeSeconds = 15,
            trainingTips = listOf(
                "Knowing exact box distances helps gauge shooting range and crossing targets instinctively."
            ),
            diagramType = PitchDiagramType.PITCH_DIMENSIONS
        ),
        FootballExercise(
            id = "rule_offside",
            name = "Offside Rule Explained Clearly",
            section = LessonSection.RULES,
            categoryKey = "RULES",
            categoryLabel = "Rules & Pitch Guide",
            description = "Master when an attacking run is onside vs flagged offside.",
            instructions = listOf(
                "You are in an offside position if any scoring body part is nearer the goal line than both the ball and the second-last opponent.",
                "Offside is judged at the exact moment your teammate touches or plays the ball.",
                "No offside occurs in your own half, or directly from throw-ins, goal kicks, or corner kicks."
            ),
            durationMinutes = 8,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "5 Line-Timing Checks",
            restTimeSeconds = 15,
            trainingTips = listOf(
                "Curve your forward run laterally along the defensive line before bursting diagonally."
            ),
            diagramType = PitchDiagramType.FORMATION_433
        ),
        FootballExercise(
            id = "rule_fouls",
            name = "Direct vs Indirect Free Kicks & Cards",
            section = LessonSection.RULES,
            categoryKey = "RULES",
            categoryLabel = "Rules & Pitch Guide",
            description = "Know what constitutes careless vs reckless tackles, handball, and cautions.",
            instructions = listOf(
                "Direct free kicks (or penalties inside the box) are awarded for tripping, pushing, holding, or deliberate handball.",
                "Indirect free kicks are awarded for dangerous high boots without contact, obstruction, or goalkeeper back-pass pickups.",
                "Yellow cards punish reckless fouls or delaying restarts; two yellows or serious foul play earn a red card."
            ),
            durationMinutes = 8,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "Review Scenarios",
            restTimeSeconds = 15,
            trainingTips = listOf(
                "Tuck arms naturally behind your frame when blocking crosses in the box."
            ),
            diagramType = PitchDiagramType.PENALTY_SET_PIECE
        ),
        FootballExercise(
            id = "rule_formations",
            name = "Formations: 4-3-3, 4-4-2 & 3-5-2",
            section = LessonSection.RULES,
            categoryKey = "RULES",
            categoryLabel = "Rules & Pitch Guide",
            description = "Compare shape strengths, midfield triangles, and wide overloads.",
            instructions = listOf(
                "4-3-3 creates natural midfield triangles, high wing pressing, and fluid 3-forward rotations.",
                "4-4-2 locks down two compact defensive banks of four and pairs two strikers for direct counter-attacks.",
                "3-5-2 uses wing-backs to provide width while packing 3 central midfielders."
            ),
            durationMinutes = 10,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "Shape Visualization",
            restTimeSeconds = 15,
            trainingTips = listOf(
                "Triangles win possession — never stand in a straight horizontal or vertical line with teammates."
            ),
            diagramType = PitchDiagramType.FORMATION_433
        ),
        FootballExercise(
            id = "rule_positions",
            name = "Positional Responsibilities Guide",
            section = LessonSection.RULES,
            categoryKey = "RULES",
            categoryLabel = "Rules & Pitch Guide",
            description = "Key duties for Goalkeepers, Defenders, Midfielders, and Forwards.",
            instructions = listOf(
                "Goalkeeper: Organize the back line, narrow shooting angles, and start calm build-up.",
                "Defenders: Hold compact line height, force attackers wide, and win aerial clearances.",
                "Midfielders: Link defense to attack, scan 360 degrees, and dictate tempo.",
                "Forwards: Stretch the back line, press center-backs, and finish chances inside the box."
            ),
            durationMinutes = 10,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "Role Checklist",
            restTimeSeconds = 15,
            trainingTips = listOf(
                "Every player defends when possession is lost and attacks when possession is won."
            ),
            diagramType = PitchDiagramType.FORMATION_433
        ),
        FootballExercise(
            id = "rule_restarts",
            name = "Penalties, Corners & Set-Piece Rules",
            section = LessonSection.RULES,
            categoryKey = "RULES",
            categoryLabel = "Rules & Pitch Guide",
            description = "Legal rules for penalty run-ups, wall distance, and quick free kicks.",
            instructions = listOf(
                "Defenders must stay at least 9.15m (10 yards) back on free kicks and corners.",
                "Penalty takers may stutter during the run-up, but cannot feint kicking once the run is complete.",
                "Goalkeepers must keep at least one foot touching or in line with the goal line at impact."
            ),
            durationMinutes = 8,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "Set-Piece Checklist",
            restTimeSeconds = 15,
            trainingTips = listOf(
                "Pick your penalty corner BEFORE placing the ball and commit 100%."
            ),
            diagramType = PitchDiagramType.PENALTY_SET_PIECE
        ),
        FootballExercise(
            id = "rule_boots",
            name = "Ball Pressure, Studs & Injury Prevention",
            section = LessonSection.RULES,
            categoryKey = "RULES",
            categoryLabel = "Rules & Pitch Guide",
            description = "Choose FG/AG/TF boots safely and maintain optimal ball inflation.",
            instructions = listOf(
                "Size 5 match ball pressure sits between 8.5 and 15.6 PSI for clean bounce and strike feel.",
                "Use Firm Ground (FG) studs on natural dry grass and Artificial Ground (AG) or Turf (TF) on synthetic turf.",
                "Always wear fitted shin guards and complete a 6-minute dynamic warm-up before sprinting."
            ),
            durationMinutes = 7,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "Pre-Session Check",
            restTimeSeconds = 15,
            trainingTips = listOf(
                "AG/TF soles significantly lower knee and ankle torque on artificial turf pitches."
            ),
            diagramType = PitchDiagramType.PITCH_DIMENSIONS
        )
    )
}
