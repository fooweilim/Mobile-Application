package com.example.githubdemo.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.outlined.BakeryDining
import androidx.compose.material.icons.outlined.DinnerDining
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.FamilyRestroom
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocalFlorist
import androidx.compose.material.icons.outlined.LunchDining
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Redeem
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Storefront
import com.example.githubdemo.model.BottomNavItem
import com.example.githubdemo.model.PageFeature

object AppData {

    // Navigation route names
    const val HOME_ROUTE = "home"
    const val MARKET_ROUTE = "market"
    const val FOOD_BOX_ROUTE = "food_box"
    const val MEALS_ROUTE = "meals"
    const val PROFILE_ROUTE = "profile"

    val bottomNavigationItems = listOf(
        BottomNavItem(
            route = HOME_ROUTE,
            label = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),

        BottomNavItem(
            route = MARKET_ROUTE,
            label = "Market",
            selectedIcon = Icons.Filled.Storefront,
            unselectedIcon = Icons.Outlined.Storefront
        ),

        BottomNavItem(
            route = FOOD_BOX_ROUTE,
            label = "Food Box",
            selectedIcon = Icons.Filled.Redeem,
            unselectedIcon = Icons.Outlined.Redeem
        ),

        BottomNavItem(
            route = MEALS_ROUTE,
            label = "Meals",
            selectedIcon = Icons.Filled.Restaurant,
            unselectedIcon = Icons.Outlined.Restaurant
        ),

        BottomNavItem(
            route = PROFILE_ROUTE,
            label = "Profile",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person
        )
    )

    val homeFeatures = listOf(
        PageFeature(
            title = "Family Food Box",
            description =
                "Save up to 45% on fresh produce bundles.",
            icon = Icons.Outlined.Inventory2
        ),

        PageFeature(
            title = "Flash Deals",
            description =
                "Discover fresh food offers available today.",
            icon = Icons.Outlined.FlashOn
        ),

        PageFeature(
            title = "Favourites",
            description =
                "Open your saved products and meals.",
            icon = Icons.Outlined.FavoriteBorder
        )
    )

    val marketFeatures = listOf(
        PageFeature(
            title = "Fresh Vegetables",
            description =
                "Shop leafy greens and everyday vegetables.",
            icon = Icons.Outlined.Eco
        ),

        PageFeature(
            title = "Fresh Fruits",
            description =
                "Find seasonal fruits at affordable prices.",
            icon = Icons.Outlined.LocalFlorist
        ),

        PageFeature(
            title = "Near Expiry Deals",
            description =
                "Reduce food waste and enjoy lower prices.",
            icon = Icons.Outlined.Sell
        )
    )

    val foodBoxFeatures = listOf(
        PageFeature(
            title = "Family Food Box",
            description =
                "A weekly selection for the whole family.",
            icon = Icons.Outlined.FamilyRestroom
        ),

        PageFeature(
            title = "Student Food Box",
            description =
                "An affordable selection for student meals.",
            icon = Icons.Outlined.School
        ),

        PageFeature(
            title = "Healthy Food Box",
            description =
                "Nutritious produce for healthy eating.",
            icon = Icons.Outlined.HealthAndSafety
        )
    )

    val mealFeatures = listOf(
        PageFeature(
            title = "Breakfast Ideas",
            description =
                "Quick recipes to start your morning.",
            icon = Icons.Outlined.BakeryDining
        ),

        PageFeature(
            title = "Healthy Lunch",
            description =
                "Balanced lunch ideas using fresh produce.",
            icon = Icons.Outlined.LunchDining
        ),

        PageFeature(
            title = "Quick Dinner",
            description =
                "Easy dinner recipes for busy evenings.",
            icon = Icons.Outlined.DinnerDining
        )
    )

    val profileFeatures = listOf(
        PageFeature(
            title = "My Orders",
            description =
                "View current orders and purchase history.",
            icon = Icons.Outlined.ReceiptLong
        ),

        PageFeature(
            title = "My Favourites",
            description =
                "Open your saved products and recipes.",
            icon = Icons.Outlined.FavoriteBorder
        ),

        PageFeature(
            title = "Account Settings",
            description =
                "Manage your personal details and preferences.",
            icon = Icons.Outlined.Settings
        )
    )
}