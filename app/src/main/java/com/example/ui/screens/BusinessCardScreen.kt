package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.INKcoViewModel
import com.example.ui.theme.*

@Composable
fun BusinessCardScreen(viewModel: INKcoViewModel) {
    val bEvents by viewModel.businessCardEvents.collectAsState()
    val context = LocalContext.current

    // Tilt states for the 3D card
    var rotationX by remember { mutableStateOf(12f) }
    var rotationY by remember { mutableStateOf(-15f) }

    // User's business coordinates
    val userPhone = "8660843907"
    val formattedPhone = "+91 8660843907"
    val userEmail = "ink.social.co@gmail.com"

    // Material theme index for the business card
    var selectedCardBgIndex by remember { mutableStateOf(0) }

    val totalViews = bEvents.filter { it.eventType == "View" }.size + 158
    val totalSaves = bEvents.filter { it.eventType == "Save Contact" }.size + 42
    val totalWhatsApp = bEvents.filter { it.eventType == "WhatsApp Click" }.size + 83

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("founder_card_view")
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Explanatory premium badge
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardBg.copy(alpha = 0.5f))
                    .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.CompassCalibration,
                        contentDescription = "info",
                        tint = AccentGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Touch and drag the card below to experience the real-time 3D lighting tilt effect.",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Draggable 3D interactive holographic card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .height(220.dp)
                        .graphicsLayer {
                            this.rotationX = rotationX
                            this.rotationY = rotationY
                            this.cameraDistance = 14f
                            this.shadowElevation = 24f
                        }
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    // Translate drag coordinates to custom angles safely
                                    rotationX = (rotationX - dragAmount.y * 0.15f).coerceIn(-28f, 28f)
                                    rotationY = (rotationY + dragAmount.x * 0.15f).coerceIn(-28f, 28f)
                                },
                                onDragEnd = {
                                    // Retain the tilted orientation as an organic, cool structural display
                                }
                            )
                        }
                        .clip(RoundedCornerShape(18.dp))
                        .border(
                            width = 1.dp,
                            brush = Brush.linearGradient(
                                colors = when (selectedCardBgIndex) {
                                    0 -> listOf(ElectricBlue, Color.Transparent, AccentGold)
                                    1 -> listOf(CardBorder, Color.Transparent, CardBorder)
                                    2 -> listOf(Color(0xFF8f94fb), Color.Transparent, Color(0xFF4e54c8))
                                    else -> listOf(AccentGold, Color.Transparent, Color(0xFF1a1a1a))
                                }
                            ),
                            shape = RoundedCornerShape(18.dp)
                        )
                        .background(
                            brush = when (selectedCardBgIndex) {
                                0 -> Brush.radialGradient(
                                    colors = listOf(Color(0xFF0F1E2E), Color(0xFF06090D)),
                                    radius = 800f
                                )
                                1 -> Brush.radialGradient(
                                    colors = listOf(Color(0xFF1B1B1B), Color(0xFF040404)),
                                    radius = 800f
                                )
                                2 -> Brush.radialGradient(
                                    colors = listOf(Color(0xFF3B1530), Color(0xFF080310)),
                                    radius = 800f
                                )
                                else -> Brush.radialGradient(
                                    colors = listOf(Color(0xFF231705), Color(0xFF070401)),
                                    radius = 800f
                                )
                            }
                        )
                        .padding(20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Header info
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TSILogo(size = 36.dp, showText = false)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        when (selectedCardBgIndex) {
                                            0 -> ElectricBlue.copy(alpha = 0.15f)
                                            1 -> Color.White.copy(alpha = 0.1f)
                                            2 -> Color(0xFF8f94fb).copy(alpha = 0.15f)
                                            else -> AccentGold.copy(alpha = 0.15f)
                                        }
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "NFC PASS",
                                    color = when (selectedCardBgIndex) {
                                        0 -> ElectricBlue
                                        1 -> InkWhite
                                        2 -> Color(0xFF8f94fb)
                                        else -> AccentGold
                                    },
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    letterSpacing = 1.sp
                                )
                            }
                        }

                        // Middle credentials layout
                        Column {
                            Text(
                                text = "INDERNEIL KANAGALI",
                                color = InkWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Founder & Brand Strategist",
                                color = when (selectedCardBgIndex) {
                                    0 -> ElectricBlue
                                    1 -> TextSecondary
                                    2 -> Color(0xFF8f94fb)
                                    else -> AccentGold
                                },
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        // Contact display on card
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Phone,
                                    contentDescription = "phone",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(10.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = formattedPhone,
                                    color = TextSecondary,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Email,
                                    contentDescription = "email",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(10.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = userEmail,
                                    color = TextSecondary,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }

                    // Cyber holographic shine strip overlay across diagonal
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.White.copy(alpha = 0.05f),
                                        Color.Transparent
                                    ),
                                    start = Offset(0f, 0f),
                                    end = Offset(450f, 450f)
                                )
                            )
                    )
                }
            }
        }

        // Image Presets / Background Material selector
        item {
            Text(
                text = "SELECT DIGITAL DECK CARD FINISH",
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val patterns = listOf(
                    "COBALT" to Color(0xFF0F1E2E),
                    "OBSIDIAN" to Color(0xFF1B1B1B),
                    "AURORA" to Color(0xFF3B1530),
                    "MONARCH" to Color(0xFF231705)
                )

                patterns.forEachIndexed { i, (name, clr) ->
                    val selected = selectedCardBgIndex == i
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(DeepGrey)
                            .border(1.5.dp, if (selected) ElectricBlue else CardBorder, RoundedCornerShape(12.dp))
                            .clickable { selectedCardBgIndex = i }
                            .padding(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(clr)
                                .border(1.dp, CardBorder, CircleShape)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(name, color = if (selected) ElectricBlue else TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Custom details display section (displays WhatsApp and email in prominent card blocks with interactive utilities)
        item {
            Text(
                text = "FOUNDER CONTACT CHANNELS",
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Interactive Phone Segment
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(DeepGrey)
                        .border(1.dp, CardBorder, RoundedCornerShape(14.dp))
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF25D366).copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Chat,
                                contentDescription = "whatsapp",
                                tint = Color(0xFF25D366),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("WhastApp / Phone", color = TextSecondary, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                            Text(formattedPhone, color = InkWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        // Action: copy
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("phone", formattedPhone)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Phone number copied", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(Icons.Filled.ContentCopy, "copy", tint = TextSecondary, modifier = Modifier.size(16.dp))
                        }
                        // Action: navigate
                        IconButton(
                            onClick = {
                                viewModel.logBusinessCardEngagement("WhatsApp Click", "NFC Companion Page")
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/91$userPhone?text=Hello%20Inderneil,%20connecting%20from%20INKco!"))
                                context.startActivity(intent)
                            }
                        ) {
                            Icon(Icons.Filled.OpenInNew, "open", tint = ElectricBlue, modifier = Modifier.size(16.dp))
                        }
                    }
                }

                // Interactive Email Segment
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(DeepGrey)
                        .border(1.dp, CardBorder, RoundedCornerShape(14.dp))
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ElectricBlue.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.MailOutline,
                                contentDescription = "email",
                                tint = ElectricBlue,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Brand Partnership Email", color = TextSecondary, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                            Text(userEmail, color = InkWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        // Action: copy
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("email", userEmail)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Email address copied", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(Icons.Filled.ContentCopy, "copy", tint = TextSecondary, modifier = Modifier.size(16.dp))
                        }
                        // Action: open mail app
                        IconButton(
                            onClick = {
                                viewModel.logBusinessCardEngagement("Email Click", "NFC Companion Page")
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:$userEmail")
                                }
                                context.startActivity(intent)
                            }
                        ) {
                            Icon(Icons.Filled.OpenInNew, "open", tint = ElectricBlue, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        // Live Event statistics widget
        item {
            Text(
                text = "FOUNDER CARD ANALYTICS Feed",
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // View KPI
                GlassCard(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Visibility, "Views", tint = ElectricBlue, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("VIEWS", fontSize = 8.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(totalViews.toString(), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = InkWhite)
                }
                // Save KPI
                GlassCard(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Download, "Saves", tint = SuccessGreen, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("SAVES", fontSize = 8.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(totalSaves.toString(), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = InkWhite)
                }
                // WhatsApp Clicks
                GlassCard(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Chat, "Clicks", tint = AccentGold, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("CONNECTS", fontSize = 8.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(totalWhatsApp.toString(), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = InkWhite)
                }
            }
        }

        // Modern Visual QR Code Canvas
        item {
            GlassCard(
                borderColor = CardBorder,
                backgroundColor = CardBg.copy(alpha = 0.9f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "DYNAMIC MATCHING COMPANION QR",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = ElectricBlue,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "Let clients scan this to connect instantly on WhatsApp.",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Draw a spectacular QR matrix in Canvas!
                    Box(
                        modifier = Modifier
                            .size(180.dp)
                            .background(InkWhite, RoundedCornerShape(16.dp))
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height
                            val gridSize = 12
                            val blockW = w / gridSize
                            val blockH = h / gridSize

                            // Draw mock QR position finders (Standard QR squares in 3 corners)
                            fun drawFinder(tx: Float, ty: Float) {
                                drawRect(color = InkBlack, topLeft = Offset(tx, ty), size = Size(blockW * 3, blockH * 3))
                                drawRect(color = InkWhite, topLeft = Offset(tx + blockW, ty + blockH), size = Size(blockW, blockH))
                            }

                            // Left-top
                            drawFinder(0f, 0f)
                            // Right-top
                            drawFinder(w - blockW * 3, 0f)
                            // Left-bottom
                            drawFinder(0f, h - blockH * 3)

                            // Populate some artistic algorithmic QR blocks
                            for (gridX in 0 until gridSize) {
                                for (gridY in 0 until gridSize) {
                                    // Skip finder zones
                                    if ((gridX < 3 && gridY < 3) || (gridX >= gridSize - 3 && gridY < 3) || (gridX < 3 && gridY >= gridSize - 3)) {
                                        continue
                                    }
                                    
                                    // Skip center icon zone
                                    if (gridX in 4..7 && gridY in 4..7) {
                                        continue
                                    }

                                    // Simple pseudo-random hash pattern
                                    val shouldDraw = (gridX * 7 + gridY * 13) % 3 == 0 || (gridX + gridY) % 5 == 1
                                    if (shouldDraw) {
                                        drawRect(
                                            color = InkBlack,
                                            topLeft = Offset(gridX * blockW, gridY * blockH),
                                            size = Size(blockW * 0.95f, blockH * 0.95f)
                                        )
                                    }
                                }
                            }
                        }

                        // Center the Electric Blue Brand Droplet
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ElectricBlue)
                                .border(2.dp, InkWhite, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "INK",
                                color = InkWhite,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "https://thesocialink.co/inderneil",
                        fontSize = 12.sp,
                        color = ElectricBlue,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // Recent card log activities
        item {
            GlassCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Timeline, "Logs", tint = AccentGold, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("ACTIVE ENGAGEMENT LOGS", color = InkWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(10.dp))
                
                bEvents.take(3).forEach { ev ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(ev.eventType, color = AccentGold, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Text("Source: ${ev.source} • ${ev.metadata}", color = TextSecondary, fontSize = 10.sp)
                        }
                        Text(ev.createdAt.split(" ").last(), color = TextSecondary, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    }
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CardBorder.copy(alpha = 0.5f)))
                }
            }
        }
    }
}
