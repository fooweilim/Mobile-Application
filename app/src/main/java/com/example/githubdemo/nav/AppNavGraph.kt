package com.example.githubdemo.nav

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.githubdemo.data.AppData
import com.example.githubdemo.data.RoleStorage
import com.example.githubdemo.data.UserRole
import com.example.githubdemo.screen.AdminDashboardScreen
import com.example.githubdemo.screen.AppBottomNavigationBar
import com.example.githubdemo.screen.FarmerDashboardScreen
import com.example.githubdemo.screen.FarmerSignUpScreen
import com.example.githubdemo.screen.FoodBoxScreen
import com.example.githubdemo.screen.HomeScreen
import com.example.githubdemo.screen.LoginScreen
import com.example.githubdemo.screen.MarketScreen
import com.example.githubdemo.screen.MealsScreen
import com.example.githubdemo.screen.ProfileScreen
import com.example.githubdemo.screen.RoleSelectionScreen
import com.example.githubdemo.screen.SignUpScreen

@Composable
fun AppNavGraph(
    navController: NavHostController =
        rememberNavController()
) {
    val context = LocalContext.current

    /*
     * Load the previously selected
     * and logged-in role.
     */
    val savedRole = remember {
        RoleStorage.getSelectedRole(
            context
        )
    }

    /*
     * If no role was saved, start from
     * the role selection screen.
     */
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
     * The bottom navigation is shown
     * only on Buyer pages.
     */
    val showBottomNavigation =
        AppData.buyerRoutes.contains(
            currentRoute
        )

    /*
     * Navigation used by the Buyer,
     * Farmer and Admin pages.
     */
    val onPageNavigate:
                (String) -> Unit = { route ->

        if (
            route ==
            AppData.ROLE_SELECTION_ROUTE
        ) {
            /*
             * Clear the logged-in role
             * when Change Role is selected.
             */
            RoleStorage.clearSelectedRole(
                context
            )

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
        } else {
            navController.navigate(
                route
            ) {
                launchSingleTop = true
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),

        bottomBar = {
            if (showBottomNavigation) {
                AppBottomNavigationBar(
                    currentRoute =
                        currentRoute,

                    onItemClick = { route ->

                        navController.navigate(
                            route
                        ) {
                            popUpTo(
                                AppData.HOME_ROUTE
                            ) {
                                saveState = true
                            }

                            launchSingleTop =
                                true

                            restoreState =
                                true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->

        NavHost(
            navController =
                navController,

            startDestination =
                startDestination,

            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            /*
             * ROLE SELECTION SCREEN
             */
            composable(
                AppData.ROLE_SELECTION_ROUTE
            ) {
                RoleSelectionScreen(
                    onRoleSelected = {
                            selectedRole ->

                        /*
                         * All three roles first
                         * navigate to Login.
                         */
                        navController.navigate(
                            AppData.getLoginRoute(
                                selectedRole
                            )
                        ) {
                            launchSingleTop =
                                true
                        }
                    }
                )
            }

            /*
             * LOGIN SCREEN
             *
             * Buyer, Farmer and Admin
             * can open this screen.
             */
            composable(
                route =
                    AppData.LOGIN_ROUTE,

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

                        /*
                         * Save the role only after
                         * the login is successful.
                         */
                        onLoginSuccess = {
                                loggedInRole ->

                            RoleStorage
                                .saveSelectedRole(
                                    context =
                                        context,

                                    userRole =
                                        loggedInRole
                                )

                            /*
                             * Open the correct page
                             * according to the role.
                             */
                            navController.navigate(
                                AppData
                                    .getRoleDestination(
                                        loggedInRole
                                    )
                            ) {
                                /*
                                 * Remove Role Selection
                                 * and Login from the stack.
                                 */
                                popUpTo(
                                    AppData
                                        .ROLE_SELECTION_ROUTE
                                ) {
                                    inclusive =
                                        true
                                }

                                launchSingleTop =
                                    true
                            }
                        },

                        /*
                         * Buyer and Farmer can
                         * navigate to Sign Up.
                         *
                         * Admin cannot Sign Up.
                         */
                        onSignUpClick = {
                                signUpRole ->

                            if (
                                UserRole.canSignUp(
                                    signUpRole
                                )
                            ) {
                                navController.navigate(
                                    AppData
                                        .getSignUpRoute(
                                            signUpRole
                                        )
                                )
                            }
                        },

                        /*
                         * Return to Role Selection.
                         */
                        onBackClick = {
                            navController
                                .popBackStack()
                        }
                    )
                }
            }

            /*
             * SIGN-UP ROUTE
             *
             * Buyer uses SignUpScreen.
             * Farmer uses FarmerSignUpScreen.
             * Admin is not included.
             */
            composable(
                route =
                    AppData.SIGN_UP_ROUTE,

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

                    /*
                     * BUYER SIGN UP
                     */
                    UserRole.BUYER -> {
                        SignUpScreen(
                            userRole =
                                UserRole.BUYER,

                            /*
                             * Return to Buyer Login
                             * after successful Sign Up.
                             */
                            onSignUpSuccess = {
                                navController
                                    .popBackStack()
                            },

                            /*
                             * Sign In text returns
                             * to Buyer Login.
                             */
                            onLoginClick = {
                                navController
                                    .popBackStack()
                            },

                            /*
                             * Back arrow returns
                             * to Buyer Login.
                             */
                            onBackClick = {
                                navController
                                    .popBackStack()
                            }
                        )
                    }

                    /*
                     * FARMER SIGN UP
                     *
                     * This opens the four-step
                     * Farmer registration:
                     *
                     * 1. Account
                     * 2. Farm Info
                     * 3. Documents
                     * 4. Review
                     */
                    UserRole.FARMER -> {
                        FarmerSignUpScreen(

                            /*
                             * After the Farmer submits
                             * the registration, return
                             * to Farmer Login.
                             */
                            onSignUpSuccess = {
                                navController
                                    .popBackStack()
                            },

                            /*
                             * Sign In text returns
                             * to Farmer Login.
                             */
                            onLoginClick = {
                                navController
                                    .popBackStack()
                            },

                            /*
                             * On the first registration
                             * step, the back arrow returns
                             * to Farmer Login.
                             *
                             * On later steps, the screen
                             * itself returns to the
                             * previous registration step.
                             */
                            onBackClick = {
                                navController
                                    .popBackStack()
                            }
                        )
                    }

                    /*
                     * There is intentionally no
                     * UserRole.ADMIN section.
                     *
                     * Admin cannot Sign Up.
                     */
                }
            }

            /*
             * BUYER HOME
             */
            composable(
                AppData.HOME_ROUTE
            ) {
                HomeScreen(
                    onNavigate =
                        onPageNavigate
                )
            }

            /*
             * BUYER MARKET
             */
            composable(
                AppData.MARKET_ROUTE
            ) {
                MarketScreen(
                    onNavigate =
                        onPageNavigate
                )
            }

            /*
             * BUYER FOOD BOX
             */
            composable(
                AppData.FOOD_BOX_ROUTE
            ) {
                FoodBoxScreen(
                    onNavigate =
                        onPageNavigate
                )
            }

            /*
             * BUYER MEALS
             */
            composable(
                AppData.MEALS_ROUTE
            ) {
                MealsScreen(
                    onNavigate =
                        onPageNavigate
                )
            }

            /*
             * BUYER PROFILE
             */
            composable(
                AppData.PROFILE_ROUTE
            ) {
                ProfileScreen(
                    onNavigate =
                        onPageNavigate
                )
            }

            /*
             * FARMER DASHBOARD
             */
            composable(
                AppData.FARMER_ROUTE
            ) {
                FarmerDashboardScreen(
                    onNavigate =
                        onPageNavigate
                )
            }

            /*
             * ADMIN DASHBOARD
             */
            composable(
                AppData.ADMIN_ROUTE
            ) {
                AdminDashboardScreen(
                    onNavigate =
                        onPageNavigate
                )
            }
        }
    }
}