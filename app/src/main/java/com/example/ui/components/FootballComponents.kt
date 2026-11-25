package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PitchDiagramType
import com.example.ui.theme.AmberTimer
import com.example.ui.theme.MintEmeraldPrimary
import com.example.ui.theme.PitchMintHighlight
import com.example.ui.theme.TrophyGold
import com.example.ui.theme.TurfAccentTeal
import com.example.ui.theme.TurfGrassDark
import com.example.ui.theme.TurfGrassLight
import com.example.viewmodel.DailyChartPoint
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SportyCard(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    borderColor: Color = MaterialTheme.colorScheme.outline.copy(alpha = 0.65f),
    elevation: Dp = 2.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(18.dp)
    Card(
        modifier = modifier
            .clip(shape)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        content()
    }
}

@Composable
fun CircularProgressRing(
    progress: Float,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = color.copy(alpha = 0.18f),
    strokeWidth: Dp = 9.dp,
    modifier: Modifier = Modifier,
    centerContent: @Composable () -> Unit
) {
    val animated by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(550),
        label = "ring"
    )
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokePx = strokeWidth.toPx()
            val arcSize = Size(size.width - strokePx, size.height - strokePx)
            val topLeft = Offset(strokePx / 2f, strokePx / 2f)
            drawArc(
                color = trackColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360f * animated,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
        }
        centerContent()
    }
}

/**
 * Interactive 100% Offline Animated Football Pitch Diagram & Motion Player
 * styled in stadium striped emerald turf with crisp white chalk lines.
 */
@Composable
fun OfflineAnimatedPitchPlayer(
    diagramType: PitchDiagramType,
    motionPhases: List<String>,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(true) }
    var slowMotion by remember { mutableStateOf(false) }

    val durationMs = if (slowMotion) 4800 else 2600
    val infinite = rememberInfiniteTransition(label = "pitch_anim")
    val animT by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMs, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "t"
    )
    val t = if (isPlaying) animT else 0.52f
    val phaseIndex = ((t * 4f).toInt()).coerceIn(0, (motionPhases.size - 1).coerceAtLeast(0))
    val textMeasurer = rememberTextMeasurer()

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Striped Emerald Pitch Turf
                val stripeCount = 8
                val stripeW = w / stripeCount
                for (i in 0 until stripeCount) {
                    drawRect(
                        color = if (i % 2 == 0) TurfGrassDark else TurfGrassLight,
                        topLeft = Offset(i * stripeW, 0f),
                        size = Size(stripeW + 1f, h)
                    )
                }

                val padX = w * 0.06f
                val padY = h * 0.08f
                val pw = w - padX * 2f
                val ph = h - padY * 2f
                val lineColor = Color.White.copy(alpha = 0.85f)

                // Outer pitch touchlines & goal lines
                drawRect(lineColor, Offset(padX, padY), Size(pw, ph), style = Stroke(1.8.dp.toPx()))
                // Halfway line & center circle
                drawLine(lineColor, Offset(w * 0.5f, padY), Offset(w * 0.5f, padY + ph), 1.4.dp.toPx())
                drawCircle(lineColor, ph * 0.22f, Offset(w * 0.5f, padY + ph * 0.5f), style = Stroke(1.4.dp.toPx()))
                drawCircle(lineColor, 3.dp.toPx(), Offset(w * 0.5f, padY + ph * 0.5f))

                // Left & Right 18-yard Penalty Boxes
                val boxW = pw * 0.16f
                val boxH = ph * 0.56f
                val boxTop = padY + (ph - boxH) / 2f
                drawRect(lineColor, Offset(padX, boxTop), Size(boxW, boxH), style = Stroke(1.4.dp.toPx()))
                drawRect(lineColor, Offset(padX + pw - boxW, boxTop), Size(boxW, boxH), style = Stroke(1.4.dp.toPx()))

                // Goalposts
                val goalH = ph * 0.24f
                val goalTop = padY + (ph - goalH) / 2f
                drawRect(TrophyGold.copy(alpha = 0.85f), Offset(padX - 4.dp.toPx(), goalTop), Size(4.dp.toPx(), goalH))
                drawRect(TrophyGold.copy(alpha = 0.85f), Offset(padX + pw, goalTop), Size(4.dp.toPx(), goalH))

                when (diagramType) {
                    PitchDiagramType.SLALOM_DRIBBLING -> {
                        val cones = (0..4).map { idx ->
                            Offset(padX + pw * (0.20f + idx * 0.14f), padY + ph * 0.50f)
                        }
                        cones.forEach { drawConeMarker(it) }
                        val zigPt = Offset(
                            padX + pw * (0.14f + t * 0.72f),
                            padY + ph * (0.50f + 0.16f * sin(t * 5 * PI).toFloat())
                        )
                        drawPitchArrow(Offset(padX + pw * 0.14f, padY + ph * 0.50f), Offset(padX + pw * 0.86f, padY + ph * 0.50f), PitchMintHighlight.copy(alpha = 0.55f), true)
                        drawPlayerMarker(zigPt, MintEmeraldPrimary)
                        drawFootball(Offset(zigPt.x + 12.dp.toPx(), zigPt.y))
                    }

                    PitchDiagramType.FINISHING_ZONES -> {
                        val shooter = Offset(padX + pw * 0.68f, padY + ph * 0.64f)
                        val cutIn = Offset(padX + pw * 0.78f, padY + ph * 0.50f)
                        val goalCorner = Offset(padX + pw, goalTop + 6.dp.toPx())
                        drawPitchArrow(shooter, cutIn, PitchMintHighlight, true)
                        drawPitchArrow(cutIn, goalCorner, TrophyGold, false)
                        drawPlayerMarker(lerpOffset(shooter, cutIn, (t * 2f).coerceIn(0f, 1f)), MintEmeraldPrimary)
                        val ballPos = if (t < 0.5f) lerpOffset(shooter, cutIn, t * 2f)
                        else lerpOffset(cutIn, goalCorner, (t - 0.5f) * 2f)
                        drawFootball(ballPos)
                    }

                    PitchDiagramType.PASSING_TRIANGLE -> {
                        val p1 = Offset(w * 0.34f, padY + ph * 0.72f)
                        val p2 = Offset(w * 0.54f, padY + ph * 0.25f)
                        val p3 = Offset(w * 0.70f, padY + ph * 0.68f)
                        drawPitchArrow(p1, p2, PitchMintHighlight, false)
                        drawPitchArrow(p2, p3, TrophyGold, false)
                        drawPitchArrow(p3, p1, PitchMintHighlight.copy(alpha = 0.7f), true)
                        drawPlayerMarker(p1, MintEmeraldPrimary)
                        drawPlayerMarker(p2, MintEmeraldPrimary)
                        drawPlayerMarker(p3, MintEmeraldPrimary)
                        val ballPos = when {
                            t < 0.33f -> lerpOffset(p1, p2, t / 0.33f)
                            t < 0.66f -> lerpOffset(p2, p3, (t - 0.33f) / 0.33f)
                            else -> lerpOffset(p3, p1, (t - 0.66f) / 0.34f)
                        }
                        drawFootball(ballPos)
                    }

                    PitchDiagramType.DEFENSE_SHADOW -> {
                        val attackerStart = Offset(padX + pw * 0.35f, padY + ph * 0.26f)
                        val attackerEnd = Offset(padX + pw * 0.76f, padY + ph * 0.22f)
                        val defenderStart = Offset(padX + pw * 0.42f, padY + ph * 0.48f)
                        val defenderEnd = Offset(padX + pw * 0.80f, padY + ph * 0.36f)
                        drawPitchArrow(attackerStart, attackerEnd, AmberTimer, true)
                        drawPitchArrow(defenderStart, defenderEnd, MintEmeraldPrimary, false)
                        drawPlayerMarker(lerpOffset(attackerStart, attackerEnd, t), AmberTimer)
                        drawPlayerMarker(lerpOffset(defenderStart, defenderEnd, t), MintEmeraldPrimary)
                        drawFootball(lerpOffset(attackerStart, attackerEnd, t) + Offset(10.dp.toPx(), 0f))
                    }

                    PitchDiagramType.PENALTY_SET_PIECE -> {
                        val kicker = Offset(padX + pw * 0.74f, padY + ph * 0.76f)
                        val wall1 = Offset(padX + pw * 0.84f, padY + ph * 0.56f)
                        val wall2 = Offset(padX + pw * 0.84f, padY + ph * 0.48f)
                        val target = Offset(padX + pw, goalTop + 5.dp.toPx())
                        drawPitchArrow(kicker, target, TrophyGold, false)
                        drawPlayerMarker(kicker, MintEmeraldPrimary)
                        drawPlayerMarker(wall1, AmberTimer)
                        drawPlayerMarker(wall2, AmberTimer)
                        drawFootball(lerpOffset(kicker, target, sin(t * PI).toFloat()))
                    }

                    PitchDiagramType.AGILITY_LADDER -> {
                        val rungs = (0..5).map { idx ->
                            Offset(padX + pw * (0.20f + idx * 0.11f), padY + ph * 0.50f)
                        }
                        rungs.forEach { r ->
                            drawRect(TrophyGold.copy(alpha = 0.75f), Offset(r.x - 4.dp.toPx(), r.y - 12.dp.toPx()), Size(8.dp.toPx(), 24.dp.toPx()), style = Stroke(1.5.dp.toPx()))
                        }
                        val runnerPos = lerpOffset(rungs.first(), rungs.last(), t)
                        drawPlayerMarker(runnerPos, MintEmeraldPrimary)
                    }

                    PitchDiagramType.FORMATION_433 -> {
                        val nodes = listOf(
                            Offset(padX + pw * 0.10f, padY + ph * 0.50f), // GK
                            Offset(padX + pw * 0.28f, padY + ph * 0.20f),
                            Offset(padX + pw * 0.25f, padY + ph * 0.40f),
                            Offset(padX + pw * 0.25f, padY + ph * 0.60f),
                            Offset(padX + pw * 0.28f, padY + ph * 0.80f),
                            Offset(padX + pw * 0.52f, padY + ph * 0.30f),
                            Offset(padX + pw * 0.48f, padY + ph * 0.50f),
                            Offset(padX + pw * 0.52f, padY + ph * 0.70f),
                            Offset(padX + pw * 0.76f, padY + ph * 0.22f),
                            Offset(padX + pw * 0.80f, padY + ph * 0.50f),
                            Offset(padX + pw * 0.76f, padY + ph * 0.78f)
                        )
                        val shift = 14.dp.toPx() * sin(t * PI).toFloat()
                        drawPitchArrow(nodes[6], nodes[8] + Offset(shift, 0f), TrophyGold, true)
                        nodes.forEachIndexed { idx, pt ->
                            val color = if (idx == 0) TrophyGold else MintEmeraldPrimary
                            drawPlayerMarker(pt + Offset(if (idx > 4) shift else shift * 0.4f, 0f), color)
                        }
                    }

                    PitchDiagramType.PITCH_DIMENSIONS -> {
                        val labelStyle = TextStyle(color = TrophyGold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        drawText(textMeasurer, "105m x 68m FIFA Standard", Offset(padX + 8.dp.toPx(), padY + 6.dp.toPx()), style = labelStyle)
                        drawText(textMeasurer, "Goal: 7.32m x 2.44m", Offset(padX + 8.dp.toPx(), padY + ph - 18.dp.toPx()), style = labelStyle)
                        drawText(textMeasurer, "18-yd Box (16.5m)", Offset(padX + boxW + 6.dp.toPx(), boxTop + 4.dp.toPx()), style = labelStyle.copy(color = Color.White))
                        val pulseR = (8.dp.toPx() + 5.dp.toPx() * sin(t * 2 * PI).toFloat())
                        drawCircle(MintEmeraldPrimary.copy(alpha = 0.4f), pulseR, Offset(w * 0.5f, padY + ph * 0.5f))
                    }

                    PitchDiagramType.GOALKEEPER_ANGLES -> {
                        val gkStart = Offset(padX + 10.dp.toPx(), padY + ph * 0.50f)
                        val gkSet = Offset(padX + pw * 0.12f, padY + ph * 0.44f)
                        val shooter = Offset(padX + pw * 0.42f, padY + ph * 0.30f)
                        drawPitchArrow(shooter, Offset(padX, goalTop + 5.dp.toPx()), AmberTimer.copy(alpha = 0.55f), true)
                        drawPitchArrow(shooter, Offset(padX, goalTop + goalH - 5.dp.toPx()), AmberTimer.copy(alpha = 0.55f), true)
                        drawPlayerMarker(shooter, AmberTimer)
                        drawPlayerMarker(lerpOffset(gkStart, gkSet, sin(t * PI).toFloat()), TrophyGold)
                        drawFootball(lerpOffset(shooter, gkSet, t))
                    }
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.Black.copy(alpha = 0.65f),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Text(
                    text = "PITCH MOTION",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = PitchMintHighlight,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { isPlaying = !isPlaying }
                    .testTag("anim_play_pause_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause animation" else "Play animation",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isPlaying) "Pause" else "Play",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (slowMotion) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { slowMotion = !slowMotion }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Speed, "Speed", modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (slowMotion) "0.5x Slow" else "1x Pace", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (motionPhases.isNotEmpty()) {
                Text(
                    text = motionPhases[phaseIndex],
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

private fun lerpOffset(a: Offset, b: Offset, f: Float): Offset =
    Offset(a.x + (b.x - a.x) * f, a.y + (b.y - a.y) * f)

private fun DrawScope.drawPlayerMarker(center: Offset, color: Color) {
    drawCircle(Color.Black.copy(alpha = 0.55f), 10.dp.toPx(), center)
    drawCircle(color, 7.5.dp.toPx(), center)
    drawCircle(Color.White, 7.5.dp.toPx(), center, style = Stroke(1.6.dp.toPx()))
}

private fun DrawScope.drawFootball(center: Offset) {
    drawCircle(TrophyGold.copy(alpha = 0.35f), 9.dp.toPx(), center)
    drawCircle(Color.White, 5.5.dp.toPx(), center)
    drawCircle(Color.Black, 2.2.dp.toPx(), center)
}

private fun DrawScope.drawConeMarker(center: Offset) {
    val r = 5.5.dp.toPx()
    val path = Path().apply {
        moveTo(center.x, center.y - r)
        lineTo(center.x - r, center.y + r)
        lineTo(center.x + r, center.y + r)
        close()
    }
    drawPath(path, AmberTimer)
}

private fun DrawScope.drawPitchArrow(start: Offset, end: Offset, color: Color, dashed: Boolean) {
    val dash = if (dashed) PathEffect.dashPathEffect(floatArrayOf(10f, 7f), 0f) else null
    drawLine(color, start, end, strokeWidth = 2.dp.toPx(), pathEffect = dash, cap = StrokeCap.Round)
    val angle = atan2((end.y - start.y).toDouble(), (end.x - start.x).toDouble())
    val head = 8.dp.toPx()
    val a1 = angle + Math.toRadians(150.0)
    val a2 = angle - Math.toRadians(150.0)
    drawLine(color, end, Offset(end.x + (cos(a1) * head).toFloat(), end.y + (sin(a1) * head).toFloat()), 2.dp.toPx(), cap = StrokeCap.Round)
    drawLine(color, end, Offset(end.x + (cos(a2) * head).toFloat(), end.y + (sin(a2) * head).toFloat()), 2.dp.toPx(), cap = StrokeCap.Round)
}

@Composable
fun WeeklyPerformanceChart(
    points: List<DailyChartPoint>,
    modifier: Modifier = Modifier
) {
    if (points.isEmpty()) return
    val maxMin = (points.maxOfOrNull { it.minutes } ?: 30).coerceAtLeast(30)
    val gridColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
    val trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f)
    val emptyBarColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.45f)
    val primary = MaterialTheme.colorScheme.primary
    val accent = MaterialTheme.colorScheme.secondary

    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(126.dp)
                .padding(horizontal = 6.dp, vertical = 6.dp)
        ) {
            val w = size.width
            val h = size.height
            val slotW = w / points.size
            val barW = (slotW * 0.44f).coerceAtMost(24.dp.toPx())

            for (i in 1..3) {
                val y = h - (i / 3f) * h
                drawLine(gridColor, Offset(0f, y), Offset(w, y), 1.dp.toPx())
            }

            points.forEachIndexed { idx, pt ->
                val xCenter = slotW * idx + slotW / 2f
                val left = xCenter - barW / 2f
                drawRoundRect(trackColor, Offset(left, 0f), Size(barW, h), CornerRadius(8.dp.toPx(), 8.dp.toPx()))
                val ratio = if (pt.minutes == 0) 0.07f else (pt.minutes.toFloat() / maxMin).coerceIn(0.14f, 1f)
                val barH = h * ratio
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        if (pt.minutes > 0) listOf(accent, primary)
                        else listOf(emptyBarColor, emptyBarColor.copy(alpha = 0.6f))
                    ),
                    topLeft = Offset(left, h - barH),
                    size = Size(barW, barH),
                    cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                )
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            points.forEach { pt ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(pt.label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = "${pt.minutes}m",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (pt.minutes > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun MonthlyPerformanceGrid(
    points: List<DailyChartPoint>,
    modifier: Modifier = Modifier
) {
    if (points.isEmpty()) return
    val weeks = points.chunked(7)
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        weeks.forEach { week ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                week.forEach { day ->
                    val bg = when {
                        day.minutes >= 35 -> MaterialTheme.colorScheme.primary
                        day.minutes >= 15 -> TurfAccentTeal
                        day.minutes > 0 -> PitchMintHighlight.copy(alpha = 0.72f)
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                    val txtColor = when {
                        day.minutes >= 15 -> Color.White
                        else -> Color(0xFF06291D)
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1.35f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(bg),
                        contentAlignment = Alignment.Center
                    ) {
                        if (day.minutes > 0) {
                            Text(
                                text = "${day.minutes}",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = txtColor
                            )
                        }
                    }
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("28-Day Monthly Performance (min/day)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Total: ${points.sumOf { it.minutes }} min", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
    }
}
