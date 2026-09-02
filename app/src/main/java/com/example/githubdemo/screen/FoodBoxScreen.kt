package com.example.githubdemo.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.githubdemo.data.AppData
import com.example.githubdemo.ui.theme.GithubDemoTheme

@Composable
fun FoodBoxScreen(
    onNavigate: (String) -> Unit = {}
) {
    CommonPageScreen(
        pageContent =
            AppData.foodBoxPageContent,

        onNavigate = onNavigate
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