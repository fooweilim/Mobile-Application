package com.example.githubdemo.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.githubdemo.data.AppData
import com.example.githubdemo.ui.theme.GithubDemoTheme

@Composable
fun MarketScreen() {
    CommonPageScreen(
        eyebrow = "Fresh and affordable",
        title = "Market",

        subtitle =
            "Choose quality produce from nearby sellers.",

        searchPlaceholder =
            "Search products or categories...",

        sectionTitle = "Browse Market",
        features = AppData.marketFeatures
    )
}

@Preview(showBackground = true)
@Composable
fun MarketScreenPreview() {
    GithubDemoTheme(
        darkTheme = false,
        dynamicColor = false
    ) {
        MarketScreen()
    }
}