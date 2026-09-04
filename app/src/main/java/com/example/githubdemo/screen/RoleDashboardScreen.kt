package com.example.githubdemo.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.githubdemo.data.AppData
import com.example.githubdemo.nav.farmer.FarmerNavGraph
import com.example.githubdemo.ui.theme.GithubDemoTheme

@Composable
fun FarmerDashboardScreen(
    onNavigate: (String) -> Unit = {}
) {
    val farmerNavController =
        rememberNavController()

    FarmerNavGraph(
        navController =
            farmerNavController,

        onSignOut = {
            onNavigate(
                AppData.ROLE_SELECTION_ROUTE
            )
        }
    )
}

@Composable
fun AdminDashboardScreen(
    onNavigate: (String) -> Unit = {}
) {
    CommonPageScreen(
        pageContent =
            AppData.adminPageContent,

        onNavigate =
            onNavigate
    )
}

@Preview(showBackground = true)
@Composable
fun FarmerDashboardPreview() {
    GithubDemoTheme(
        darkTheme = false,
        dynamicColor = false
    ) {
        FarmerDashboardScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun AdminDashboardPreview() {
    GithubDemoTheme(
        darkTheme = false,
        dynamicColor = false
    ) {
        AdminDashboardScreen()
    }
}