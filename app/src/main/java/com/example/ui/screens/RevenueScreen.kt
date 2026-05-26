package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.ui.theme.*

@Composable
fun RevenueScreen(viewModel: INKcoViewModel) {
    val invoices by viewModel.invoices.collectAsState()
    val expenses by viewModel.expenses.collectAsState()

    // Calculate interactive sums
    val totalPaid = invoices.filter { it.status == "Paid" }.sumOf { it.amount }
    val totalPending = invoices.filter { it.status == "Pending" }.sumOf { it.amount }
    val totalOverdue = invoices.filter { it.status == "Overdue" }.sumOf { it.amount }
    val totalExpenses = expenses.sumOf { it.amount }
    val netProfit = totalPaid - totalExpenses

    var filterState by remember { mutableStateOf("All") } // "All", "Paid", "Pending", "Overdue"

    val displayedInvoices = remember(invoices, filterState) {
        if (filterState == "All") invoices
        else invoices.filter { it.status.equals(filterState, ignoreCase = true) }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("revenue_view")
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Quick Summary KPIs
        item {
            GlassCard(
                borderColor = ElectricBlue.copy(alpha = 0.3f),
                backgroundColor = CardBg.copy(alpha = 0.75f)
            ) {
                Text(
                    text = "AGENCY REVENUE OPERATION SUMMARY",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = ElectricBlue,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(14.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("NET REALIZED PROFIT (Q2)", fontSize = 11.sp, color = TextSecondary)
                        Text("₹${String.format("%,.2f", netProfit)}", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SuccessGreen.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("Ledger Safe", color = SuccessGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("PAID BILLINGS", fontSize = 10.sp, color = TextSecondary)
                        Text("₹${String.format("%,.0f", totalPaid)}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = InkWhite)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("PENDING INVOICES", fontSize = 10.sp, color = TextSecondary)
                        Text("₹${String.format("%,.0f", totalPending)}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PendingOrange)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("TOTAL OPERATIONALS", fontSize = 10.sp, color = TextSecondary)
                        Text("₹${String.format("%,.0f", totalExpenses)}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ErrorRed)
                    }
                }
            }
        }

        // Expense Category Visualizer
        item {
            GlassCard {
                Text(
                    text = "OPERATIONAL EXPENSE INDEX BY SECTOR",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                
                // Group expenses for visual chart
                val categorized = expenses.groupBy { it.category }.mapValues { entry -> entry.value.sumOf { it.amount } }
                val categories = categorized.keys.toList()
                val sums = categorized.values.toList()

                PremiumBarChart(
                    values = sums,
                    labels = categories.map { it.split(" ").first() },
                    barColor = ErrorRed
                )
            }
        }

        // Filter Controls
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("FILTER BY STATUS:", fontSize = 10.sp, color = TextSecondary, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                Spacer(modifier = Modifier.width(4.dp))
                listOf("All", "Paid", "Pending", "Overdue").forEach { status ->
                    val active = filterState == status
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (active) ElectricBlue.copy(alpha = 0.2f) else DeepGrey)
                            .border(1.dp, if (active) ElectricBlue else CardBorder, RoundedCornerShape(8.dp))
                            .clickable { filterState = status }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(status, color = if (active) ElectricBlue else TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Ledger of Invoices
        item {
            Text(
                text = "INVOICES TRANSACTION LOGS",
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 1.sp
            )
        }

        if (displayedInvoices.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No invoices matched current filters.", color = TextSecondary, fontSize = 13.sp)
                }
            }
        }

        items(displayedInvoices) { inv ->
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
                    Text(
                        text = inv.clientName,
                        color = InkWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Due By: ${inv.dueDate} • ${inv.id.substringBefore("-")}",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    
                    // Live trigger option to settle dues instantly!
                    if (inv.status != "Paid") {
                        Button(
                            onClick = { viewModel.payInvoice(inv.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue.copy(alpha = 0.15f)),
                            modifier = Modifier.height(28.dp).testTag("pay_invoice_btn_${inv.id}"),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Icon(Icons.Filled.Check, "Mark Settle", tint = ElectricBlue, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Mark Settle", color = ElectricBlue, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Text(
                            text = "Paid on: ${inv.paidAt ?: "Today"}",
                            color = SuccessGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₹${String.format("%,.2f", inv.amount)}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (inv.status == "Paid") SuccessGreen else (if (inv.status == "Pending") PendingOrange else ErrorRed)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                when (inv.status) {
                                    "Paid" -> SuccessGreen.copy(alpha = 0.12f)
                                    "Pending" -> PendingOrange.copy(alpha = 0.12f)
                                    else -> ErrorRed.copy(alpha = 0.12f)
                                }
                            )
                            .border(
                                1.dp,
                                when (inv.status) {
                                    "Paid" -> SuccessGreen.copy(alpha = 0.3f)
                                    "Pending" -> PendingOrange.copy(alpha = 0.3f)
                                    else -> ErrorRed.copy(alpha = 0.3f)
                                },
                                RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = inv.status.uppercase(),
                            color = when (inv.status) {
                                "Paid" -> SuccessGreen
                                "Pending" -> PendingOrange
                                else -> ErrorRed
                            },
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}
