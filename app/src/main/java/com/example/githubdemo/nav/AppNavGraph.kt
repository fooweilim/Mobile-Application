package com.example.githubdemo.nav

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.githubdemo.data.AppData
import com.example.githubdemo.data.UserRole
import com.example.githubdemo.data.local.LocalAccountStorage
import com.example.githubdemo.screen.AdminDashboardScreen
import com.example.githubdemo.screen.AppBottomNavigationBar
import com.example.githubdemo.screen.FarmerDashboardScreen
import com.example.githubdemo.screen.FoodBoxScreen
import com.example.githubdemo.screen.HomeScreen
import com.example.githubdemo.screen.MarketScreen
import com.example.githubdemo.screen.MealsScreen
import com.example.githubdemo.screen.RoleSelectionScreen
import com.example.githubdemo.screen.authentication.FarmerSignUpScreen
import com.example.githubdemo.screen.authentication.LoginScreen
import com.example.githubdemo.screen.authentication.SignUpScreen
import com.example.githubdemo.screen.userprofile.ProfileScreen
import com.example.githubdemo.viewmodel.authentication.AuthViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController =
        rememberNavController()
) {
    val context = LocalContext.current

    val authViewModel: AuthViewModel =
        viewModel()

    /*
     * Check local storage.
     *
     * If a user has already logged in, the app
     * opens the correct dashboard.
     *
     * Otherwise, the role selection screen opens.
     */
    val savedRole = remember {
        LocalAccountStorage.getSelectedRole(
            context
        )
    }

    val startDestination =
        if (savedRole == null) {
            AppData.ROLE_SELECTION_ROUTE
        } else {
            AppData.getRoleDestination(
                savedRole
            )
        }

    val navBackStackEntry by
    navController
        .currentBackStackEntryAsState()

    val currentRoute =
        navBackStackEntry
            ?.destination
            ?.route

    /*
     * The buyer bottom navigation only appears
     * on buyer pages.
     *
     * It does not appear on:
     * - role selection
     * - login
     * - signup
     * - farmer dashboard
     * - admin dashboard
     */
    val showBottomNavigation =
        AppData.buyerRoutes.contains(
            currentRoute
        )

    /*
     * Used by Home, Market, Food Box,
     * Meals and Profile screens.
     */
    val onPageNavigate: (String) -> Unit = {
            route ->

        if (
            route ==
            AppData.ROLE_SELECTION_ROUTE
        ) {
            /*
             * When the user presses Logout,
             * sign out from Supabase and clear
             * the local account information.
             */
            authViewModel.signOut {
                navController.navigate(
                    AppData.ROLE_SELECTION_ROUTE
                ) {
                    popUpTo(
                        navController
                            .graph
                            .startDestinationId
                    ) {
                        inclusive = true
                    }

                    launchSingleTop = true
                }
            }
        } else {
            navController.navigate(route) {
                launchSingleTop = true
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),

        bottomBar = {
            if (showBottomNavigation) {
                AppBottomNavigationBar(
                    currentRoute = currentRoute,

                    onItemClick = { route ->
                        navController.navigate(
                            route
                        ) {
                            popUpTo(
                                AppData.HOME_ROUTE
                            ) {
                                saveState = true
                            }

                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = startDestination,

            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            /*
             * Role selection
             */
            composable(
                route =
                    AppData.ROLE_SELECTION_ROUTE
            ) {
                RoleSelectionScreen(
                    onRoleSelected = {
                            selectedRole ->

                        navController.navigate(
                            AppData.getLoginRoute(
                                selectedRole
                            )
                        ) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            /*
             * Login route
             *
             * Example routes:
             * login/buyer
             * login/farmer
             * login/admin
             */
            composable(
                route = AppData.LOGIN_ROUTE,

                arguments = listOf(
                    navArgument("role") {
                        type =
                            NavType.StringType
                    }
                )
            ) { backStackEntry ->

                val userRole =
                    backStackEntry
                        .arguments
                        ?.getString("role")

                if (
                    userRole != null &&
                    UserRole.isValidRole(
                        userRole
                    )
                ) {
                    LoginScreen(
                        userRole = userRole,

                        onLoginSuccess = {
                                loggedInRole ->

                            navController.navigate(
                                AppData
                                    .getRoleDestination(
                                        loggedInRole
                                    )
                            ) {
                                /*
                                 * Remove login and role
                                 * selection after login.
                                 */
                                popUpTo(
                                    AppData
                                        .ROLE_SELECTION_ROUTE
                                ) {
                                    inclusive = true
                                }

                                launchSingleTop = true
                            }
                        },

                        onSignUpClick = {
                                signUpRole ->

                            /*
                             * Admin is not allowed
                             * to create an account.
                             */
                            if (
                                signUpRole !=
                                UserRole.ADMIN
                            ) {
                                navController.navigate(
                                    AppData
                                        .getSignUpRoute(
                                            signUpRole
                                        )
                                )
                            } else {
                                authViewModel
                                    .showErrorMessage(
                                        "Admin accounts cannot sign up."
                                    )
                            }
                        },

                        onBackClick = {
                            authViewModel
                                .clearMessage()

                            navController
                                .popBackStack()
                        },

                        authViewModel =
                            authViewModel
                    )
                }
            }

            /*
             * Signup route
             *
             * Buyer:
             * Uses the normal SignUpScreen.
             *
             * Farmer:
             * Uses the four-step
             * FarmerSignUpScreen.
             *
             * Admin:
             * Cannot access signup.
             */
            composable(
                route = AppData.SIGN_UP_ROUTE,

                arguments = listOf(
                    navArgument("role") {
                        type =
                            NavType.StringType
                    }
                )
            ) { backStackEntry ->

                val userRole =
                    backStackEntry
                        .arguments
                        ?.getString("role")

                when (userRole) {
                    UserRole.BUYER -> {
                        SignUpScreen(
                            userRole =
                                UserRole.BUYER,

                            onSignUpSuccess = {
                                authViewModel
                                    .clearMessage()

                                navController
                                    .popBackStack()
                            },

                            onLoginClick = {
                                authViewModel
                                    .clearMessage()

                                navController
                                    .popBackStack()
                            },

                            onBackClick = {
                                authViewModel
                                    .clearMessage()

                                navController
                                    .popBackStack()
                            },

                            authViewModel =
                                authViewModel
                        )
                    }

                    UserRole.FARMER -> {
                        FarmerSignUpScreen(
                            onSignUpSuccess = {
                                authViewModel
                                    .clearMessage()

                                navController
                                    .popBackStack()
                            },

                            onLoginClick = {
                                authViewModel
                                    .clearMessage()

                                navController
                                    .popBackStack()
                            },

                            onBackClick = {
                                authViewModel
                                    .clearMessage()

                                navController
                                    .popBackStack()
                            },

                            authViewModel =
                                authViewModel
                        )
                    }

                    /*
                     * Admin and invalid roles are
                     * sent back to the previous page.
                     */
                    else -> {
                        LaunchedEffect(userRole) {
                            authViewModel
                                .showErrorMessage(
                                    "Admin accounts cannot sign up."
                                )

                            navController
                                .popBackStack()
                        }
                    }
                }
            }

            /*
             * Buyer Home
             */
            composable(
                route = AppData.HOME_ROUTE
            ) {
                HomeScreen(
                    onNavigate =
                        onPageNavigate
                )
            }

            /*
             * Buyer Market
             */
            composable(
                route = AppData.MARKET_ROUTE
            ) {
                MarketScreen(
                    onNavigate =
                        onPageNavigate
                )
            }

            /*
             * Buyer Food Box
             */
            composable(
                route =
                    AppData.FOOD_BOX_ROUTE
            ) {
                FoodBoxScreen(
                    onNavigate =
                        onPageNavigate
                )
            }

            /*
             * Buyer Meals
             */
            composable(
                route = AppData.MEALS_ROUTE
            ) {
                MealsScreen(
                    onNavigate =
                        onPageNavigate
                )
            }

            /*
             * Buyer Profile
             */
            composable(
                route = AppData.PROFILE_ROUTE
            ) {
                ProfileScreen(
                    onNavigate =
                        onPageNavigate
                )
            }

            /*
             * Farmer Dashboard
             */
            composable(
                route = AppData.FARMER_ROUTE
            ) {
                FarmerDashboardScreen(
                    onNavigate =
                        onPageNavigate
                )
            }

            /*
             * Admin Dashboard
             */
            composable(
                route = AppData.ADMIN_ROUTE
            ) {
                AdminDashboardScreen(
                    onNavigate =
                        onPageNavigate
                )
            }
        }
    }
}