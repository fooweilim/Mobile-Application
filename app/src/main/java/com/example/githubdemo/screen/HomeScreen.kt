package com.example.githubdemo.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.githubdemo.data.AppData
import com.example.githubdemo.ui.theme.GithubDemoTheme

@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit = {}
) {
    CommonPageScreen(
        pageContent =
            AppData.homePageContent,

        onNavigate = onNavigate
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