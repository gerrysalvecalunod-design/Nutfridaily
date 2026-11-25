package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.model.SkillLevel
import com.example.ui.components.SportyCard
import com.example.ui.theme.ArenaDeepNavy
import com.example.ui.theme.ArenaRoyalBlue
import com.example.ui.theme.CobaltAccent
import com.example.ui.theme.LightBorderBlue
import com.example.ui.theme.SkyBlueHighlight
import com.example.viewmodel.BadmintonViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    onFinished: () -> Unit
) {
    val alphaAnim = remember { Animatable(0f) }
    val scaleAnim = remember { Animatable(0.86f) }
    val loadingProgress = remember { Animatable(0f) }

    val pulseTransition = rememberInfiniteTransition(label = "splash_pulse")
    val pulseScale by pulseTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.045f,
        animationSpec = infiniteRepeatable(
            animation = tween(950, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    LaunchedEffect(Unit) {
        launch { alphaAnim.animateTo(1f, tween(450)) }
        launch { scaleAnim.animateTo(1f, tween(550, easing = FastOutSlowInEasing)) }
        loadingProgress.animateTo(1f, tween(1650, easing = FastOutSlowInEasing))
        delay(120L)
        onFinished()
    }

    // Crisp White-to-Ice-Blue Arena Loading Screen seamlessly matching the uploaded logo
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFFFFFFF),
                        Color(0xFFF6FAFF),
                        Color(0xFFE7F2FF)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .alpha(alphaAnim.value)
                .scale(scaleAnim.value)
                .padding(32.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(36.dp),
                color = Color.White,
                shadowElevation = 14.dp,
                border = BorderStroke(2.dp, SkyBlueHighlight.copy(alpha = 0.45f)),
                modifier = Modifier
                    .size(216.dp)
                    .scale(pulseScale)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_logo_badminton_pro),
                    contentDescription = "Badminton Pro Official Logo",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = "BADMINTON PRO",
                style = MaterialTheme.typography.displayLarge,
                color = ArenaDeepNavy,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "TRAIN SMART. PLAY BETTER.",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = CobaltAccent,
                letterSpacing = 3.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            LinearProgressIndicator(
                progress = { loadingProgress.value },
                modifier = Modifier
                    .width(210.dp)
                    .height(7.dp)
                    .clip(CircleShape),
                color = ArenaRoyalBlue,
                trackColor = LightBorderBlue
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "LOADING OFFLINE COURT PROGRESS...",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = CobaltAccent,
                letterSpacing = 1.2.sp
            )
        }
    }
}

@Composable
fun WelcomeScreen(
    viewModel: BadmintonViewModel
) {
    val profile by viewModel.playerProfile.collectAsStateWithLifecycle()
    val resetVersion by viewModel.profileResetVersion.collectAsStateWithLifecycle()

    var initialized by remember(resetVersion) { mutableStateOf(false) }
    var name by remember(resetVersion) { mutableStateOf("") }
    var selectedLevel by remember(resetVersion) { mutableStateOf(SkillLevel.BEGINNER) }
    var preferredDuration by remember(resetVersion) { mutableIntStateOf(30) }
    var goalText by remember(resetVersion) {
        mutableStateOf("Improve 6-corner footwork, serve consistency, and smash speed.")
    }

    LaunchedEffect(profile, resetVersion) {
        val p = profile ?: return@LaunchedEffect
        if (!initialized) {
            name = p.playerName
            selectedLevel = SkillLevel.entries.find { it.name == p.skillLevel } ?: SkillLevel.BEGINNER
            preferredDuration = p.preferredDurationMinutes
            if (p.trainingGoals.isNotBlank()) goalText = p.trainingGoals
            initialized = true
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 36.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(ArenaDeepNavy, ArenaRoyalBlue, MaterialTheme.colorScheme.background)
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 18.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            shadowElevation = 6.dp,
                            modifier = Modifier.size(64.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_logo_badminton_pro),
                                contentDescription = "Badminton Pro Logo",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(6.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = SkyBlueHighlight.copy(alpha = 0.22f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Bolt, null, tint = SkyBlueHighlight, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("100% OFFLINE • AUTO-SAVED LOCALLY", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "BADMINTON PRO",
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Train Smart. Play Better. All drills, history, and progress save automatically offline.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                SportyCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Personalize Your Court Plan", style = MaterialTheme.typography.titleLarge)
                            Icon(Icons.Default.CheckCircle, "Auto-save enabled", tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(18.dp))
                        }

                        OutlinedTextField(
                            value = name,
                            onValueChange = {
                                name = it
                                viewModel.autoUpdatePlayerName(it)
                            },
                            label = { Text("Your Name") },
                            placeholder = { Text("Enter your name") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("welcome_name_input")
                        )

                        Text("Skill Level", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            SkillLevel.entries.forEach { lvl ->
                                val selected = lvl == selectedLevel
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable {
                                            selectedLevel = lvl
                                            viewModel.autoUpdateSkillLevel(lvl)
                                        }
                                        .testTag("welcome_level_${lvl.name.lowercase()}")
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = lvl.displayName,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }

                        Text("Preferred Session Duration", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(15, 20, 30, 45, 60).forEach { mins ->
                                val selected = preferredDuration == mins
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable {
                                            preferredDuration = mins
                                            viewModel.autoUpdatePreferredDuration(mins)
                                        }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "${mins}m",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }

                        OutlinedTextField(
                            value = goalText,
                            onValueChange = {
                                goalText = it
                                viewModel.autoUpdateTrainingGoals(it)
                            },
                            label = { Text("Training Goal") },
                            minLines = 2,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Button(
                    onClick = {
                        viewModel.completeOnboarding(
                            name = name,
                            skillLevel = selectedLevel,
                            preferredDurationMinutes = preferredDuration,
                            goalText = goalText
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("start_training_button")
                ) {
                    Icon(Icons.Default.SportsTennis, null, tint = MaterialTheme.colorScheme.onPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Start Training Offline",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                OutlinedButton(
                    onClick = {
                        viewModel.completeOnboarding(
                            name = name.ifBlank { "Player" },
                            skillLevel = selectedLevel,
                            preferredDurationMinutes = preferredDuration,
                            goalText = goalText
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Quick Enter Now", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
