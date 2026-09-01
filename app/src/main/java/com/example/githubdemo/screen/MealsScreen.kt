package com.example.githubdemo.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.githubdemo.data.AppData
import com.example.githubdemo.ui.theme.GithubDemoTheme

@Composable
fun MealsScreen() {
    CommonPageScreen(
        eyebrow = "Cook with less waste",
        title = "Meals",

        subtitle =
            "Simple recipes using affordable ingredients.",

        searchPlaceholder =
            "Search recipes or ingredients...",

        sectionTitle = "Meal Ideas",
        features = AppData.mealFeatures
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