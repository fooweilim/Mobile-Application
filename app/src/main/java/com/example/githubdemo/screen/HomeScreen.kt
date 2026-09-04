package com.example.githubdemo.screen

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.githubdemo.data.AppData
import com.example.githubdemo.location.LocationHelper
import com.example.githubdemo.screen.userprofile.BuyerProfileEntry
import com.example.githubdemo.ui.theme.PrimaryGreen
import com.example.githubdemo.viewmodel.homepage.HomeViewModel
import com.example.githubdemo.viewmodel.market.CartViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit = {},
    cartViewModel: CartViewModel = viewModel(),
    homeViewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val products by homeViewModel.products.collectAsState()
    val userName by homeViewModel.userName.collectAsState()

    var locationText by remember {
        mutableStateOf("Getting location...")
    }

    fun loadLocation() {
        scope.launch {
            locationText = try {
                LocationHelper.getCurrentLocation(context)
            } catch (exception: Exception) {
                "Location unavailable"
            }
        }
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                loadLocation()
            } else {
                locationText = "Location unavailable"
            }
        }

    LaunchedEffect(Unit) {
        homeViewModel.refresh()

        if (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            loadLocation()
        } else {
            permissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6EE)),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Surface(
                color = PrimaryGreen,
                shape = RoundedCornerShape(
                    bottomStart = 35.dp,
                    bottomEnd = 35.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 35.dp,
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 25.dp
                        )
                ) {
                    Text(
                        text = "Welcome Back 🌱",
                        color = Color.White
                    )

                    Text(
                        text = userName,
                        color = Color.White,
                        style =
                            MaterialTheme.typography.headlineMedium
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = "📍 $locationText",
                        color = Color.White
                    )
                }
            }
        }

        item {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFF9B62)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "✨ This Week Only",
                            color = Color.White
                        )

                        Text(
                            text = "Family Food Box",
                            color = Color.White,
                            style =
                                MaterialTheme.typography.headlineSmall
                        )

                        Text(
                            text =
                                "Fresh produce bundle for your family",
                            color = Color.White
                        )

                        Spacer(Modifier.height(15.dp))

                        Button(
                            onClick = {
                                onNavigate(
                                    AppData.FOOD_BOX_ROUTE
                                )
                            },
                            modifier = Modifier
                                .height(40.dp)
                                .width(150.dp),
                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor =
                                        PrimaryGreen
                                ),
                            shape =
                                RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Subscribe Now",
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    text = "Quick Access",
                    color = PrimaryGreen,
                    style =
                        MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.SpaceAround
                ) {
                    HomeShortcut(
                        title = "Favourite",
                        icon =
                            Icons.Default.FavoriteBorder
                    ) {
                        onNavigate("meal_favourites")
                    }

                    HomeShortcut(
                        title = "Meals",
                        icon = Icons.Default.Fastfood
                    ) {
                        onNavigate(AppData.MEALS_ROUTE)
                    }

                    HomeShortcut(
                        title = "Orders",
                        icon = Icons.Default.Inventory2
                    ) {
                        BuyerProfileEntry.openOrders()
                        onNavigate(AppData.PROFILE_ROUTE)
                    }
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    text = "⚡ Flash Deals",
                    color = PrimaryGreen,
                    style =
                        MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(10.dp))

                if (products.isEmpty()) {
                    Text(
                        text =
                            "No flash deals available.",
                        color = Color.Gray
                    )
                } else {
                    LazyRow(
                        horizontalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {
                        items(products.take(2)) { product ->
                            ProductDealCard(
                                name = product.name,
                                price =
                                    "RM %.2f".format(
                                        product.price
                                    ),
                                image =
                                    product.image_url
                                        .orEmpty(),
                                onAddCart = {
                                    product.id?.let(
                                        cartViewModel::addToCart
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
private fun HomeShortcut(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Surface(
            onClick = onClick,
            modifier = Modifier.size(55.dp),
            shape = RoundedCornerShape(18.dp),
            color = Color(0xFFDFF5E8)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = PrimaryGreen,
                modifier = Modifier.padding(12.dp)
            )
        }

        Text(text = title)
    }
}

@Composable
private fun ProductDealCard(
    name: String,
    price: String,
    image: String,
    onAddCart: () -> Unit
) {
    Card(
        modifier = Modifier.width(160.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column {
            AsyncImage(
                model = image,
                contentDescription = name,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    text = name,
                    maxLines = 1
                )

                Text(
                    text = price,
                    color = PrimaryGreen
                )

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = onAddCart,
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                PrimaryGreen
                        ),
                    shape =
                        RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Add Cart",
                        color = Color.White
                    )
                }
            }
        }
    }
}