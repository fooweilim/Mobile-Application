package com.example.githubdemo.nav

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.githubdemo.data.AppData
import com.example.githubdemo.screen.AppBottomNavigationBar
import com.example.githubdemo.screen.FoodBoxScreen
import com.example.githubdemo.screen.HomeScreen
import com.example.githubdemo.screen.MarketScreen
import com.example.githubdemo.screen.MealsScreen
import com.example.githubdemo.screen.ProfileScreen


@Composable
fun AppNavGraph(
    navController: NavHostController =
        rememberNavController()
) {
    val navBackStackEntry by
    navController.currentBackStackEntryAsState()

    val currentRoute =
        navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),

        bottomBar = {
            AppBottomNavigationBar(
                currentRoute = currentRoute,

                onItemClick = { route ->
                    navController.navigate(route) {

                        popUpTo(AppData.HOME_ROUTE) {
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = AppData.HOME_ROUTE,

            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(AppData.HOME_ROUTE) {
                HomeScreen()
            }

            composable(AppData.MARKET_ROUTE) {
                MarketScreen()
            }

            composable(AppData.FOOD_BOX_ROUTE) {
                FoodBoxScreen()
            }

            composable(AppData.MEALS_ROUTE) {
                MealsScreen()
            }

            composable(AppData.PROFILE_ROUTE) {
                ProfileScreen()
            }
        }
    }
}