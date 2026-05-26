package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.INKcoViewModel
import com.example.data.UserRole
import com.example.ui.theme.*

@Composable
fun SettingsScreen(viewModel: INKcoViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()
    val isGlassEnabled by viewModel.glassmorphismEnabled.collectAsState()
    val activeRole by viewModel.userRole.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("settings_view")
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Settings Summary Header
        item {
            GlassCard(
                borderColor = ElectricBlue.copy(alpha = 0.3f),
                backgroundColor = CardBg.copy(alpha = 0.75f)
            ) {
                Text(
                    text = "AGENCY CONSOLE CONTROL PANEL",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = ElectricBlue,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "System Settings",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = InkWhite
                )
            }
        }

        // Active Session Toggler (ROLE SWITCHER)
        item {
            Text(
                text = "DYNAMIC ROLE PRIVILEGES",
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            GlassCard {
                Text(
                    text = "Simulate role actions instantly to test different SaaS flow workflows.",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                UserRole.values().forEach { role ->
                    val active = activeRole == role
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (active) ElectricBlue.copy(alpha = 0.15f) else Color.Transparent)
                            .clickable { viewModel.changeUserRole(role) }
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(if (active) ElectricBlue else CardBorder)
                                    .border(1.dp, if (active) ElectricBlue else TextSecondary, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    role.name,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (active) ElectricBlue else InkWhite
                                )
                                Text(
                                    text = when(role) {
                                        UserRole.ADMIN -> "Full access to billing, tasks, team boards, settings."
                                        UserRole.MANAGER -> "Approves content, updates boards, manages pipeline."
                                        UserRole.CREATOR -> "Uploads captions, changes draft statuses, writes Reels."
                                        UserRole.CLIENT -> "Secures overview, pays bills, checks pitch assets."
                                    },
                                    fontSize = 10.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Profile Details Card
        item {
            Text(
                text = "STAFF IDENTITY WORKSPACE",
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            GlassCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(ElectricBlue.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (currentUser?.name ?: "Inderneil").take(2).uppercase(),
                            color = ElectricBlue,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = currentUser?.name ?: "Inderneil Kanagali",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = InkWhite
                        )
                        Text(
                            text = currentUser?.email ?: "inderneilkanagali@gmail.com",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                GlassButton(
                    text = "Revoke Console Access Token (Logout)",
                    onClick = { viewModel.logout() },
                    isPrimary = false,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Theme Glassmorphism Customizer Card
        item {
            Text(
                text = "INTERFACE & COSMETICS",
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            GlassCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1.1f)) {
                        Text("Cyber Glassmorphism Style", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = InkWhite)
                        Text("Enable soft gradient translucency values on agency layout containers.", fontSize = 11.sp, color = TextSecondary)
                    }
                    Switch(
                        checked = isGlassEnabled,
                        onCheckedChange = { viewModel.toggleGlassmorphism() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = ElectricBlue,
                            checkedTrackColor = ElectricBlue.copy(alpha = 0.3f),
                            uncheckedThumbColor = TextSecondary,
                            uncheckedTrackColor = CardBorder
                        )
                    )
                }
            }
        }

        // Future AI roadmap placeholders
        item {
            Text(
                text = "FUTURE AI ROADMAP PIPELINES",
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                val futureFeatures = listOf(
                    "AI Smart Proposal Generator" to "Instantly write enterprise-grade client pitch decks mapping design sprints.",
                    "Automated CRM Onboarding flows" to "Self-provision client accounts, agreements, and Stripe subscription channels automatically.",
                    "Smart Analytical Reporting" to "Predictive social virality indexes using automated machine learning vectors."
                )

                futureFeatures.forEach { (title, desc) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(DeepGrey)
                            .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(AccentGold.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.AutoAwesome, "AI Preview", tint = AccentGold, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(title, color = InkWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(AccentGold.copy(alpha = 0.12f))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text("PAID AI", color = AccentGold, fontSize = 7.sp, fontWeight = FontWeight.ExtraBold)
                                }
                            }
                            Text(desc, color = TextSecondary, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}
