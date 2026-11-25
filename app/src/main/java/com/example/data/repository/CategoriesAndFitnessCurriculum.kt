package com.example.data.repository

import com.example.data.model.BadmintonExercise
import com.example.data.model.CourtDiagramType
import com.example.data.model.FitnessArea
import com.example.data.model.LessonSection
import com.example.data.model.SkillLevel
import com.example.data.model.TrainingCategory

object CategoriesAndFitnessCurriculum {

    val categoryExercises: List<BadmintonExercise> = listOf(
        // 1. FOOTWORK TRAINING
        BadmintonExercise(
            id = "cat_foot_6corner",
            name = "6-Corner Shadow Footwork",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.FOOTWORK.name,
            categoryLabel = TrainingCategory.FOOTWORK.displayName,
            description = "Build smooth, effortless court coverage across all six corners from the central base position.",
            instructions = listOf(
                "Begin in low athletic stance slightly behind the center T.",
                "Split-step and move to Front Forehand -> recover -> Front Backhand -> recover.",
                "Continue to Mid Forehand, Mid Backhand, Rear Forehand, and Rear Backhand corners.",
                "Execute a shadow swing at every corner before pushing cleanly back to base."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "5 sets x 60 sec continuous shadow",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Land heel-first on front lunges to protect your knees and spring back faster.",
                "Keep your eyes level—avoid bouncing up and down as you slide."
            ),
            diagramType = CourtDiagramType.SIX_CORNER_FOOTWORK,
            motionPhases = listOf(
                "Center Base Split-Step",
                "Chasse Slide to target corner",
                "Balanced Lunge & Shadow Swing",
                "Push Back to Center T"
            )
        ),
        BadmintonExercise(
            id = "cat_foot_split",
            name = "Explosive Split-Step & Recovery",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.FOOTWORK.name,
            categoryLabel = TrainingCategory.FOOTWORK.displayName,
            description = "Train reactive first-step speed by pairing micro split-steps with rapid directional bursts.",
            instructions = listOf(
                "Perform a quick wide-stance split-step on the balls of your feet every 2 seconds.",
                "Immediately explode 2 steps diagonally forward or backward as if chasing a shuttle.",
                "Touch the imaginary contact point with your racket, then recover to center within 2 steps.",
                "Alternate left and right sides each repetition."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 45,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "6 sets x 12 directional bursts",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Land slightly wider than shoulder width on the split-step so you can push off either foot.",
                "Keep your racket tip higher than your wrist at all times."
            ),
            diagramType = CourtDiagramType.SIX_CORNER_FOOTWORK,
            motionPhases = listOf(
                "Micro Hop Split-Step",
                "Instant Weight Shift",
                "2-Step Diagonal Burst",
                "Rapid Base Recovery"
            )
        ),
        BadmintonExercise(
            id = "cat_foot_chinajump",
            name = "Round-the-Head & China Jump",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.FOOTWORK.name,
            categoryLabel = TrainingCategory.FOOTWORK.displayName,
            description = "Intercept fast attacking clears on your backhand/forehand side without turning your back to the net.",
            instructions = listOf(
                "From base, push laterally off your left foot toward the rear backhand corner for a round-the-head overhead strike.",
                "On the forehand mid-rear side, execute a low sideways China Jump off the racket leg.",
                "Strike cleanly in mid-air and land with feet staggered to accelerate back to center.",
                "Maintain tight balance without leaning backward."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 45,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "5 sets x 10 lateral jumps",
            restTimeSeconds = 40,
            trainingTips = listOf(
                "Keep your non-racket arm tucked for balance during sideways leaps.",
                "Focus on horizontal speed rather than jumping unnecessarily high."
            ),
            diagramType = CourtDiagramType.SIX_CORNER_FOOTWORK,
            motionPhases = listOf(
                "Read Flat Clear Early",
                "Lateral Push / China Jump",
                "Mid-Air Overhead Intercept",
                "Rebound Landing to Center"
            )
        ),

        // 2. SMASH TRAINING
        BadmintonExercise(
            id = "cat_smash_full",
            name = "Full Power Scissor-Kick Smash",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.SMASH.name,
            categoryLabel = TrainingCategory.SMASH.displayName,
            description = "Coordinate rear-court footwork, full chest rotation, and forearm pronation for heavy attacking smashes.",
            instructions = listOf(
                "Chasse backward, turn sideways, and raise your non-racket arm to track the shuttle.",
                "Rotate hips and shoulders forward as you swap feet with a crisp scissor kick.",
                "Strike at peak extension in front of your forehead using full forearm pronation.",
                "Follow through downward and step forward on your non-racket leg."
            ),
            durationMinutes = 16,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "5 sets x 12 smashes",
            restTimeSeconds = 45,
            trainingTips = listOf(
                "Relax your shoulder until impact—tension slows down racket head speed.",
                "Target the opponent's racket hip or sideline."
            ),
            diagramType = CourtDiagramType.SMASH_TRAJECTORY,
            motionPhases = listOf(
                "Sideways Trophy Coil",
                "Scissor Kick Weight Transfer",
                "Forearm Pronation Snap",
                "Step Forward to Net Cover"
            )
        ),
        BadmintonExercise(
            id = "cat_smash_stick",
            name = "Stick Smash & Half Smash",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.SMASH.name,
            categoryLabel = TrainingCategory.SMASH.displayName,
            description = "Deliver sudden, steep half-smashes with minimal backswing so you can recover immediately for the next shot.",
            instructions = listOf(
                "Use a compact overhead swing and stop your racket sharply right after impact ('stick' finish).",
                "Focus on crisp finger tightening and steep downward angles landing around mid-court.",
                "Alternate between straight stick smashes and cross-court angle smashes.",
                "Recover immediately to T-position after each rep."
            ),
            durationMinutes = 14,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 15 stick smashes",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Stick smashes save energy and preserve recovery speed in long singles rallies.",
                "Angle the shuttle sharply toward the sidelines to pull defenders wide."
            ),
            diagramType = CourtDiagramType.SMASH_TRAJECTORY,
            motionPhases = listOf(
                "Compact Overhead Load",
                "Finger-Tightened Snap",
                "Sudden Stick Follow-Through",
                "Immediate Center Recovery"
            )
        ),
        BadmintonExercise(
            id = "cat_smash_jump",
            name = "Explosive Jump Smash Conditioning",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.SMASH.name,
            categoryLabel = TrainingCategory.SMASH.displayName,
            description = "Combine vertical jump power with rapid mid-air shoulder rotation for tournament-grade finishing shots.",
            instructions = listOf(
                "Plant both feet behind the shuttle line and sink briefly into a quarter-squat.",
                "Jump explosively while tucking heels and coiling chest sideways.",
                "Unwind core and smash steeply downward at the peak of your jump.",
                "Land softly on both toes and transition smoothly into a net-kill lunge."
            ),
            durationMinutes = 18,
            timerWorkSeconds = 45,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "5 sets x 8 jump smashes",
            restTimeSeconds = 50,
            trainingTips = listOf(
                "Keep your landing quiet—loud heavy landings stress your knees.",
                "Always pair your jump smash with a forward step for the third shot."
            ),
            diagramType = CourtDiagramType.SMASH_TRAJECTORY,
            motionPhases = listOf(
                "Quarter-Squat Load",
                "Vertical Take-Off & Arch",
                "Apex Pronation Strike",
                "Quiet Landing & Net Follow"
            )
        ),

        // 3. SERVING TRAINING
        BadmintonExercise(
            id = "cat_serve_low",
            name = "Backhand Low Short Serve",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.SERVING.name,
            categoryLabel = TrainingCategory.SERVING.displayName,
            description = "Develop pinpoint consistency skimming the shuttle tightly over the net tape onto the short service line.",
            instructions = listOf(
                "Stand near the center T with racket foot forward and weight slightly over the front toe.",
                "Hold the shuttle skirt gently in front of your waist using backhand thumb grip.",
                "Push forward in one smooth motion from the elbow, aiming for the shuttle to peak at the net tape.",
                "Score yourself: count how many out of 20 land within 15cm past the short service line."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "4 sets x 20 precision serves",
            restTimeSeconds = 20,
            trainingTips = listOf(
                "Never flick your wrist upward on a low serve—keep the racket face steady.",
                "Immediately raise your racket to chest height after serving."
            ),
            diagramType = CourtDiagramType.SERVE_ZONES,
            motionPhases = listOf(
                "Front-Foot Weight Load",
                "Hold Shuttle Below 1.15m",
                "Smooth Controlled Push",
                "Raise Racket for 3rd Shot"
            )
        ),
        BadmintonExercise(
            id = "cat_serve_flick",
            name = "Deceptive Backhand Flick Serve",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.SERVING.name,
            categoryLabel = "Serving Training",
            description = "Punish receivers who creep too close to the front line using an identical setup followed by a late thumb snap.",
            instructions = listOf(
                "Adopt the exact same stance, grip, and slow backswing as your low short serve.",
                "At the final 10cm before impact, snap your thumb and fingers explosively upward.",
                "Aim high enough to clear the receiver's outstretched racket and drop inside the back doubles service line.",
                "Mix 4 low serves with 1 surprise flick serve."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 15 mixed serves",
            restTimeSeconds = 25,
            trainingTips = listOf(
                "Do not telegraph the flick by pulling your racket farther back—use finger speed only.",
                "Keep the flick low enough that it lands fast, not floating slowly."
            ),
            diagramType = CourtDiagramType.SERVE_ZONES,
            motionPhases = listOf(
                "Identical Low-Serve Setup",
                "Smooth Forward Approach",
                "Late Thumb & Finger Snap",
                "Drop Inside Rear Line"
            )
        ),
        BadmintonExercise(
            id = "cat_serve_high",
            name = "Forehand High Deep Singles Serve",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.SERVING.name,
            categoryLabel = TrainingCategory.SERVING.displayName,
            description = "Send the shuttle soaring vertically down onto the singles back boundary line to neutralize aggressive receivers.",
            instructions = listOf(
                "Stand 1.5m behind the short service line with non-racket foot forward.",
                "Drop the shuttle cleanly and swing smoothly upward with forearm rotation.",
                "Follow through high across your opposite shoulder.",
                "Aim for the shuttle to drop almost straight down within 30cm of the back line."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "3 sets x 20 deep serves",
            restTimeSeconds = 25,
            trainingTips = listOf(
                "A high vertical drop forces the opponent to generate all their own smash power.",
                "Contact the shuttle below waist height to keep every serve 100% legal."
            ),
            diagramType = CourtDiagramType.SERVE_ZONES,
            motionPhases = listOf(
                "Staggered Stance 1.5m Back",
                "Clean Release & Underhand Swing",
                "High Arc Trajectory",
                "Vertical Drop on Back Line"
            )
        ),

        // 4. DEFENSIVE TRAINING
        BadmintonExercise(
            id = "cat_def_block",
            name = "Soft Smash Block & Net Reset",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.DEFENSIVE.name,
            categoryLabel = TrainingCategory.DEFENSIVE.displayName,
            description = "Take the pace off steep smashes and drop the shuttle gently near the net to flip attack into defense.",
            instructions = listOf(
                "Hold a low, wide stance with knees bent and backhand grip ready.",
                "Push your racket forward to meet the shuttle in front of your body rather than beside it.",
                "Relax your fingers on impact so the shuttle rebounds softly just over the net.",
                "Immediately step forward to cover the net corner."
            ),
            durationMinutes = 14,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "5 sets x 45 sec block shadows",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Avoid swinging backward on fast smashes—let the shuttle's own speed do the work.",
                "Cross-court blocks open up huge space when the smasher is stuck in one rear corner."
            ),
            diagramType = CourtDiagramType.DEFENSE_BLOCK,
            motionPhases = listOf(
                "Low Wide Defensive Base",
                "Meet Shuttle Out Front",
                "Soft Cushion Block",
                "Step In to Cover Net"
            )
        ),
        BadmintonExercise(
            id = "cat_def_lift",
            name = "High Defensive Back-Line Lifts",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.DEFENSIVE.name,
            categoryLabel = TrainingCategory.DEFENSIVE.displayName,
            description = "Drive heavy smashes high and deep to the rear corners using compact wrist and finger acceleration.",
            instructions = listOf(
                "Drop racket head slightly below shuttle level on backhand or forehand side.",
                "Whip the racket upward with a short pronation/supination snap.",
                "Aim high enough to reach the opponent's back tramline without floating mid-court.",
                "Recover immediately into side-by-side (doubles) or central (singles) defense."
            ),
            durationMinutes = 14,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "5 sets x 15 defensive lifts",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Short, fast wrist snaps beat big shoulder swings when defending body smashes.",
                "Target the diagonal rear corner to make the attacker run further."
            ),
            diagramType = CourtDiagramType.DEFENSE_BLOCK,
            motionPhases = listOf(
                "Low Squat & Early Read",
                "Compact Wrist Drop",
                "Explosive Upward Snap",
                "High Deep Corner Flight"
            )
        ),
        BadmintonExercise(
            id = "cat_def_flat",
            name = "Flat Body-Shot Counter Drives",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.DEFENSIVE.name,
            categoryLabel = TrainingCategory.DEFENSIVE.displayName,
            description = "Neutralize flat smashes aimed at your chest and racket hip using rapid elbow-tuck shifts.",
            instructions = listOf(
                "Use backhand thumb grip as default; tuck elbow slightly inward if attacked on the hip.",
                "Rotate hips slightly out of the shuttle's line to create space for your racket swing.",
                "Counter-drive flat and fast past the net player toward the mid-court sidelines.",
                "Practice 30-second rapid-fire shadow blocks left, body, right."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 45,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "6 sets x 45 sec rapid defense",
            restTimeSeconds = 35,
            trainingTips = listOf(
                "Keeping your racket slightly further in front gives you extra milliseconds to angle the face.",
                "Stay light on your toes so a quick hip turn happens automatically."
            ),
            diagramType = CourtDiagramType.DEFENSE_BLOCK,
            motionPhases = listOf(
                "Racket Out Front Ready",
                "Micro Hip Turn to Clear Body",
                "Flat Snap Counter-Drive",
                "Instant Stance Reset"
            )
        ),

        // 5. SPEED AND AGILITY
        BadmintonExercise(
            id = "cat_spd_4corner",
            name = "4-Corner Shuttle Touch Sprint",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.SPEED_AGILITY.name,
            categoryLabel = TrainingCategory.SPEED_AGILITY.displayName,
            description = "Sharpen explosive court acceleration and braking control by touching shuttle markers across 4 corners.",
            instructions = listOf(
                "Place 1 shuttle upright in each of the 4 court corners (or a 4m x 4m square).",
                "Sprint/lunge from center to Corner 1, knock the shuttle down with your racket hand, return to center.",
                "Repeat for Corners 2, 3, and 4 as fast as possible with proper lunge posture.",
                "Rest 40 seconds and repeat while keeping form crisp."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 40,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "5 sets x 8 corner touches",
            restTimeSeconds = 40,
            trainingTips = listOf(
                "Always face forward toward the net—never turn your back when recovering.",
                "Decelerate smoothly into the lunge rather than sliding out of control."
            ),
            diagramType = CourtDiagramType.CONDITIONING_AGILITY,
            motionPhases = listOf(
                "Center Base Start",
                "Explosive Push to Corner",
                "Low Lunge & Touch",
                "Fast Drive Back to T"
            )
        ),
        BadmintonExercise(
            id = "cat_spd_lateral",
            name = "Lateral Chasse Side-to-Side",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.SPEED_AGILITY.name,
            categoryLabel = TrainingCategory.SPEED_AGILITY.displayName,
            description = "Build rapid side-court coverage for defending smashes and intercepting mid-court drives.",
            instructions = listOf(
                "Start in center mid-court in low defensive squat.",
                "Chasse explosively to the forehand sideline, lunge and shadow-drive.",
                "Push off hard and chasse directly across to the backhand sideline.",
                "Maintain hip height without bobbing up during the slide."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 30,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "6 sets x 30 sec side-to-side",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Keep your feet wide and never let your heels click together during the chasse.",
                "Lead with your racket head on every side lunge."
            ),
            diagramType = CourtDiagramType.CONDITIONING_AGILITY,
            motionPhases = listOf(
                "Low Defensive Squat",
                "Lateral Chasse Push",
                "Sideline Lunge & Drive",
                "Drive Back Across Court"
            )
        ),
        BadmintonExercise(
            id = "cat_spd_burpee",
            name = "Split-Step to Sprint Bursts",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.SPEED_AGILITY.name,
            categoryLabel = TrainingCategory.SPEED_AGILITY.displayName,
            description = "Train reactive burst acceleration out of a static base stance for late-game clutch retrieves.",
            instructions = listOf(
                "Hold a half-squat ready stance for 2 seconds.",
                "Split-step and burst 3 steps forward to a tight net lunge.",
                "Recover backward, split-step again, and burst into a rear-court jump.",
                "Repeat continuously for 30 seconds at maximum effort."
            ),
            durationMinutes = 14,
            timerWorkSeconds = 30,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "6 sets x 30 sec all-out bursts",
            restTimeSeconds = 45,
            trainingTips = listOf(
                "Push through the balls of your feet like a sprinter out of blocks.",
                "Take full 45-second rests so every rep stays truly explosive."
            ),
            diagramType = CourtDiagramType.CONDITIONING_AGILITY,
            motionPhases = listOf(
                "Hold Coiled Stance",
                "Explosive Split-Step",
                "3-Step Net Burst",
                "Rear-Court Jump Transition"
            )
        ),

        // 6. ENDURANCE TRAINING
        BadmintonExercise(
            id = "cat_end_multi",
            name = "Multi-Shuttle Interval Endurance",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.ENDURANCE.name,
            categoryLabel = TrainingCategory.ENDURANCE.displayName,
            description = "Simulate grueling 25-shot third-game rallies with sustained 45-second high-tempo shadow or multi-shuttle intervals.",
            instructions = listOf(
                "Set the timer for 45 seconds work / 30 seconds rest for 8 rounds.",
                "Move at 85% match speed through random rear clears, smashes, and net recoveries.",
                "Focus on maintaining clean lunges and high racket carriage even when fatigued.",
                "Control breathing during the 30-second recovery window (4s inhale, 4s exhale)."
            ),
            durationMinutes = 16,
            timerWorkSeconds = 45,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "8 rounds x 45s work / 30s rest",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Never drop your hands onto your knees during rest—walk upright to open your lungs.",
                "Stay disciplined with split-steps even in Rounds 7 and 8."
            ),
            diagramType = CourtDiagramType.SIX_CORNER_FOOTWORK,
            motionPhases = listOf(
                "Sustained 85% Match Pace",
                "Front-Back Transition Flow",
                "Maintain Lunge Posture",
                "Controlled 30s Recovery Breath"
            )
        ),
        BadmintonExercise(
            id = "cat_end_pyramid",
            name = "20-40-60 Pyramid Rally Run",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.ENDURANCE.name,
            categoryLabel = TrainingCategory.ENDURANCE.displayName,
            description = "Build anaerobic recovery capacity with ascending and descending shadow court intervals.",
            instructions = listOf(
                "Round 1: 20s continuous footwork, 20s rest.",
                "Round 2: 40s continuous footwork, 30s rest.",
                "Round 3: 60s continuous footwork, 45s rest.",
                "Descend back down (40s then 20s) to complete 1 full pyramid."
            ),
            durationMinutes = 18,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "2 full pyramids",
            restTimeSeconds = 45,
            trainingTips = listOf(
                "Pace the 60-second peak round smoothly so you don't burn out in the first 15 seconds.",
                "Keep shadow swings crisp to train late-match stroke accuracy."
            ),
            diagramType = CourtDiagramType.SIX_CORNER_FOOTWORK,
            motionPhases = listOf(
                "20s Speed Opener",
                "40s Tempo Climb",
                "60s Peak Endurance Hold",
                "40s-20s Controlled Descent"
            )
        ),
        BadmintonExercise(
            id = "cat_end_suicide",
            name = "Court Line Shuttle Sprints",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.ENDURANCE.name,
            categoryLabel = TrainingCategory.ENDURANCE.displayName,
            description = "Strengthen calves, lungs, and mental grit with classic badminton line-to-line court suicides.",
            instructions = listOf(
                "Start at the back boundary line.",
                "Sprint forward to the short service line, touch with racket hand, backpedal to back line.",
                "Sprint to the center net tape, backpedal to back line, then sprint to the far short service line.",
                "Rest 45 seconds between sets."
            ),
            durationMinutes = 14,
            timerWorkSeconds = 45,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "5 sets x full court shuttle",
            restTimeSeconds = 45,
            trainingTips = listOf(
                "Stay low when touching each line so you don't round your lower back.",
                "Use short quick steps when decelerating near the net."
            ),
            diagramType = CourtDiagramType.CONDITIONING_AGILITY,
            motionPhases = listOf(
                "Back Line Start",
                "Sprint & Touch Short Line",
                "Controlled Backpedal Return",
                "Repeat to Net & Far Line"
            )
        ),

        // 7. ACCURACY TRAINING
        BadmintonExercise(
            id = "cat_acc_net",
            name = "Tight Hairpin Net Precision",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.ACCURACY.name,
            categoryLabel = TrainingCategory.ACCURACY.displayName,
            description = "Master delicate finger feel to tumble shuttles within centimeters of the net tape.",
            instructions = listOf(
                "Lunge toward the net with a relaxed forehand or backhand grip.",
                "Contact the shuttle high near the tape and gently slice/brush underneath the cork.",
                "Aim to land the shuttle inside a 50cm target zone right behind the opponent's net.",
                "Perform 20 forehand hairpins and 20 backhand hairpins."
            ),
            durationMinutes = 14,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 20 net touches",
            restTimeSeconds = 25,
            trainingTips = listOf(
                "Hold the racket slightly higher up the handle for even finer net control.",
                "Keep your eyes locked on the shuttle cork through impact."
            ),
            diagramType = CourtDiagramType.NET_HAIRPIN,
            motionPhases = listOf(
                "Extended Net Lunge",
                "High Take at Tape Level",
                "Gentle Cork Spin Brush",
                "Tight Drop Inside 50cm"
            )
        ),
        BadmintonExercise(
            id = "cat_acc_drop",
            name = "Tramline Drop Shot Targets",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.ACCURACY.name,
            categoryLabel = TrainingCategory.ACCURACY.displayName,
            description = "Place deceptive straight and cross-court drops accurately inside the singles sidelines.",
            instructions = listOf(
                "Place a towel or shuttle tube target just inside the front-court sidelines near the short service line.",
                "Execute overhead drop swings from both rear corners.",
                "Adjust racket-face angle subtly rather than swinging slower.",
                "Aim for 15+ clean target hits per 20 attempts."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 20 drop targets",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Aim to cross the net as low as possible so the shuttle gives the opponent zero reaction time.",
                "Keep your elbow high to maintain full control over trajectory."
            ),
            diagramType = CourtDiagramType.CLEAR_AND_DROP,
            motionPhases = listOf(
                "Rear-Corner Overhead Load",
                "Deceptive Smooth Swing",
                "Skim Net Tape Low",
                "Land Inside Sideline Target"
            )
        ),
        BadmintonExercise(
            id = "cat_acc_servebox",
            name = "4-Zone Precision Serve Grid",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.ACCURACY.name,
            categoryLabel = TrainingCategory.ACCURACY.displayName,
            description = "Hit all four corners of the opponent's service box on command—T-junction, wide corner, and both deep back corners.",
            instructions = listOf(
                "Call out your target zone BEFORE every serve: Inside-T, Wide Short, Deep Inside, or Deep Wide.",
                "Reset feet and breathing cleanly between serves.",
                "Rotate through all 4 zones for 20 total serves per set.",
                "Track your accuracy percentage in Personal Records."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 120,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "3 sets x 20 called-zone serves",
            restTimeSeconds = 25,
            trainingTips = listOf(
                "Serving accurately to the T-junction cuts off the receiver's cross-court angle.",
                "Occasional wide short serves stretch tall receivers off balance."
            ),
            diagramType = CourtDiagramType.SERVE_ZONES,
            motionPhases = listOf(
                "Call Zone (T / Wide / Deep)",
                "Consistent Routine & Breath",
                "Execute Low or Flick Serve",
                "Log Accuracy Score"
            )
        ),

        // 8. REACTION TIME TRAINING
        BadmintonExercise(
            id = "cat_react_wall",
            name = "Rapid Wall Volley Reflexes",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.REACTION_TIME.name,
            categoryLabel = TrainingCategory.REACTION_TIME.displayName,
            description = "Supercharge hand-eye coordination and wrist snap speed with non-stop close-range wall rallies.",
            instructions = listOf(
                "Stand 1.8 meters from a flat wall holding a backhand thumb grip.",
                "Rally a shuttle (use nylon or older feather) continuously against the wall without letting it drop.",
                "Switch smoothly between backhand and forehand as rebounds jump unpredictably.",
                "Count your max streak in 45 seconds and try to beat it each round."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 45,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "5 sets x 45 sec wall volleys",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Keep your racket up in front of your face—never drop it down to your waist.",
                "Use short forearm and finger taps, not full swings."
            ),
            diagramType = CourtDiagramType.DEFENSE_BLOCK,
            motionPhases = listOf(
                "Stand 1.8m from Wall",
                "Racket Up at Chest Height",
                "Rapid Forehand/Backhand Taps",
                "Track Consecutive Streak"
            )
        ),
        BadmintonExercise(
            id = "cat_react_drive",
            name = "Mid-Court Flat Drive Exchange",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.REACTION_TIME.name,
            categoryLabel = TrainingCategory.REACTION_TIME.displayName,
            description = "Train lightning-fast doubles mid-court reactions and compact racket recovery between flat drives.",
            instructions = listOf(
                "Stand in mid-court with knees bent and racket parallel to the floor.",
                "Practice alternating forehand-hip and backhand-chest flat shadow drives every 0.8 seconds.",
                "Focus on bringing the racket immediately back to the center ready point after every hit.",
                "Keep swings short—your wrist and fingers do 90% of the work."
            ),
            durationMinutes = 14,
            timerWorkSeconds = 40,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "6 sets x 40 sec rapid drives",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Step slightly forward as you drive to take time away from your opponents.",
                "Keep your grip loose until the microsecond of contact."
            ),
            diagramType = CourtDiagramType.DOUBLES_ROTATION,
            motionPhases = listOf(
                "Mid-Court Ready Squat",
                "Rapid Backhand/Forehand Switch",
                "Compact Flat Drive Snap",
                "Instant Center Racket Reset"
            )
        ),
        BadmintonExercise(
            id = "cat_react_dropcatch",
            name = "Shuttle Drop Reflex Catch",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.REACTION_TIME.name,
            categoryLabel = TrainingCategory.REACTION_TIME.displayName,
            description = "Solo offline drill that builds explosive first-step reaction and hand-speed acceleration.",
            instructions = listOf(
                "Hold a shuttle at shoulder height with one arm extended straight forward.",
                "Release the shuttle cleanly without warning and immediately split-step + lunge forward to catch it before it hits the floor.",
                "Progress by tossing the shuttle slightly left or right as you release.",
                "Complete 12 sharp catches per set on each side."
            ),
            durationMinutes = 10,
            timerWorkSeconds = 45,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "4 sets x 12 catches",
            restTimeSeconds = 25,
            trainingTips = listOf(
                "Keep your back straight and lunge with your racket leg just like a net shot.",
                "React the instant your fingers release the feathers."
            ),
            diagramType = CourtDiagramType.NET_HAIRPIN,
            motionPhases = listOf(
                "Extend Arm at Shoulder Height",
                "Release Shuttle Cleanly",
                "Explosive Split-Step & Lunge",
                "Catch Before Floor Bounce"
            )
        ),

        // 9. MATCH STRATEGY
        BadmintonExercise(
            id = "cat_strat_singles",
            name = "Singles 4-Corner Pressure Pattern",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.MATCH_STRATEGY.name,
            categoryLabel = TrainingCategory.MATCH_STRATEGY.displayName,
            description = "Learn how to move a singles opponent diagonally across the court to force weak mid-court lifts.",
            instructions = listOf(
                "Pattern 1: High Clear to opponent's Backhand Rear -> Straight Drop to Forehand Net -> Attacking Lob to Backhand Rear.",
                "Pattern 2: Tight Net Shot -> Cross-Court Lift -> Straight Stick Smash into open sideline.",
                "Shadow-walk and then shadow-run each 4-shot sequence at 80% pace.",
                "Visualize your opponent's position before choosing your next corner."
            ),
            durationMinutes = 16,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 8 tactical sequences",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Never give your opponent two consecutive shots from the same comfortable corner.",
                "Use high clears to buy time whenever you are stretched out of position."
            ),
            diagramType = CourtDiagramType.CLEAR_AND_DROP,
            motionPhases = listOf(
                "1. Push Deep to Backhand Rear",
                "2. Pull Forward with Tight Drop",
                "3. Spot Open Sideline Space",
                "4. Finish with Attacking Clear/Smash"
            )
        ),
        BadmintonExercise(
            id = "cat_strat_doubles",
            name = "Doubles Attack & Defense Rotation",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.MATCH_STRATEGY.name,
            categoryLabel = TrainingCategory.MATCH_STRATEGY.displayName,
            description = "Master seamless front-back attacking rotation and side-by-side defensive coverage in doubles matches.",
            instructions = listOf(
                "When attacking (smash/drop from rear): rear player drives forward as front player shifts into the T-zone with racket raised.",
                "When forced to lift high: rear player moves to one half-court while front player backs up into the opposite half (side-by-side).",
                "Whoever hits a soft block at the net stays forward while the partner rotates backward.",
                "Shadow-rehearse both front-role and back-role transitions."
            ),
            durationMinutes = 16,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "4 sets x 10 rotation transitions",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Keep your lifts low or super deep—short high lifts get smashed straight down in doubles.",
                "Communicate clearly ('Mine!' / 'Yours!') on center mid-court shots."
            ),
            diagramType = CourtDiagramType.DOUBLES_ROTATION,
            motionPhases = listOf(
                "Attack: Front-Back Formation",
                "Lift High: Split Side-by-Side",
                "Block Tight: Rush Net Position",
                "Partner Covers Rear Court"
            )
        ),
        BadmintonExercise(
            id = "cat_strat_clutch",
            name = "Endgame 18-18 Clutch Tactics",
            section = LessonSection.CATEGORY,
            categoryKey = TrainingCategory.MATCH_STRATEGY.name,
            categoryLabel = TrainingCategory.MATCH_STRATEGY.displayName,
            description = "Make high-percentage tactical choices during high-pressure closing points without unforced errors.",
            instructions = listOf(
                "Practice serving under pressure: visualize 19-19 and execute 5 clean low serves in a row.",
                "Favor 80% body/hip smashes and high-percentage straight drops over risky sideline flicks.",
                "Take a deliberate 3-second calm breath behind the service line before crucial rallies.",
                "Keep rallies structured around your highest-confidence patterns."
            ),
            durationMinutes = 14,
            timerWorkSeconds = 90,
            difficulty = SkillLevel.ADVANCED,
            recommendedReps = "3 sets x 5 clutch rally simulations",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Avoid changing your tactical plan recklessly at 19-19—stick to what won you points.",
                "Aim 30cm inside lines during deuce points to eliminate unforced errors."
            ),
            diagramType = CourtDiagramType.SERVE_ZONES,
            motionPhases = listOf(
                "3-Second Box Breath Reset",
                "High-Percentage Low Serve",
                "Target Body / Safe Margins",
                "Finish Loose Mid-Court Lift"
            )
        )
    )

    val fitnessExercises: List<BadmintonExercise> = listOf(
        BadmintonExercise(
            id = "fit_warmup",
            name = "Warm-up Exercises",
            section = LessonSection.FITNESS,
            categoryKey = FitnessArea.WARMUP.name,
            categoryLabel = FitnessArea.WARMUP.displayName,
            description = "Elevate heart rate, lubricate shoulder/knee joints, and prime your wrists before touching a shuttle.",
            instructions = listOf(
                "2 minutes light jog with side shuffles and high knees.",
                "20 forward and 20 backward shoulder arm circles.",
                "15 wrist rotations each direction + 15 torso twists.",
                "10 gentle bodyweight half-squats and ankle rolls."
            ),
            durationMinutes = 8,
            timerWorkSeconds = 60,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "1 full 8-min activation flow",
            restTimeSeconds = 15,
            trainingTips = listOf(
                "Warm up until you break a light sweat before attempting any smash or deep lunge.",
                "Keep movements smooth and controlled—never jerk cold joints."
            ),
            safetyInstructions = listOf(
                "Never bounce aggressively into cold hamstrings or shoulders.",
                "Stop immediately if you feel sharp joint pinching; keep range pain-free."
            ),
            diagramType = CourtDiagramType.CONDITIONING_AGILITY,
            motionPhases = listOf(
                "Light Jog & Side Shuffle",
                "Arm Circles & Wrist Mobility",
                "Torso Rotations & Hip Openers",
                "Light Calf Bounces"
            )
        ),
        BadmintonExercise(
            id = "fit_stretching",
            name = "Stretching & Mobility",
            section = LessonSection.FITNESS,
            categoryKey = FitnessArea.STRETCHING.name,
            categoryLabel = FitnessArea.STRETCHING.displayName,
            description = "Maintain full overhead shoulder reach, forearm flexibility, and deep lunge hip mobility.",
            instructions = listOf(
                "Overhead triceps & lat stretch: hold 25 seconds per arm.",
                "Forearm flexor/extensor stretch: gently extend wrist palm-up and palm-down for 20s each.",
                "Kneeling hip flexor lunge stretch: 30 seconds per leg.",
                "Standing calf & Achilles wall stretch: 30 seconds per leg."
            ),
            durationMinutes = 10,
            timerWorkSeconds = 30,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "2 rounds x 25-30s per side",
            restTimeSeconds = 10,
            trainingTips = listOf(
                "Breathe out slowly as you ease into each stretch to relax muscle tension.",
                "Flexible hips make deep net lunges effortless and protect your lower back."
            ),
            safetyInstructions = listOf(
                "Stretch to mild tension only—never force past sharp pain.",
                "Avoid holding static stretches longer than 30s right before explosive jumps; save deep holds for post-session."
            ),
            diagramType = CourtDiagramType.CONDITIONING_AGILITY,
            motionPhases = listOf(
                "Shoulder & Lat Reach",
                "Forearm & Wrist Extension",
                "Hip Flexor Lunge Hold",
                "Calf & Achilles Wall Stretch"
            )
        ),
        BadmintonExercise(
            id = "fit_jumprope",
            name = "Jump Rope Exercises",
            section = LessonSection.FITNESS,
            categoryKey = FitnessArea.JUMP_ROPE.name,
            categoryLabel = FitnessArea.JUMP_ROPE.displayName,
            description = "Build springy calves, fast split-step rhythm, and court stamina using double-foot and boxer-step skips.",
            instructions = listOf(
                "Round 1–2: 45 seconds steady double-foot bounce on the balls of your feet.",
                "Round 3–4: 45 seconds alternating boxer step (left-left, right-right).",
                "Round 5–6: 30 seconds fast double-unders or high-tempo single skips.",
                "Keep elbows tucked close to your ribs and turn the rope using your wrists."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 45,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "6 rounds x 45s skip / 20s rest",
            restTimeSeconds = 20,
            trainingTips = listOf(
                "Jump only 2–3cm off the ground—just enough to clear the rope cleanly.",
                "Boxer-step skipping mimics badminton weight shifting between feet."
            ),
            safetyInstructions = listOf(
                "Always wear supportive court shoes and skip on a wooden floor or mat, never bare concrete.",
                "Stop if you experience shin splints or sharp Achilles tightness."
            ),
            diagramType = CourtDiagramType.CONDITIONING_AGILITY,
            motionPhases = listOf(
                "Elbows Tucked, Wrist Spin",
                "Soft Toe Bounce (2-3cm)",
                "Boxer-Step Weight Shift",
                "High-Tempo Finish"
            )
        ),
        BadmintonExercise(
            id = "fit_squats",
            name = "Squats & Explosive Power",
            section = LessonSection.FITNESS,
            categoryKey = FitnessArea.SQUATS.name,
            categoryLabel = FitnessArea.SQUATS.displayName,
            description = "Develop quad, glute, and calf power for lower defensive stances and higher jump smashes.",
            instructions = listOf(
                "Stand with feet slightly wider than shoulder width, toes turned slightly out.",
                "Lower hips back and down until thighs are parallel to the floor while keeping chest proud.",
                "Drive upward powerfully through your heels (or explode into a controlled jump squat for intermediate/advanced).",
                "Complete 15 controlled reps or 10 jump squats per set."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 40,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "4 sets x 15 squats",
            restTimeSeconds = 40,
            trainingTips = listOf(
                "Keep knees tracking in line with your second toes—never let them cave inward.",
                "Pause 1 second at the bottom of defensive squat holds to build isometric endurance."
            ),
            safetyInstructions = listOf(
                "Keep your lower back neutral (no rounding) at the bottom of the squat.",
                "Land softly with bent knees on jump squats to absorb impact safely."
            ),
            diagramType = CourtDiagramType.CONDITIONING_AGILITY,
            motionPhases = listOf(
                "Brace Core & Neutral Spine",
                "Hips Back, Knees Track Over Toes",
                "Parallel Depth Control",
                "Drive Up Through Heels"
            )
        ),
        BadmintonExercise(
            id = "fit_lunges",
            name = "Badminton Lunges",
            section = LessonSection.FITNESS,
            categoryKey = FitnessArea.LUNGES.name,
            categoryLabel = FitnessArea.LUNGES.displayName,
            description = "Strengthen knee stability and glute push-off power directly tailored to front-net badminton lunges.",
            instructions = listOf(
                "Stand tall with hands on hips or holding a light racket.",
                "Step forward cleanly, landing heel-first and rolling onto the flat foot.",
                "Lower until both knees bend near 90 degrees while keeping your torso upright.",
                "Drive forcefully off the front heel back to the starting position; alternate or focus on dominant racket leg."
            ),
            durationMinutes = 12,
            timerWorkSeconds = 45,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "4 sets x 12 lunges per leg",
            restTimeSeconds = 35,
            trainingTips = listOf(
                "Keep your front shin vertical so your knee doesn't shoot far past your toes.",
                "Add diagonal 45-degree lunges to match real front-court corner angles."
            ),
            safetyInstructions = listOf(
                "Never slam your back knee onto a hard floor—stop 2–3cm above the surface.",
                "Keep front knee aligned with toes; avoid inward twisting."
            ),
            diagramType = CourtDiagramType.SIX_CORNER_FOOTWORK,
            motionPhases = listOf(
                "Upright Chest & Core Braced",
                "Heel-First Step Forward",
                "90-Degree Knee Control",
                "Explosive Heel Push Back"
            )
        ),
        BadmintonExercise(
            id = "fit_planks",
            name = "Planks & Core Stability",
            section = LessonSection.FITNESS,
            categoryKey = FitnessArea.PLANKS.name,
            categoryLabel = FitnessArea.PLANKS.displayName,
            description = "Build rock-solid core stability to transfer leg power into overhead smashes and keep balance in wide lunges.",
            instructions = listOf(
                "Forearm Plank: align elbows directly under shoulders, squeeze glutes and abs into a straight line for 45 seconds.",
                "Side Plank: stack feet and lift hips off the floor for 30 seconds each side.",
                "Plank Shoulder Taps: hold high plank and tap opposite shoulders slowly without swaying hips.",
                "Focus on steady nasal breathing throughout every hold."
            ),
            durationMinutes = 10,
            timerWorkSeconds = 45,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "4 rounds x 45s holds",
            restTimeSeconds = 30,
            trainingTips = listOf(
                "Imagine pulling your elbows toward your toes without moving them—this activates deep abs immediately.",
                "Side planks protect your obliques during heavy round-the-head smashes."
            ),
            safetyInstructions = listOf(
                "Do not let your lower back sag toward the floor; drop to knees or reset if form breaks.",
                "Keep neck neutral looking straight down at your hands."
            ),
            diagramType = CourtDiagramType.CONDITIONING_AGILITY,
            motionPhases = listOf(
                "Elbows Under Shoulders",
                "Glutes & Core Tightly Braced",
                "Straight Line Ear-to-Ankle",
                "Switch to Left/Right Side Plank"
            )
        ),
        BadmintonExercise(
            id = "fit_agility",
            name = "Agility Drills (T-Drill & Ladder)",
            section = LessonSection.FITNESS,
            categoryKey = FitnessArea.AGILITY_DRILLS.name,
            categoryLabel = FitnessArea.AGILITY_DRILLS.displayName,
            description = "Train rapid deceleration, lateral shuffling, and multi-directional foot speed with zero equipment or 4 cones.",
            instructions = listOf(
                "Set 4 cones/markers in a T-shape (4m forward, 3m left, 3m right).",
                "Sprint forward to the center cone, side-shuffle left, side-shuffle all the way right, return center, and backpedal to start.",
                "Stay low and never cross your feet during the lateral shuffles.",
                "Follow with 30 seconds of imaginary ladder quick-feet in-in-out-out."
            ),
            durationMinutes = 15,
            timerWorkSeconds = 35,
            difficulty = SkillLevel.INTERMEDIATE,
            recommendedReps = "6 sets x 35s T-Drill sprints",
            restTimeSeconds = 40,
            trainingTips = listOf(
                "Look ahead toward the net—don't stare down at the cones.",
                "Lower your hips before changing direction for faster braking."
            ),
            safetyInstructions = listOf(
                "Ensure floor surface is dry and free of dust/sweat to prevent ankle rolls.",
                "Never backpedal blindly over obstacles—keep rear area clear."
            ),
            diagramType = CourtDiagramType.CONDITIONING_AGILITY,
            motionPhases = listOf(
                "Forward Sprint to Center",
                "Low Side-Shuffle Left",
                "Full Shuffle Across Right",
                "Center & Safe Backpedal"
            )
        ),
        BadmintonExercise(
            id = "fit_cooldown",
            name = "Cool-down Exercises",
            section = LessonSection.FITNESS,
            categoryKey = FitnessArea.COOLDOWN.name,
            categoryLabel = FitnessArea.COOLDOWN.displayName,
            description = "Lower your heart rate gradually, flush lactic acid from legs, and prevent next-day shoulder and calf soreness.",
            instructions = listOf(
                "Walk slowly around the court perimeter for 2 minutes while shaking out arms and calves.",
                "Perform gentle cross-body shoulder stretch and forearm wrist stretch (30s each).",
                "Seated hamstring and butterfly groin stretch: 35 seconds each.",
                "Finish with 6 slow deep belly breaths (4s inhale, 6s exhale)."
            ),
            durationMinutes = 8,
            timerWorkSeconds = 45,
            difficulty = SkillLevel.BEGINNER,
            recommendedReps = "1 complete 8-min recovery",
            restTimeSeconds = 10,
            trainingTips = listOf(
                "Cooling down immediately after intense footwork dramatically reduces next-day stiffness.",
                "Hydrate with water right after your breathing reset."
            ),
            safetyInstructions = listOf(
                "Never sit down abruptly right after an all-out sprint—walk for 1–2 minutes first.",
                "Keep all stretches gentle, calm, and pain-free."
            ),
            diagramType = CourtDiagramType.CONDITIONING_AGILITY,
            motionPhases = listOf(
                "2-Min Slow Recovery Walk",
                "Shoulder & Forearm Release",
                "Hamstring & Groin Stretch",
                "6 Deep Recovery Breaths"
            )
        )
    )
}
