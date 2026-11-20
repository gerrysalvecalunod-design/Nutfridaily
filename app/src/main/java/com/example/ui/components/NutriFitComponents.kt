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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.DateUtils
import com.example.data.model.DayProgressSummary
import com.example.data.model.WeightLogEntity
import com.example.ui.theme.CitrusGold
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ForestDeep
import com.example.ui.theme.HydrationBlue
import com.example.ui.theme.HydrationLight
import com.example.ui.theme.MintContainerLight
import com.example.ui.theme.MintVibrant
import com.example.ui.theme.VioletWeight
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun BotanicalCard(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    borderColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
    elevation: Dp = 2.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(20.dp)
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
fun DateSelectorStrip(
    selectedDateKey: String,
    onPreviousDay: () -> Unit,
    onNextDay: () -> Unit,
    onJumpToday: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isToday = selectedDateKey == DateUtils.todayKey()
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPreviousDay) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Previous day",
                    tint = EmeraldPrimary
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onJumpToday() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = DateUtils.formatDisplayDate(selectedDateKey),
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            IconButton(onClick = onNextDay, enabled = !isToday) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Next day",
                    tint = if (isToday) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f) else EmeraldPrimary
                )
            }
        }
    }
}

@Composable
fun CircularProgressRing(
    progress: Float,
    color: Color,
    trackColor: Color = color.copy(alpha = 0.15f),
    strokeWidth: Dp = 10.dp,
    modifier: Modifier = Modifier,
    centerContent: @Composable () -> Unit
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 700),
        label = "ring_progress"
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
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
        }
        centerContent()
    }
}

@Composable
fun HydrationWaveCup(
    progress: Float,
    currentMl: Int,
    goalMl: Int,
    modifier: Modifier = Modifier
) {
    val animatedFill by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1.05f),
        animationSpec = tween(800),
        label = "water_fill"
    )
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2f * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_phase"
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = size.minDimension / 2f - 8.dp.toPx()
            val center = Offset(size.width / 2f, size.height / 2f)

            // Outer ring
            drawCircle(
                color = HydrationLight,
                radius = radius,
                center = center
            )

            // Clip inside circle using path
            val circlePath = Path().apply {
                addOval(
                    androidx.compose.ui.geometry.Rect(
                        center.x - radius,
                        center.y - radius,
                        center.x + radius,
                        center.y + radius
                    )
                )
            }

            // Compute water height
            val clamped = animatedFill.coerceIn(0f, 1f)
            val waterTopY = (center.y + radius) - (radius * 2f * clamped)

            val wavePath = Path().apply {
                val startX = center.x - radius
                val endX = center.x + radius
                moveTo(startX, center.y + radius)
                var x = startX
                while (x <= endX) {
                    val norm = (x - startX) / (radius * 2f)
                    val waveY = waterTopY + sin(norm * 2f * PI + wavePhase).toFloat() * 7.dp.toPx() * (if (clamped > 0.02f && clamped < 0.98f) 1f else 0.2f)
                    lineTo(x, waveY)
                    x += 6f
                }
                lineTo(endX, center.y + radius)
                close()
            }

            // Intersect wave with circle
            val clippedWave = Path().apply {
                op(circlePath, wavePath, androidx.compose.ui.graphics.PathOperation.Intersect)
            }

            drawPath(
                path = clippedWave,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF38BDF8), HydrationBlue),
                    startY = waterTopY,
                    endY = center.y + radius
                )
            )

            drawCircle(
                color = HydrationBlue.copy(alpha = 0.35f),
                radius = radius,
                center = center,
                style = Stroke(width = 4.dp.toPx())
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${currentMl} ml",
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (animatedFill > 0.55f) Color.White else ForestDeep
            )
            Text(
                text = "of ${goalMl} ml",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (animatedFill > 0.55f) Color.White.copy(alpha = 0.9f) else HydrationBlue
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color.White.copy(alpha = 0.85f)
            ) {
                Text(
                    text = "${(progress * 100).toInt().coerceAtMost(999)}%",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = HydrationBlue,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun WeightTrendChart(
    logs: List<WeightLogEntity>,
    targetWeightKg: Float,
    modifier: Modifier = Modifier
) {
    val chronological = logs.take(10).reversed()
    if (chronological.isEmpty()) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text("Record your weight to see your trend line", style = MaterialTheme.typography.bodyMedium)
        }
        return
    }

    val allValues = chronological.map { it.weightKg } + targetWeightKg
    val minVal = (allValues.minOrNull() ?: 60f) - 1.5f
    val maxVal = (allValues.maxOrNull() ?: 80f) + 1.5f
    val range = (maxVal - minVal).coerceAtLeast(2f)

    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .padding(horizontal = 8.dp, vertical = 12.dp)
        ) {
            val w = size.width
            val h = size.height

            // Target line
            val targetY = h - ((targetWeightKg - minVal) / range) * h
            drawLine(
                color = EmeraldPrimary.copy(alpha = 0.65f),
                start = Offset(0f, targetY),
                end = Offset(w, targetY),
                strokeWidth = 2.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f)
            )

            // Points
            val count = chronological.size
            val points = chronological.mapIndexed { idx, entry ->
                val x = if (count == 1) w / 2f else (idx.toFloat() / (count - 1)) * w
                val y = h - ((entry.weightKg - minVal) / range) * h
                Offset(x, y)
            }

            if (points.size >= 2) {
                val linePath = Path().apply {
                    moveTo(points.first().x, points.first().y)
                    for (i in 1 until points.size) {
                        lineTo(points[i].x, points[i].y)
                    }
                }
                val areaPath = Path().apply {
                    addPath(linePath)
                    lineTo(points.last().x, h)
                    lineTo(points.first().x, h)
                    close()
                }
                drawPath(
                    path = areaPath,
                    brush = Brush.verticalGradient(
                        listOf(VioletWeight.copy(alpha = 0.25f), Color.Transparent)
                    )
                )
                drawPath(
                    path = linePath,
                    color = VioletWeight,
                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                )
            }

            points.forEachIndexed { idx, pt ->
                drawCircle(
                    color = Color.White,
                    radius = 6.dp.toPx(),
                    center = pt
                )
                drawCircle(
                    color = if (idx == points.lastIndex) EmeraldPrimary else VioletWeight,
                    radius = 4.dp.toPx(),
                    center = pt
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = chronological.first().dateKey.takeLast(5),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Target: ${String.format("%.1f", targetWeightKg)} kg",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldPrimary
            )
            Text(
                text = chronological.last().dateKey.takeLast(5),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun WeeklyHabitBarChart(
    days: List<DayProgressSummary>,
    modifier: Modifier = Modifier
) {
    if (days.isEmpty()) return

    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(145.dp)
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            val w = size.width
            val h = size.height
            val slotW = w / days.size
            val barW = (slotW * 0.45f).coerceAtMost(28.dp.toPx())

            // Guide lines (0, 1, 2, 3 habits)
            for (level in 1..3) {
                val y = h - (level / 3f) * h
                drawLine(
                    color = Color.Gray.copy(alpha = 0.15f),
                    start = Offset(0f, y),
                    end = Offset(w, y),
                    strokeWidth = 1.dp.toPx()
                )
            }

            days.forEachIndexed { index, day ->
                val xCenter = slotW * index + slotW / 2f
                val left = xCenter - barW / 2f
                // Background track
                drawRoundRect(
                    color = MintContainerLight,
                    topLeft = Offset(left, 0f),
                    size = Size(barW, h),
                    cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
                )
                val ratio = (day.habitsMetCount / 3f).coerceIn(0.08f, 1f)
                val barHeight = h * ratio
                val barColor = when (day.habitsMetCount) {
                    3 -> EmeraldPrimary
                    2 -> MintVibrant
                    1 -> CitrusGold
                    else -> Color(0xFFCBD5E1)
                }
                drawRoundRect(
                    color = barColor,
                    topLeft = Offset(left, h - barHeight),
                    size = Size(barW, barHeight),
                    cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            days.forEach { day ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = day.shortDayLabel,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${day.habitsMetCount}/3",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary
                    )
                }
            }
        }
    }
}
