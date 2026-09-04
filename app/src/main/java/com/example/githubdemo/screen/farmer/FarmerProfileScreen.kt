package com.example.githubdemo.screen.farmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Nature
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubdemo.data.local.LocalAccountStorage
import com.example.githubdemo.model.farmer.Order
import com.example.githubdemo.repository.farmer.OrderRepository
import com.example.githubdemo.repository.farmer.ProductFarmerRepository
import com.example.githubdemo.screen.farmer.components.FarmerBottomBar

private val FarmerProfileGreen =
    Color(0xFF28785B)

private val FarmerProfileBackground =
    Color(0xFFF8F5ED)

@Composable
fun FarmerProfileScreen(
    onNavigate: (String) -> Unit,
    onSignOut: () -> Unit
) {
    val context =
        LocalContext.current

    val profile = remember {
        LocalAccountStorage
            .getProfile(context)
    }

    val productRepository = remember {
        ProductFarmerRepository()
    }

    val orderRepository = remember {
        OrderRepository()
    }

    var productCount by remember {
        mutableStateOf(0)
    }

    var orders by remember {
        mutableStateOf(
            emptyList<Order>()
        )
    }

    LaunchedEffect(Unit) {
        try {
            productCount =
                productRepository
                    .getProducts()
                    .size

            orders =
                orderRepository
                    .getOrders()
        } catch (
            exception: Exception
        ) {
            productCount = 0
            orders = emptyList()
        }
    }

    val totalEarnings =
        orders
            .filter {
                it.status.equals(
                    other = "Delivered",
                    ignoreCase = true
                )
            }
            .sumOf {
                it.price
            }

    Scaffold(
        containerColor =
            FarmerProfileBackground,

        bottomBar = {
            FarmerBottomBar(
                current = "profile",

                onNavigate =
                    onNavigate
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(185.dp)
                    .background(
                        FarmerProfileGreen
                    )
            ) {
                Column(
                    modifier =
                        Modifier.padding(
                            20.dp
                        )
                ) {
                    Text(
                        text = "Profile",

                        color = Color.White,

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
                            Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(65.dp)
                                .background(
                                    color =
                                        Color(
                                            0xFFE4EEE7
                                        ),

                                    shape =
                                        CircleShape
                                ),

                            contentAlignment =
                                Alignment.Center
                        ) {
                            Text(
                                text = "🌱",
                                fontSize = 28.sp
                            )
                        }

                        Spacer(
                            modifier =
                                Modifier.padding(
                                    horizontal =
                                        8.dp
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
                                        profile
                                            ?.fullName
                                            ?: "Farmer",

                                    color =
                                        Color.White,

                                    fontWeight =
                                        FontWeight.Bold,

                                    fontSize =
                                        18.sp
                                )

                                Spacer(
                                    modifier =
                                        Modifier.padding(
                                            horizontal =
                                                4.dp
                                        )
                                )

                                Text(
                                    text =
                                        " VERIFIED ",

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
                                                    4.dp,

                                                vertical =
                                                    2.dp
                                            )
                                )
                            }

                            Text(
                                text =
                                    profile?.email
                                        ?: "No email",

                                color =
                                    Color.White
                            )

                            Text(
                                text =
                                    "Registered Farmer",

                                color =
                                    Color(
                                        0xFFD9E8DD
                                    )
                            )
                        }
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(
                        20.dp
                    )
            )

            Card(
                modifier = Modifier
                    .padding(
                        horizontal =
                            20.dp
                    )
                    .fillMaxWidth(),

                shape =
                    RoundedCornerShape(
                        22.dp
                    )
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                20.dp
                            ),

                    horizontalArrangement =
                        Arrangement.SpaceAround
                ) {
                    FarmerProfileStat(
                        value =
                            "RM %.2f".format(
                                totalEarnings
                            ),

                        title =
                            "Earnings"
                    )

                    FarmerProfileStat(
                        value =
                            orders.size
                                .toString(),

                        title =
                            "Orders"
                    )

                    FarmerProfileStat(
                        value =
                            productCount
                                .toString(),

                        title =
                            "Products"
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(
                        20.dp
                    )
            )

            Column(
                modifier = Modifier
                    .padding(
                        horizontal =
                            20.dp
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

                    title =
                        "Farm Details"
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
                        Icons.Default.Star,

                    title =
                        "Premium Plan"
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

            Spacer(
                modifier =
                    Modifier.height(
                        20.dp
                    )
            )

            OutlinedButton(
                onClick =
                    onSignOut,

                modifier = Modifier
                    .padding(
                        horizontal =
                            20.dp
                    )
                    .fillMaxWidth()
            ) {
                Icon(
                    imageVector =
                        Icons.Default.Logout,

                    contentDescription =
                        "Sign Out"
                )

                Spacer(
                    modifier =
                        Modifier.padding(
                            horizontal =
                                4.dp
                        )
                )

                Text(
                    text = "Sign Out"
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        20.dp
                    )
            )
        }
    }
}

@Composable
private fun FarmerProfileStat(
    value: String,
    title: String
) {
    Column(
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text = value,

            fontWeight =
                FontWeight.Bold
        )

        Text(
            text = title,

            fontSize =
                12.sp
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
                horizontal =
                    15.dp
            ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,

            contentDescription =
                title,

            tint =
                FarmerProfileGreen
        )

        Spacer(
            modifier =
                Modifier.padding(
                    horizontal =
                        8.dp
                )
        )

        Text(
            text = title,

            modifier =
                Modifier.weight(1f)
        )

        Icon(
            imageVector =
                Icons.Default
                    .ChevronRight,

            contentDescription =
                null
        )
    }
}