package com.example.githubdemo.screen.userprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.githubdemo.data.AppData
import com.example.githubdemo.data.UserRole
import com.example.githubdemo.ui.theme.ForestGreen
import com.example.githubdemo.ui.theme.MainText
import com.example.githubdemo.ui.theme.PageBackground
import com.example.githubdemo.ui.theme.PrimaryGreen
import com.example.githubdemo.ui.theme.SecondaryText
import com.example.githubdemo.viewmodel.userprofile.ProfileViewModel

@Composable
fun ProfileScreen(
    onNavigate: (String) -> Unit,

    profileViewModel:
    ProfileViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBackground),

        contentPadding =
            PaddingValues(bottom = 28.dp)
    ) {
        item {
            ProfileHeader()
        }

        item {
            when {
                profileViewModel.isLoading &&
                        profileViewModel.profile ==
                        null -> {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(48.dp),

                        contentAlignment =
                            Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = PrimaryGreen
                        )
                    }
                }

                profileViewModel.profile !=
                        null -> {

                    val profile =
                        profileViewModel.profile!!

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),

                        verticalArrangement =
                            Arrangement
                                .spacedBy(14.dp)
                    ) {
                        if (
                            profileViewModel
                                .isUsingLocalData
                        ) {
                            Text(
                                text =
                                    "Offline copy from local storage",

                                color =
                                    Color(0xFF9A5A00),

                                fontSize = 13.sp
                            )
                        }

                        Text(
                            text = profile.fullName,

                            color = MainText,

                            fontSize = 25.sp,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            text =
                                UserRole.getRoleName(
                                    profile.userRole
                                ),

                            color = PrimaryGreen,

                            fontSize = 15.sp,

                            fontWeight =
                                FontWeight.SemiBold
                        )

                        ProfileInformationCard(
                            icon =
                                Icons.Outlined.Email,

                            label =
                                "Email Address",

                            value = profile.email
                        )

                        ProfileInformationCard(
                            icon =
                                Icons.Outlined.Phone,

                            label =
                                "Phone Number",

                            value =
                                profile.phoneNumber
                        )

                        ProfileInformationCard(
                            icon =
                                Icons.Outlined.Home,

                            label =
                                UserRole
                                    .getAdditionalFieldLabel(
                                        profile.userRole
                                    ),

                            value =
                                profile
                                    .additionalInformation
                        )

                        OutlinedButton(
                            onClick = {
                                profileViewModel
                                    .loadProfile()
                            },

                            modifier =
                                Modifier.fillMaxWidth(),

                            shape =
                                RoundedCornerShape(
                                    16.dp
                                )
                        ) {
                            Text(
                                "Refresh Cloud Profile"
                            )
                        }

                        Button(
                            onClick = {
                                onNavigate(
                                    AppData
                                        .ROLE_SELECTION_ROUTE
                                )
                            },

                            modifier =
                                Modifier.fillMaxWidth(),

                            shape =
                                RoundedCornerShape(
                                    16.dp
                                ),

                            colors =
                                ButtonDefaults
                                    .buttonColors(
                                        containerColor =
                                            ForestGreen
                                    ),

                            contentPadding =
                                PaddingValues(
                                    vertical = 14.dp
                                )
                        ) {
                            Text(
                                text =
                                    "Sign Out and Change Role",

                                fontWeight =
                                    FontWeight.Bold
                            )
                        }
                    }
                }

                else -> {
                    ErrorProfileCard(
                        message =
                            profileViewModel
                                .errorMessage,

                        onRetry = {
                            profileViewModel
                                .loadProfile()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = ForestGreen,

                shape = RoundedCornerShape(
                    bottomStart = 34.dp,
                    bottomEnd = 34.dp
                )
            )
            .statusBarsPadding()
            .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(82.dp)
                .background(
                    color = Color(0xFF3F8269),
                    shape = CircleShape
                ),

            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector =
                    Icons.Outlined.Person,

                contentDescription =
                    "User profile",

                tint = Color.White,

                modifier =
                    Modifier.size(42.dp)
            )
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "My Profile",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text =
                "Cloud profile with local backup",

            color = Color(0xFFC6D9D1),

            fontSize = 14.sp
        )
    }
}

@Composable
fun ProfileInformationCard(
    icon: ImageVector,
    label: String,
    value: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(18.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 2.dp
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
                contentDescription = label,
                tint = PrimaryGreen,
                modifier = Modifier.size(26.dp)
            )

            Column(
                modifier =
                    Modifier.padding(start = 16.dp)
            ) {
                Text(
                    text = label,
                    color = SecondaryText,
                    fontSize = 13.sp
                )

                Text(
                    text = value,
                    color = MainText,
                    fontSize = 16.sp,
                    fontWeight =
                        FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun ErrorProfileCard(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            color = Color(0xFFB3261E)
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            onClick = onRetry,

            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        PrimaryGreen
                )
        ) {
            Text("Try Again")
        }
    }
}