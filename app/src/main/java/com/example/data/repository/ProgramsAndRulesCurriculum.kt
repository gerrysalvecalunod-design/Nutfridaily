package com.example.data.repository

import com.example.data.model.BadmintonExercise
import com.example.data.model.BadmintonProgram
import com.example.data.model.CourtDiagramType
import com.example.data.model.LessonSection
import com.example.data.model.SkillLevel

object ProgramsAndRulesCurriculum {

    val programs: List<BadmintonProgram> = listOf(
        BadmintonProgram(
            id = "prog_beginner",
            level = SkillLevel.BEGINNER,
            title = "Beginner Training",
            badge = "FOUNDATIONS",
            description = "Master core rules, proper grips, ready stance, basic footwork, forehand/backhand strokes, and reliable serving.",
            focusSummary = "6 Core Modules • Ideal for new & returning players",
            moduleIds = listOf("beg_rules", "beg_grip", "beg_ready", "beg_footwork", "beg_strokes", "beg_serve")
        ),
        BadmintonProgram(
            id = "prog_intermediate",
            level = SkillLevel.INTERMEDIATE,
            title = "Intermediate Training",
            badge = "COURT CONTROL",
            description = "Develop steep smashes, deceptive drop shots, deep clears, tight net shots, solid defense, and smooth 6-corner movement.",
            focusSummary = "6 Core Modules • Rally consistency & shot variety",
            moduleIds = listOf("int_smash", "int_drop", "int_clear", "int_net", "int_defense", "int_movement")
        ),
        BadmintonProgram(
            id = "prog_advanced",
            level = SkillLevel.ADVANCED,
            title = "Advanced Training",
            badge = "ELITE MATCH",
            description = "Execute explosive jump smashes, hold-and-flick deception, rapid split-step recovery, fast defense, and tactical match combos.",
            focusSummary = "6 Core Modules • Tournament speed & tactical mastery",
            moduleIds = listOf("adv_jumpsmash", "adv_deception", "adv_footwork", "adv_defense", "adv_strategy", "adv_combos")
        )
    )

    val programExercises: List<BadmintonExercise> = listOf(
        // BEGINNER (6 modules)
        BadmintonExercise(
            id = "beg_rules",
            name = "Basic Badminton Rules",
            section = LessonSection.PROGRAM,
            categoryKey = SkillLevel.BEGINNER.name,
            categoryLabel = "Beginner Program",
            description = "Learn how rallies are scored, where the shuttle must land in singles vs doubles, and what counts as a fault.",
            instructions = listOf(
                "A match is played best of 3 games to 21 points using rally scoring (point scored on every serve).",
                "Serve diagonally across the net into the opponent's opposite service box below 1.15m waist height.",
                "Even score (0, 2, 4...) serves from the right box; odd score (1, 3, 5...) serves from the left box.",
                "In singles, the court is long and narrow; in doubles, the court is wide (and shorter on the serve only)."
            ),
            durationMinutes = 10,
            timerWorkSeconds = 180,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "3 shadow rule walk-throughs",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Memorize the inner side lines for singles and outer side lines for doubles.",
                "Never touch the net with your racket or body while the shuttle is in play."
            ),
            diagramType = CourtDiagramType.COURT_DIMENSIONS,
            motionPhases = listOf(
                "Check Score: Even = Right Box, Odd = Left Box",
                "Serve Diagonally into opposite box",
                "Rally Inside Singles/Doubles boundaries",
                "Point Won on every rally"
            )
        ),
        BadmintonExercise(
            id = "beg_grip",
            name = "Proper Racket Grip",
            section = LessonSection.PROGRAM,
            categoryKey = SkillLevel.BEGINNER.name,
            categoryLabel = "Beginner Program",
            description = "Build effortless wrist power by switching cleanly between the Forehand V-Grip and Backhand Thumb Grip.",
            instructions = listOf(
                "For the Forehand V-Grip, shake hands with the racket handle so the V between thumb and index finger sits on the top bevel.",
                "Keep a relaxed, light hold until the moment of impact—never strangle the grip.",
                "For the Backhand Thumb Grip, rotate the racket slightly and place your thumb flat on the wider back bevel.",
                "Practice 30 quick grip switches in front of a mirror without looking down."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 120,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "4 sets x 30 grip switches",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "A slight gap between your index and middle finger allows greater wrist snap.",
                "Avoid the 'panhandle' grip for overhead shots—it locks your forearm rotation."
            ),
            diagramType = CourtDiagramType.SERVE_ZONES,
            motionPhases = listOf(
                "Neutral Relaxed Hold at ready",
                "Forehand V-Grip on top bevel",
                "Rotate 15 degrees for Backhand",
                "Press Thumb flat for backhand snap"
            )
        ),
        BadmintonExercise(
            id = "beg_ready",
            name = "Ready Position & Split-Step",
            section = LessonSection.PROGRAM,
            categoryKey = SkillLevel.BEGINNER.name,
            categoryLabel = "Beginner Program",
            description = "Establish an athletic base stance in mid-court that lets you explode toward any of the six corners.",
            instructions = listOf(
                "Stand slightly behind the T-line in center court with feet wider than shoulder width.",
                "Bend knees deeply, lean chest slightly forward, and keep weight on the balls of your feet.",
                "Hold your racket in front of your chest, head up at eye level, non-racket arm balanced.",
                "Perform a micro split-step hop just before your opponent contacts the shuttle."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "5 sets x 45 sec holds & hops",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Keep your heels lightly off the floor so your calves act like coiled springs.",
                "Do not drop your racket head below waist height between shots."
            ),
            diagramType = CourtDiagramType.SIX_CORNER_FOOTWORK,
            motionPhases = listOf(
                "Base Position just behind T-line",
                "Knees Bent, heels lifted",
                "Racket Up in front of chest",
                "Split-Step micro hop on cue"
            )
        ),
        BadmintonExercise(
            id = "beg_footwork",
            name = "Basic Footwork",
            section = LessonSection.PROGRAM,
            categoryKey = SkillLevel.BEGINNER.name,
            categoryLabel = "Beginner Program",
            description = "Learn smooth chasse steps and balanced racket-leg lunges to reach the front net and side lines without crossing feet awkwardly.",
            instructions = listOf(
                "Start from central base in ready stance.",
                "Push off your back foot with a quick side chasse toward the forehand net corner.",
                "Finish with a controlled lunge on your racket leg, heel landing first, toe pointing toward the shuttle.",
                "Push off the front heel immediately and chasse back to base before repeating to the backhand net."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "4 sets x 12 corner touches",
            restTimeSeconds = 40,
            trainingTips = listOf(
                "Always lunge with your racket-side leg forward for maximum reach and balance.",
                "Keep your torso upright during the lunge so you can recover quickly."
            ),
            diagramType = CourtDiagramType.SIX_CORNER_FOOTWORK,
            motionPhases = listOf(
                "Center Base split-step",
                "Chasse Slide toward corner",
                "Heel-Toe Lunge with racket forward",
                "Push Back cleanly to center"
            )
        ),
        BadmintonExercise(
            id = "beg_strokes",
            name = "Forehand and Backhand",
            section = LessonSection.PROGRAM,
            categoryKey = SkillLevel.BEGINNER.name,
            categoryLabel = "Beginner Program",
            description = "Develop clean contact points and follow-through mechanics for basic forehand and backhand shadow swings.",
            instructions = listOf(
                "Forehand overhead: turn shoulders sideways, raise elbow, rotate forearm (pronation) through impact high above your head.",
                "Follow through smoothly across your non-racket hip.",
                "Backhand drive/clear: switch to thumb grip, lead with the elbow toward the shuttle, and snap the wrist via supination.",
                "Shadow swing 20 forehands and 20 backhands in rhythm with a 1-second pause at ready stance."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "4 sets x 20 forehand + 20 backhand",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Hit the shuttle at the highest comfortable reach in front of your body.",
                "Power comes from forearm rotation, not a stiff shoulder swing."
            ),
            diagramType = CourtDiagramType.CLEAR_AND_DROP,
            motionPhases = listOf(
                "Turn Sideways & load non-racket arm",
                "Lead Elbow upward toward shuttle",
                "Forearm Pronation / Thumb Snap",
                "Follow Through across body"
            )
        ),
        BadmintonExercise(
            id = "beg_serve",
            name = "Basic Serving",
            section = LessonSection.PROGRAM,
            categoryKey = SkillLevel.BEGINNER.name,
            categoryLabel = "Beginner Program",
            description = "Master consistent, legal low backhand serves and high forehand singles serves that start every rally on your terms.",
            instructions = listOf(
                "Backhand low serve: stand near the front service line, racket foot forward, hold shuttle by the feathers below waist.",
                "Use a short, smooth forward push from the elbow and thumb—aim to skim 5cm over the net tape.",
                "Forehand high serve: stand 1.5m behind the service line, drop shuttle, and swing smoothly upward to land deep near the back boundary.",
                "Practice 25 low serves and 15 high serves aiming at court corner targets."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 120,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "3 sets x 20 serves",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Keep the racket head pointing downward below your wrist at contact to avoid service faults.",
                "Breathe out slowly before initiating your serve for steady hands."
            ),
            diagramType = CourtDiagramType.SERVE_ZONES,
            motionPhases = listOf(
                "Stance near T-line, weight forward",
                "Hold Feathers below 1.15m waist",
                "Smooth Forward Push over net",
                "Racket Up ready for return"
            )
        ),

        // INTERMEDIATE (6 modules)
        BadmintonExercise(
            id = "int_smash",
            name = "Smash Technique",
            section = LessonSection.PROGRAM,
            categoryKey = SkillLevel.INTERMEDIATE.name,
            categoryLabel = "Intermediate Program",
            description = "Generate steep, penetrating smashes using full body rotation, scissor kick footwork, and explosive forearm pronation.",
            instructions = listOf(
                "Move behind the shuttle early and turn sideways with non-racket arm pointing up toward the shuttle.",
                "Drop the racket behind your back into the 'trophy pose' while loading weight onto your rear leg.",
                "Rotate hips and shoulders forward while executing a scissor kick, striking the shuttle at peak height in front of your forehead.",
                "Snap the wrist through pronation for a steep downward angle, landing on your non-racket foot to recover forward."
            ),
            durationMinutes = 18,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "5 sets x 12 shadow/multi-shuttle smashes",
            restTimeSeconds = 45,
            trainingTips = listOf(
                "Squeeze the grip tight ONLY at the exact millisecond of impact for maximum whip.",
                "Steepness beats raw speed—contact the shuttle slightly in front of your body."
            ),
            diagramType = CourtDiagramType.SMASH_TRAJECTORY,
            motionPhases = listOf(
                "Sideways Trophy Load behind shuttle",
                "Hip & Chest Rotation + Scissor Kick",
                "Peak Contact with Forearm Pronation",
                "Land Forward & recover to mid-court"
            )
        ),
        BadmintonExercise(
            id = "int_drop",
            name = "Drop Shots",
            section = LessonSection.PROGRAM,
            categoryKey = SkillLevel.INTERMEDIATE.name,
            categoryLabel = "Intermediate Program",
            description = "Disguise fast and slow drop shots with the exact same preparation as your overhead clear and smash.",
            instructions = listOf(
                "Prepare sideways with the exact same high elbow and racket backswing used for a smash.",
                "At impact, decelerate the racket head slightly and slice gently across the cork.",
                "Aim for the shuttle to peak just above the net and drop tightly between the net and short service line.",
                "Alternate 10 straight drops and 10 cross-court drops to build touch and deception."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 20 shadow/feed drops",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Keep your swing speed identical until the last 20cm before contact to hide your intention.",
                "Follow every drop shot immediately toward mid-court to cover a tight net reply."
            ),
            diagramType = CourtDiagramType.CLEAR_AND_DROP,
            motionPhases = listOf(
                "Full Overhead Preparation (like smash)",
                "Gentle Slice & cushion at contact",
                "Tight Arc skimming net tape",
                "Step Forward to cover net reply"
            )
        ),
        BadmintonExercise(
            id = "int_clear",
            name = "Clear Shots",
            section = LessonSection.PROGRAM,
            categoryKey = SkillLevel.INTERMEDIATE.name,
            categoryLabel = "Intermediate Program",
            description = "Push opponents deep into their rear corners using high defensive clears for recovery time and flat attacking clears to rush them.",
            instructions = listOf(
                "Get behind the shuttle early using chasse and pivot steps.",
                "Hit high defensive clears with a slightly open racket face so the shuttle arcs high and drops vertically on the back line.",
                "Hit attacking clears flatter and faster just out of the opponent's overhead reach.",
                "Follow through completely and recover to base immediately."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 16 rear-corner clears",
            restTimeSeconds = 35,
            trainingTips = listOf(
                "Aim 30cm inside the back singles boundary so you don't drift long under pressure.",
                "Use high clears when out of position to buy 1.5 seconds of recovery time."
            ),
            diagramType = CourtDiagramType.CLEAR_AND_DROP,
            motionPhases = listOf(
                "Pivot Back & get behind shuttle",
                "Full Extension overhead strike",
                "High Trajectory to rear tramline",
                "Recover to Center Base"
            )
        ),
        BadmintonExercise(
            id = "int_net",
            name = "Net Shots",
            section = LessonSection.PROGRAM,
            categoryKey = SkillLevel.INTERMEDIATE.name,
            categoryLabel = "Intermediate Program",
            description = "Spin tumbling hairpin net shots and sharp cross-court net replies that force opponents to lift defensively.",
            instructions = listOf(
                "Lunge cleanly to the net with your racket arm extended in front of your body.",
                "Take the shuttle as high as possible near the top of the net tape.",
                "Brush gently across the cork with relaxed fingers to create a tumbling spin over the net.",
                "Stay balanced on your lunge leg and push back to mid-court with racket still high."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 20 net lunges",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Keep your elbow slightly bent and fingers soft—never jab stiffly at the shuttle.",
                "Watch the opponent's racket head after your net shot to anticipate a quick net kill or lift."
            ),
            diagramType = CourtDiagramType.NET_HAIRPIN,
            motionPhases = listOf(
                "Split-Step & forward lunge",
                "High Take near net tape",
                "Soft Brush for tumbling spin",
                "Recover with Racket Up"
            )
        ),
        BadmintonExercise(
            id = "int_defense",
            name = "Defensive Techniques",
            section = LessonSection.PROGRAM,
            categoryKey = SkillLevel.INTERMEDIATE.name,
            categoryLabel = "Intermediate Program",
            description = "Absorb heavy smashes with a low, wide defensive stance and redirect shuttles cleanly via short blocks or deep lifts.",
            instructions = listOf(
                "Drop into a low defensive squat with feet wide and racket held out in front of your torso using a backhand thumb grip.",
                "Keep your racket head slightly angled forward so you can cover both forehand hip and body shots.",
                "To block: soften your grip at impact so the shuttle drops gently over the net.",
                "To lift: add a compact wrist snap upward to drive the shuttle deep to the opponent's back line."
            ),
            durationMinutes = 16,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "5 sets x 45 sec defensive blocks/lifts",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Keep backswing minimal—short compact movements beat wide swings against fast smashes.",
                "Default to backhand grip in defense; it covers 75% of body and left-hip attacks."
            ),
            diagramType = CourtDiagramType.DEFENSE_BLOCK,
            motionPhases = listOf(
                "Low Wide Squat, racket in front",
                "Backhand Thumb Grip ready",
                "Compact Block or Wrist Lift",
                "Reset Stance immediately"
            )
        ),
        BadmintonExercise(
            id = "int_movement",
            name = "Court Movement",
            section = LessonSection.PROGRAM,
            categoryKey = SkillLevel.INTERMEDIATE.name,
            categoryLabel = "Intermediate Program",
            description = "Connect all 6 corners of the court into one seamless, rhythmic shadow movement flow.",
            instructions = listOf(
                "Start at central base with a crisp split-step.",
                "Move in order: Front Forehand -> Front Backhand -> Mid Forehand -> Mid Backhand -> Rear Forehand -> Rear Backhand.",
                "Execute a shadow shot at each corner before recovering smoothly to center base.",
                "Maintain steady breathing and upright posture across the full 60-second set."
            ),
            durationMinutes = 18,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "5 sets x 60 sec continuous flow",
            restTimeSeconds = 40,
            trainingTips = listOf(
                "Use 2–3 efficient steps per corner rather than tiny hurried steps.",
                "Never pause inside a corner—your lunge should rebound right back to base."
            ),
            diagramType = CourtDiagramType.SIX_CORNER_FOOTWORK,
            motionPhases = listOf(
                "Split-Step at Center Base",
                "Front Corners: Chasse + Lunge",
                "Mid Sides: Quick Lateral Push",
                "Rear Corners: Pivot + Scissor Kick"
            )
        ),

        // ADVANCED (6 modules)
        BadmintonExercise(
            id = "adv_jumpsmash",
            name = "Jump Smash",
            section = LessonSection.PROGRAM,
            categoryKey = SkillLevel.ADVANCED.name,
            categoryLabel = "Advanced Program",
            description = "Unleash maximum vertical height and torso torque to hit devastating steep jump smashes with rapid landing recovery.",
            instructions = listOf(
                "Track a high lift early, plant both feet slightly staggered behind the shuttle, and coil knees downward.",
                "Explode vertically while arching your chest and pulling your knees slightly up to load your core.",
                "Snap your torso forward and pronate your forearm at the apex of the jump for a sharp downward angle.",
                "Land softly on both balls of the feet, absorb the impact, and step forward into the court."
            ),
            durationMinutes = 20,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "5 sets x 8 explosive jump smashes",
            restTimeSeconds = 60,
            trainingTips = listOf(
                "Do not jump straight up under the shuttle—jump slightly backward so contact stays in front of you.",
                "Quality over volume: stop the set if jump height drops."
            ),
            diagramType = CourtDiagramType.SMASH_TRAJECTORY,
            motionPhases = listOf(
                "Plant & Coil behind shuttle",
                "Vertical Take-off & Core Arch",
                "Apex Strike with Steep Angle",
                "Soft Two-Foot Land & Advance"
            )
        ),
        BadmintonExercise(
            id = "adv_deception",
            name = "Deceptive Shots",
            section = LessonSection.PROGRAM,
            categoryKey = SkillLevel.ADVANCED.name,
            categoryLabel = "Advanced Program",
            description = "Freeze opponents at the split-step using hold-and-flick net shots, reverse slices, and late cross-court wrist changes.",
            instructions = listOf(
                "Net hold & flick: lunge as if playing a tight hairpin net shot, pause 0.2s as the opponent leans in, then flick past them with your fingers.",
                "Overhead reverse slice: prepare for a straight smash, then slice across the outside of the shuttle to send a sharp cross-court drop.",
                "Keep your head, eyes, and shoulder alignment identical until impact.",
                "Alternate deceptive variations every 3rd shadow rep."
            ),
            durationMinutes = 18,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "4 sets x 16 deceptive patterns",
            restTimeSeconds = 40,
            trainingTips = listOf(
                "Deception only works if your standard basic strokes are already threatening.",
                "Use finger power (tightening index/thumb) rather than big arm movements for late flicks."
            ),
            diagramType = CourtDiagramType.NET_HAIRPIN,
            motionPhases = listOf(
                "Identical Approach & Lunge",
                "Micro Hold to read opponent",
                "Sudden Finger Flick / Reverse Slice",
                "Recover Ready for weak return"
            )
        ),
        BadmintonExercise(
            id = "adv_footwork",
            name = "Advanced Footwork",
            section = LessonSection.PROGRAM,
            categoryKey = SkillLevel.ADVANCED.name,
            categoryLabel = "Advanced Program",
            description = "Master round-the-head China jumps, late backhand lunges, and high-tempo split-step recovery at match pace.",
            instructions = listOf(
                "Practice explosive lateral China Jumps off the racket leg to intercept flat clears on the forehand side.",
                "Execute deep backhand rear-corner lunges followed by an immediate two-step recovery toward mid-court.",
                "Add randomized directional cues (or mental calls) between corners to train unpredictable transitions.",
                "Keep your center of gravity low and smooth throughout."
            ),
            durationMinutes = 20,
            timerWorkSeconds = 45,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "6 sets x 45 sec high-tempo drills",
            restTimeSeconds = 45,
            trainingTips = listOf(
                "Keep your split-step timed to the opponent's racket impact, never too late.",
                "On China Jumps, push sideways—not high—so you land balanced and ready."
            ),
            diagramType = CourtDiagramType.SIX_CORNER_FOOTWORK,
            motionPhases = listOf(
                "Reactive Split-Step on cue",
                "Lateral China Jump interception",
                "Deep Rear Lunge & clean clear",
                "Explosive Push to center base"
            )
        ),
        BadmintonExercise(
            id = "adv_defense",
            name = "Fast Defensive Reactions",
            section = LessonSection.PROGRAM,
            categoryKey = SkillLevel.ADVANCED.name,
            categoryLabel = "Advanced Program",
            description = "Turn opponent smashes into counter-attacks with rapid flat drives, cross-court blocks, and wall reflex volleys.",
            instructions = listOf(
                "Stand 1.5m from a flat wall and hit continuous rapid backhand/forehand flat drives at chest height.",
                "Alternate straight and angled wall rebounds to force rapid grip micro-adjustments.",
                "Transition to court shadow defense: absorb 2 smashes then drive flat past the net player.",
                "Track the shuttle early out of the opponent's racket strings."
            ),
            durationMinutes = 18,
            timerWorkSeconds = 45,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "6 sets x 45 sec fast wall/shadow drives",
            restTimeSeconds = 35,
            trainingTips = listOf(
                "Keep your racket head up and elbows slightly away from your ribs for quick horizontal sweeps.",
                "Use short finger squeezes instead of long arm swings."
            ),
            diagramType = CourtDiagramType.DEFENSE_BLOCK,
            motionPhases = listOf(
                "Low Athletic Stance",
                "Early Visual Track off strings",
                "Compact Counter-Drive or Cross Block",
                "Instant Racket Reset"
            )
        ),
        BadmintonExercise(
            id = "adv_strategy",
            name = "Match Strategies",
            section = LessonSection.PROGRAM,
            categoryKey = SkillLevel.ADVANCED.name,
            categoryLabel = "Advanced Program",
            description = "Control rallies tactically in Singles (4-corner stretch & attack) and Doubles (front-back attack vs side-by-side defense).",
            instructions = listOf(
                "Singles pattern: push deep to backhand corner -> follow straight drop -> finish open forehand space.",
                "Doubles attack: when partner smashes from the rear, front player covers net tape with racket high to kill loose blocks.",
                "Doubles defense: shift side-by-side in the mid-court when lifting high, covering straight and cross-court lanes.",
                "Shadow-rehearse 3 full 6-shot tactical sequences for both singles and doubles."
            ),
            durationMinutes = 18,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "4 sets x 6 tactical sequences",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Attack the opponent's hitting shoulder/hip to jam their backswing.",
                "In doubles, whoever lifts the shuttle takes the defensive lane straight ahead."
            ),
            diagramType = CourtDiagramType.DOUBLES_ROTATION,
            motionPhases = listOf(
                "Serve & Third-Shot Setup",
                "Front-Back Attacking Formation",
                "Side-by-Side Defensive Shift",
                "Rotate Smoothly on Transition"
            )
        ),
        BadmintonExercise(
            id = "adv_combos",
            name = "Combination Drills",
            section = LessonSection.PROGRAM,
            categoryKey = SkillLevel.ADVANCED.name,
            categoryLabel = "Advanced Program",
            description = "Link full multi-shot winning combinations: Serve -> Net Push -> Rear Jump Smash -> Follow-up Net Kill.",
            instructions = listOf(
                "Combo A (Attack): Low Serve -> Split-Step -> Rear Smash -> Sprint Forward -> Net Kill.",
                "Combo B (Counter): Defensive Lift -> Side-Step Block -> Tight Cross Net -> Rear Attacking Clear.",
                "Execute each 4-shot combination cleanly at 90% game intensity with full footwork.",
                "Rest 45 seconds between sets to preserve sharp footwork quality."
            ),
            durationMinutes = 20,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "5 sets x 6 full 4-shot combos",
            restTimeSeconds = 45,
            trainingTips = listOf(
                "Always follow a smash forward—expect a short defensive block.",
                "Keep your transitions seamless without stopping between shots."
            ),
            diagramType = CourtDiagramType.SMASH_TRAJECTORY,
            motionPhases = listOf(
                "1. Low Serve / Defense Setup",
                "2. Transition to Rear Court",
                "3. Heavy Smash / Attacking Drop",
                "4. Rush Forward & Net Kill"
            )
        )
    )

    val rulesLessons: List<BadmintonExercise> = listOf(
        BadmintonExercise(
            id = "rule_overview",
            name = "Badminton Rules Overview",
            section = LessonSection.RULES,
            categoryKey = "RULES",
            categoryLabel = "Rules & Learning",
            description = "Complete guide to official BWF badminton match flow, coin toss, intervals, and rally rules.",
            instructions = listOf(
                "Before play, toss a coin or spin a shuttle: the winner chooses either to serve/receive first OR which side of the court to start on.",
                "Players may hit the shuttle only once before it crosses the net (no double hits or carrying).",
                "A shuttle hitting the net and crossing into the valid opponent court during play or serve is LIVE (play continues).",
                "A 60-second interval occurs when the leading score reaches 11 points, and a 2-minute interval between games."
            ),
            durationMinutes = 8,
            timerWorkSeconds = 120,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "Review & visualize 2x",
            restTimeSeconds = 15,
            trainingTips = listOf(
                "Lines are considered IN—if any part of the cork touches the boundary line, the shuttle is good.",
                "Call 'let' immediately if an external shuttle enters your court during a rally."
            ),
            diagramType = CourtDiagramType.COURT_DIMENSIONS,
            motionPhases = listOf(
                "Coin Toss: Serve or Side",
                "Rally Play: 1 Hit per side",
                "Interval at 11 pts (60s)",
                "Change Ends between games"
            )
        ),
        BadmintonExercise(
            id = "rule_court",
            name = "Court Measurements",
            section = LessonSection.RULES,
            categoryKey = "RULES",
            categoryLabel = "Rules & Learning",
            description = "Exact official dimensions of a standard badminton court, net height, and service box boundaries.",
            instructions = listOf(
                "Full Length: 13.40 meters (44 ft) overall, divided into two 6.70m halves by the center net.",
                "Width: 6.10 meters (20 ft) for Doubles (outer tramlines) and 5.18 meters (17 ft) for Singles (inner tramlines).",
                "Short Service Line sits 1.98m from the net on each side; the area between is the non-volley/short zone on serves.",
                "Doubles Long Service Line is 0.76m inside the back boundary; Net height is 1.524m at the center and 1.55m at the posts."
            ),
            durationMinutes = 8,
            timerWorkSeconds = 120,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "Study court blueprint",
            restTimeSeconds = 15,
            trainingTips = listOf(
                "Every line is 40mm wide and painted white or yellow for maximum visibility.",
                "Always know where the 1.98m short service line is—serves landing short are faults."
            ),
            diagramType = CourtDiagramType.COURT_DIMENSIONS,
            motionPhases = listOf(
                "Length: 13.40m | Doubles Width: 6.10m",
                "Singles Width: 5.18m (inner lines)",
                "Short Service Line: 1.98m from net",
                "Net Height: 1.524m center / 1.55m posts"
            )
        ),
        BadmintonExercise(
            id = "rule_singles_doubles",
            name = "Singles and Doubles",
            section = LessonSection.RULES,
            categoryKey = "RULES",
            categoryLabel = "Rules & Learning",
            description = "Understand the key boundary, serving, and partner positioning differences between Singles and Doubles matches.",
            instructions = listOf(
                "Singles Court: Long and narrow (uses inner side lines and outermost back boundary line for both serve and rally).",
                "Doubles Serve Box: Wide and short (uses outer side lines, but the inner back doubles service line).",
                "Doubles Rally Court: Full court (once the serve is returned, ALL outer side and back boundaries are in play).",
                "In Doubles, only the diagonal receiver may return the serve; partners switch boxes ONLY when their own side wins a point while serving."
            ),
            durationMinutes = 8,
            timerWorkSeconds = 120,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "Walk both boundary sets",
            restTimeSeconds = 15,
            trainingTips = listOf(
                "Remember the Doubles serve phrase: 'Wide and Short on the serve, Full court in the rally.'",
                "In Doubles, there is no 'second server'—each team gets one service turn per side-out."
            ),
            diagramType = CourtDiagramType.DOUBLES_ROTATION,
            motionPhases = listOf(
                "Singles: Long & Narrow always",
                "Doubles Serve: Wide & Short",
                "Doubles Rally: Full Outer Court",
                "Serve Side: Even=Right, Odd=Left"
            )
        ),
        BadmintonExercise(
            id = "rule_scoring",
            name = "Scoring System",
            section = LessonSection.RULES,
            categoryKey = "RULES",
            categoryLabel = "Rules & Learning",
            description = "Master the 21-point rally scoring system, deuce 'setting' rules, and match-deciding 3rd game rules.",
            instructions = listOf(
                "Best of 3 games; first side to 21 points wins a game (points are scored on every rally regardless of who served).",
                "If the score reaches 20-20, you must win by 2 clear points (e.g., 22-20, 24-22).",
                "If the score reaches 29-29, the side that wins the 30th point wins the game immediately (30-29 cap).",
                "In the 3rd deciding game, players switch court ends once the leading score reaches 11 points."
            ),
            durationMinutes = 6,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "Practice score calling",
            restTimeSeconds = 15,
            trainingTips = listOf(
                "Always announce the server's score first before serving in casual matches.",
                "Whoever wins a rally serves next—keep track of even/odd boxes automatically."
            ),
            diagramType = CourtDiagramType.COURT_DIMENSIONS,
            motionPhases = listOf(
                "Rally Scoring to 21 Points",
                "20-20 Deuce: Win by 2 points",
                "29-29 Sudden Death: First to 30",
                "Game 3: Switch ends at 11"
            )
        ),
        BadmintonExercise(
            id = "rule_faults",
            name = "Faults and Violations",
            section = LessonSection.RULES,
            categoryKey = "RULES",
            categoryLabel = "Rules & Learning",
            description = "Avoid losing free points by learning every common net, racket, body, and shuttle fault.",
            instructions = listOf(
                "Net Touch Fault: Touching the net or posts with your racket, clothing, or body while the shuttle is in play.",
                "Invasion Fault: Reaching your racket over the net to hit a shuttle before it has crossed to your side (follow-through over the net after hitting on your side IS legal).",
                "Double Hit / Carry: Striking the shuttle twice in one stroke or catching/slinging it on the strings.",
                "Out / Obstruction: Shuttle lands outside boundary lines, passes under the net, hits ceiling/walls, or touches a player's body."
            ),
            durationMinutes = 8,
            timerWorkSeconds = 120,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "Checklist review",
            restTimeSeconds = 15,
            trainingTips = listOf(
                "In doubles, if both partners swing and touch the shuttle once each, it is a fault.",
                "If your opponent hits a clear going way out, let it drop—catching it before it bounces is a fault against YOU!"
            ),
            diagramType = CourtDiagramType.NET_HAIRPIN,
            motionPhases = listOf(
                "No Net or Post Contact",
                "Contact Shuttle on Your Side",
                "Single Clean Hit (No carry)",
                "Let Out-Bound Shuttles Drop"
            )
        ),
        BadmintonExercise(
            id = "rule_service",
            name = "Service Rules",
            section = LessonSection.RULES,
            categoryKey = "RULES",
            categoryLabel = "Rules & Learning",
            description = "Serve cleanly and legally under the modern 1.15m fixed-height BWF service law.",
            instructions = listOf(
                "Both feet of the server and receiver must stay in contact with the court surface inside their boxes until impact.",
                "Under BWF rules, the entire shuttle must be below 1.15 meters from the court surface at the instant of contact.",
                "The server's racket must move continuously forward once the serve begins—no feints, pauses, or backward double-pumps.",
                "Wait until the receiver is ready before starting your service motion."
            ),
            durationMinutes = 8,
            timerWorkSeconds = 120,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "20 legal serve checks",
            restTimeSeconds = 20,
            trainingTips = listOf(
                "Never lift your rear toe completely off the floor before hitting the shuttle.",
                "If you miss the shuttle completely during a service swing, it counts as a fault."
            ),
            diagramType = CourtDiagramType.SERVE_ZONES,
            motionPhases = listOf(
                "Both Feet Grounded & Stationary",
                "Shuttle Below 1.15m at impact",
                "One Continuous Forward Swing",
                "Land Inside Diagonal Box"
            )
        ),
        BadmintonExercise(
            id = "rule_equipment",
            name = "Equipment and Racket Knowledge",
            section = LessonSection.RULES,
            categoryKey = "RULES",
            categoryLabel = "Rules & Learning",
            description = "Choose the right racket weight, balance point, string tension, grip size, and shuttlecock type for your play style.",
            instructions = listOf(
                "Racket Weight: 3U (85–89g) offers solid power; 4U (80–84g) is the versatile standard; 5U (75–79g) maximizes net speed.",
                "Balance Point: Head-Heavy boosts smashes; Even-Balance suits all-round singles; Head-Light accelerates doubles defense.",
                "String Tension: Beginners use 20–22 lbs (larger sweet spot & power); Intermediates 23–25 lbs; Advanced 26–30+ lbs for crisp control.",
                "Shuttlecocks: Natural goose/duck feather shuttles offer elite flight and spin; Nylon shuttles last far longer for outdoor or casual drills."
            ),
            durationMinutes = 8,
            timerWorkSeconds = 120,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "Gear inspection checklist",
            restTimeSeconds = 15,
            trainingTips = listOf(
                "Replace overgrips every 2–3 weeks so sweat never causes racket twist on smashes.",
                "Higher string tension does NOT automatically give more power—it demands faster swing speed."
            ),
            diagramType = CourtDiagramType.SMASH_TRAJECTORY,
            motionPhases = listOf(
                "Weight: 4U (82g) All-Round",
                "Balance: Heavy / Even / Light",
                "Tension: 21-24 lbs Control/Power",
                "Fresh Overgrip for Clean Snap"
            )
        )
    )
}
