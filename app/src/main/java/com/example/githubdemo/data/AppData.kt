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
import com.example.githubdemo.model.PageContent
import com.example.githubdemo.model.PageFeature

object AppData {

    const val ROLE_SELECTION_ROUTE =
        "role_selection"

    const val HOME_ROUTE =
        "home"

    const val MARKET_ROUTE =
        "market"

    const val FOOD_BOX_ROUTE =
        "food_box"

    const val MEALS_ROUTE =
        "meals"

    const val PROFILE_ROUTE =
        "profile"

    const val FARMER_ROUTE =
        "farmer_dashboard"

    const val ADMIN_ROUTE =
        "admin_dashboard"

    const val LOGIN_ROUTE =
        "login/{role}"

    const val SIGN_UP_ROUTE =
        "sign_up/{role}"

    const val FOOD_BOX_DETAIL_ROUTE =
        "food_box_detail/{planId}"

    const val FOOD_BOX_CUSTOMIZE_ROUTE =
        "food_box_customize"

    const val FOOD_BOX_SCHEDULE_ROUTE =
        "food_box_schedule"

    const val FOOD_BOX_CHECKOUT_ROUTE =
        "food_box_checkout"

    const val FOOD_BOX_SUCCESS_ROUTE =
        "food_box_success"

    const val FOOD_BOX_MANAGE_ROUTE =
        "food_box_manage"

    val foodBoxRoutes = listOf(
        FOOD_BOX_ROUTE,
        FOOD_BOX_DETAIL_ROUTE,
        FOOD_BOX_CUSTOMIZE_ROUTE,
        FOOD_BOX_SCHEDULE_ROUTE,
        FOOD_BOX_CHECKOUT_ROUTE,
        FOOD_BOX_SUCCESS_ROUTE,
        FOOD_BOX_MANAGE_ROUTE
    )

    val buyerRoutes = listOf(
        HOME_ROUTE,
        MARKET_ROUTE,
        MEALS_ROUTE,
        PROFILE_ROUTE
    ) + foodBoxRoutes

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

    private val homeFeatures = listOf(
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

    private val marketFeatures = listOf(
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

    private val foodBoxFeatures = listOf(
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

    private val mealFeatures = listOf(
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

    private val profileFeatures = listOf(
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
        ),
        PageFeature(
            title = "Change Role",
            description =
                "Return to the role selection page.",
            icon = Icons.Outlined.Home,
            route = ROLE_SELECTION_ROUTE
        )
    )

    private val farmerFeatures = listOf(
        PageFeature(
            title = "Manage Products",
            description =
                "Add and update farm products for buyers.",
            icon = Icons.Outlined.Eco
        ),
        PageFeature(
            title = "Manage Orders",
            description =
                "View and prepare customer orders.",
            icon = Icons.Outlined.ReceiptLong
        ),
        PageFeature(
            title = "Farm Verification",
            description =
                "Check identity and farm document status.",
            icon = Icons.Outlined.HealthAndSafety
        ),
        PageFeature(
            title = "Change Role",
            description =
                "Return to the role selection page.",
            icon = Icons.Outlined.Home,
            route = ROLE_SELECTION_ROUTE
        )
    )

    private val adminFeatures = listOf(
        PageFeature(
            title = "Manage Users",
            description =
                "Review buyer and farmer accounts.",
            icon = Icons.Outlined.Person
        ),
        PageFeature(
            title = "Manage Farmers",
            description =
                "Review farmer verification information.",
            icon = Icons.Outlined.Eco
        ),
        PageFeature(
            title = "Manage Orders",
            description =
                "Monitor orders placed on the platform.",
            icon = Icons.Outlined.ReceiptLong
        ),
        PageFeature(
            title = "Platform Settings",
            description =
                "Manage general platform preferences.",
            icon = Icons.Outlined.Settings
        ),
        PageFeature(
            title = "Change Role",
            description =
                "Return to the role selection page.",
            icon = Icons.Outlined.Home,
            route = ROLE_SELECTION_ROUTE
        )
    )

    val homePageContent = PageContent(
        eyebrow = "Good morning 👋",
        title = "Siti Aminah",
        subtitle = "Kuala Lumpur, 50450",
        searchPlaceholder =
            "Search fresh produce...",
        sectionTitle = "Quick Access",
        features = homeFeatures
    )

    val marketPageContent = PageContent(
        eyebrow = "Fresh and affordable",
        title = "Market",
        subtitle =
            "Choose quality produce from nearby sellers.",
        searchPlaceholder =
            "Search products or categories...",
        sectionTitle = "Browse Market",
        features = marketFeatures
    )

    val foodBoxPageContent = PageContent(
        eyebrow = "Weekly value bundles",
        title = "Food Box",
        subtitle =
            "Subscribe to a box that fits your lifestyle.",
        searchPlaceholder =
            "Search food boxes...",
        sectionTitle = "Choose Your Box",
        features = foodBoxFeatures
    )

    val mealsPageContent = PageContent(
        eyebrow = "Cook with less waste",
        title = "Meals",
        subtitle =
            "Simple recipes using affordable ingredients.",
        searchPlaceholder =
            "Search recipes or ingredients...",
        sectionTitle = "Meal Ideas",
        features = mealFeatures
    )

    val profilePageContent = PageContent(
        eyebrow = "Welcome back",
        title = "My Profile",
        subtitle =
            "Siti Aminah • Kuala Lumpur",
        searchPlaceholder =
            "Search profile options...",
        sectionTitle = "My Account",
        features = profileFeatures
    )

    val farmerPageContent = PageContent(
        eyebrow = "Sell directly to families",
        title = "Farmer Dashboard",
        subtitle =
            "Manage products, verification, and customer orders.",
        searchPlaceholder =
            "Search farmer options...",
        sectionTitle = "Farmer Management",
        features = farmerFeatures
    )

    val adminPageContent = PageContent(
        eyebrow = "Platform management",
        title = "Admin Dashboard",
        subtitle =
            "Keep HarvestLink safe, reliable, and organised.",
        searchPlaceholder =
            "Search admin options...",
        sectionTitle = "Admin Management",
        features = adminFeatures
    )

    fun getRoleDestination(
        userRole: String
    ): String {
        return when (userRole) {
            UserRole.BUYER ->
                HOME_ROUTE

            UserRole.FARMER ->
                FARMER_ROUTE

            UserRole.ADMIN ->
                ADMIN_ROUTE

            else ->
                ROLE_SELECTION_ROUTE
        }
    }

    fun getLoginRoute(
        userRole: String
    ): String {
        return "login/$userRole"
    }

    fun getSignUpRoute(
        userRole: String
    ): String {
        return "sign_up/$userRole"
    }

    fun getFoodBoxDetailRoute(
        planId: String
    ): String {
        return "food_box_detail/$planId"
    }
}