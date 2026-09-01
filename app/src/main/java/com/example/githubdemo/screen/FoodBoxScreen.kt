package com.example.githubdemo.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.githubdemo.data.AppData
import com.example.githubdemo.ui.theme.GithubDemoTheme

@Composable
fun FoodBoxScreen() {
    CommonPageScreen(
        eyebrow = "Weekly value bundles",
        title = "Food Box",

        subtitle =
            "Subscribe to a box that fits your lifestyle.",

        searchPlaceholder = "Search food boxes...",
        sectionTitle = "Choose Your Box",
        features = AppData.foodBoxFeatures
    )
}

@Preview(showBackground = true)
@Composable
fun FoodBoxScreenPreview() {
    GithubDemoTheme(
        darkTheme = false,
        dynamicColor = false
    ) {
        FoodBoxScreen()
    }
}