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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubdemo.data.local.LocalAccountStorage
import com.example.githubdemo.model.farmer.Order
import com.example.githubdemo.model.farmer.Product
import com.example.githubdemo.repository.farmer.OrderRepository
import com.example.githubdemo.repository.farmer.ProductFarmerRepository
import com.example.githubdemo.screen.farmer.components.FarmerBottomBar

private val FarmerDashboardGreen =
    Color(0xFF28785B)

private val FarmerDashboardBackground =
    Color(0xFFF8F5ED)

@Composable
fun FarmerDashboardScreen(
    onNavigate: (String) -> Unit
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

    var products by remember {
        mutableStateOf(
            emptyList<Product>()
        )
    }

    var orders by remember {
        mutableStateOf(
            emptyList<Order>()
        )
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        isLoading = true
        errorMessage = ""

        try {
            products =
                productRepository
                    .getProducts()

            orders =
                orderRepository
                    .getOrders()
        } catch (
            exception: Exception
        ) {
            errorMessage =
                exception.message
                    ?: "Unable to load dashboard."
        } finally {
            isLoading = false
        }
    }

    val activeOrderCount =
        orders.count {
            it.status.equals(
                other = "Pending",
                ignoreCase = true
            ) ||
                    it.status.equals(
                        other = "Active",
                        ignoreCase = true
                    )
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

    val customerCount =
        orders
            .mapNotNull {
                it.customer_id
            }
            .distinct()
            .size

    Scaffold(
        containerColor =
            FarmerDashboardBackground,

        bottomBar = {
            FarmerBottomBar(
                current =
                    "dashboard",

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
                    .height(220.dp)
                    .background(
                        FarmerDashboardGreen
                    )
            ) {
                Column(
                    modifier =
                        Modifier.padding(
                            20.dp
                        )
                ) {
                    Text(
                        text =
                            "Welcome back,",

                        color =
                            Color.White.copy(
                                alpha = 0.8f
                            ),

                        fontSize =
                            13.sp
                    )

                    Row(
                        verticalAlignment =
                            Alignment
                                .CenterVertically
                    ) {
                        Text(
                            text =
                                "${profile?.fullName ?: "Farmer"} 🌱",

                            color =
                                Color.White,

                            fontSize =
                                22.sp,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Spacer(
                            modifier =
                                Modifier.padding(
                                    horizontal =
                                        5.dp
                                )
                        )

                        Box(
                            modifier = Modifier
                                .background(
                                    color =
                                        Color(
                                            0xFFFFA726
                                        ),

                                    shape =
                                        RoundedCornerShape(
                                            20.dp
                                        )
                                )
                                .padding(
                                    horizontal =
                                        10.dp,

                                    vertical =
                                        4.dp
                                )
                        ) {
                            Text(
                                text =
                                    "✓ VERIFIED",

                                color =
                                    Color.White,

                                fontSize =
                                    10.sp
                            )
                        }
                    }

                    Spacer(
                        modifier =
                            Modifier.height(
                                25.dp
                            )
                    )

                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.spacedBy(
                                12.dp
                            )
                    ) {
                        FarmerDashboardStatCard(
                            title =
                                "Total Earnings",

                            value =
                                "RM %.2f".format(
                                    totalEarnings
                                ),

                            modifier =
                                Modifier.weight(
                                    1f
                                )
                        )

                        FarmerDashboardStatCard(
                            title =
                                "Active Orders",

                            value =
                                activeOrderCount
                                    .toString(),

                            modifier =
                                Modifier.weight(
                                    1f
                                )
                        )
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(
                        20.dp
                    )
            )

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),

                    contentAlignment =
                        Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color =
                            FarmerDashboardGreen
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .padding(
                            horizontal =
                                20.dp
                        )
                        .fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.spacedBy(
                            12.dp
                        )
                ) {
                    FarmerSmallStatCard(
                        value =
                            products.size
                                .toString(),

                        title =
                            "Products",

                        modifier =
                            Modifier.weight(
                                1f
                            )
                    )

                    FarmerSmallStatCard(
                        value =
                            activeOrderCount
                                .toString(),

                        title =
                            "Orders",

                        modifier =
                            Modifier.weight(
                                1f
                            )
                    )

                    FarmerSmallStatCard(
                        value =
                            customerCount
                                .toString(),

                        title =
                            "Customers",

                        modifier =
                            Modifier.weight(
                                1f
                            )
                    )
                }
            }

            if (
                errorMessage.isNotEmpty()
            ) {
                Text(
                    text =
                        errorMessage,

                    color =
                        Color.Red,

                    fontSize =
                        13.sp,

                    modifier =
                        Modifier.padding(
                            horizontal =
                                20.dp,

                            vertical =
                                10.dp
                        )
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        25.dp
                    )
            )

            Row(
                modifier = Modifier
                    .padding(
                        horizontal =
                            20.dp
                    )
                    .fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {
                Text(
                    text =
                        "Recent Orders",

                    fontSize =
                        18.sp,

                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text =
                        "${orders.size} orders",

                    color =
                        FarmerDashboardGreen
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        10.dp
                    )
            )

            if (
                !isLoading &&
                orders.isEmpty()
            ) {
                Card(
                    modifier = Modifier
                        .padding(
                            horizontal =
                                20.dp
                        )
                        .fillMaxWidth(),

                    shape =
                        RoundedCornerShape(
                            18.dp
                        )
                ) {
                    Text(
                        text =
                            "No recent orders.",

                        color =
                            Color.Gray,

                        modifier =
                            Modifier.padding(
                                20.dp
                            )
                    )
                }
            } else {
                orders
                    .take(2)
                    .forEach { order ->
                        FarmerRecentOrderItem(
                            id =
                                order.id
                                    ?.take(8)
                                    ?: "Order",

                            name =
                                "${order.customer_name} • ${order.product_name}",

                            price =
                                "RM %.2f".format(
                                    order.price
                                )
                        )
                    }
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
private fun FarmerDashboardStatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(100.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color(
                        0xFF4F8973
                    )
            ),

        shape =
            RoundedCornerShape(
                18.dp
            )
    ) {
        Column(
            modifier =
                Modifier.padding(
                    15.dp
                )
        ) {
            Text(
                text = value,

                color =
                    Color.White,

                fontSize =
                    22.sp,

                fontWeight =
                    FontWeight.Bold
            )

            Text(
                text = title,

                color =
                    Color.White.copy(
                        alpha = 0.8f
                    ),

                fontSize =
                    12.sp
            )
        }
    }
}

@Composable
private fun FarmerSmallStatCard(
    value: String,
    title: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(70.dp),

        shape =
            RoundedCornerShape(
                15.dp
            )
    ) {
        Column(
            modifier =
                Modifier.fillMaxSize(),

            horizontalAlignment =
                Alignment
                    .CenterHorizontally,

            verticalArrangement =
                Arrangement.Center
        ) {
            Text(
                text = value,

                fontSize =
                    18.sp,

                fontWeight =
                    FontWeight.Bold
            )

            Text(
                text = title,

                fontSize =
                    11.sp
            )
        }
    }
}

@Composable
private fun FarmerRecentOrderItem(
    id: String,
    name: String,
    price: String
) {
    Card(
        modifier = Modifier
            .padding(
                horizontal =
                    20.dp,

                vertical =
                    5.dp
            )
            .fillMaxWidth(),

        shape =
            RoundedCornerShape(
                18.dp
            )
    ) {
        Row(
            modifier =
                Modifier.padding(
                    12.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Text(
                text = "📦",
                fontSize = 25.sp
            )

            Spacer(
                modifier =
                    Modifier.padding(
                        horizontal =
                            5.dp
                    )
            )

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {
                Text(
                    text = id,

                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text = name,

                    fontSize =
                        12.sp
                )
            }

            Text(
                text = price,

                color =
                    FarmerDashboardGreen,

                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}