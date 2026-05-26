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
import com.example.ui.Screen
import com.example.data.UserRole
import com.example.data.TaskPriority
import com.example.data.TaskStatus
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: INKcoViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()
    val invoices by viewModel.invoices.collectAsState()
    val contentItems by viewModel.contentItems.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val activities by viewModel.activities.collectAsState()

    var showInvoiceDialog by remember { mutableStateOf(false) }
    var showTaskDialog by remember { mutableStateOf(false) }

    // Dialog state controllers
    var invClient by remember { mutableStateOf("") }
    var invAmount by remember { mutableStateOf("") }
    var invDate by remember { mutableStateOf("") }

    var taskTitle by remember { mutableStateOf("") }
    var taskClient by remember { mutableStateOf("") }
    var taskPriority by remember { mutableStateOf(TaskPriority.MEDIUM) }
    var taskInitialStatus by remember { mutableStateOf(TaskStatus.TO_DO) }
    var statusDropdownExpanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("dashboard_view")
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Bento-Style Custom Logo & Profile Header Row
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Brand Segment
                TSILogo(size = 38.dp, showText = true)

                // Right Profile & Action Segment
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Glass Search Icon Circle
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(DeepGrey)
                            .border(1.dp, Color(0x13FFFFFF), CircleShape)
                            .clickable { /* Search Action */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Gold Border Avatar Card
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(DeepGrey)
                            .border(1.5.dp, AccentGold, CircleShape)
                            .clickable { viewModel.navigateTo(Screen.SETTINGS) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (currentUser?.name ?: "IK").take(2).uppercase(),
                            color = InkWhite,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // Bento Row List 1: Clients Count (Small, Left) & Task Velocity (Small, Right)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Active Clients Count Box
                GlassCard(
                    modifier = Modifier.weight(1.0f).height(125.dp).testTag("bento_clients_card")
                ) {
                    Text(
                        text = "CLIENTS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${viewModel.clients.value.size}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = InkWhite,
                            fontFamily = FontFamily.SansSerif
                        )
                        
                        // Stacked avatar indicators from the UI mockup
                        Row(
                            horizontalArrangement = Arrangement.spacedBy((-8).dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(ElectricBlue)
                                    .border(1.dp, DeepGrey, CircleShape)
                            )
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(AccentGold)
                                    .border(1.dp, DeepGrey, CircleShape)
                            )
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray)
                                    .border(1.dp, DeepGrey, CircleShape)
                            )
                        }
                    }
                }

                // System Velocity - Dynamic Solid Accent Color Card!
                val total = tasks.size
                val completed = tasks.filter { it.status == TaskStatus.COMPLETED }.size
                val velocity = if (total > 0) (completed * 100 / total) else 88

                KPICard(
                    title = "VELOCITY",
                    value = "${velocity}%",
                    changeText = "COMPLETED",
                    isPositive = true,
                    modifier = Modifier.weight(1.0f).height(125.dp).testTag("bento_velocity_card"),
                    highlightColor = ElectricBlue
                )
            }
        }

        // Bento Card 3: Content Pipeline tracking block with inline statuses (col-span-2)
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth().testTag("bento_pipeline_card")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CONTENT PIPELINE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        letterSpacing = 1.2.sp
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0x13FFFFFF))
                            .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "PRO EDITION",
                            color = AccentGold,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val displayedPipeline = if (contentItems.isNotEmpty()) {
                        contentItems.take(3)
                    } else {
                        listOf(
                            com.example.data.ContentItem(title = "Luxury Retail Reel", status = com.example.data.ContentStatus.EDITING, clientName = "Luxury Retail Co", format = "Reels", caption = "", hashtags = "", scheduledAt = ""),
                            com.example.data.ContentItem(title = "Fintech Case Study", status = com.example.data.ContentStatus.SCHEDULED, clientName = "Fintech Retainer", format = "Reels", caption = "", hashtags = "", scheduledAt = ""),
                            com.example.data.ContentItem(title = "Agency POV Series", status = com.example.data.ContentStatus.APPROVED, clientName = "The Social INK", format = "Stories", caption = "", hashtags = "", scheduledAt = "")
                        )
                    }

                    displayedPipeline.forEach { item ->
                        val indicatorColor = when (item.status) {
                            com.example.data.ContentStatus.EDITING -> ElectricBlue
                            com.example.data.ContentStatus.SCHEDULED -> SuccessGreen
                            else -> AccentGold
                        }
                        val statusText = when (item.status) {
                            com.example.data.ContentStatus.DRAFT -> "Draft"
                            com.example.data.ContentStatus.EDITING -> "Editing"
                            com.example.data.ContentStatus.APPROVED -> "Review"
                            com.example.data.ContentStatus.SCHEDULED -> "Scheduled"
                            com.example.data.ContentStatus.POSTED -> "Posted"
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0x0AFFFFFF))
                                .border(1.dp, Color(0x05FFFFFF), RoundedCornerShape(16.dp))
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(indicatorColor)
                                )
                                Text(
                                    text = item.title,
                                    color = InkWhite,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Text(
                                text = statusText.uppercase(),
                                color = TextSecondary,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Bento Element 4: Elegant Horizontal Action pill buttons (col-span-2)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // New Task Action in solid brilliant high-contrast White
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .clip(RoundedCornerShape(26.dp))
                        .background(InkWhite)
                        .clickable { showTaskDialog = true }
                        .testTag("action_new_task"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "NEW TASK",
                        color = InkBlack,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                }

                // New Invoice Action in dark outlines
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .clip(RoundedCornerShape(26.dp))
                        .background(DeepGrey)
                        .border(1.dp, Color(0x13FFFFFF), RoundedCornerShape(26.dp))
                        .clickable { showInvoiceDialog = true }
                        .testTag("action_new_invoice"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "NEW INVOICE",
                        color = InkWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }
        }

        // Bento Row List 2: Pulse Indicator & Analytics Reach (Small elements side-by-side)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Engagement Pulse Box
                GlassCard(
                    modifier = Modifier.weight(1f).height(130.dp).testTag("bento_pulse_card")
                ) {
                    Text(
                        text = "PULSE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(0.4f)
                                .clip(RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
                                .background(AccentGold.copy(alpha = 0.2f))
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(0.6f)
                                .clip(RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
                                .background(AccentGold.copy(alpha = 0.4f))
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(0.8f)
                                .clip(RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
                                .background(AccentGold.copy(alpha = 0.6f))
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(1.0f)
                                .clip(RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
                                .background(AccentGold)
                        )
                    }
                }

                // Reach Indicator Box
                GlassCard(
                    modifier = Modifier.weight(1f).height(130.dp).testTag("bento_reach_card")
                ) {
                    Text(
                        text = "REACH",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "4.2M",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = InkWhite,
                        fontFamily = FontFamily.SansSerif,
                        letterSpacing = (-0.5).sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "↑ 8.2% avg.",
                        color = SuccessGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Bento Element 5: Upcoming Tasks (col-span-2)
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth().testTag("bento_upcoming_tasks_card")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Assignment,
                            contentDescription = "Upcoming Tasks",
                            tint = AccentGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "UPCOMING WORK SPRINT",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = InkWhite,
                            letterSpacing = 1.2.sp
                        )
                    }
                    val upcomingCount = tasks.filter { it.status != TaskStatus.COMPLETED }.size
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(AccentGold.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "$upcomingCount RUNNING",
                            color = AccentGold,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    val upcomingList = tasks.filter { it.status != TaskStatus.COMPLETED }.take(4)
                    if (upcomingList.isEmpty()) {
                        Text(
                            text = "All tasks completed! Sprint is clear. 🛡️",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    } else {
                        upcomingList.forEach { task ->
                            var showDropdown by remember { mutableStateOf(false) }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0x06FFFFFF))
                                    .border(1.dp, Color(0x10FFFFFF), RoundedCornerShape(16.dp))
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(
                                                when(task.priority) {
                                                    TaskPriority.HIGH -> ErrorRed.copy(alpha = 0.15f)
                                                    TaskPriority.MEDIUM -> PendingOrange.copy(alpha = 0.15f)
                                                    TaskPriority.LOW -> SuccessGreen.copy(alpha = 0.15f)
                                                }
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = task.priority.name.take(1),
                                            color = when(task.priority) {
                                                TaskPriority.HIGH -> ErrorRed
                                                TaskPriority.MEDIUM -> PendingOrange
                                                TaskPriority.LOW -> SuccessGreen
                                            },
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = task.title,
                                            color = InkWhite,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "${task.clientName} • Due ${task.dueDate}",
                                            color = TextSecondary,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                // Status Dropdown Select trigger
                                Box {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(DeepGrey)
                                            .border(1.dp, CardBorder, RoundedCornerShape(6.dp))
                                            .clickable { showDropdown = true }
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(
                                                text = task.status.name.replace("_", " "),
                                                color = ElectricBlue,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily.Monospace
                                            )
                                            Icon(Icons.Filled.ArrowDropDown, null, tint = ElectricBlue, modifier = Modifier.size(10.dp))
                                        }
                                    }

                                    DropdownMenu(
                                        expanded = showDropdown,
                                        onDismissRequest = { showDropdown = false },
                                        modifier = Modifier.background(DeepGrey).border(1.dp, CardBorder)
                                    ) {
                                        TaskStatus.values().forEach { st ->
                                            DropdownMenuItem(
                                                text = {
                                                    Text(
                                                        text = st.name.replace("_", " "),
                                                        color = if (task.status == st) ElectricBlue else InkWhite,
                                                        fontSize = 11.sp,
                                                        fontFamily = FontFamily.Monospace
                                                    )
                                                },
                                                onClick = {
                                                    viewModel.updateTaskStatus(task.id, st)
                                                    showDropdown = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal dialogue - Create Invoice
    if (showInvoiceDialog) {
        AlertDialog(
            onDismissRequest = { showInvoiceDialog = false },
            title = { Text("Draft New Client Invoice", color = InkWhite) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = invClient,
                        onValueChange = { invClient = it },
                        label = { Text("Client Name") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                    )
                    OutlinedTextField(
                        value = invAmount,
                        onValueChange = { invAmount = it },
                        label = { Text("Invoice Amount (₹)") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                    )
                    OutlinedTextField(
                        value = invDate,
                        onValueChange = { invDate = it },
                        label = { Text("Due Date (YYYY-MM-DD)") },
                        placeholder = { Text("e.g. 2026-06-10") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val amt = invAmount.toDoubleOrNull() ?: 0.0
                        if (invClient.isNotBlank() && amt > 0) {
                            viewModel.createInvoice(invClient, amt, invDate.ifBlank { "2026-06-15" })
                            showInvoiceDialog = false
                            invClient = ""
                            invAmount = ""
                            invDate = ""
                        }
                    }
                ) {
                    Text("Provision Invoice", color = ElectricBlue)
                }
            },
            dismissButton = {
                TextButton(onClick = { showInvoiceDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = DeepGrey
        )
    }

    // Modal dialogue - Create Task
    if (showTaskDialog) {
        AlertDialog(
            onDismissRequest = { showTaskDialog = false },
            title = { Text("Delegate Live Task", color = InkWhite) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = taskTitle,
                        onValueChange = { taskTitle = it },
                        label = { Text("Task Description") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = taskClient,
                        onValueChange = { taskClient = it },
                        label = { Text("Client Name") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Text("System Urgency Priority:", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        TaskPriority.values().forEach { pri ->
                            val active = taskPriority == pri
                            Button(
                                onClick = { taskPriority = pri },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (active) AccentGold.copy(alpha = 0.2f) else CardBg
                                ),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                modifier = Modifier.height(32.dp).border(1.dp, if (active) AccentGold else CardBorder, RoundedCornerShape(8.dp))
                            ) {
                                Text(pri.name, fontSize = 10.sp, color = if (active) AccentGold else TextSecondary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Task Pipeline Status Dropdown:", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(CardBg)
                                .border(1.dp, CardBorder, RoundedCornerShape(8.dp))
                                .clickable { statusDropdownExpanded = true }
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val label = when(taskInitialStatus) {
                                    TaskStatus.TO_DO -> "Draft (TO DO)"
                                    TaskStatus.IN_PROGRESS -> "Editing (IN PROGRESS)"
                                    TaskStatus.REVIEW -> "Review"
                                    TaskStatus.APPROVED -> "Approved"
                                    TaskStatus.COMPLETED -> "Completed"
                                }
                                Text(text = label, color = InkWhite, fontSize = 13.sp)
                                Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown", tint = ElectricBlue)
                            }
                        }
                        DropdownMenu(
                            expanded = statusDropdownExpanded,
                            onDismissRequest = { statusDropdownExpanded = false },
                            modifier = Modifier.background(DeepGrey).border(1.dp, CardBorder)
                        ) {
                            TaskStatus.values().forEach { st ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = when(st) {
                                                TaskStatus.TO_DO -> "Draft (TO DO)"
                                                TaskStatus.IN_PROGRESS -> "Editing (IN PROGRESS)"
                                                TaskStatus.REVIEW -> "Review"
                                                TaskStatus.APPROVED -> "Approved"
                                                TaskStatus.COMPLETED -> "Completed"
                                            },
                                            color = InkWhite,
                                            fontSize = 12.sp
                                        )
                                    },
                                    onClick = {
                                        taskInitialStatus = st
                                        statusDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (taskTitle.isNotBlank() && taskClient.isNotBlank()) {
                            viewModel.createTask(taskTitle, taskPriority, taskClient, status = taskInitialStatus)
                            showTaskDialog = false
                            taskTitle = ""
                            taskClient = ""
                            taskInitialStatus = TaskStatus.TO_DO
                        }
                    }
                ) {
                    Text("Delegate Job", color = ElectricBlue)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTaskDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = DeepGrey
        )
    }
}
