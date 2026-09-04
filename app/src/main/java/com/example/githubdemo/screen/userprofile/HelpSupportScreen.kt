package com.example.githubdemo.screen.userprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.githubdemo.ui.theme.PrimaryGreen

@Composable
fun HelpSupportScreen(
    onBack: () -> Unit = {}
) {
    var selectedTitle by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFFF4F6EE)
            )
    ) {
        BuyerProfilePageHeader(
            title = "Help & Support",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(16.dp)
        ) {
            HelpItem(
                icon = Icons.Default.Help,
                title =
                    "Frequently Asked Questions",
                description =
                    "Find answers to common questions",
                details =
                    "Use My Orders to check purchases, Food Box Management to manage subscriptions, and Profile Information to update your details.",
                selected =
                    selectedTitle ==
                            "Frequently Asked Questions",
                onClick = {
                    selectedTitle =
                        toggleSelection(
                            selectedTitle,
                            "Frequently Asked Questions"
                        )
                }
            )

            HelpItem(
                icon = Icons.Default.Email,
                title = "Contact Us",
                description =
                    "Contact the HarvestLink support team",
                details =
                    "Use the support contact supplied by your HarvestLink administrator.",
                selected =
                    selectedTitle ==
                            "Contact Us",
                onClick = {
                    selectedTitle =
                        toggleSelection(
                            selectedTitle,
                            "Contact Us"
                        )
                }
            )

            HelpItem(
                icon =
                    Icons.Default.ReportProblem,
                title = "Report a Problem",
                description =
                    "Tell us about an issue",
                details =
                    "Include the screen name, what you selected, and the error message when reporting a problem.",
                selected =
                    selectedTitle ==
                            "Report a Problem",
                onClick = {
                    selectedTitle =
                        toggleSelection(
                            selectedTitle,
                            "Report a Problem"
                        )
                }
            )

            HelpItem(
                icon = Icons.Default.Info,
                title = "About HarvestLink",
                description =
                    "Application information",
                details =
                    "HarvestLink connects buyers with food products, meal ideas, and recurring food boxes.",
                selected =
                    selectedTitle ==
                            "About HarvestLink",
                onClick = {
                    selectedTitle =
                        toggleSelection(
                            selectedTitle,
                            "About HarvestLink"
                        )
                }
            )
        }
    }
}

private fun toggleSelection(
    current: String,
    selected: String
): String {
    return if (current == selected) {
        ""
    } else {
        selected
    }
}

@Composable
private fun HelpItem(
    icon: ImageVector,
    title: String,
    description: String,
    details: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PrimaryGreen
                )

                Spacer(Modifier.width(16.dp))

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        style =
                            MaterialTheme.typography
                                .titleMedium
                    )

                    Text(
                        text = description,
                        color = Color.Gray
                    )
                }
            }

            if (selected) {
                Text(
                    text = details,
                    modifier =
                        Modifier.padding(
                            top = 14.dp
                        ),
                    color = Color(0xFF4B4B4B)
                )
            }
        }
    }
}