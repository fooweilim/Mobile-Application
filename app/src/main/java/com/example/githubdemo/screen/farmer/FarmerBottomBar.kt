package com.example.githubdemo.screen.farmer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.githubdemo.nav.FarmerRoute

private val FarmerGreen =
    Color(0xFF28785B)

@Composable
fun FarmerBottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 5.dp
    ) {
        NavigationBarItem(
            selected =
                currentRoute ==
                        FarmerRoute.DASHBOARD,

            onClick = {
                onNavigate(
                    FarmerRoute.DASHBOARD
                )
            },

            icon = {
                Icon(
                    imageVector =
                        Icons.Default.BarChart,

                    contentDescription =
                        "Dashboard"
                )
            },

            label = {
                Text("Dashboard")
            }
        )

        NavigationBarItem(
            selected =
                currentRoute ==
                        FarmerRoute.PRODUCTS,

            onClick = {
                onNavigate(
                    FarmerRoute.PRODUCTS
                )
            },

            icon = {
                Icon(
                    imageVector =
                        Icons.Default.Inventory,

                    contentDescription =
                        "Products"
                )
            },

            label = {
                Text("Products")
            }
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(80.dp),

            contentAlignment =
                Alignment.Center
        ) {
            FloatingActionButton(
                onClick = {
                    onNavigate(
                        FarmerRoute.ADD_PRODUCT
                    )
                },

                containerColor =
                    FarmerGreen,

                contentColor =
                    Color.White
            ) {
                Icon(
                    imageVector =
                        Icons.Default.Add,

                    contentDescription =
                        "Add Product"
                )
            }
        }

        NavigationBarItem(
            selected =
                currentRoute ==
                        FarmerRoute.ORDERS,

            onClick = {
                onNavigate(
                    FarmerRoute.ORDERS
                )
            },

            icon = {
                Icon(
                    imageVector =
                        Icons.Default.LocalShipping,

                    contentDescription =
                        "Orders"
                )
            },

            label = {
                Text("Orders")
            }
        )

        NavigationBarItem(
            selected =
                currentRoute ==
                        FarmerRoute.PROFILE,

            onClick = {
                onNavigate(
                    FarmerRoute.PROFILE
                )
            },

            icon = {
                Icon(
                    imageVector =
                        Icons.Default.Person,

                    contentDescription =
                        "Profile"
                )
            },

            label = {
                Text("Profile")
            }
        )
    }
}