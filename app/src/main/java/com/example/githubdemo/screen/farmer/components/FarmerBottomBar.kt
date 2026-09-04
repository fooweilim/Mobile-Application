package com.example.githubdemo.screen.farmer.components

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

private val FarmerNavigationGreen =
    Color(0xFF28785B)

private const val DASHBOARD_ROUTE =
    "dashboard"

private const val PRODUCTS_ROUTE =
    "products"

private const val ADD_PRODUCT_ROUTE =
    "addProduct"

private const val ORDERS_ROUTE =
    "orders"

private const val PROFILE_ROUTE =
    "profile"

@Composable
fun FarmerBottomBar(
    current: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor =
            Color.White,

        tonalElevation =
            5.dp
    ) {
        NavigationBarItem(
            selected =
                current ==
                        DASHBOARD_ROUTE,

            onClick = {
                onNavigate(
                    DASHBOARD_ROUTE
                )
            },

            icon = {
                Icon(
                    imageVector =
                        Icons.Default
                            .BarChart,

                    contentDescription =
                        "Dashboard"
                )
            },

            label = {
                Text(
                    text = "Dashboard"
                )
            }
        )

        NavigationBarItem(
            selected =
                current ==
                        PRODUCTS_ROUTE,

            onClick = {
                onNavigate(
                    PRODUCTS_ROUTE
                )
            },

            icon = {
                Icon(
                    imageVector =
                        Icons.Default
                            .Inventory,

                    contentDescription =
                        "Products"
                )
            },

            label = {
                Text(
                    text = "Products"
                )
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
                        ADD_PRODUCT_ROUTE
                    )
                },

                containerColor =
                    FarmerNavigationGreen,

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
                current ==
                        ORDERS_ROUTE,

            onClick = {
                onNavigate(
                    ORDERS_ROUTE
                )
            },

            icon = {
                Icon(
                    imageVector =
                        Icons.Default
                            .LocalShipping,

                    contentDescription =
                        "Orders"
                )
            },

            label = {
                Text(
                    text = "Orders"
                )
            }
        )

        NavigationBarItem(
            selected =
                current ==
                        PROFILE_ROUTE,

            onClick = {
                onNavigate(
                    PROFILE_ROUTE
                )
            },

            icon = {
                Icon(
                    imageVector =
                        Icons.Default
                            .Person,

                    contentDescription =
                        "Profile"
                )
            },

            label = {
                Text(
                    text = "Profile"
                )
            }
        )
    }
}