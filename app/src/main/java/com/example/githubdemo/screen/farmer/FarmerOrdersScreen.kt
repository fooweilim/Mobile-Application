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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubdemo.model.farmer.Order
import com.example.githubdemo.repository.farmer.OrderRepository
import com.example.githubdemo.screen.farmer.components.FarmerBottomBar
import kotlinx.coroutines.launch

private val FarmerOrdersBackground =
    Color(0xFFF8F5ED)

private val FarmerOrdersGreen =
    Color(0xFF28785B)

private const val PENDING_STATUS =
    "Pending"

private const val ACTIVE_STATUS =
    "Active"

private const val DELIVERED_STATUS =
    "Delivered"

@Composable
fun FarmerOrdersScreen(
    onNavigate: (String) -> Unit
) {
    val repository = remember {
        OrderRepository()
    }

    val coroutineScope =
        rememberCoroutineScope()

    var orders by remember {
        mutableStateOf(
            emptyList<Order>()
        )
    }

    var selectedTab by remember {
        mutableStateOf(
            PENDING_STATUS
        )
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    fun loadOrders() {
        coroutineScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                orders =
                    repository.getOrders()
            } catch (
                exception: Exception
            ) {
                orders =
                    emptyList()

                errorMessage =
                    exception.message
                        ?: "Unable to load orders."
            } finally {
                isLoading = false
            }
        }
    }

    fun updateOrderStatus(
        order: Order,
        newStatus: String
    ) {
        val orderId =
            order.id

        if (orderId.isNullOrBlank()) {
            errorMessage =
                "The order ID is missing."

            return
        }

        coroutineScope.launch {
            errorMessage = ""

            try {
                repository.updateStatus(
                    id = orderId,
                    status = newStatus
                )

                loadOrders()
            } catch (
                exception: Exception
            ) {
                errorMessage =
                    exception.message
                        ?: "Unable to update order."
            }
        }
    }

    LaunchedEffect(Unit) {
        loadOrders()
    }

    val filteredOrders =
        orders.filter {
            it.status.equals(
                other = selectedTab,
                ignoreCase = true
            )
        }

    Scaffold(
        containerColor =
            FarmerOrdersBackground,

        bottomBar = {
            FarmerBottomBar(
                current = "orders",

                onNavigate =
                    onNavigate
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(
                    horizontal =
                        20.dp
                )
                .fillMaxSize()
        ) {
            Spacer(
                modifier =
                    Modifier.height(
                        20.dp
                    )
            )

            Text(
                text = "Orders",

                fontSize = 24.sp,

                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(
                        15.dp
                    )
            )

            FarmerOrderTabs(
                selected =
                    selectedTab,

                onSelect = {
                    selectedTab = it
                }
            )

            Spacer(
                modifier =
                    Modifier.height(
                        15.dp
                    )
            )

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
                            bottom =
                                10.dp
                        )
                )
            }

            when {
                isLoading -> {
                    Box(
                        modifier =
                            Modifier.fillMaxSize(),

                        contentAlignment =
                            Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color =
                                FarmerOrdersGreen
                        )
                    }
                }

                filteredOrders.isEmpty() -> {
                    Box(
                        modifier =
                            Modifier.fillMaxSize(),

                        contentAlignment =
                            Alignment.Center
                    ) {
                        Text(
                            text =
                                "No $selectedTab Orders",

                            color =
                                Color.Gray
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        verticalArrangement =
                            Arrangement.spacedBy(
                                15.dp
                            )
                    ) {
                        items(
                            items =
                                filteredOrders,

                            key = { order ->
                                order.id
                                    ?: "${order.customer_name}-${order.product_name}"
                            }
                        ) { order ->
                            FarmerOrderCard(
                                order = order,

                                onAccept = {
                                    updateOrderStatus(
                                        order =
                                            order,

                                        newStatus =
                                            ACTIVE_STATUS
                                    )
                                },

                                onDelivered = {
                                    updateOrderStatus(
                                        order =
                                            order,

                                        newStatus =
                                            DELIVERED_STATUS
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FarmerOrderTabs(
    selected: String,
    onSelect: (String) -> Unit
) {
    val tabs = listOf(
        PENDING_STATUS,
        ACTIVE_STATUS,
        DELIVERED_STATUS
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color =
                    Color(
                        0xFFEDE7DC
                    ),

                shape =
                    RoundedCornerShape(
                        25.dp
                    )
            )
            .padding(5.dp),

        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {
        tabs.forEach { tab ->
            Button(
                onClick = {
                    onSelect(tab)
                },

                colors =
                    ButtonDefaults
                        .buttonColors(
                            containerColor =
                                if (
                                    selected ==
                                    tab
                                ) {
                                    FarmerOrdersGreen
                                } else {
                                    Color.Gray
                                }
                        ),

                shape =
                    RoundedCornerShape(
                        20.dp
                    )
            ) {
                Text(
                    text = tab,

                    fontSize = 12.sp,

                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun FarmerOrderCard(
    order: Order,
    onAccept: () -> Unit,
    onDelivered: () -> Unit
) {
    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                20.dp
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation =
                    3.dp
            )
    ) {
        Column(
            modifier =
                Modifier.padding(
                    15.dp
                )
        ) {
            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {
                Text(
                    text =
                        order.id
                            ?.take(8)
                            ?: "Order",

                    fontWeight =
                        FontWeight.Bold
                )

                FarmerOrderStatusBadge(
                    status =
                        order.status
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color =
                                Color(
                                    0xFFE8F5EC
                                ),

                            shape =
                                RoundedCornerShape(
                                    50
                                )
                        ),

                    contentAlignment =
                        Alignment.Center
                ) {
                    Text(
                        text = "👤"
                    )
                }

                Spacer(
                    modifier =
                        Modifier.padding(
                            horizontal =
                                6.dp
                        )
                )

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {
                    Text(
                        text =
                            order.customer_name,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(
                        text =
                            order.product_name,

                        fontSize =
                            12.sp
                    )

                    Text(
                        text =
                            "Quantity: ${order.quantity}",

                        fontSize =
                            12.sp
                    )
                }

                Text(
                    text =
                        "RM %.2f".format(
                            order.price
                        ),

                    color =
                        FarmerOrdersGreen,

                    fontWeight =
                        FontWeight.Bold
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        10.dp
                    )
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.End
            ) {
                if (
                    order.status.equals(
                        other =
                            PENDING_STATUS,

                        ignoreCase =
                            true
                    )
                ) {
                    Button(
                        onClick = onAccept,

                        colors =
                            ButtonDefaults
                                .buttonColors(
                                    containerColor =
                                        FarmerOrdersGreen
                                )
                    ) {
                        Text(
                            text =
                                "Accept Order",

                            fontSize =
                                12.sp
                        )
                    }
                }

                if (
                    order.status.equals(
                        other =
                            ACTIVE_STATUS,

                        ignoreCase =
                            true
                    )
                ) {
                    Button(
                        onClick =
                            onDelivered,

                        colors =
                            ButtonDefaults
                                .buttonColors(
                                    containerColor =
                                        Color(
                                            0xFFFFA726
                                        )
                                )
                    ) {
                        Text(
                            text =
                                "Mark Delivered",

                            fontSize =
                                12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FarmerOrderStatusBadge(
    status: String
) {
    val backgroundColor =
        when {
            status.equals(
                other =
                    DELIVERED_STATUS,

                ignoreCase =
                    true
            ) -> {
                Color(
                    0xFFE5F3EA
                )
            }

            status.equals(
                other =
                    ACTIVE_STATUS,

                ignoreCase =
                    true
            ) -> {
                Color(
                    0xFFFFF1D6
                )
            }

            else -> {
                Color(
                    0xFFE7EDF8
                )
            }
        }

    Box(
        modifier = Modifier
            .background(
                color =
                    backgroundColor,

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
                status.uppercase(),

            fontSize =
                9.sp
        )
    }
}