package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.INKcoViewModel
import com.example.ui.Screen
import com.example.data.UserRole
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    viewModel: INKcoViewModel,
    isLogin: Boolean = true
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(UserRole.ADMIN) }
    var showError by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(InkBlack),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Elegant TSI Brand Header
            TSILogo(size = 64.dp, showText = true)
            
            Spacer(modifier = Modifier.height(30.dp))

            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = CardBorder
            ) {
                Text(
                    text = if (isLogin) "Console Sign In" else "Create System Account",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = InkWhite,
                    modifier = Modifier.align(Alignment.Start)
                )
                Text(
                    text = if (isLogin) "Access the agency operating system console." else "Provision new agency workspace workspace credentials.",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 4.dp, bottom = 16.dp)
                )

                if (!isLogin) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name", color = TextSecondary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = CardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("name_input")
                            .padding(bottom = 8.dp)
                    )
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Agency Email address", color = TextSecondary) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricBlue,
                        unfocusedBorderColor = CardBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("email_input")
                        .padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Access Keyphrase", color = TextSecondary) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricBlue,
                        unfocusedBorderColor = CardBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("password_input")
                        .padding(bottom = 12.dp)
                )

                if (!isLogin) {
                    Text(
                        text = "System Privilege Role:",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(bottom = 6.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        UserRole.values().forEach { role ->
                            val active = selectedRole == role
                            Button(
                                onClick = { selectedRole = role },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (active) ElectricBlue.copy(alpha = 0.2f) else Color.Transparent,
                                    contentColor = if (active) ElectricBlue else TextSecondary
                                ),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                modifier = Modifier
                                    .border(
                                        width = 1.dp,
                                        color = if (active) ElectricBlue else CardBorder,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .height(32.dp)
                            ) {
                                Text(role.name, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (showError) {
                    Text(
                        text = "Invalid credentials. Fill out the fields to proceed.",
                        color = ErrorRed,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                GlassButton(
                    text = if (isLogin) "Authenticate" else "Provision Credentials",
                    onClick = {
                        if (isLogin && email.isBlank()) {
                            // Demo access bypass
                            viewModel.login("inderneilkanagali@gmail.com", "Inderneil Kanagali", UserRole.ADMIN)
                        } else if (!isLogin && (name.isBlank() || email.isBlank())) {
                            showError = true
                        } else {
                            viewModel.login(email, if (isLogin) "Staff Guest" else name, selectedRole)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    tag = "submit_auth"
                )

                Spacer(modifier = Modifier.height(10.dp))

                GlassButton(
                    text = if (isLogin) "Bypass Console Dev Auth" else "Already have an account",
                    onClick = {
                        if (isLogin) {
                            // Instant dev bypass with Admin Access
                            viewModel.login("inderneilkanagali@gmail.com", "Inderneil Kanagali", UserRole.ADMIN)
                        } else {
                            viewModel.navigateTo(Screen.LOGIN)
                        }
                    },
                    isPrimary = false,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = {
                    if (isLogin) {
                        viewModel.navigateTo(Screen.SIGNUP)
                    } else {
                        viewModel.navigateTo(Screen.LOGIN)
                    }
                }
            ) {
                Text(
                    text = if (isLogin) "Request operational client workspace access" else "Console Sign In",
                    color = ElectricBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
