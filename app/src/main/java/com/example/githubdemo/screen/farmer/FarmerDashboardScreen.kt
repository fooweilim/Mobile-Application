package com.example.githubdemo.screen.farmer


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


import com.example.githubdemo.model.farmer.FarmerOrder
import com.example.githubdemo.model.market.Product

import com.example.githubdemo.nav.FarmerRoute
import com.example.githubdemo.repository.FarmerOrderRepository
import com.example.githubdemo.supabase.FarmerProductRepository




private val DashboardGreen =
    Color(0xFF28785B)


private val DashboardBackground =
    Color(0xFFF8F5ED)





@Composable
fun FarmerDashboardScreen(

    farmerName:String,

    farmerId:String,

    onNavigate:(String)->Unit

){



    val productRepository =
        remember {

            FarmerProductRepository()

        }



    val orderRepository =
        remember {

            FarmerOrderRepository()

        }





    var products by remember {

        mutableStateOf(
            emptyList<Product>()
        )

    }





    var orders by remember {

        mutableStateOf(
            emptyList<FarmerOrder>()
        )

    }




    var errorMessage by remember {

        mutableStateOf("")

    }






    LaunchedEffect(Unit){


        try{


            products =
                productRepository
                    .getProducts()



            orders =
                orderRepository
                    .getFarmerOrders(
                        farmerId
                    )


        }
        catch(e:Exception){


            errorMessage =
                e.message
                    ?: "Unable to load dashboard."


        }


    }








    val activeOrderCount =
        orders.count {


            it.status.equals(
                "Pending",
                true
            )
                    ||
                    it.status.equals(
                        "Active",
                        true
                    )


        }







    val deliveredEarnings =
        orders
            .filter {


                it.status.equals(
                    "Delivered",
                    true
                )


            }
            .sumOf {


                it.price


            }








    val customerCount =
        orders
            .map {


                it.customer_id


            }
            .filter {


                !it.isNullOrBlank()


            }
            .distinct()
            .size








    Scaffold(

        containerColor =
            DashboardBackground,


        bottomBar = {


            FarmerBottomBar(

                currentRoute =
                    FarmerRoute.DASHBOARD,


                onNavigate =
                    onNavigate

            )


        }


    ){ padding ->





        Column(

            modifier =
                Modifier
                    .padding(padding)
                    .fillMaxSize()

        ){



            Box(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                        .background(
                            DashboardGreen
                        )

            ){



                Column(

                    modifier =
                        Modifier
                            .statusBarsPadding()
                            .padding(20.dp)

                ){



                    Text(

                        text =
                            "Welcome back,",


                        color =
                            Color.White.copy(
                                alpha = 0.8f
                            ),

                        fontSize =
                            13.sp

                    )





                    Text(

                        text =
                            "$farmerName 🌱",


                        color =
                            Color.White,


                        fontSize =
                            22.sp,


                        fontWeight =
                            FontWeight.Bold

                    )





                    Spacer(
                        Modifier.height(25.dp)
                    )






                    Row(

                        horizontalArrangement =
                            Arrangement.spacedBy(12.dp)

                    ){



                        DashboardStatCard(

                            title =
                                "Delivered Earnings",


                            value =
                                "RM %.2f"
                                    .format(
                                        deliveredEarnings
                                    ),


                            modifier =
                                Modifier.weight(1f)

                        )





                        DashboardStatCard(

                            title =
                                "Active Orders",


                            value =
                                activeOrderCount
                                    .toString(),


                            modifier =
                                Modifier.weight(1f)

                        )


                    }



                }


            }








            Spacer(
                Modifier.height(20.dp)
            )






            Row(

                modifier =
                    Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(),


                horizontalArrangement =
                    Arrangement.spacedBy(12.dp)

            ){



                SmallDashboardStatCard(

                    value =
                        products.size.toString(),


                    title =
                        "Products",


                    modifier =
                        Modifier.weight(1f)

                )





                SmallDashboardStatCard(

                    value =
                        orders.size.toString(),


                    title =
                        "Orders",


                    modifier =
                        Modifier.weight(1f)

                )





                SmallDashboardStatCard(

                    value =
                        customerCount.toString(),


                    title =
                        "Customers",


                    modifier =
                        Modifier.weight(1f)

                )


            }








            Spacer(
                Modifier.height(20.dp)
            )






            Text(

                text =
                    "Recent Orders",


                modifier =
                    Modifier.padding(horizontal = 20.dp),


                fontSize =
                    18.sp,


                fontWeight =
                    FontWeight.Bold

            )






            Spacer(
                Modifier.height(10.dp)
            )






            orders
                .take(3)
                .forEach { order ->


                    RecentOrderItem(

                        order = order

                    )


                }




        }


    }



}








@Composable
private fun DashboardStatCard(

    title:String,

    value:String,

    modifier:Modifier = Modifier

){


    Card(

        modifier =
            modifier.height(100.dp),


        colors =
            CardDefaults.cardColors(

                containerColor =
                    Color(0xFF4F8973)

            ),


        shape =
            RoundedCornerShape(18.dp)

    ){



        Column(

            modifier =
                Modifier.padding(15.dp)

        ){


            Text(

                value,

                color =
                    Color.White,

                fontSize =
                    22.sp,

                fontWeight =
                    FontWeight.Bold

            )



            Text(

                title,

                color =
                    Color.White

            )


        }


    }


}








@Composable
private fun SmallDashboardStatCard(

    value:String,

    title:String,

    modifier:Modifier = Modifier

){


    Card(

        modifier =
            modifier.height(70.dp),


        shape =
            RoundedCornerShape(15.dp)

    ){



        Column(

            modifier =
                Modifier.fillMaxSize(),


            horizontalAlignment =
                Alignment.CenterHorizontally,


            verticalArrangement =
                Arrangement.Center

        ){


            Text(

                value,

                fontWeight =
                    FontWeight.Bold

            )



            Text(

                title

            )


        }


    }


}








@Composable
private fun RecentOrderItem(

    order:FarmerOrder

){



    Card(

        modifier =
            Modifier
                .padding(
                    horizontal = 20.dp,
                    vertical = 5.dp
                )
                .fillMaxWidth(),


        shape =
            RoundedCornerShape(18.dp)

    ){



        Row(

            modifier =
                Modifier.padding(12.dp)

        ){



            Column(

                modifier =
                    Modifier.weight(1f)

            ){



                Text(

                    text =
                        "Customer ID: ${order.customer_id ?: "Unknown"}",


                    fontWeight =
                        FontWeight.Bold

                )




                Text(

                    text =
                        order.product_name

                )



                Text(

                    text =
                        "Quantity: ${order.quantity}"

                )



            }





            Text(

                text =
                    "RM %.2f"
                        .format(
                            order.price
                        ),


                color =
                    DashboardGreen

            )



        }



    }



}