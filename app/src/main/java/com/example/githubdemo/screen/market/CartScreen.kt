package com.example.githubdemo.screen.market

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.githubdemo.data.market.SelectedCartManager
import com.example.githubdemo.ui.theme.PrimaryGreen
import com.example.githubdemo.viewmodel.market.CartViewModel
import com.example.githubdemo.viewmodel.market.MarketViewModel

@Composable
fun CartScreen(
    onBack: () -> Unit = {},
    onCheckout: () -> Unit = {},
    cartViewModel: CartViewModel = viewModel(),
    marketViewModel: MarketViewModel = viewModel()
) {
    val cartItems by cartViewModel.cartProducts.collectAsState()

    val recommendations = marketViewModel.getRecommendation(
        cartItems.map { it.productId }.toSet()
    )

    var selectedIds by remember {
        mutableStateOf(emptySet<String>())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6EE))
    ) {
        Surface(
            color = PrimaryGreen,
            shape = RoundedCornerShape(
                bottomStart = 35.dp,
                bottomEnd = 35.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 35.dp,
                        start = 10.dp,
                        end = 10.dp,
                        bottom = 20.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                Text(
                    "Cart",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        ) {
            if (cartItems.isEmpty()) {
                item {
                    Text(
                        "Your cart is empty",
                        modifier = Modifier.padding(20.dp)
                    )
                }
            } else {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selectedIds.size == cartItems.size,
                            onCheckedChange = { checked ->
                                selectedIds =
                                    if (checked) {
                                        cartItems.map {
                                            it.productId
                                        }.toSet()
                                    } else {
                                        emptySet()
                                    }
                            }
                        )

                        Text(
                            "Select All",
                            modifier = Modifier.weight(1f)
                        )

                        if (selectedIds.isNotEmpty()) {
                            TextButton(
                                onClick = {
                                    val deleteItems =
                                        cartItems.filter {
                                            selectedIds.contains(
                                                it.productId
                                            )
                                        }

                                    cartViewModel.deleteSelected(
                                        deleteItems
                                    )

                                    selectedIds = emptySet()
                                }
                            ) {
                                Text(
                                    "Delete",
                                    color = Color.Red
                                )
                            }
                        }
                    }
                }

                items(cartItems) { item ->
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
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = selectedIds.contains(
                                    item.productId
                                ),
                                onCheckedChange = { checked ->
                                    selectedIds =
                                        if (checked) {
                                            selectedIds + item.productId
                                        } else {
                                            selectedIds - item.productId
                                        }
                                }
                            )

                            AsyncImage(
                                model = item.product.image_url,
                                contentDescription = item.product.name,
                                modifier = Modifier.size(85.dp)
                            )

                            Spacer(
                                Modifier.width(12.dp)
                            )

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    item.product.name,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    item.product.category,
                                    color = Color.Gray
                                )

                                Text(
                                    "RM %.2f".format(
                                        item.product.price
                                    ),
                                    color = PrimaryGreen
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = {
                                            cartViewModel.decrease(item)
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.Remove,
                                            contentDescription = null
                                        )
                                    }

                                    Text(
                                        item.quantity.toString()
                                    )

                                    IconButton(
                                        onClick = {
                                            cartViewModel.increase(item)
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.Add,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }

                            IconButton(
                                onClick = {
                                    cartViewModel.delete(item)
                                    selectedIds =
                                        selectedIds - item.productId
                                }
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
            }
            item {
                Spacer(
                    Modifier.height(20.dp)
                )

                Text(
                    "You May Also Like",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(
                    Modifier.height(10.dp)
                )

                if (recommendations.isEmpty()) {
                    Text(
                        "No recommendation available",
                        color = Color.Gray
                    )
                } else {
                    recommendations.forEach { product ->
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
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = product.image_url,
                                    contentDescription = product.name,
                                    modifier = Modifier.size(70.dp)
                                )

                                Spacer(
                                    Modifier.width(12.dp)
                                )

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        product.name,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Text(
                                        "RM %.2f".format(
                                            product.price
                                        ),
                                        color = PrimaryGreen
                                    )
                                }

                                Button(
                                    onClick = {
                                        product.id?.let {
                                            cartViewModel.addToCart(it)
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = PrimaryGreen
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        "Add",
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(
                topStart = 25.dp,
                topEnd = 25.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                val total =
                    cartItems
                        .filter {
                            selectedIds.contains(
                                it.productId
                            )
                        }
                        .sumOf {
                            it.product.price * it.quantity
                        }

                Text(
                    "Total: RM %.2f".format(total),
                    color = Color.Black,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(
                    Modifier.height(10.dp)
                )

                Button(
                    onClick = {
                        if (selectedIds.isNotEmpty()) {

                            val checkoutItems =
                                cartItems.filter {
                                    selectedIds.contains(
                                        it.productId
                                    )
                                }

                            SelectedCartManager.saveSelectedCart(
                                checkoutItems
                            )

                            onCheckout()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen
                    )
                ) {
                    Text(
                        "Checkout",
                        color = Color.White
                    )
                }
            }
        }
    }
}
