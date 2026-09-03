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
import com.example.githubdemo.screen.MealsScreen
import com.example.githubdemo.screen.RoleSelectionScreen


// changed market import
import com.example.githubdemo.screen.market.MarketScreen
import com.example.githubdemo.screen.market.CartScreen
import com.example.githubdemo.screen.market.MarketPaymentScreen


import com.example.githubdemo.screen.authentication.FarmerSignUpScreen
import com.example.githubdemo.screen.authentication.LoginScreen
import com.example.githubdemo.screen.authentication.SignUpScreen

import com.example.githubdemo.screen.userprofile.ProfileScreen

import com.example.githubdemo.viewmodel.authentication.AuthViewModel
import com.example.githubdemo.viewmodel.market.CartViewModel



private const val CART_ROUTE = "cart"

private const val PAYMENT_ROUTE = "payment"



@Composable
fun AppNavGraph(
    navController: NavHostController =
        rememberNavController()
){

    val context =
        LocalContext.current


    val authViewModel: AuthViewModel =
        viewModel()

    val cartViewModel: CartViewModel =
        viewModel()



    val savedRole = remember {

        LocalAccountStorage
            .getSelectedRole(
                context
            )

    }



    val startDestination =

        if(savedRole == null){

            AppData.ROLE_SELECTION_ROUTE

        }else{

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






    val showBottomNavigation =

        AppData.buyerRoutes
            .contains(
                currentRoute
            )






    val onPageNavigate:(String)->Unit = {

            route ->



        if(
            route ==
            AppData.ROLE_SELECTION_ROUTE
        ){


            authViewModel.signOut {


                navController.navigate(

                    AppData.ROLE_SELECTION_ROUTE

                ){

                    popUpTo(

                        navController
                            .graph
                            .startDestinationId

                    ){

                        inclusive = true

                    }


                    launchSingleTop = true


                }


            }


        }else{


            navController.navigate(route){

                launchSingleTop = true

            }


        }


    }






    Scaffold(

        modifier =
            Modifier.fillMaxSize(),



        bottomBar = {


            if(showBottomNavigation){


                AppBottomNavigationBar(


                    currentRoute = currentRoute,


                    onItemClick = {

                            route ->


                        navController.navigate(route){


                            popUpTo(

                                AppData.HOME_ROUTE

                            ){

                                saveState = true

                            }



                            launchSingleTop = true


                            restoreState = true


                        }


                    }



                )


            }


        }



    ){ innerPadding ->





        NavHost(

            navController = navController,

            startDestination = startDestination,


            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)

        ){






            composable(

                route =
                    AppData.ROLE_SELECTION_ROUTE

            ){


                RoleSelectionScreen(


                    onRoleSelected = {

                            selectedRole ->



                        navController.navigate(

                            AppData.getLoginRoute(
                                selectedRole
                            )

                        ){

                            launchSingleTop = true

                        }



                    }



                )


            }







            composable(

                route = AppData.LOGIN_ROUTE,


                arguments = listOf(

                    navArgument("role"){

                        type =
                            NavType.StringType

                    }

                )

            ){ backStackEntry ->




                val userRole =

                    backStackEntry
                        .arguments
                        ?.getString("role")





                if(
                    userRole != null &&
                    UserRole.isValidRole(
                        userRole
                    )
                ){



                    LoginScreen(


                        userRole = userRole,


                        onLoginSuccess = {

                                loggedInRole ->



                            navController.navigate(

                                AppData
                                    .getRoleDestination(
                                        loggedInRole
                                    )

                            ){

                                popUpTo(

                                    AppData
                                        .ROLE_SELECTION_ROUTE

                                ){

                                    inclusive = true

                                }


                                launchSingleTop = true


                            }


                        },



                        onSignUpClick = {

                                signUpRole ->




                            if(
                                signUpRole !=
                                UserRole.ADMIN
                            ){


                                navController.navigate(

                                    AppData
                                        .getSignUpRoute(
                                            signUpRole
                                        )

                                )


                            }else{


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

                route = AppData.SIGN_UP_ROUTE,


                arguments = listOf(

                    navArgument("role"){

                        type =
                            NavType.StringType

                    }

                )


            ){ backStackEntry ->



                val userRole =

                    backStackEntry
                        .arguments
                        ?.getString("role")




                when(userRole){


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


                        LaunchedEffect(userRole){


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

            ){


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
            ){
                MarketScreen(
                    onNavigate = {
                            route ->
                        navController.navigate(route)
                    },
                    cartViewModel = cartViewModel
                )
            }






            /*
             * Cart
             */
            composable(
                route = CART_ROUTE
            ){
                CartScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onCheckout = {
                        navController.navigate(
                            PAYMENT_ROUTE
                        )
                    },
                    cartViewModel = cartViewModel
                )
            }







            /*
             * Payment
             */
            composable(

                route = PAYMENT_ROUTE

            ){



                MarketPaymentScreen(


                    onBack = {


                        navController.popBackStack()


                    },



                    onPaymentSuccess = {


                        navController.navigate(

                            AppData.MARKET_ROUTE

                        ){

                            popUpTo(

                                AppData.MARKET_ROUTE

                            ){

                                inclusive = true

                            }


                        }


                    }



                )


            }








            /*
             * Buyer Food Box
             */
            composable(

                route =
                    AppData.FOOD_BOX_ROUTE

            ){



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

            ){



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

            ){



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

            ){



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

            ){



                AdminDashboardScreen(

                    onNavigate =
                        onPageNavigate

                )


            }



        }


    }


}