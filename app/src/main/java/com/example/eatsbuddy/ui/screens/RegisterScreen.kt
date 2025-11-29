package com.example.eatsbuddy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eatsbuddy.ui.theme.DividerColor
import com.example.eatsbuddy.ui.theme.EatsBuddyTheme
import com.example.eatsbuddy.ui.theme.GreenDark
import com.example.eatsbuddy.ui.theme.GreenLight
import com.example.eatsbuddy.ui.theme.GreenPrimary
import com.example.eatsbuddy.ui.theme.GreenSurface
import com.example.eatsbuddy.ui.theme.InputBackground
import com.example.eatsbuddy.ui.theme.TextPrimary
import com.example.eatsbuddy.ui.theme.TextSecondary
import com.example.eatsbuddy.ui.theme.White

@Composable
fun RegisterScreen(
    onRegisterClick: (email: String, password: String, confirmPassword: String) -> Unit = { _, _, _ -> },
    onLoginClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(GreenSurface, White)
                )
            )
    ) {
        // Top Decorative Circle (positioned top-left for register)
        Box(
            modifier = Modifier
                .size(160.dp)
                .offset(x = (-50).dp, y = (-80).dp)
                .alpha(0.12f)
                .clip(CircleShape)
                .background(GreenPrimary)
        )

        // Bottom Decorative Circle (positioned bottom-right for register)
        Box(
            modifier = Modifier
                .size(140.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 50.dp, y = 0.dp)
                .alpha(0.1f)
                .clip(CircleShape)
                .background(GreenLight)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Back Button Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = GreenPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Back to Home",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = GreenPrimary,
                    modifier = Modifier.clickable { onBackClick() }
                )
            }

            // Register Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo Circle
                    Card(
                        modifier = Modifier.size(90.dp),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(containerColor = GreenSurface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "🍽️",
                                fontSize = 36.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Title
                    Text(
                        text = "Create Account",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = GreenDark,
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Subtitle
                    Text(
                        text = "Join EatsBuddy and start your food journey",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // Email Input
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email",
                                tint = GreenPrimary
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = InputBackground,
                            unfocusedContainerColor = InputBackground,
                            focusedBorderColor = GreenPrimary,
                            unfocusedBorderColor = Color.Transparent,
                            focusedLabelColor = GreenPrimary,
                            cursorColor = GreenPrimary
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Password Input
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password",
                                tint = GreenPrimary
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Done else Icons.Default.Close,
                                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                    tint = TextSecondary
                                )
                            }
                        },
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = InputBackground,
                            unfocusedContainerColor = InputBackground,
                            focusedBorderColor = GreenPrimary,
                            unfocusedBorderColor = Color.Transparent,
                            focusedLabelColor = GreenPrimary,
                            cursorColor = GreenPrimary
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Confirm Password Input
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm Password") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Confirm Password",
                                tint = GreenPrimary
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(
                                    imageVector = if (confirmPasswordVisible) Icons.Default.Done else Icons.Default.Close,
                                    contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password",
                                    tint = TextSecondary
                                )
                            }
                        },
                        singleLine = true,
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = InputBackground,
                            unfocusedContainerColor = InputBackground,
                            focusedBorderColor = GreenPrimary,
                            unfocusedBorderColor = Color.Transparent,
                            focusedLabelColor = GreenPrimary,
                            cursorColor = GreenPrimary
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(26.dp))

                    // Register Button
                    Button(
                        onClick = { onRegisterClick(email, password, confirmPassword) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Text(
                            text = "Create Account",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Divider with OR
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = DividerColor
                )
                Text(
                    text = "OR",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = DividerColor
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Login Link Container
            Card(
                modifier = Modifier.padding(horizontal = 20.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = White.copy(alpha = 0.9f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Already have an account?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Log In",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimary,
                        modifier = Modifier
                            .clickable { onLoginClick() }
                            .padding(4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    EatsBuddyTheme {
        RegisterScreen()
    }
}
