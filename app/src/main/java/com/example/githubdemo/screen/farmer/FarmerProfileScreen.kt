package com.example.githubdemo.screen.farmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Nature
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubdemo.nav.FarmerRoute
import com.example.githubdemo.supabase.CloudProfile
import com.example.githubdemo.supabase.FarmerProductRepository

private val FarmerProfileGreen =
    Color(0xFF28785B)

private val FarmerProfileBackground =
    Color(0xFFF8F5ED)

@Composable
fun FarmerProfileScreen(
    profile: CloudProfile?,
    onNavigate: (String) -> Unit,
    onSignOut: () -> Unit
) {
    val repository = remember {
        FarmerProductRepository()
    }

    var productCount by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(Unit) {
        try {
            productCount =
                repository
                    .getProducts()
                    .size
        } catch (_: Exception) {
            productCount = 0
        }
    }

    val farmInformation =
        remember(
            profile
                ?.additionalInformation
        ) {
            profile
                ?.additionalInformation
                ?.split("|")
                ?.map {
                    it.trim()
                }
                .orEmpty()
        }

    val farmerName =
        profile
            ?.fullName
            ?.ifBlank {
                "Farmer"
            }
            ?: "Farmer"

    val farmerEmail =
        profile
            ?.email
            ?.ifBlank {
                "No email"
            }
            ?: "No email"

    val farmName =
        farmInformation
            .getOrNull(0)
            .orEmpty()
            .ifBlank {
                "Farm details not available"
            }

    val stateName =
        farmInformation
            .getOrNull(1)
            .orEmpty()
            .ifBlank {
                "Malaysia"
            }

    val farmType =
        farmInformation
            .getOrNull(2)
            .orEmpty()
            .ifBlank {
                "Farmer"
            }

    Scaffold(
        containerColor =
            FarmerProfileBackground,

        bottomBar = {
            FarmerBottomBar(
                currentRoute =
                    FarmerRoute.PROFILE,

                onNavigate =
                    onNavigate
            )
        }
    ) { paddingValues ->

        LazyColumn(
            contentPadding =
                PaddingValues(
                    bottom = 24.dp
                ),

            modifier =
                Modifier.padding(
                    paddingValues
                )
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            FarmerProfileGreen
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .statusBarsPadding()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Profile",

                            color =
                                Color.White,

                            fontSize = 22.sp,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Spacer(
                            modifier =
                                Modifier.height(
                                    20.dp
                                )
                        )

                        Row(
                            verticalAlignment =
                                Alignment
                                    .CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(65.dp)
                                    .background(
                                        color =
                                            Color(
                                                0xFFE8F5EC
                                            ),

                                        shape =
                                            CircleShape
                                    ),

                                contentAlignment =
                                    Alignment.Center
                            ) {
                                Icon(
                                    imageVector =
                                        Icons.Default
                                            .Person,

                                    contentDescription =
                                        null,

                                    tint =
                                        FarmerProfileGreen,

                                    modifier =
                                        Modifier
                                            .size(34.dp)
                                )
                            }

                            Spacer(
                                modifier =
                                    Modifier.width(
                                        15.dp
                                    )
                            )

                            Column {
                                Row(
                                    verticalAlignment =
                                        Alignment
                                            .CenterVertically
                                ) {
                                    Text(
                                        text =
                                            farmerName,

                                        color =
                                            Color.White,

                                        fontWeight =
                                            FontWeight
                                                .Bold,

                                        fontSize =
                                            18.sp
                                    )

                                    if (
                                        profile
                                            ?.emailVerified ==
                                        true
                                    ) {
                                        Spacer(
                                            modifier =
                                                Modifier
                                                    .width(
                                                        8.dp
                                                    )
                                        )

                                        Text(
                                            text =
                                                "VERIFIED",

                                            color =
                                                Color.White,

                                            fontSize =
                                                10.sp,

                                            modifier =
                                                Modifier
                                                    .background(
                                                        color =
                                                            Color(
                                                                0xFFFFA726
                                                            ),

                                                        shape =
                                                            RoundedCornerShape(
                                                                10.dp
                                                            )
                                                    )
                                                    .padding(
                                                        horizontal =
                                                            7.dp,

                                                        vertical =
                                                            3.dp
                                                    )
                                        )
                                    }
                                }

                                Text(
                                    text =
                                        farmerEmail,

                                    color =
                                        Color.White
                                )

                                Text(
                                    text =
                                        "$stateName, Malaysia",

                                    color =
                                        Color(
                                            0xFFD9E8DD
                                        )
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                Card(
                    modifier = Modifier
                        .padding(
                            horizontal = 20.dp
                        )
                        .fillMaxWidth(),

                    shape =
                        RoundedCornerShape(
                            22.dp
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),

                        horizontalArrangement =
                            Arrangement
                                .SpaceAround
                    ) {
                        FarmerProfileStat(
                            value =
                                productCount
                                    .toString(),

                            title = "Products"
                        )

                        FarmerProfileStat(
                            value = "Verified",
                            title = "Status"
                        )

                        FarmerProfileStat(
                            value = farmType,
                            title = "Farm Type"
                        )
                    }
                }
            }

            item {
                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                Column(
                    modifier = Modifier
                        .padding(
                            horizontal = 20.dp
                        )
                        .background(
                            color =
                                Color.White,

                            shape =
                                RoundedCornerShape(
                                    25.dp
                                )
                        )
                ) {
                    FarmerProfileMenuItem(
                        icon =
                            Icons.Default.Nature,

                        title = farmName
                    )

                    FarmerProfileMenuItem(
                        icon =
                            Icons.Default
                                .AccountBalance,

                        title =
                            "Banking Info"
                    )

                    FarmerProfileMenuItem(
                        icon =
                            Icons.Default
                                .VerifiedUser,

                        title =
                            "Verification Status"
                    )

                    FarmerProfileMenuItem(
                        icon =
                            Icons.Default
                                .Notifications,

                        title =
                            "Notifications"
                    )
                }
            }

            item {
                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                OutlinedButton(
                    onClick = onSignOut,

                    modifier = Modifier
                        .padding(
                            horizontal = 20.dp
                        )
                        .fillMaxWidth()
                ) {
                    Icon(
                        imageVector =
                            Icons.Default.Logout,

                        contentDescription =
                            null
                    )

                    Spacer(
                        modifier =
                            Modifier.width(8.dp)
                    )

                    Text("Sign Out")
                }
            }
        }
    }
}

@Composable
private fun FarmerProfileStat(
    value: String,
    title: String
) {
    Column(
        modifier =
            Modifier.padding(
                horizontal = 4.dp
            ),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text = value,

            fontWeight =
                FontWeight.Bold,

            maxLines = 1
        )

        Text(
            text = title,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun FarmerProfileMenuItem(
    icon: ImageVector,
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .padding(
                horizontal = 15.dp
            ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,

            tint =
                FarmerProfileGreen
        )

        Spacer(
            modifier =
                Modifier.width(15.dp)
        )

        Text(
            text = title,

            modifier =
                Modifier.weight(1f),

            maxLines = 1
        )

        Icon(
            imageVector =
                Icons.Default
                    .ChevronRight,

            contentDescription = null
        )
    }
}