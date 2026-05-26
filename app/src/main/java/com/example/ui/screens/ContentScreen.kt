package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.ContentItem
import com.example.data.ContentStatus
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentScreen(viewModel: INKcoViewModel) {
    val contentItems by viewModel.contentItems.collectAsState()
    
    var activeFilter by remember { mutableStateOf<ContentStatus?>(null) }
    var showDraftForm by remember { mutableStateOf(false) }

    // Draft form parameters
    var draftTitle by remember { mutableStateOf("") }
    var draftClient by remember { mutableStateOf("Fintech Retainer") }
    var draftFormat by remember { mutableStateOf("Reels") }
    var draftCaption by remember { mutableStateOf("") }
    var draftHashtags by remember { mutableStateOf("") }

    val filteredItems = remember(contentItems, activeFilter) {
        if (activeFilter == null) contentItems
        else contentItems.filter { it.status == activeFilter }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("content_view")
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Creative Calendar Hero Header
        item {
            GlassCard(
                borderColor = ElectricBlue.copy(alpha = 0.3f),
                backgroundColor = CardBg.copy(alpha = 0.75f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "SOCIAL CONTENT PRODUCTION DECK",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = ElectricBlue,
                            letterSpacing = 1.2.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Active Content Engines",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = InkWhite
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(ElectricBlue)
                            .clickable { showDraftForm = !showDraftForm }
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = if (showDraftForm) Icons.Filled.Close else Icons.Filled.Add,
                            contentDescription = "New Draft",
                            tint = InkWhite,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Expandable Inline Draft Creator
        item {
            AnimatedVisibility(
                visible = showDraftForm,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                GlassCard(borderColor = ElectricBlue.copy(alpha = 0.5f)) {
                    Text("CREATE NEW CREATIVE ASSET DRAFT", fontSize = 12.sp, color = ElectricBlue, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = draftTitle,
                        onValueChange = { draftTitle = it },
                        label = { Text("Asset Concept Title") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = draftClient,
                            onValueChange = { draftClient = it },
                            label = { Text("Client") },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = draftFormat,
                            onValueChange = { draftFormat = it },
                            label = { Text("Format") },
                            placeholder = { Text("Reels / Carousel") },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Box(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                        OutlinedTextField(
                            value = draftCaption,
                            onValueChange = { draftCaption = it },
                            label = { Text("Content Caption Copy") },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
                            minLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Built-in local AI Caption Generator / Suggestion Engine!
                        Button(
                            onClick = {
                                viewModel.logBusinessCardEngagement("AI Draft Trigger", "Content Composer")
                                draftCaption = when(draftFormat.lowercase()) {
                                    "reels" -> "Why ${draftClient} is radically shifting client retention games this weekend. Stop outsourcing and start automating. Full roadmap in bio. 🦾✨\n\n#thesocialink #brand #growth"
                                    "carousels" -> "Slide 1: Structure is identity.\nSlide 2: Asymmetry is memorable.\nSlide 3: Premium minimalism outperforms clutter. Period.\n\nSave this for your design sprints. 📘📂 #minimalism #design"
                                    else -> "Built with purpose. Positioned for luxury. How we designed the creative ecosystem for ${draftClient}. Scale with intent. 🌱📈 #luxury #branding"
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = DeepGrey),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(6.dp)
                                .height(26.dp)
                                .border(1.dp, ElectricBlue, RoundedCornerShape(8.dp))
                        ) {
                            Icon(Icons.Filled.AutoAwesome, "AI Copy", tint = ElectricBlue, modifier = Modifier.size(10.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("AI Caption Assist", fontSize = 8.sp, color = ElectricBlue, fontWeight = FontWeight.Bold)
                        }
                    }

                    OutlinedTextField(
                        value = draftHashtags,
                        onValueChange = { draftHashtags = it },
                        label = { Text("Hashtags database") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                if (draftTitle.isNotBlank()) {
                                    viewModel.createContent(draftClient, draftTitle, draftFormat, draftCaption, draftHashtags)
                                    draftTitle = ""
                                    draftCaption = ""
                                    draftHashtags = ""
                                    showDraftForm = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Enlist Draft", fontWeight = FontWeight.Bold, color = InkWhite)
                        }
                        Button(
                            onClick = { showDraftForm = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            border = ButtonDefaults.outlinedButtonBorder,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(0.5f)
                        ) {
                            Text("Cancel", color = TextSecondary)
                        }
                    }
                }
            }
        }

        // Horizontal status steppers/filters
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // ALL trigger
                val isAll = activeFilter == null
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isAll) ElectricBlue.copy(alpha = 0.2f) else DeepGrey)
                        .border(1.dp, if (isAll) ElectricBlue else CardBorder, RoundedCornerShape(8.dp))
                        .clickable { activeFilter = null }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("ALL", color = if (isAll) ElectricBlue else TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }

                ContentStatus.values().forEach { status ->
                    val active = activeFilter == status
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (active) ElectricBlue.copy(alpha = 0.2f) else DeepGrey)
                            .border(1.dp, if (active) ElectricBlue else CardBorder, RoundedCornerShape(8.dp))
                            .clickable { activeFilter = status }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(status.name, color = if (active) ElectricBlue else TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Active items timeline feed
        if (filteredItems.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No creative assets matching this stage.", color = TextSecondary, fontSize = 13.sp)
                }
            }
        }

        items(filteredItems) { item ->
            GlassCard(
                borderColor = when(item.status) {
                    ContentStatus.POSTED -> SuccessGreen.copy(alpha = 0.4f)
                    ContentStatus.APPROVED -> ElectricBlue.copy(alpha = 0.4f)
                    ContentStatus.SCHEDULED -> AccentGold.copy(alpha = 0.4f)
                    else -> CardBorder
                }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1.1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(ElectricBlue.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(item.format.uppercase(), color = ElectricBlue, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = item.clientName,
                                color = TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = item.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = InkWhite
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.caption,
                            fontSize = 11.sp,
                            color = TextSecondary,
                            maxLines = 3
                        )
                        if (item.hashtags.isNotBlank()) {
                            Text(
                                text = item.hashtags,
                                fontSize = 10.sp,
                                color = ElectricBlue,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    // Format indicator icon
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when(item.format.lowercase()) {
                                "reels", "shorts" -> Icons.Filled.OndemandVideo
                                "carousels" -> Icons.Filled.ViewCarousel
                                "stories" -> Icons.Filled.Bolt
                                "founder pov" -> Icons.Filled.Face
                                else -> Icons.Filled.Photo
                            },
                            contentDescription = "Format icon",
                            tint = ElectricBlue,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Interactive status transition trigger!
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.CalendarMonth, "ScheduledAt", tint = TextSecondary, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(item.scheduledAt, color = TextSecondary, fontSize = 10.sp)
                    }

                    // Status pill button to cycle the stages easily!
                    if (item.status != ContentStatus.POSTED) {
                        Button(
                            onClick = {
                                val nextStatus = when(item.status) {
                                    ContentStatus.DRAFT -> ContentStatus.EDITING
                                    ContentStatus.EDITING -> ContentStatus.APPROVED
                                    ContentStatus.APPROVED -> ContentStatus.SCHEDULED
                                    ContentStatus.SCHEDULED -> ContentStatus.POSTED
                                    else -> ContentStatus.POSTED
                                }
                                viewModel.updateContentStatus(item.id, nextStatus)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CardBorder),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(28.dp).testTag("promote_content_btn_${item.id}"),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "PROMOTE TO: " + when(item.status) {
                                    ContentStatus.DRAFT -> "EDITING"
                                    ContentStatus.EDITING -> "APPROVED"
                                    ContentStatus.APPROVED -> "SCHEDULED"
                                    ContentStatus.SCHEDULED -> "POSTED"
                                    else -> "POSTED"
                                },
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = InkWhite
                            )
                        }
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.CheckCircle, "Live", tint = SuccessGreen, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("SHIPPED & LIVE", color = SuccessGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
