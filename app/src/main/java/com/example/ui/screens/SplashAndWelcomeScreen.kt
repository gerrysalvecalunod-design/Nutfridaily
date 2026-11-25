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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.model.PlayerPosition
import com.example.data.model.SkillLevel
import com.example.ui.components.SportyCard
import com.example.ui.theme.MintEmeraldPrimary
import com.example.ui.theme.PitchBorderGreen
import com.example.ui.theme.PitchMintHighlight
import com.example.ui.theme.StadiumDeepBlack
import com.example.ui.theme.StadiumForestDark
import com.example.viewmodel.FootballViewModel
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
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    LaunchedEffect(Unit) {
        launch { alphaAnim.animateTo(1f, tween(450)) }
        launch { scaleAnim.animateTo(1f, tween(550, easing = FastOutSlowInEasing)) }
        loadingProgress.animateTo(1f, tween(1600, easing = FastOutSlowInEasing))
        delay(120L)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        StadiumDeepBlack,
                        StadiumForestDark,
                        Color(0xFF0A2817)
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
                color = Color(0xFFFCF3E4),
                shadowElevation = 20.dp,
                border = BorderStroke(2.5.dp, PitchMintHighlight.copy(alpha = 0.75f)),
                modifier = Modifier
                    .size(200.dp)
                    .scale(pulseScale)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_football_app_icon),
                    contentDescription = "FOOTBALL PRO Official Logo",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(34.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "FOOTBALL PRO",
                style = MaterialTheme.typography.displayLarge,
                color = Color.White,
                letterSpacing = 2.5.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Train Hard. Play Smart. Become Better.",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = PitchMintHighlight,
                letterSpacing = 0.6.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            LinearProgressIndicator(
                progress = { loadingProgress.value },
                modifier = Modifier
                    .width(220.dp)
                    .height(7.dp)
                    .clip(CircleShape),
                color = MintEmeraldPrimary,
                trackColor = PitchBorderGreen
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "LOADING 100% OFFLINE TRAINING STUDIO...",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = PitchMintHighlight,
                letterSpacing = 1.1.sp
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WelcomeScreen(
    viewModel: FootballViewModel
) {
    val profile by viewModel.playerProfile.collectAsStateWithLifecycle()
    val resetVersion by viewModel.profileResetVersion.collectAsStateWithLifecycle()

    var initialized by remember(resetVersion) { mutableStateOf(false) }
    var name by remember(resetVersion) { mutableStateOf("") }
    var ageText by remember(resetVersion) { mutableStateOf("18") }
    var selectedLevel by remember(resetVersion) { mutableStateOf(SkillLevel.BEGINNER) }
    var selectedPosition by remember(resetVersion) { mutableStateOf(PlayerPosition.STRIKER) }
    var preferredDuration by remember(resetVersion) { mutableIntStateOf(30) }
    var goalText by remember(resetVersion) {
        mutableStateOf("Sharpen ball mastery, first touch, and clinical finishing.")
    }

    LaunchedEffect(profile, resetVersion) {
        val p = profile ?: return@LaunchedEffect
        if (!initialized) {
            name = p.playerName
            ageText = p.age.toString()
            selectedLevel = SkillLevel.entries.find { it.name == p.skillLevel } ?: SkillLevel.BEGINNER
            selectedPosition = PlayerPosition.entries.find { it.name == p.playerPosition } ?: PlayerPosition.STRIKER
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
                    .height(236.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bg_dashboard_ronaldo),
                    contentDescription = "Football Motivation Background",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                0.0f to StadiumDeepBlack.copy(alpha = 0.48f),
                                0.65f to StadiumDeepBlack.copy(alpha = 0.82f),
                                1.0f to MaterialTheme.colorScheme.background
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFFCF3E4),
                            border = BorderStroke(2.dp, PitchMintHighlight),
                            shadowElevation = 8.dp,
                            modifier = Modifier.size(62.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_football_app_icon),
                                contentDescription = "FOOTBALL PRO Crest",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = PitchMintHighlight.copy(alpha = 0.18f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Bolt, null, tint = PitchMintHighlight, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("PLAYER SETUP • AUTO-SAVED OFFLINE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "FOOTBALL PRO",
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Train Hard. Play Smart. Become Better.",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = PitchMintHighlight
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
                            Text("Player Profile Setup", style = MaterialTheme.typography.titleLarge)
                            Icon(Icons.Default.CheckCircle, "Auto-save active", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = name,
                                onValueChange = {
                                    name = it
                                    viewModel.autoUpdatePlayerName(it)
                                },
                                label = { Text("Player Name") },
                                placeholder = { Text("Your name") },
                                singleLine = true,
                                modifier = Modifier
                                    .weight(1.7f)
                                    .testTag("welcome_name_input")
                            )

                            OutlinedTextField(
                                value = ageText,
                                onValueChange = { input ->
                                    val digits = input.filter { it.isDigit() }.take(2)
                                    ageText = digits
                                    digits.toIntOrNull()?.let { viewModel.autoUpdatePlayerAge(it) }
                                },
                                label = { Text("Age") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier
                                    .weight(0.75f)
                                    .testTag("welcome_age_input")
                            )
                        }

                        Text("Football Position (10 Roles)", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PlayerPosition.entries.forEach { pos ->
                                val selected = pos == selectedPosition
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier
                                        .height(36.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable {
                                            selectedPosition = pos
                                            viewModel.autoUpdatePosition(pos)
                                        }
                                        .testTag("welcome_pos_${pos.name.lowercase()}")
                                ) {
                                    Box(modifier = Modifier.padding(horizontal = 12.dp), contentAlignment = Alignment.Center) {
                                        Text(
                                            text = pos.displayName,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }

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

                        Text("Preferred Training Duration", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                            label = { Text("Training Goals") },
                            minLines = 2,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Button(
                    onClick = {
                        viewModel.completeOnboarding(
                            name = name,
                            age = ageText.toIntOrNull() ?: 18,
                            skillLevel = selectedLevel,
                            position = selectedPosition,
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
                    Icon(Icons.Default.SportsSoccer, null, tint = MaterialTheme.colorScheme.onPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Start Training",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}
