package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DrillDiagramType
import com.example.ui.theme.AmberGlow
import com.example.ui.theme.AmberTimer
import com.example.ui.theme.CrimsonOpponent
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.NeonPitchGreen
import com.example.ui.theme.SpotlightGold
import com.example.ui.theme.TacticalCyan
import com.example.viewmodel.DailyChartPoint
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun StadiumCard(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    borderColor: Color = MaterialTheme.colorScheme.outline.copy(alpha = 0.55f),
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
        animationSpec = tween(700),
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

@Composable
fun DrillDiagramCanvas(
    diagramType: DrillDiagramType,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(170.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        val w = size.width
        val h = size.height

        // Turf stripes background
        drawRect(
            brush = Brush.verticalGradient(listOf(Color(0xFF0A2E1A), Color(0xFF061B0F)))
        )
        val stripeW = w / 6f
        for (i in 0 until 6 step 2) {
            drawRect(
                color = Color(0xFF103E24).copy(alpha = 0.45f),
                topLeft = Offset(i * stripeW, 0f),
                size = Size(stripeW, h)
            )
        }

        // Pitch border lines
        drawRoundRect(
            color = NeonPitchGreen.copy(alpha = 0.38f),
            topLeft = Offset(10.dp.toPx(), 10.dp.toPx()),
            size = Size(w - 20.dp.toPx(), h - 20.dp.toPx()),
            cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
            style = Stroke(width = 1.5.dp.toPx())
        )

        val dashEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 10f), 0f)

        when (diagramType) {
            DrillDiagramType.CONES_SLALOM -> {
                val cones = listOf(0.22f, 0.36f, 0.50f, 0.64f, 0.78f).map { Offset(w * it, h * 0.52f) }
                // Slalom wave path
                val slalom = Path().apply {
                    moveTo(w * 0.12f, h * 0.52f)
                    cones.forEachIndexed { idx, pt ->
                        val dy = if (idx % 2 == 0) -24.dp.toPx() else 24.dp.toPx()
                        quadraticTo(pt.x - 10.dp.toPx(), pt.y + dy, pt.x + 18.dp.toPx(), pt.y)
                    }
                }
                drawPath(slalom, color = NeonPitchGreen, style = Stroke(width = 2.5.dp.toPx(), pathEffect = dashEffect))
                cones.forEach { drawCone(it) }
                drawBall(Offset(w * 0.12f, h * 0.52f))
                drawPlayerDot(Offset(w * 0.88f, h * 0.52f), NeonPitchGreen)
            }

            DrillDiagramType.TRIANGLE_PASSING -> {
                val p1 = Offset(w * 0.22f, h * 0.76f)
                val p2 = Offset(w * 0.50f, h * 0.24f)
                val p3 = Offset(w * 0.78f, h * 0.76f)
                drawDiagramArrow(p1, p2, NeonPitchGreen, dashed = false)
                drawDiagramArrow(p2, p3, TacticalCyan, dashed = true)
                drawDiagramArrow(p3, p1, NeonPitchGreen, dashed = false)
                drawPlayerDot(p1, NeonPitchGreen)
                drawCone(p2)
                drawPlayerDot(p3, TacticalCyan)
                drawBall(Offset((p1.x + p2.x) / 2f, (p1.y + p2.y) / 2f))
            }

            DrillDiagramType.SHOOTING_ARCS -> {
                // Goal frame at right
                drawRect(
                    color = Color.White.copy(alpha = 0.85f),
                    topLeft = Offset(w * 0.82f, h * 0.26f),
                    size = Size(10.dp.toPx(), h * 0.48f),
                    style = Stroke(width = 2.5.dp.toPx())
                )
                // Penalty box arc
                drawRect(
                    color = Color.White.copy(alpha = 0.25f),
                    topLeft = Offset(w * 0.62f, h * 0.16f),
                    size = Size(w * 0.20f, h * 0.68f),
                    style = Stroke(width = 1.5.dp.toPx())
                )
                val ballPos = Offset(w * 0.34f, h * 0.56f)
                drawDiagramArrow(Offset(w * 0.16f, h * 0.68f), ballPos, TacticalCyan, dashed = true)
                drawDiagramArrow(ballPos, Offset(w * 0.82f, h * 0.34f), NeonPitchGreen, dashed = false)
                drawDiagramArrow(ballPos, Offset(w * 0.82f, h * 0.66f), AmberTimer, dashed = true)
                drawBall(ballPos)
                drawCone(Offset(w * 0.80f, h * 0.32f))
                drawCone(Offset(w * 0.80f, h * 0.68f))
            }

            DrillDiagramType.DEFENSIVE_CHANNEL -> {
                val topLeft = Offset(w * 0.20f, h * 0.25f)
                val botRight = Offset(w * 0.80f, h * 0.75f)
                drawCone(topLeft)
                drawCone(Offset(botRight.x, topLeft.y))
                drawCone(Offset(topLeft.x, botRight.y))
                drawCone(botRight)
                drawDiagramArrow(Offset(w * 0.25f, h * 0.50f), Offset(w * 0.52f, h * 0.45f), CrimsonLine, dashed = false)
                drawDiagramArrow(Offset(w * 0.68f, h * 0.52f), Offset(w * 0.48f, h * 0.34f), NeonPitchGreen, dashed = true)
                drawPlayerDot(Offset(w * 0.25f, h * 0.50f), AmberTimer)
                drawPlayerDot(Offset(w * 0.68f, h * 0.52f), NeonPitchGreen)
                drawBall(Offset(w * 0.29f, h * 0.50f))
            }

            DrillDiagramType.GOALKEEPER_ARCS -> {
                // Goal bar at bottom
                drawLine(
                    color = Color.White,
                    start = Offset(w * 0.28f, h * 0.82f),
                    end = Offset(w * 0.72f, h * 0.82f),
                    strokeWidth = 3.dp.toPx()
                )
                // GK angle arc
                drawArc(
                    color = NeonPitchGreen.copy(alpha = 0.45f),
                    startAngle = 190f,
                    sweepAngle = 160f,
                    useCenter = false,
                    topLeft = Offset(w * 0.30f, h * 0.50f),
                    size = Size(w * 0.40f, h * 0.55f),
                    style = Stroke(width = 2.dp.toPx(), pathEffect = dashEffect)
                )
                drawPlayerDot(Offset(w * 0.50f, h * 0.66f), NeonPitchGreen)
                drawDiagramArrow(Offset(w * 0.50f, h * 0.22f), Offset(w * 0.36f, h * 0.72f), AmberTimer, dashed = false)
                drawDiagramArrow(Offset(w * 0.50f, h * 0.22f), Offset(w * 0.64f, h * 0.72f), TacticalCyan, dashed = true)
                drawBall(Offset(w * 0.50f, h * 0.22f))
            }

            DrillDiagramType.SPRINT_LANES -> {
                val laneY = listOf(0.32f, 0.50f, 0.68f)
                laneY.forEachIndexed { idx, yf ->
                    val start = Offset(w * 0.15f, h * yf)
                    val end = Offset(w * 0.85f, h * yf)
                    drawCone(start)
                    drawCone(end)
                    drawDiagramArrow(start, end, if (idx == 1) NeonPitchGreen else TacticalCyan, dashed = idx != 1)
                }
                drawPlayerDot(Offset(w * 0.15f, h * 0.50f), NeonPitchGreen)
            }

            DrillDiagramType.JUGGLE_CIRCLE -> {
                val center = Offset(w * 0.50f, h * 0.50f)
                drawCircle(
                    color = NeonPitchGreen.copy(alpha = 0.35f),
                    radius = 52.dp.toPx(),
                    center = center,
                    style = Stroke(width = 2.dp.toPx(), pathEffect = dashEffect)
                )
                drawCircle(
                    color = TacticalCyan.copy(alpha = 0.25f),
                    radius = 30.dp.toPx(),
                    center = center,
                    style = Stroke(width = 1.5.dp.toPx())
                )
                drawPlayerDot(center, NeonPitchGreen)
                drawBall(Offset(center.x + 22.dp.toPx(), center.y - 16.dp.toPx()))
                drawCone(Offset(w * 0.22f, h * 0.25f))
                drawCone(Offset(w * 0.78f, h * 0.25f))
                drawCone(Offset(w * 0.22f, h * 0.75f))
                drawCone(Offset(w * 0.78f, h * 0.75f))
            }
        }
    }
}

private val CrimsonLine = Color(0xFFEF4444)

private fun DrawScope.drawCone(center: Offset) {
    val r = 6.dp.toPx()
    val triangle = Path().apply {
        moveTo(center.x, center.y - r)
        lineTo(center.x - r, center.y + r)
        lineTo(center.x + r, center.y + r)
        close()
    }
    drawPath(triangle, color = AmberTimer)
}

private fun DrawScope.drawPlayerDot(center: Offset, color: Color) {
    drawCircle(color = Color.Black.copy(alpha = 0.45f), radius = 9.dp.toPx(), center = center)
    drawCircle(color = color, radius = 7.dp.toPx(), center = center)
    drawCircle(color = Color.White, radius = 7.dp.toPx(), center = center, style = Stroke(width = 1.5.dp.toPx()))
}

private fun DrawScope.drawBall(center: Offset) {
    drawCircle(color = Color.White, radius = 6.dp.toPx(), center = center)
    drawCircle(color = Color.Black, radius = 2.5.dp.toPx(), center = center)
}

private fun DrawScope.drawDiagramArrow(start: Offset, end: Offset, color: Color, dashed: Boolean) {
    val dash = if (dashed) PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f) else null
    drawLine(
        color = color,
        start = start,
        end = end,
        strokeWidth = 2.2.dp.toPx(),
        pathEffect = dash,
        cap = StrokeCap.Round
    )
    val angle = atan2((end.y - start.y).toDouble(), (end.x - start.x).toDouble())
    val headLen = 9.dp.toPx()
    val a1 = angle + Math.toRadians(150.0)
    val a2 = angle - Math.toRadians(150.0)
    val p1 = Offset(end.x + (cos(a1) * headLen).toFloat(), end.y + (sin(a1) * headLen).toFloat())
    val p2 = Offset(end.x + (cos(a2) * headLen).toFloat(), end.y + (sin(a2) * headLen).toFloat())
    drawLine(color, end, p1, strokeWidth = 2.2.dp.toPx(), cap = StrokeCap.Round)
    drawLine(color, end, p2, strokeWidth = 2.2.dp.toPx(), cap = StrokeCap.Round)
}

@Composable
fun WeeklyActivityBarChart(
    points: List<DailyChartPoint>,
    modifier: Modifier = Modifier
) {
    if (points.isEmpty()) return
    val maxMin = (points.maxOfOrNull { it.minutes } ?: 30).coerceAtLeast(30)
    val gridColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
    val trackBarColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
    val activeTopColor = MaterialTheme.colorScheme.primary
    val activeBottomColor = CrimsonOpponent

    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .padding(horizontal = 6.dp, vertical = 6.dp)
        ) {
            val w = size.width
            val h = size.height
            val slotW = w / points.size
            val barW = (slotW * 0.44f).coerceAtMost(24.dp.toPx())

            for (i in 1..3) {
                val y = h - (i / 3f) * h
                drawLine(
                    color = gridColor,
                    start = Offset(0f, y),
                    end = Offset(w, y),
                    strokeWidth = 1.dp.toPx()
                )
            }

            points.forEachIndexed { idx, pt ->
                val xCenter = slotW * idx + slotW / 2f
                val left = xCenter - barW / 2f
                drawRoundRect(
                    color = trackBarColor,
                    topLeft = Offset(left, 0f),
                    size = Size(barW, h),
                    cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                )
                val ratio = if (pt.minutes == 0) 0.06f else (pt.minutes.toFloat() / maxMin).coerceIn(0.12f, 1f)
                val barH = h * ratio
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        if (pt.minutes > 0) listOf(activeTopColor, activeBottomColor)
                        else listOf(Color(0xFF47372B), Color(0xFF2B2018))
                    ),
                    topLeft = Offset(left, h - barH),
                    size = Size(barW, barH),
                    cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            points.forEach { pt ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = pt.label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
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
fun MonthlyActivityGrid(
    points: List<DailyChartPoint>,
    modifier: Modifier = Modifier
) {
    if (points.isEmpty()) return
    val weeks = points.chunked(7)
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        weeks.forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                week.forEach { day ->
                    val bg = when {
                        day.minutes >= 35 -> SpotlightGold
                        day.minutes >= 15 -> AmberGlow.copy(alpha = 0.80f)
                        day.minutes > 0 -> FlameOrange.copy(alpha = 0.55f)
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1.3f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(bg),
                        contentAlignment = Alignment.Center
                    ) {
                        if (day.minutes > 0) {
                            Text(
                                text = "${day.minutes}",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("28-Day Training Intensity (minutes/day)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Total: ${points.sumOf { it.minutes }} min", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
    }
}
