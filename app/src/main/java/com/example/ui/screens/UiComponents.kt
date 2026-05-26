package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

// Premium Glassmorphism Bento Card Wrapper
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    borderColor: Color = Color(0x13FFFFFF), // Elegant thin white/5 border
    backgroundColor: Color = DeepGrey, // Authentic #171717 background
    glowColor: Color? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    var isHovered by remember { mutableStateOf(false) }
    
    val baseModifier = modifier
        .shadow(
            elevation = if (glowColor != null) 12.dp else 0.dp,
            shape = RoundedCornerShape(24.dp),
            clip = false,
            ambientColor = glowColor ?: Color.Transparent,
            spotColor = glowColor ?: Color.Transparent
        )
        .clip(RoundedCornerShape(24.dp))
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    backgroundColor,
                    backgroundColor.copy(alpha = 0.92f)
                )
            )
        )
        .border(
            width = 1.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    borderColor,
                    borderColor.copy(alpha = 0.2f)
                )
            ),
            shape = RoundedCornerShape(24.dp)
        )

    val clickableModifier = if (onClick != null) {
        baseModifier.clickable(onClick = onClick)
    } else {
        baseModifier
    }

    Column(
        modifier = clickableModifier.padding(20.dp),
        content = content
    )
}

// Custom Draw TSI Brand Logo
@Composable
fun TSILogo(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    showText: Boolean = true
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        // Tilted black squircle containing bold white "TSI" lettering
        Box(
            modifier = Modifier
                .size(size)
                .rotate(-7f)
                .clip(RoundedCornerShape(size * 0.23f))
                .background(Color.Black)
                .border(
                    width = 1.dp,
                    color = AccentGold.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(size * 0.23f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "TSI",
                color = Color.White,
                fontSize = (size.value * 0.35f).sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = (-0.5).sp
            )
        }
        
        if (showText) {
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "THE SOCIAL INK",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = 1.8.sp,
                    color = InkWhite
                )
                Text(
                    text = "INKco OPERATING SYSTEM",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 0.5.sp,
                    color = ElectricBlue
                )
            }
        }
    }
}

// Reusable KPI Metric Card with Bento Highlight support
@Composable
fun KPICard(
    title: String,
    value: String,
    changeText: String,
    isPositive: Boolean,
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    isPremiumGlow: Boolean = false,
    highlightColor: Color? = null
) {
    val isHighlighted = highlightColor != null
    val bg = highlightColor ?: DeepGrey
    val borderCol = if (isHighlighted) Color.Transparent else if (isPremiumGlow) AccentGold.copy(alpha = 0.5f) else Color(0x13FFFFFF)
    val textCol = if (isHighlighted) InkWhite else TextSecondary
    val mainValCol = if (isHighlighted) InkWhite else if (isPremiumGlow) AccentGold else InkWhite

    GlassCard(
        modifier = modifier,
        borderColor = borderCol,
        backgroundColor = bg,
        glowColor = if (isPremiumGlow) AccentGold.copy(alpha = 0.12f) else if (isHighlighted) highlightColor?.copy(alpha = 0.25f) else null
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 11.sp,
                color = if (isHighlighted) InkWhite.copy(alpha = 0.8f) else textCol,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp,
                fontFamily = FontFamily.SansSerif
            )
            icon?.invoke()
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = mainValCol,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = (-0.5).sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (isHighlighted) InkWhite.copy(alpha = 0.2f)
                        else if (isPositive) SuccessGreen.copy(alpha = 0.15f)
                        else ErrorRed.copy(alpha = 0.15f)
                    )
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = if (isPositive) "+$changeText" else "-$changeText",
                    color = if (isHighlighted) InkWhite else if (isPositive) SuccessGreen else ErrorRed,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "vs yesterday",
                fontSize = 10.sp,
                color = if (isHighlighted) InkWhite.copy(alpha = 0.7f) else TextSecondary
            )
        }
    }
}

// Custom Recharts-like Line Chart in Compose
@Composable
fun PremiumLineChart(
    points: List<Float>,
    labels: List<String>,
    modifier: Modifier = Modifier,
    lineColor: Color = ElectricBlue,
    fillColor: Color = ElectricBlue.copy(alpha = 0.15f)
) {
    if (points.isEmpty()) return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(top = 10.dp, bottom = 20.dp, start = 10.dp, end = 10.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val spacing = width / (points.size - 1)

            val maxVal = points.maxOrNull() ?: 1f
            val minVal = points.minOrNull() ?: 0f
            val range = if (maxVal - minVal == 0f) 1f else (maxVal - minVal)

            // Draw Grid Lines (Horizontal Guidelines)
            val gridCount = 4
            for (i in 0..gridCount) {
                val y = height * (i.toFloat() / gridCount)
                drawLine(
                    color = CardBorder.copy(alpha = 0.3f),
                    start = Offset(0f, y),
                    end = Offset(width, y),
                    strokeWidth = 1.dp.toPx()
                )
            }

            // Construct Path for line chart
            val path = Path()
            val fillPath = Path()

            points.forEachIndexed { index, value ->
                val x = index * spacing
                // Normalize value to fit within 10% - 90% of bounding canvas height
                val normalizedY = height - (0.1f * height + ((value - minVal) / range) * (height * 0.75f))

                if (index == 0) {
                    path.moveTo(x, normalizedY)
                    fillPath.moveTo(x, height)
                    fillPath.lineTo(x, normalizedY)
                } else {
                    val prevX = (index - 1) * spacing
                    val prevVal = points[index - 1]
                    val prevNormalizedY = height - (0.1f * height + ((prevVal - minVal) / range) * (height * 0.75f))

                    // Draw smooth cubic curves
                    path.cubicTo(
                        (prevX + x) / 2f, prevNormalizedY,
                        (prevX + x) / 2f, normalizedY,
                        x, normalizedY
                    )
                    fillPath.cubicTo(
                        (prevX + x) / 2f, prevNormalizedY,
                        (prevX + x) / 2f, normalizedY,
                        x, normalizedY
                    )
                }

                if (index == points.size - 1) {
                    fillPath.lineTo(x, height)
                    fillPath.close()
                }

                // Draw pulsing point targets
                drawCircle(
                    color = lineColor,
                    radius = 3.dp.toPx(),
                    center = Offset(x, normalizedY)
                )
            }

            // Draw gradient area fill
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(fillColor, Color.Transparent)
                )
            )

            // Draw line border
            drawPath(
                path = path,
                color = lineColor,
                style = Stroke(width = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
            )
        }

        // Draw horizontal labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .offset(y = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            labels.forEach { label ->
                Text(
                    text = label,
                    color = TextSecondary,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// Reusable Custom Bar Chart
@Composable
fun PremiumBarChart(
    values: List<Double>,
    labels: List<String>,
    modifier: Modifier = Modifier,
    barColor: Color = ElectricBlue
) {
    if (values.isEmpty()) return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(top = 10.dp, bottom = 20.dp, start = 12.dp, end = 12.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val maxVal = values.maxOrNull() ?: 1.0
            val barCount = values.size
            val groupWidth = width / barCount
            val barWidth = groupWidth * 0.55f

            values.forEachIndexed { i, value ->
                val x = (i * groupWidth) + (groupWidth - barWidth) / 2f
                val pct = if (maxVal > 0) (value / maxVal).toFloat() else 0.1f
                val barHeight = height * pct * 0.85f
                val y = height - barHeight

                // Draw background shadow guide
                drawRoundRect(
                    color = CardBorder.copy(alpha = 0.15f),
                    topLeft = Offset(x, 0f),
                    size = Size(barWidth, height),
                    cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                )

                // Draw filled gradient bar
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(barColor, barColor.copy(alpha = 0.5f))
                    ),
                    topLeft = Offset(x, y),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                )
            }
        }

        // Horizontal bottom labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .offset(y = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            labels.forEach { label ->
                Text(
                    text = label,
                    color = TextSecondary,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// Custom Premium Concentric Circular Progress Ring
@Composable
fun ConcentricProgressRing(
    progress: Float,
    label: String,
    subLabel: String,
    modifier: Modifier = Modifier,
    ringColor: Color = ElectricBlue,
    ringSize: Dp = 100.dp
) {
    val animateProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "ring_anim"
    )

    Box(
        modifier = modifier.size(ringSize),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 8.dp.toPx()
            
            // Background arc track
            drawCircle(
                color = CardBorder.copy(alpha = 0.3f),
                style = Stroke(width = strokeWidth)
            )

            // Progress arc
            drawArc(
                color = ringColor,
                startAngle = -90f,
                sweepAngle = animateProgress * 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = androidx.compose.ui.graphics.StrokeCap.Round)
            )
        }
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                color = InkWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
            Text(
                text = subLabel,
                color = TextSecondary,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Glass Button
@Composable
fun GlassButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = true,
    colors: Color = ElectricBlue,
    tag: String = "action_button"
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPrimary) colors else Color.Transparent,
            contentColor = InkWhite
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .testTag(tag)
            .border(
                width = if (isPrimary) 0.dp else 1.dp,
                color = if (isPrimary) Color.Transparent else CardBorder,
                shape = RoundedCornerShape(12.dp)
            ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = 0.5.sp
        )
    }
}
