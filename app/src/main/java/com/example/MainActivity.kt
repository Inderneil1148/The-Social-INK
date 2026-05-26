package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.INKcoViewModel
import com.example.ui.Screen
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: INKcoViewModel = viewModel()
                AppShell(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AppShell(viewModel: INKcoViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val searchPrompt by viewModel.searchQuery.collectAsState()
    val notificationsAll by viewModel.notifications.collectAsState()

    var showNotificationTray by remember { mutableStateOf(false) }

    // If logged out, render AuthGate view
    if (currentUser == null) {
        if (currentScreen == Screen.SIGNUP) {
            AuthScreen(viewModel = viewModel, isLogin = false)
        } else {
            AuthScreen(viewModel = viewModel, isLogin = true)
        }
        return
    }

    // Measure dimensions class width
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = InkBlack
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            
            // Left Sidebar - Desktop Navigation Rail/Sidebar for wide screens (Tablets/Landscape)
            if (isTablet) {
                SidebarNavigation(
                    currentScreen = currentScreen,
                    onNavigate = { viewModel.navigateTo(it) },
                    onLogout = { viewModel.logout() },
                    currentUser = currentUser?.name ?: "Inderneil"
                )
                
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                        .background(CardBorder)
                )
            }

            // Central content stack
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                // Top Custom Command Header Bar
                TopSearchBar(
                    query = searchPrompt,
                    onQueryChange = { viewModel.setSearchQuery(it) },
                    onNotificationClick = { showNotificationTray = !showNotificationTray },
                    notificationCount = notificationsAll.size,
                    currentScreenName = when(currentScreen) {
                        Screen.DASHBOARD -> "CONSOLE CONTEXT"
                        Screen.COMPANY -> "THE SOCIAL INK PROFILE"
                        Screen.BUSINESS_CARD -> "INDERNEIL CARD INTERACTIVE"
                        Screen.REVENUE -> "INVOICE TRANSACTIONS"
                        Screen.CONTENT -> "CREATIVE SCHEDULER"
                        Screen.TASKS -> "KANBAN FLOW SPRINT"
                        Screen.ANALYTICS -> "SOCIAL AUDIENCE RADAR"
                        Screen.SETTINGS -> "SYSTEM PRIVILEGES"
                        else -> "WORKSPACE DECK"
                    }
                )

                // Render match results when search query is typed! (Local lookup engine)
                if (searchPrompt.isNotBlank()) {
                    SearchAutoCompleteResults(query = searchPrompt, onTriggerRoute = {
                        viewModel.navigateTo(it)
                        viewModel.setSearchQuery("")
                    })
                } else {
                    // Active content container with dynamic state fades
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        AnimatedContent(
                            targetState = currentScreen,
                            transitionSpec = {
                                (fadeIn(animationSpec = tween(220)) + scaleIn(initialScale = 0.96f, animationSpec = tween(220)))
                                    .togetherWith(fadeOut(animationSpec = tween(140)))
                            },
                            label = "screen_stepper"
                        ) { screen ->
                            when (screen) {
                                Screen.DASHBOARD -> DashboardScreen(viewModel = viewModel)
                                Screen.COMPANY -> CompanyScreen(viewModel = viewModel)
                                Screen.BUSINESS_CARD -> BusinessCardScreen(viewModel = viewModel)
                                Screen.REVENUE -> RevenueScreen(viewModel = viewModel)
                                Screen.CONTENT -> ContentScreen(viewModel = viewModel)
                                Screen.TASKS -> TasksScreen(viewModel = viewModel)
                                Screen.ANALYTICS -> AnalyticsScreen(viewModel = viewModel)
                                Screen.SETTINGS -> SettingsScreen(viewModel = viewModel)
                                else -> DashboardScreen(viewModel = viewModel)
                            }
                        }

                        // Floating dismissable notification tray overview
                        if (showNotificationTray) {
                            NotificationSlideTray(
                                list = notificationsAll,
                                onDismissIndex = { viewModel.dismissNotification(it) },
                                onClose = { showNotificationTray = false },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(16.dp)
                            )
                        }
                    }
                }

                // Sticky Bottom navigation for Mobile Compact devices
                if (!isTablet) {
                    BottomInteractiveNavigation(
                        currentScreen = currentScreen,
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                }
            }
        }
    }
}

// Left Sidebar Layout Component
@Composable
fun SidebarNavigation(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    onLogout: () -> Unit,
    currentUser: String
) {
    Column(
        modifier = Modifier
            .width(260.dp)
            .fillMaxHeight()
            .background(DeepGrey)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            // Header
            TSILogo(size = 38.dp, showText = true)
            
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                "OPERATIONS CHANNELS",
                fontSize = 10.sp,
                color = TextSecondary,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(start = 12.dp, bottom = 10.dp)
            )

            // Dynamic Nav list
            val navItems = listOf(
                Triple(Screen.DASHBOARD, "Dashboard Console", Icons.Filled.Dashboard),
                Triple(Screen.COMPANY, "Company Profile", Icons.Filled.Business),
                Triple(Screen.BUSINESS_CARD, "Digital Biz Card", Icons.Filled.ContactPhone),
                Triple(Screen.REVENUE, "Revenue ledger", Icons.Filled.AccountBalanceWallet),
                Triple(Screen.CONTENT, "Creative Calendar", Icons.Filled.CalendarMonth),
                Triple(Screen.TASKS, "Kanban Workspace", Icons.Filled.TaskAlt),
                Triple(Screen.ANALYTICS, "Audience Radar", Icons.Filled.Analytics),
                Triple(Screen.SETTINGS, "System Privileges", Icons.Filled.Settings)
            )

            navItems.forEach { (screen, title, icon) ->
                val active = currentScreen == screen
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (active) ElectricBlue.copy(alpha = 0.15f) else Color.Transparent)
                        .clickable { onNavigate(screen) }
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = if (active) ElectricBlue else TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = title,
                        color = if (active) InkWhite else TextSecondary,
                        fontSize = 13.sp,
                        fontWeight = if (active) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        // Sidebar bottom profile
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CardBorder.copy(alpha = 0.3f))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(ElectricBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text(currentUser.take(1).uppercase(), color = InkWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(currentUser, color = InkWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("INKco Admin", color = TextSecondary, fontSize = 10.sp)
                }
            }

            Icon(
                imageVector = Icons.Filled.ExitToApp,
                contentDescription = "Log Out",
                tint = ErrorRed,
                modifier = Modifier
                    .size(18.dp)
                    .clickable { onLogout() }
            )
        }
    }
}

// Top Command / Search Bar Layer Component
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TopSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onNotificationClick: () -> Unit,
    notificationCount: Int,
    currentScreenName: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(
                text = currentScreenName,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = ElectricBlue,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(2.dp))

            // Text input acts as dynamic Command Input bar
            TextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Search fintech, luxe, tasks, bio...", color = TextSecondary, fontSize = 12.sp) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = DeepGrey,
                    unfocusedContainerColor = DeepGrey,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                leadingIcon = { Icon(Icons.Filled.Search, "Look", tint = TextSecondary, modifier = Modifier.size(18.dp)) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("command_search_box")
                    .height(44.dp)
            )
        }

        // Notification Icon with badge
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(DeepGrey)
                .clickable { onNotificationClick() }
                .testTag("notification_anchor"),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Notifications, "Inbound notifications", tint = InkWhite, modifier = Modifier.size(20.dp))
            if (notificationCount > 0) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(ErrorRed)
                        .align(Alignment.TopEnd)
                        .offset(x = (-8).dp, y = 8.dp)
                )
            }
        }
    }
}

// Inbound Slide Notification Tray Component
@Composable
fun NotificationSlideTray(
    list: List<String>,
    onDismissIndex: (Int) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(280.dp)
            .shadow(12.dp, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = DeepGrey),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("WORKSPACE NOTIFICATIONS", fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = AccentGold)
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    tint = TextSecondary,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { onClose() }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            
            if (list.isEmpty()) {
                Text("All systems nominal. No outstanding alerts.", color = TextSecondary, fontSize = 11.sp, modifier = Modifier.padding(vertical = 12.dp))
            } else {
                list.forEachIndexed { i, msg ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(modifier = Modifier.weight(1.1f)) {
                            Box(modifier = Modifier.size(6.dp).offset(y = 6.dp).clip(CircleShape).background(ElectricBlue))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(msg, color = TextPrimary, fontSize = 12.sp)
                        }
                        Icon(
                            Icons.Filled.Delete,
                            "Dismiss",
                            tint = ErrorRed.copy(alpha = 0.6f),
                            modifier = Modifier
                                .size(14.dp)
                                .clickable { onDismissIndex(i) }
                        )
                    }
                    Divider(color = CardBorder.copy(alpha = 0.5f))
                }
            }
        }
    }
}

// Local command autocomplete indexing search display
@Composable
fun SearchAutoCompleteResults(
    query: String,
    onTriggerRoute: (Screen) -> Unit
) {
    val results = remember(query) {
        listOf(
            Triple("Digital Business Card profile /inderneil", Screen.BUSINESS_CARD, listOf("inderneil", "card", "nfc", "whatsapp", "qr")),
            Triple("Financial Ledger, bills & dues breakdown", Screen.REVENUE, listOf("invoice", "payment", "revenue", "dollar", "expense", "fee", "payout")),
            Triple("Content Reels / Carousel schedulers", Screen.CONTENT, listOf("reel", "carousel", "caption", "post", "hashtag", "calendar")),
            Triple("Productivity pipelines & sprint Kanban", Screen.TASKS, listOf("task", "todo", "sprint", "kanban", "work", "job", "dev")),
            Triple("Social Engagement metrics calculators", Screen.ANALYTICS, listOf("analytics", "followers", "engagement", "reach", "likes", "calculator")),
            Triple("Company Showcase pitch & timeline", Screen.COMPANY, listOf("company", "the social ink", "about", "founder", "timeline", "pitch"))
        ).filter { (_, _, tags) ->
            tags.any { it.contains(query.lowercase()) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "MATCHED COMMAND SEARCH CHANNELS",
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = AccentGold,
            letterSpacing = 1.2.sp
        )

        if (results.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No matching operational routes or resources.", color = TextSecondary, fontSize = 13.sp)
            }
        } else {
            results.forEach { (title, screen, _) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(DeepGrey)
                        .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                        .clickable { onTriggerRoute(screen) }
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Shortcut, "Go", tint = ElectricBlue, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(title, color = InkWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Icon(Icons.Filled.ArrowForwardIos, "Deeper", tint = TextSecondary, modifier = Modifier.size(12.dp))
                }
            }
        }
    }
}

// Mobile Bottom Sticky Bar
@Composable
fun BottomInteractiveNavigation(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit
) {
    val items = listOf(
        Triple(Screen.DASHBOARD, "Board", Icons.Filled.Dashboard),
        Triple(Screen.COMPANY, "Agency", Icons.Filled.Business),
        Triple(Screen.BUSINESS_CARD, "Card", Icons.Filled.ContactPhone),
        Triple(Screen.REVENUE, "Pool", Icons.Filled.AccountBalanceWallet),
        Triple(Screen.CONTENT, "Social", Icons.Filled.CalendarMonth),
        Triple(Screen.TASKS, "Kanban", Icons.Filled.TaskAlt),
        Triple(Screen.ANALYTICS, "Radar", Icons.Filled.Analytics),
        Triple(Screen.SETTINGS, "Admin", Icons.Filled.Settings)
    )

    NavigationBar(
        containerColor = DeepGrey,
        tonalElevation = 8.dp,
        modifier = Modifier
            .navigationBarsPadding()
            .border(width = 1.dp, color = CardBorder, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
    ) {
        items.forEach { (screen, title, icon) ->
            val active = currentScreen == screen
            NavigationBarItem(
                selected = active,
                onClick = { onNavigate(screen) },
                icon = { Icon(icon, title, modifier = Modifier.size(18.dp)) },
                label = { Text(title, fontSize = 9.sp, fontWeight = FontWeight.Bold, maxLines = 1) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ElectricBlue,
                    selectedTextColor = ElectricBlue,
                    indicatorColor = ElectricBlue.copy(alpha = 0.15f),
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary
                )
            )
        }
    }
}
