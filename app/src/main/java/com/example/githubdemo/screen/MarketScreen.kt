package com.example.githubdemo.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.githubdemo.data.AppData
import com.example.githubdemo.ui.theme.GithubDemoTheme

@Composable
fun MarketScreen(
    onNavigate: (String) -> Unit = {}
) {
    CommonPageScreen(
        pageContent =
            AppData.marketPageContent,

        onNavigate = onNavigate
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