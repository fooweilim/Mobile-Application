package com.example.githubdemo.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.githubdemo.data.AppData
import com.example.githubdemo.ui.theme.GithubDemoTheme

@Composable
fun HomeScreen() {
    CommonPageScreen(
        eyebrow = "Good morning 👋",
        title = "Siti Aminah",
        subtitle = "Kuala Lumpur, 50450",

        searchPlaceholder =
            "Search fresh produce...",

        sectionTitle = "Quick Access",
        features = AppData.homeFeatures
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    GithubDemoTheme(
        darkTheme = false,
        dynamicColor = false
    ) {
        HomeScreen()
    }
}