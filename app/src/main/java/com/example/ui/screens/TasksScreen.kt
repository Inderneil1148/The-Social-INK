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
import com.example.data.Task
import com.example.data.TaskStatus
import com.example.data.TaskPriority
import com.example.ui.theme.*

@Composable
fun TasksScreen(viewModel: INKcoViewModel) {
    val tasks by viewModel.tasks.collectAsState()
    var selectedColumnFilter by remember { mutableStateOf<TaskStatus?>(null) } // null means show all structured by categories
    
    // Dialog / adding state controllers
    var showAddTaskDialog by remember { mutableStateOf(false) }
    var taskTitle by remember { mutableStateOf("") }
    var taskClient by remember { mutableStateOf("") }
    var taskPriority by remember { mutableStateOf(TaskPriority.MEDIUM) }
    var taskInitialStatus by remember { mutableStateOf(TaskStatus.TO_DO) }
    var statusDropdownExpanded by remember { mutableStateOf(false) }

    // Quick statistics
    val totalCount = tasks.size
    val doneCount = tasks.filter { it.status == TaskStatus.COMPLETED }.size
    val riskCount = tasks.filter { it.priority == TaskPriority.HIGH && it.status != TaskStatus.COMPLETED }.size

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("tasks_view")
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Task Operations Metrics
            item {
                GlassCard(
                    borderColor = AccentGold.copy(alpha = 0.3f),
                    backgroundColor = CardBg.copy(alpha = 0.75f)
                ) {
                    Text(
                        text = "AGENCY VELOCITY & WORKFLOW",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = AccentGold,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Sprint Target Delivery", fontSize = 11.sp, color = TextSecondary)
                            Text("$doneCount / $totalCount Jobs Done", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = InkWhite)
                        }
                        
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (riskCount > 0) ErrorRed.copy(alpha = 0.15f) else SuccessGreen.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (riskCount > 0) "$riskCount HIGH RISK" else "SPEED OPTIMAL",
                                color = if (riskCount > 0) ErrorRed else SuccessGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Active linear progress bar
                    val ratio = if (totalCount > 0) doneCount.toFloat() / totalCount else 0.5f
                    LinearProgressIndicator(
                        progress = { ratio },
                        modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(4.dp)),
                        color = ElectricBlue,
                        trackColor = CardBorder
                    )
                }
            }

            // Kanban Horizontal Columns Switcher
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // ALL trigger
                    val isAll = selectedColumnFilter == null
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isAll) ElectricBlue.copy(alpha = 0.2f) else DeepGrey)
                            .border(1.dp, if (isAll) ElectricBlue else CardBorder, RoundedCornerShape(8.dp))
                            .clickable { selectedColumnFilter = null }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("ALL PIPELINES", color = if (isAll) ElectricBlue else TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }

                    TaskStatus.values().forEach { status ->
                        val active = selectedColumnFilter == status
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (active) ElectricBlue.copy(alpha = 0.2f) else DeepGrey)
                                .border(1.dp, if (active) ElectricBlue else CardBorder, RoundedCornerShape(8.dp))
                                .clickable { selectedColumnFilter = status }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(status.name.replace("_", " "), color = if (active) ElectricBlue else TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Standard task render blocks
            val filteredList = if (selectedColumnFilter == null) tasks else tasks.filter { it.status == selectedColumnFilter }

            if (filteredList.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No task assignments in this status slot.", color = TextSecondary, fontSize = 13.sp)
                    }
                }
            }

            items(filteredList) { task ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(DeepGrey)
                        .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1.1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Priority Tag
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        when (task.priority) {
                                            TaskPriority.HIGH -> ErrorRed.copy(alpha = 0.15f)
                                            TaskPriority.MEDIUM -> PendingOrange.copy(alpha = 0.15f)
                                            TaskPriority.LOW -> SuccessGreen.copy(alpha = 0.15f)
                                        }
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = task.priority.name,
                                    color = when (task.priority) {
                                        TaskPriority.HIGH -> ErrorRed
                                        TaskPriority.MEDIUM -> PendingOrange
                                        TaskPriority.LOW -> SuccessGreen
                                    },
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = task.clientName,
                                color = TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = task.title,
                            color = InkWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Owner Initials Avatar
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .clip(CircleShape)
                                    .background(ElectricBlue.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(task.ownerName.take(1), fontSize = 8.sp, color = ElectricBlue, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Assigned: ${task.ownerName} • Due ${task.dueDate}",
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }

                    // Dynamic Status Select Dropdown Menu
                    var itemStatusDropdownExpanded by remember { mutableStateOf(false) }

                    Column(horizontalAlignment = Alignment.End) {
                        Box {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(DeepGrey)
                                    .border(1.5.dp, if (task.status == TaskStatus.COMPLETED) SuccessGreen else ElectricBlue, RoundedCornerShape(8.dp))
                                    .clickable { itemStatusDropdownExpanded = true }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    val statusLabel = when(task.status) {
                                        TaskStatus.TO_DO -> "Draft (TO DO)"
                                        TaskStatus.IN_PROGRESS -> "Editing (IN PROGRESS)"
                                        TaskStatus.REVIEW -> "Review"
                                        TaskStatus.APPROVED -> "Approved"
                                        TaskStatus.COMPLETED -> "Completed"
                                    }
                                    Text(
                                        text = statusLabel,
                                        color = if (task.status == TaskStatus.COMPLETED) SuccessGreen else ElectricBlue,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    Icon(
                                        imageVector = Icons.Filled.ArrowDropDown,
                                        contentDescription = "Select status",
                                        tint = if (task.status == TaskStatus.COMPLETED) SuccessGreen else ElectricBlue,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }

                            DropdownMenu(
                                expanded = itemStatusDropdownExpanded,
                                onDismissRequest = { itemStatusDropdownExpanded = false },
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
                                                color = if (task.status == st) ElectricBlue else InkWhite,
                                                fontSize = 12.sp
                                            )
                                        },
                                        onClick = {
                                            viewModel.updateTaskStatus(task.id, st)
                                            itemStatusDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Priority: ${task.priority.name}",
                            fontSize = 9.sp,
                            color = TextSecondary,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // Floating Action Button to Add Task beautifully
        FloatingActionButton(
            onClick = { showAddTaskDialog = true },
            containerColor = ElectricBlue,
            contentColor = InkBlack,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 24.dp, end = 16.dp)
                .testTag("fab_add_task")
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add task assignment")
        }
    }

    // Modal Add Task dialogue local to Sprint board
    if (showAddTaskDialog) {
        AlertDialog(
            onDismissRequest = { showAddTaskDialog = false },
            title = { Text("Appoint New Sprint Task", color = InkWhite) },
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
                    
                    Text("Select Urgency Priority:", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
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
                    Text("Select Initial Pipeline Status:", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
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
                            showAddTaskDialog = false
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
                TextButton(onClick = { showAddTaskDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = DeepGrey
        )
    }
}
