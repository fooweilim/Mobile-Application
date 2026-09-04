package com.example.githubdemo.nav


import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext


import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


import com.example.githubdemo.data.local.LocalAccountStorage


import com.example.githubdemo.screen.farmer.AddProductScreen
import com.example.githubdemo.screen.farmer.EditProductScreen
import com.example.githubdemo.screen.farmer.FarmerDashboardScreen
import com.example.githubdemo.screen.farmer.FarmerOrdersScreen
import com.example.githubdemo.screen.farmer.FarmerProductScreen
import com.example.githubdemo.screen.farmer.FarmerProfileScreen




object FarmerRoute {


    const val DASHBOARD =
        "farmer_dashboard_home"


    const val PRODUCTS =
        "farmer_products"


    const val ADD_PRODUCT =
        "farmer_add_product"


    const val EDIT_PRODUCT =
        "farmer_edit_product/{productId}"


    const val ORDERS =
        "farmer_orders"


    const val PROFILE =
        "farmer_profile"



    fun getEditProductRoute(
        productId:String
    ):String{

        return "farmer_edit_product/$productId"

    }

}







@Composable
fun FarmerNavGraph(

    onSignOut:()->Unit,

    modifier:Modifier = Modifier,

    navController:NavHostController =
        rememberNavController()

){



    val context =
        LocalContext.current



    val profile =
        remember {


            LocalAccountStorage
                .getProfile(context)


        }






    fun navigateToTopLevel(

        route:String

    ){



        navController.navigate(route){


            popUpTo(
                FarmerRoute.DASHBOARD
            ){

                saveState = true

            }


            launchSingleTop = true


            restoreState = true


        }


    }









    NavHost(

        navController = navController,


        startDestination =
            FarmerRoute.DASHBOARD,


        modifier = modifier

    ){







        composable(

            FarmerRoute.DASHBOARD

        ){



            FarmerDashboardScreen(



                farmerName =

                    profile
                        ?.fullName
                        .orEmpty(),



                farmerId =

                    profile
                        ?.id
                        .orEmpty(),




                onNavigate =

                    ::navigateToTopLevel



            )


        }









        composable(

            FarmerRoute.PRODUCTS

        ){



            FarmerProductScreen(



                onAddProduct = {


                    navController.navigate(

                        FarmerRoute.ADD_PRODUCT

                    )


                },





                onEditProduct = {

                        product ->



                    product.id?.let {


                            productId ->



                        navController.navigate(

                            FarmerRoute
                                .getEditProductRoute(
                                    productId
                                )

                        )

                    }


                },





                onNavigate =

                    ::navigateToTopLevel



            )



        }








        composable(

            FarmerRoute.ADD_PRODUCT

        ){



            AddProductScreen(



                onBack = {


                    navController
                        .popBackStack()


                },




                onProductAdded = {


                    navController
                        .popBackStack()


                }



            )


        }









        composable(

            FarmerRoute.EDIT_PRODUCT

        ){ backStackEntry ->



            val productId =

                backStackEntry
                    .arguments
                    ?.getString(
                        "productId"
                    )
                    .orEmpty()






            EditProductScreen(



                productId = productId,




                onBack = {


                    navController
                        .popBackStack()


                },





                onUpdated = {


                    navController
                        .popBackStack()


                }



            )



        }









        composable(

            FarmerRoute.ORDERS

        ){



            FarmerOrdersScreen(



                farmerId =

                    profile
                        ?.id
                        .orEmpty(),




                onNavigate =

                    ::navigateToTopLevel



            )



        }









        composable(

            FarmerRoute.PROFILE

        ){



            FarmerProfileScreen(



                profile = profile,




                onNavigate =

                    ::navigateToTopLevel,




                onSignOut =

                    onSignOut



            )



        }





    }



}