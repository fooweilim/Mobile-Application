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
import com.example.githubdemo.data.meals.MealData
import com.example.githubdemo.screen.AdminDashboardScreen
import com.example.githubdemo.screen.AppBottomNavigationBar
import com.example.githubdemo.screen.FarmerDashboardScreen
import com.example.githubdemo.screen.HomeScreen
import com.example.githubdemo.screen.MealDetailScreen
import com.example.githubdemo.screen.RoleSelectionScreen
import com.example.githubdemo.screen.authentication.FarmerSignUpScreen
import com.example.githubdemo.screen.authentication.LoginScreen
import com.example.githubdemo.screen.authentication.SignUpScreen
import com.example.githubdemo.screen.foodbox.CustomizeFoodBoxScreen
import com.example.githubdemo.screen.foodbox.DeliveryScheduleScreen
import com.example.githubdemo.screen.foodbox.FoodBoxCheckoutScreen
import com.example.githubdemo.screen.foodbox.FoodBoxDetailScreen
import com.example.githubdemo.screen.foodbox.FoodBoxPlansScreen
import com.example.githubdemo.screen.foodbox.ManageSubscriptionScreen
import com.example.githubdemo.screen.foodbox.SubscriptionSuccessScreen
import com.example.githubdemo.screen.market.CartScreen
import com.example.githubdemo.screen.market.MarketPaymentScreen
import com.example.githubdemo.screen.market.MarketScreen
import com.example.githubdemo.screen.meals.FavouriteScreen
import com.example.githubdemo.screen.meals.MealsScreen
import com.example.githubdemo.screen.userprofile.ProfileScreen
import com.example.githubdemo.viewmodel.authentication.AuthViewModel
import com.example.githubdemo.viewmodel.foodbox.FoodBoxViewModel
import com.example.githubdemo.viewmodel.market.CartViewModel

private const val ROLE_ARGUMENT =
    "role"

private const val PLAN_ID_ARGUMENT =
    "planId"

private const val CART_ROUTE =
    "cart"

private const val PAYMENT_ROUTE =
    "payment"

private const val MEAL_FAVOURITES_ROUTE =
    "meal_favourites"

private const val MEAL_ID_ARGUMENT =
    "mealId"

private const val MEAL_DETAIL_ROUTE =
    "meal_detail/{mealId}"

private fun getMealDetailRoute(
    mealId: Int
): String {
    return "meal_detail/$mealId"
}

@Composable
fun AppNavGraph(
    navController: NavHostController =
        rememberNavController()
) {
    val context = LocalContext.current

    val authViewModel: AuthViewModel =
        viewModel()

    val cartViewModel: CartViewModel =
        viewModel()

    val foodBoxViewModel: FoodBoxViewModel =
        viewModel()

    val savedRole = remember {
        LocalAccountStorage
            .getSelectedRole(context)
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

    val isMealSubPage =
        currentRoute == MEAL_FAVOURITES_ROUTE ||
                currentRoute == MEAL_DETAIL_ROUTE

    val isFoodBoxPage =
        currentRoute != null &&
                currentRoute in AppData.foodBoxRoutes

    val selectedBottomRoute =
        when {
            isMealSubPage ->
                AppData.MEALS_ROUTE

            isFoodBoxPage ->
                AppData.FOOD_BOX_ROUTE

            else ->
                currentRoute
        }

    val showBottomNavigation =
        currentRoute in AppData.buyerRoutes ||
                isMealSubPage

    val onPageNavigate: (String) -> Unit = { route ->

        if (
            route ==
            AppData.ROLE_SELECTION_ROUTE
        ) {
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
                    currentRoute =
                        selectedBottomRoute,

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
            startDestination =
                startDestination,

            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
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

            composable(
                route = AppData.LOGIN_ROUTE,

                arguments = listOf(
                    navArgument(
                        ROLE_ARGUMENT
                    ) {
                        type =
                            NavType.StringType
                    }
                )
            ) { backStackEntry ->

                val userRole =
                    backStackEntry
                        .arguments
                        ?.getString(
                            ROLE_ARGUMENT
                        )

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

            composable(
                route =
                    AppData.SIGN_UP_ROUTE,

                arguments = listOf(
                    navArgument(
                        ROLE_ARGUMENT
                    ) {
                        type =
                            NavType.StringType
                    }
                )
            ) { backStackEntry ->

                val userRole =
                    backStackEntry
                        .arguments
                        ?.getString(
                            ROLE_ARGUMENT
                        )

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

                    else -> {
                        LaunchedEffect(
                            userRole
                        ) {
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

            composable(
                route = AppData.HOME_ROUTE
            ) {
                HomeScreen(
                    onNavigate =
                        onPageNavigate
                )
            }

            composable(
                route = AppData.MARKET_ROUTE
            ) {
                MarketScreen(
                    onNavigate = { route ->
                        navController.navigate(
                            route
                        )
                    },

                    cartViewModel =
                        cartViewModel
                )
            }

            composable(
                route = CART_ROUTE
            ) {
                CartScreen(
                    onBack = {
                        navController
                            .popBackStack()
                    },

                    onCheckout = {
                        navController.navigate(
                            PAYMENT_ROUTE
                        )
                    },

                    cartViewModel =
                        cartViewModel
                )
            }

            composable(
                route = PAYMENT_ROUTE
            ) {
                MarketPaymentScreen(
                    onBack = {
                        navController
                            .popBackStack()
                    },

                    onPaymentSuccess = {
                        navController.navigate(
                            AppData.MARKET_ROUTE
                        ) {
                            popUpTo(
                                AppData.MARKET_ROUTE
                            ) {
                                inclusive = true
                            }
                        }
                    },

                    cartViewModel =
                        cartViewModel
                )
            }
            composable(
                route =
                    AppData.FOOD_BOX_ROUTE
            ) {
                val userId =
                    LocalAccountStorage
                        .getProfile(context)
                        ?.id

                LaunchedEffect(userId) {
                    foodBoxViewModel
                        .refreshActiveSubscription(
                            userId
                        )
                }

                FoodBoxPlansScreen(
                    foodBoxViewModel =
                        foodBoxViewModel,

                    onViewPlanClick = {
                            planId ->

                        navController.navigate(
                            AppData
                                .getFoodBoxDetailRoute(
                                    planId
                                )
                        )
                    },

                    onManageClick = {
                        navController.navigate(
                            AppData
                                .FOOD_BOX_MANAGE_ROUTE
                        )
                    }
                )
            }

            composable(
                route =
                    AppData.FOOD_BOX_DETAIL_ROUTE,

                arguments = listOf(
                    navArgument(
                        PLAN_ID_ARGUMENT
                    ) {
                        type =
                            NavType.StringType
                    }
                )
            ) { backStackEntry ->

                val planId =
                    backStackEntry
                        .arguments
                        ?.getString(
                            PLAN_ID_ARGUMENT
                        )
                        .orEmpty()

                FoodBoxDetailScreen(
                    planId = planId,

                    foodBoxViewModel =
                        foodBoxViewModel,

                    onBackClick = {
                        navController
                            .popBackStack()
                    },

                    onCustomizeClick = {
                        foodBoxViewModel
                            .selectPlan(planId)

                        navController.navigate(
                            AppData
                                .FOOD_BOX_CUSTOMIZE_ROUTE
                        )
                    },

                    onManageClick = {
                        navController.navigate(
                            AppData
                                .FOOD_BOX_MANAGE_ROUTE
                        )
                    }
                )
            }

            composable(
                route =
                    AppData.FOOD_BOX_CUSTOMIZE_ROUTE
            ) {
                CustomizeFoodBoxScreen(
                    foodBoxViewModel =
                        foodBoxViewModel,

                    onBackClick = {
                        navController
                            .popBackStack()
                    },

                    onContinueClick = {
                        navController.navigate(
                            AppData
                                .FOOD_BOX_SCHEDULE_ROUTE
                        )
                    }
                )
            }

            composable(
                route =
                    AppData.FOOD_BOX_SCHEDULE_ROUTE
            ) {
                DeliveryScheduleScreen(
                    foodBoxViewModel =
                        foodBoxViewModel,

                    onBackClick = {
                        navController
                            .popBackStack()
                    },

                    onContinueClick = {
                        navController.navigate(
                            AppData
                                .FOOD_BOX_CHECKOUT_ROUTE
                        )
                    }
                )
            }

            composable(
                route =
                    AppData.FOOD_BOX_CHECKOUT_ROUTE
            ) {
                FoodBoxCheckoutScreen(
                    foodBoxViewModel =
                        foodBoxViewModel,

                    onBackClick = {
                        navController
                            .popBackStack()
                    },

                    onSubscribeClick = {
                        val userId =
                            LocalAccountStorage
                                .getProfile(context)
                                ?.id

                        foodBoxViewModel
                            .confirmSubscription(
                                userId = userId,

                                onFinished = {
                                    navController.navigate(
                                        AppData
                                            .FOOD_BOX_SUCCESS_ROUTE
                                    ) {
                                        popUpTo(
                                            AppData
                                                .FOOD_BOX_ROUTE
                                        )
                                    }
                                }
                            )
                    }
                )
            }

            composable(
                route =
                    AppData.FOOD_BOX_SUCCESS_ROUTE
            ) {
                SubscriptionSuccessScreen(
                    foodBoxViewModel =
                        foodBoxViewModel,

                    onManageClick = {
                        navController.navigate(
                            AppData
                                .FOOD_BOX_MANAGE_ROUTE
                        )
                    },

                    onBrowseMoreClick = {
                        navController.navigate(
                            AppData.FOOD_BOX_ROUTE
                        ) {
                            popUpTo(
                                AppData.FOOD_BOX_ROUTE
                            )

                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(
                route =
                    AppData.FOOD_BOX_MANAGE_ROUTE
            ) {
                ManageSubscriptionScreen(
                    foodBoxViewModel =
                        foodBoxViewModel,

                    onBackClick = {
                        navController
                            .popBackStack()
                    },

                    onBrowsePlansClick = {
                        navController.navigate(
                            AppData.FOOD_BOX_ROUTE
                        ) {
                            popUpTo(
                                AppData.FOOD_BOX_ROUTE
                            )

                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(
                route =
                    AppData.MEALS_ROUTE
            ) {
                MealsScreen(
                    onViewDetails = {
                            mealId ->

                        navController.navigate(
                            getMealDetailRoute(
                                mealId
                            )
                        )
                    },

                    onFavouriteClick = {
                        navController.navigate(
                            MEAL_FAVOURITES_ROUTE
                        )
                    }
                )
            }

            composable(
                route =
                    MEAL_FAVOURITES_ROUTE
            ) {
                FavouriteScreen(
                    onBack = {
                        navController
                            .popBackStack()
                    },

                    onViewDetails = {
                            mealId ->

                        navController.navigate(
                            getMealDetailRoute(
                                mealId
                            )
                        )
                    }
                )
            }

            composable(
                route =
                    MEAL_DETAIL_ROUTE,

                arguments = listOf(
                    navArgument(
                        MEAL_ID_ARGUMENT
                    ) {
                        type =
                            NavType.IntType
                    }
                )
            ) { backStackEntry ->

                val mealId =
                    backStackEntry
                        .arguments
                        ?.getInt(
                            MEAL_ID_ARGUMENT
                        )

                val meal =
                    mealId?.let {
                        MealData
                            .getMealById(it)
                    }

                if (meal != null) {
                    MealDetailScreen(
                        meal = meal,

                        onBack = {
                            navController
                                .popBackStack()
                        }
                    )
                } else {
                    LaunchedEffect(mealId) {
                        navController
                            .popBackStack()
                    }
                }
            }

            composable(
                route =
                    AppData.PROFILE_ROUTE
            ) {
                ProfileScreen(
                    onNavigate =
                        onPageNavigate
                )
            }

            composable(
                route =
                    AppData.FARMER_ROUTE
            ) {
                FarmerDashboardScreen(
                    onNavigate =
                        onPageNavigate
                )
            }

            composable(
                route =
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