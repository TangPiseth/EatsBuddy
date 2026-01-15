package com.example.eatsbuddy.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eatsbuddy.ui.theme.EatsBuddyTheme
import com.example.eatsbuddy.ui.theme.GreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegalsPage(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Legal 📜",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Terms of Service
            item {
                LegalSection(
                    title = "Terms of Service",
                    emoji = "📋",
                    content = """
                        Last Updated: December 2025
                        
                        Welcome to EatsBuddy! By using our app, you agree to these terms.
                        
                        1. Acceptance of Terms
                        By accessing or using EatsBuddy, you agree to be bound by these Terms of Service and our Privacy Policy.
                        
                        2. Use of Service
                        EatsBuddy provides recipe browsing, meal planning, and grocery list features for personal, non-commercial use. You agree not to misuse our services or help anyone else do so.
                        
                        3. User Accounts
                        You are responsible for maintaining the confidentiality of your account credentials and for all activities under your account.
                        
                        4. Content
                        Recipes and content provided in EatsBuddy are for informational purposes only. We do not guarantee the accuracy of nutritional information or cooking instructions.
                        
                        5. Modifications
                        We reserve the right to modify or discontinue the service at any time without notice.
                        
                        6. Limitation of Liability
                        EatsBuddy is provided "as is" without warranties of any kind. We are not liable for any damages arising from your use of the app.
                    """.trimIndent()
                )
            }

            // Privacy Policy
            item {
                LegalSection(
                    title = "Privacy Policy",
                    emoji = "🔒",
                    content = """
                        Last Updated: December 2025
                        
                        Your privacy is important to us. This policy explains how we collect, use, and protect your information.
                        
                        1. Information We Collect
                        • Account information (email, name) when you register
                        • Usage data (recipes viewed, meal plans created)
                        • Device information for app optimization
                        
                        2. How We Use Your Information
                        • To provide and improve our services
                        • To personalize your experience
                        • To communicate with you about updates
                        
                        3. Data Storage
                        Your data is stored securely using industry-standard encryption. Meal plans and grocery lists are stored locally on your device and in our secure cloud servers.
                        
                        4. Third-Party Services
                        We may use third-party services for analytics and authentication. These services have their own privacy policies.
                        
                        5. Your Rights
                        You can request access to, correction of, or deletion of your personal data at any time by contacting us.
                        
                        6. Contact
                        For privacy-related questions, contact us at privacy@eatsbuddy.com
                    """.trimIndent()
                )
            }

            // Disclaimer
            item {
                LegalSection(
                    title = "Disclaimer",
                    emoji = "⚠️",
                    content = """
                        Nutritional & Health Information
                        
                        The nutritional information and recipes provided in EatsBuddy are for general informational purposes only. They are not intended as medical or dietary advice.
                        
                        • Always consult with a healthcare professional before making significant changes to your diet
                        • Check all ingredients for potential allergens
                        • Nutritional values are estimates and may vary
                        • Cooking times may vary based on equipment and conditions
                        
                        Food Safety
                        
                        Users are responsible for following proper food safety guidelines when preparing recipes. EatsBuddy is not responsible for any illness or injury resulting from the preparation or consumption of recipes.
                    """.trimIndent()
                )
            }
        }
    }
}

@Composable
fun LegalSection(
    title: String,
    emoji: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "$emoji $title",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = GreenPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                lineHeight = 22.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LegalsPagePreview() {
    EatsBuddyTheme {
        LegalsPage()
    }
}
