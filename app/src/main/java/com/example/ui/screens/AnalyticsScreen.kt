package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.INKcoViewModel
import com.example.data.SampleData
import com.example.ui.theme.*

@Composable
fun AnalyticsScreen(viewModel: INKcoViewModel) {
    // Platform Switcher State
    var activePlatform by remember { mutableStateOf("Instagram") } // "Instagram", "TikTok", "LinkedIn"

    val activeSnapshot = remember(activePlatform) {
        when(activePlatform) {
            "Instagram" -> SampleData.analyticsInstagram
            "TikTok" -> SampleData.analyticsTikTok
            else -> SampleData.analyticsLinkedIn
        }
    }

    // Connect to calculators in view model
    val followers by viewModel.calcFollowers.collectAsState()
    val likes by viewModel.calcLikes.collectAsState()
    val comments by viewModel.calcComments.collectAsState()
    val shares by viewModel.calcShares.collectAsState()
    val saves by viewModel.calcSaves.collectAsState()
    val reach by viewModel.calcReach.collectAsState()
    val impressions by viewModel.calcImpressions.collectAsState()

    val stdEngRate = viewModel.calculateStandardEngagement()
    val reachEngRate = viewModel.calculateReachEngagement()
    val impEngRate = viewModel.calculateImpressionEngagement()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("analytics_view")
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Analytics Summary Header
        item {
            GlassCard(
                borderColor = ElectricBlue.copy(alpha = 0.3f),
                backgroundColor = CardBg.copy(alpha = 0.75f)
            ) {
                Text(
                    text = "AGENCY AUDIENCE ENGAGEMENT RADAR",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = ElectricBlue,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Social Platform Analytics",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = InkWhite
                )
            }
        }

        // Platform Switcher
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Instagram", "TikTok", "LinkedIn").forEach { platform ->
                    val active = activePlatform == platform
                    Button(
                        onClick = { activePlatform = platform },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (active) ElectricBlue.copy(alpha = 0.25f) else DeepGrey,
                            contentColor = if (active) ElectricBlue else TextSecondary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                            .border(1.dp, if (active) ElectricBlue else CardBorder, RoundedCornerShape(12.dp))
                    ) {
                        Text(platform, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Concentric dial and key stats block
        item {
            GlassCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1.1f)) {
                        Text(
                            text = "${activePlatform.uppercase()} CORE AUDIENCE STATS",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${String.format("%,.0f", activeSnapshot.followers.toDouble())} followers",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = InkWhite
                        )
                        Text(
                            text = "Platform range: ${activeSnapshot.capturedAt}",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    // Concentric Circular Index Gauge
                    ConcentricProgressRing(
                        progress = when(activePlatform) {
                            "Instagram" -> 0.124f
                            "TikTok" -> 0.183f
                            else -> 0.082f
                        },
                        label = when(activePlatform) {
                            "Instagram" -> "12.4%"
                            "TikTok" -> "18.3%"
                            else -> "8.2%"
                        },
                        subLabel = "E.R.%",
                        ringColor = AccentGold,
                        ringSize = 72.dp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Breakdown list
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("TOTAL REACH", fontSize = 9.sp, color = TextSecondary)
                        Text(String.format("%,.0f", activeSnapshot.reach.toDouble()), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = InkWhite)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("IMPRESSIONS", fontSize = 9.sp, color = TextSecondary)
                        Text(String.format("%,.0f", activeSnapshot.impressions.toDouble()), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = InkWhite)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("ENGAGEMENTS", fontSize = 9.sp, color = TextSecondary)
                        Text(String.format("%,.0f", (activeSnapshot.likes + activeSnapshot.comments + activeSnapshot.shares + activeSnapshot.saves).toDouble()), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ElectricBlue)
                    }
                }
            }
        }

        // Audience reach progression curves
        item {
            GlassCard {
                Text(
                    text = "REACTIVE AUDIENCE IMPRESSIONS TREND",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                
                val pointsList = when(activePlatform) {
                    "Instagram" -> listOf(110000f, 134000f, 180000f, 220000f, 290000f, 450000f)
                    "TikTok" -> listOf(600000f, 850000f, 1100000f, 1600000f, 1900000f, 2400000f)
                    else -> listOf(38000f, 45000f, 60000f, 82000f, 110000f, 155000f)
                }
                
                PremiumLineChart(
                    points = pointsList,
                    labels = listOf("Jan", "Feb", "Mar", "Apr", "May", "Current"),
                     lineColor = ElectricBlue,
                     fillColor = ElectricBlue.copy(alpha = 0.1f)
                )
            }
        }

        // Best Posting Times widget
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GlassCard(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Timer, "Best Time", tint = AccentGold, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("OPTIVE SLOTS", fontSize = 9.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Mon 6:00 PM", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = InkWhite)
                    Text("Highest attention rate", fontSize = 10.sp, color = TextSecondary)
                }

                GlassCard(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Timer, "Best Time", tint = ElectricBlue, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("CYCLE B", fontSize = 9.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Wed 5:30 PM", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = InkWhite)
                    Text("Luxe brand peaks", fontSize = 10.sp, color = TextSecondary)
                }
            }
        }

        // ENGAGEMENT CALCULATOR SYSTEM
        item {
            Text(
                text = "INTERACTIVE CALCULATOR",
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            GlassCard(
                borderColor = ElectricBlue.copy(alpha = 0.3f),
                backgroundColor = CardBg.copy(alpha = 0.85f)
            ) {
                Text(
                    text = "ENGAGEMENT RATE FORMULAS",
                    fontSize = 12.sp,
                    color = AccentGold,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Input boxes (Grid layout)
                OutlinedTextField(
                    value = followers,
                    onValueChange = { viewModel.calcFollowers.value = it },
                    label = { Text("Followers Base") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = likes,
                        onValueChange = { viewModel.calcLikes.value = it },
                        label = { Text("Likes") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = comments,
                        onValueChange = { viewModel.calcComments.value = it },
                        label = { Text("Comments") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = shares,
                        onValueChange = { viewModel.calcShares.value = it },
                        label = { Text("Shares") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = saves,
                        onValueChange = { viewModel.calcSaves.value = it },
                        label = { Text("Saves") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = reach,
                        onValueChange = { viewModel.calcReach.value = it },
                        label = { Text("Post Reach") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = impressions,
                        onValueChange = { viewModel.calcImpressions.value = it },
                        label = { Text("Impressions") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
                        modifier = Modifier.weight(1f)
                    )
                }

                Divider(color = CardBorder, modifier = Modifier.padding(bottom = 16.dp))

                // Real time calculation outputs mapped precisely to prompt equations
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Standard Engagement Rate
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(CardBorder.copy(alpha = 0.2f))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Standard E.R. Formula", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = InkWhite)
                            Text("(Likes + Comments + Shares + Saves) / Followers × 100", fontSize = 8.sp, color = TextSecondary)
                        }
                        Text("${String.format("%.2f", stdEngRate)}%", color = ElectricBlue, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                    }

                    // Reach Engagement
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(CardBorder.copy(alpha = 0.2f))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Reach Engagement Formula", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = InkWhite)
                            Text("Total Engagement / Reach × 100", fontSize = 8.sp, color = TextSecondary)
                        }
                        Text("${String.format("%.2f", reachEngRate)}%", color = AccentGold, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                    }

                    // Impression Engagement
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(CardBorder.copy(alpha = 0.2f))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Impression Engagement Formula", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = InkWhite)
                            Text("Total Engagement / Impressions × 100", fontSize = 8.sp, color = TextSecondary)
                        }
                        Text("${String.format("%.2f", impEngRate)}%", color = SuccessGreen, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }
    }
}


