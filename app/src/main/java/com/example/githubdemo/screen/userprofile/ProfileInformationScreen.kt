package com.example.githubdemo.screen.userprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.githubdemo.ui.theme.PrimaryGreen
import com.example.githubdemo.viewmodel.userprofile.ProfileViewModel

@Composable
fun ProfileInformationScreen(
    onBack: () -> Unit = {},
    onEditClick: () -> Unit = {},
    profileViewModel: ProfileViewModel
) {
    val profile by
    profileViewModel.profile
        .collectAsState()

    val message by
    profileViewModel.message
        .collectAsState()

    val messageIsError by
    profileViewModel.messageIsError
        .collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFFF4F6EE)
            )
    ) {
        BuyerProfilePageHeader(
            title = "Profile Information",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(16.dp),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment =
                    Alignment.Center
            ) {
                Icon(
                    imageVector =
                        Icons.Default.Person,
                    contentDescription = null,
                    tint = PrimaryGreen,
                    modifier =
                        Modifier.size(55.dp)
                )
            }

            Spacer(Modifier.height(20.dp))

            ProfileDetailCard(
                icon = Icons.Default.Person,
                title = "Full Name",
                value =
                    profile?.fullName
                        ?: "Not available"
            )

            ProfileDetailCard(
                icon = Icons.Default.Email,
                title = "Email",
                value =
                    profile?.email
                        ?: "Not available"
            )

            ProfileDetailCard(
                icon = Icons.Default.Phone,
                title = "Phone Number",
                value =
                    profile?.phoneNumber
                        ?: "Not available"
            )

            ProfileDetailCard(
                icon = Icons.Default.Home,
                title = "Address",
                value =
                    profile
                        ?.additionalInformation
                        ?: "Not available"
            )

            if (message.isNotBlank()) {
                Text(
                    text = message,
                    color =
                        if (messageIsError) {
                            MaterialTheme
                                .colorScheme.error
                        } else {
                            PrimaryGreen
                        },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    profileViewModel
                        .clearMessage()

                    onEditClick()
                },
                enabled = profile != null,
                modifier =
                    Modifier.fillMaxWidth(),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            PrimaryGreen
                    ),
                shape =
                    RoundedCornerShape(14.dp)
            ) {
                Text("Edit Profile")
            }
        }
    }
}

@Composable
private fun ProfileDetailCard(
    icon: ImageVector,
    title: String,
    value: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
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
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryGreen
            )

            Column(
                modifier =
                    Modifier.padding(start = 16.dp)
            ) {
                Text(
                    text = title,
                    color = Color.Gray,
                    style =
                        MaterialTheme.typography
                            .labelMedium
                )

                Text(
                    text = value,
                    style =
                        MaterialTheme.typography
                            .bodyLarge
                )
            }
        }
    }
}