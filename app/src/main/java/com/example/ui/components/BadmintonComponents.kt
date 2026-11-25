package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.RepeatMode
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
import com.example.data.model.CourtDiagramType
import com.example.ui.theme.AmberTimer
import com.example.ui.theme.CobaltAccent
import com.example.ui.theme.CourtBlueCanvasDark
import com.example.ui.theme.CourtBlueCanvasLight
import com.example.ui.theme.CourtCyan
import com.example.ui.theme.ElectricCyanPrimary
import com.example.ui.theme.SkyBlueHighlight
import com.example.ui.theme.VictoryGold
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
 * Interactive 100% Offline Animated Badminton Court Illustration & Motion Player
 * styled in BWF Olympic Blue with Cyan & Gold motion vectors.
 */
@Composable
fun OfflineAnimatedCourtPlayer(
    diagramType: CourtDiagramType,
    motionPhases: List<String>,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(true) }
    var slowMotion by remember { mutableStateOf(false) }

    val durationMs = if (slowMotion) 4800 else 2600
    val infinite = rememberInfiniteTransition(label = "court_anim")
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

                // BWF Royal Blue Badminton Court Surface
                drawRect(
                    brush = Brush.verticalGradient(listOf(CourtBlueCanvasDark, CourtBlueCanvasLight, CourtBlueCanvasDark))
                )

                val padX = w * 0.08f
                val padY = h * 0.08f
                val cw = w - padX * 2f
                val ch = h - padY * 2f
                val lineColor = Color.White.copy(alpha = 0.84f)
                val faintLine = Color.White.copy(alpha = 0.36f)

                drawRect(lineColor, Offset(padX, padY), Size(cw, ch), style = Stroke(1.8.dp.toPx()))
                val sideInset = cw * 0.08f
                drawLine(faintLine, Offset(padX + sideInset, padY), Offset(padX + sideInset, padY + ch), 1.2.dp.toPx())
                drawLine(faintLine, Offset(padX + cw - sideInset, padY), Offset(padX + cw - sideInset, padY + ch), 1.2.dp.toPx())

                val netY = padY + ch * 0.50f
                drawLine(
                    color = VictoryGold,
                    start = Offset(padX - 6.dp.toPx(), netY),
                    end = Offset(padX + cw + 6.dp.toPx(), netY),
                    strokeWidth = 3.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f), 0f)
                )

                val shortOffset = ch * 0.145f
                drawLine(lineColor, Offset(padX, netY - shortOffset), Offset(padX + cw, netY - shortOffset), 1.4.dp.toPx())
                drawLine(lineColor, Offset(padX, netY + shortOffset), Offset(padX + cw, netY + shortOffset), 1.4.dp.toPx())

                drawLine(lineColor, Offset(w * 0.5f, padY), Offset(w * 0.5f, netY - shortOffset), 1.4.dp.toPx())
                drawLine(lineColor, Offset(w * 0.5f, netY + shortOffset), Offset(w * 0.5f, padY + ch), 1.4.dp.toPx())

                val rearOffset = ch * 0.06f
                drawLine(faintLine, Offset(padX, padY + rearOffset), Offset(padX + cw, padY + rearOffset), 1.1.dp.toPx())
                drawLine(faintLine, Offset(padX, padY + ch - rearOffset), Offset(padX + cw, padY + ch - rearOffset), 1.1.dp.toPx())

                val basePt = Offset(w * 0.50f, netY + ch * 0.25f)
                when (diagramType) {
                    CourtDiagramType.SIX_CORNER_FOOTWORK -> {
                        val corners = listOf(
                            Offset(padX + cw * 0.16f, netY + ch * 0.10f),
                            Offset(padX + cw * 0.84f, netY + ch * 0.10f),
                            Offset(padX + cw * 0.84f, netY + ch * 0.42f),
                            Offset(padX + cw * 0.16f, netY + ch * 0.42f)
                        )
                        corners.forEach { c ->
                            drawCourtArrow(basePt, c, ElectricCyanPrimary.copy(alpha = 0.55f), dashed = true)
                            drawCircle(VictoryGold.copy(alpha = 0.65f), 4.dp.toPx(), c)
                        }
                        val seg = (t * 4f).toInt().coerceIn(0, 3)
                        val localT = (t * 4f) - seg
                        val pingPong = if (localT < 0.5f) localT * 2f else (1f - localT) * 2f
                        val target = corners[seg]
                        val playerPos = lerpOffset(basePt, target, pingPong)
                        drawPlayerMarker(playerPos, ElectricCyanPrimary)
                    }

                    CourtDiagramType.SMASH_TRAJECTORY -> {
                        val rearOrigin = Offset(padX + cw * 0.72f, netY + ch * 0.40f)
                        val oppTarget = Offset(padX + cw * 0.22f, padY + ch * 0.30f)
                        val recoverPt = Offset(w * 0.50f, netY + ch * 0.20f)
                        drawCourtArrow(rearOrigin, oppTarget, VictoryGold, dashed = false)
                        drawCourtArrow(rearOrigin, recoverPt, CourtCyan.copy(alpha = 0.75f), dashed = true)
                        val playerPos = lerpOffset(rearOrigin, recoverPt, (t * 1.2f).coerceIn(0f, 1f))
                        val shuttlePos = lerpOffset(rearOrigin, oppTarget, (sin(t * PI).toFloat()))
                        drawPlayerMarker(playerPos, ElectricCyanPrimary)
                        drawShuttlecock(shuttlePos)
                    }

                    CourtDiagramType.SERVE_ZONES -> {
                        val server = Offset(w * 0.44f, netY + shortOffset + 12.dp.toPx())
                        val shortTarget = Offset(w * 0.68f, netY - shortOffset - 8.dp.toPx())
                        val flickTarget = Offset(w * 0.78f, padY + ch * 0.12f)
                        drawCourtArrow(server, shortTarget, ElectricCyanPrimary, dashed = false)
                        drawCourtArrow(server, flickTarget, AmberTimer, dashed = true)
                        drawCircle(ElectricCyanPrimary.copy(alpha = 0.35f), 14.dp.toPx(), shortTarget)
                        drawPlayerMarker(server, ElectricCyanPrimary)
                        val shuttlePos = if (t < 0.5f) lerpOffset(server, shortTarget, t * 2f)
                        else lerpOffset(server, flickTarget, (t - 0.5f) * 2f)
                        drawShuttlecock(shuttlePos)
                    }

                    CourtDiagramType.NET_HAIRPIN -> {
                        val playerStart = basePt
                        val netLunge = Offset(padX + cw * 0.72f, netY + 14.dp.toPx())
                        val overNet = Offset(padX + cw * 0.72f, netY - 14.dp.toPx())
                        drawCourtArrow(playerStart, netLunge, CourtCyan, dashed = true)
                        drawCourtArrow(netLunge, overNet, VictoryGold, dashed = false)
                        val pPos = lerpOffset(playerStart, netLunge, sin(t * PI).toFloat())
                        drawPlayerMarker(pPos, ElectricCyanPrimary)
                        val sPos = lerpOffset(netLunge, overNet, t)
                        drawShuttlecock(sPos)
                    }

                    CourtDiagramType.DEFENSE_BLOCK -> {
                        val attacker = Offset(padX + cw * 0.25f, padY + ch * 0.12f)
                        val defender = Offset(padX + cw * 0.62f, netY + ch * 0.26f)
                        val blockDrop = Offset(padX + cw * 0.75f, netY - 16.dp.toPx())
                        drawCourtArrow(attacker, defender, AmberTimer, dashed = false)
                        drawCourtArrow(defender, blockDrop, ElectricCyanPrimary, dashed = false)
                        drawPlayerMarker(attacker, AmberTimer)
                        drawPlayerMarker(defender, ElectricCyanPrimary)
                        val sPos = if (t < 0.5f) lerpOffset(attacker, defender, t * 2f)
                        else lerpOffset(defender, blockDrop, (t - 0.5f) * 2f)
                        drawShuttlecock(sPos)
                    }

                    CourtDiagramType.CLEAR_AND_DROP -> {
                        val hitPt = Offset(padX + cw * 0.24f, netY + ch * 0.40f)
                        val clearTarget = Offset(padX + cw * 0.76f, padY + ch * 0.08f)
                        val dropTarget = Offset(padX + cw * 0.76f, netY - ch * 0.12f)
                        drawCourtArrow(hitPt, clearTarget, CourtCyan, dashed = true)
                        drawCourtArrow(hitPt, dropTarget, VictoryGold, dashed = false)
                        drawPlayerMarker(hitPt, ElectricCyanPrimary)
                        val sPos = if (t < 0.5f) lerpOffset(hitPt, clearTarget, t * 2f)
                        else lerpOffset(hitPt, dropTarget, (t - 0.5f) * 2f)
                        drawShuttlecock(sPos)
                    }

                    CourtDiagramType.DOUBLES_ROTATION -> {
                        val p1Start = Offset(w * 0.50f, netY + ch * 0.16f)
                        val p2Start = Offset(w * 0.50f, netY + ch * 0.38f)
                        val p1Def = Offset(padX + cw * 0.28f, netY + ch * 0.28f)
                        val p2Def = Offset(padX + cw * 0.72f, netY + ch * 0.28f)
                        val blend = sin(t * PI).toFloat()
                        drawCourtArrow(p1Start, p1Def, CourtCyan, dashed = true)
                        drawCourtArrow(p2Start, p2Def, CourtCyan, dashed = true)
                        drawPlayerMarker(lerpOffset(p1Start, p1Def, blend), ElectricCyanPrimary)
                        drawPlayerMarker(lerpOffset(p2Start, p2Def, blend), VictoryGold)
                    }

                    CourtDiagramType.COURT_DIMENSIONS -> {
                        val labelStyle = TextStyle(color = VictoryGold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        drawText(textMeasurer, "13.40m Total Length", Offset(padX + 6.dp.toPx(), padY + 4.dp.toPx()), style = labelStyle)
                        drawText(textMeasurer, "6.10m Doubles | 5.18m Singles", Offset(padX + 6.dp.toPx(), padY + ch - 18.dp.toPx()), style = labelStyle)
                        drawText(textMeasurer, "NET 1.524m", Offset(w * 0.5f - 28.dp.toPx(), netY - 14.dp.toPx()), style = labelStyle.copy(color = Color.White))
                        drawText(textMeasurer, "1.98m Short Line", Offset(padX + 6.dp.toPx(), netY + shortOffset + 3.dp.toPx()), style = labelStyle.copy(color = CourtCyan))
                        val pulseR = (8.dp.toPx() + 4.dp.toPx() * sin(t * 2 * PI).toFloat())
                        drawCircle(ElectricCyanPrimary.copy(alpha = 0.35f), pulseR, Offset(w * 0.5f, netY))
                    }

                    CourtDiagramType.CONDITIONING_AGILITY -> {
                        val cones = listOf(
                            Offset(w * 0.50f, padY + ch * 0.78f),
                            Offset(w * 0.50f, padY + ch * 0.52f),
                            Offset(padX + cw * 0.22f, padY + ch * 0.52f),
                            Offset(padX + cw * 0.78f, padY + ch * 0.52f)
                        )
                        drawCourtArrow(cones[0], cones[1], ElectricCyanPrimary, false)
                        drawCourtArrow(cones[1], cones[2], VictoryGold, true)
                        drawCourtArrow(cones[2], cones[3], VictoryGold, false)
                        cones.forEach { drawConeMarker(it) }
                        val idx = (t * 3f).toInt().coerceIn(0, 2)
                        val lt = (t * 3f) - idx
                        val pPos = when (idx) {
                            0 -> lerpOffset(cones[0], cones[1], lt)
                            1 -> lerpOffset(cones[1], cones[2], lt)
                            else -> lerpOffset(cones[2], cones[3], lt)
                        }
                        drawPlayerMarker(pPos, ElectricCyanPrimary)
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
                    text = "OFFLINE MOTION",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = ElectricCyanPrimary,
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

private fun DrawScope.drawShuttlecock(center: Offset) {
    drawCircle(VictoryGold.copy(alpha = 0.35f), 9.dp.toPx(), center)
    drawCircle(Color.White, 5.dp.toPx(), center)
    drawCircle(Color.Black, 2.dp.toPx(), center)
}

private fun DrawScope.drawConeMarker(center: Offset) {
    val r = 6.dp.toPx()
    val path = Path().apply {
        moveTo(center.x, center.y - r)
        lineTo(center.x - r, center.y + r)
        lineTo(center.x + r, center.y + r)
        close()
    }
    drawPath(path, AmberTimer)
}

private fun DrawScope.drawCourtArrow(start: Offset, end: Offset, color: Color, dashed: Boolean) {
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
                        day.minutes >= 15 -> CobaltAccent
                        day.minutes > 0 -> SkyBlueHighlight.copy(alpha = 0.72f)
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                    val txtColor = when {
                        day.minutes >= 15 -> Color.White
                        else -> Color(0xFF061B44)
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
