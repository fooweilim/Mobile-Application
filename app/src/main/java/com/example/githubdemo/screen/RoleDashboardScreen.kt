package com.example.githubdemo.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.githubdemo.data.AppData
import com.example.githubdemo.ui.theme.GithubDemoTheme

@Composable
fun FarmerDashboardScreen(
    onNavigate: (String) -> Unit = {}
) {
    CommonPageScreen(
        pageContent =
            AppData.farmerPageContent,

        onNavigate = onNavigate
    )
}

@Composable
fun AdminDashboardScreen(
    onNavigate: (String) -> Unit = {}
) {
    CommonPageScreen(
        pageContent =
            AppData.adminPageContent,

        onNavigate = onNavigate
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