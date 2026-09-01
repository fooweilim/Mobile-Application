package com.example.githubdemo.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.githubdemo.data.AppData
import com.example.githubdemo.ui.theme.GithubDemoTheme

@Composable
fun ProfileScreen() {
    CommonPageScreen(
        eyebrow = "Welcome back",
        title = "My Profile",
        subtitle = "Siti Aminah • Kuala Lumpur",

        searchPlaceholder =
            "Search profile options...",

        sectionTitle = "My Account",
        features = AppData.profileFeatures
    )
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    GithubDemoTheme(
        darkTheme = false,
        dynamicColor = false
    ) {
        ProfileScreen()
    }
}