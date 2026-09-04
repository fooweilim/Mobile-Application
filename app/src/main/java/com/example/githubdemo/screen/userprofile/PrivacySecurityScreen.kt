package com.example.githubdemo.screen.userprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.githubdemo.ui.theme.PrimaryGreen

@Composable
fun PrivacySecurityScreen(
    onBack: () -> Unit = {},
    onChangePassword: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFFF4F6EE)
            )
    ) {
        BuyerProfilePageHeader(
            title = "Privacy & Security",
            onBack = onBack
        )

        Card(
            onClick = onChangePassword,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape =
                RoundedCornerShape(18.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Color.White
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Icon(
                    imageVector =
                        Icons.Default.Password,
                    contentDescription = null,
                    tint = PrimaryGreen
                )

                Spacer(Modifier.width(16.dp))

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {
                    Text(
                        text = "Change Password",
                        style =
                            MaterialTheme.typography
                                .titleMedium
                    )

                    Text(
                        text =
                            "Update your account password",
                        color = Color.Gray
                    )
                }

                Icon(
                    imageVector =
                        Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
    }
}