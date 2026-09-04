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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.githubdemo.model.farmer.Order
import com.example.githubdemo.ui.theme.PrimaryGreen
import com.example.githubdemo.viewmodel.userprofile.CustomerOrderViewModel

@Composable
fun MyOrderScreen(
    onBack: () -> Unit = {},
    orderViewModel:
    CustomerOrderViewModel
) {
    val orders by
    orderViewModel.orders
        .collectAsState()

    val isLoading by
    orderViewModel.isLoading
        .collectAsState()

    val errorMessage by
    orderViewModel.errorMessage
        .collectAsState()

    LaunchedEffect(Unit) {
        orderViewModel.loadOrders()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFFF4F6EE)
            )
    ) {
        BuyerProfilePageHeader(
            title = "My Orders",
            onBack = onBack
        )

        when {
            isLoading -> {
                Box(
                    modifier =
                        Modifier.fillMaxSize(),
                    contentAlignment =
                        Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = PrimaryGreen
                    )
                }
            }

            errorMessage.isNotBlank() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment =
                        Alignment.CenterHorizontally,
                    verticalArrangement =
                        Arrangement.Center
                ) {
                    Text(
                        text = errorMessage,
                        color =
                            MaterialTheme
                                .colorScheme.error
                    )

                    Spacer(
                        Modifier.height(16.dp)
                    )

                    Button(
                        onClick =
                            orderViewModel::loadOrders,
                        colors =
                            ButtonDefaults
                                .buttonColors(
                                    containerColor =
                                        PrimaryGreen
                                )
                    ) {
                        Text("Try Again")
                    }
                }
            }

            orders.isEmpty() -> {
                Box(
                    modifier =
                        Modifier.fillMaxSize(),
                    contentAlignment =
                        Alignment.Center
                ) {
                    Text("No orders yet")
                }
            }

            else -> {
                LazyColumn(
                    modifier =
                        Modifier.fillMaxSize(),
                    contentPadding =
                        PaddingValues(16.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(
                            12.dp
                        )
                ) {
                    items(orders) { order ->
                        OrderCard(order)
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderCard(
    order: Order
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween,
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Text(
                    text =
                        order.product_name
                            .ifBlank {
                                "Product"
                            },
                    style =
                        MaterialTheme.typography
                            .titleMedium,
                    fontWeight =
                        FontWeight.SemiBold,
                    modifier =
                        Modifier.weight(1f)
                )

                Text(
                    text =
                        order.status.ifBlank {
                            "Pending"
                        },
                    color = PrimaryGreen,
                    style =
                        MaterialTheme.typography
                            .labelLarge
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                "Quantity: ${order.quantity}"
            )

            Text(
                "Unit price: RM %.2f".format(
                    order.price
                )
            )

            Text(
                text =
                    "Total: RM %.2f".format(
                        order.price *
                                order.quantity
                    ),
                fontWeight = FontWeight.Bold
            )

            if (
                order.payment_method
                    .isNotBlank()
            ) {
                Text(
                    "Payment: ${order.payment_method}"
                )
            }
        }
    }
}