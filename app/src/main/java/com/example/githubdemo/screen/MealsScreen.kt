package com.example.githubdemo.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.githubdemo.data.AppData
import com.example.githubdemo.ui.theme.GithubDemoTheme

@Composable
fun MealsScreen(
    onNavigate: (String) -> Unit = {}
) {
    CommonPageScreen(
        pageContent =
            AppData.mealsPageContent,

        onNavigate = onNavigate
    )
}

@Preview(showBackground = true)
@Composable
fun MealsScreenPreview() {
    GithubDemoTheme(
        darkTheme = false,
        dynamicColor = false
    ) {
        MealsScreen()
    }
}