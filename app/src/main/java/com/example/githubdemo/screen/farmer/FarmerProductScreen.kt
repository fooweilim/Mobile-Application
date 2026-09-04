package com.example.githubdemo.screen.farmer


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


import com.example.githubdemo.model.farmer.FarmerOrder
import com.example.githubdemo.nav.FarmerRoute
import com.example.githubdemo.repository.FarmerOrderRepository

import kotlinx.coroutines.launch



private val OrderBackground =
    Color(0xFFF8F5ED)


private val OrderGreen =
    Color(0xFF28785B)



private val FarmerOrderTabs =
    listOf(
        "Pending",
        "Active",
        "Delivered"
    )





@Composable
fun FarmerOrdersScreen(

    farmerId:String,

    onNavigate:(String)->Unit

){



    val repository =
        remember {

            FarmerOrderRepository()

        }



    val scope =
        rememberCoroutineScope()



    var orders by remember {

        mutableStateOf(
            emptyList<FarmerOrder>()
        )

    }



    var selectedTab by remember {

        mutableStateOf(
            "Pending"
        )

    }



    var isLoading by remember {

        mutableStateOf(true)

    }



    var errorMessage by remember {

        mutableStateOf("")

    }






    fun loadOrders(){


        scope.launch{


            try{


                isLoading = true


                orders =
                    repository
                        .getFarmerOrders(
                            farmerId
                        )


            }
            catch(e:Exception){


                errorMessage =
                    e.message
                        ?: "Unable to load orders."


            }
            finally{


                isLoading = false


            }



        }


    }







    fun updateOrderStatus(

        order:FarmerOrder,

        status:String

    ){



        order.id?.let { id ->



            scope.launch{


                try{


                    repository.updateStatus(

                        orderId = id,

                        newStatus = status

                    )


                    loadOrders()



                }
                catch(e:Exception){


                    errorMessage =
                        e.message
                            ?: "Unable to update order."

                }



            }


        }



    }







    LaunchedEffect(Unit){

        loadOrders()

    }







    val filteredOrders =

        orders.filter{


            it.status.equals(

                selectedTab,

                true

            )


        }








    Scaffold(

        containerColor =
            OrderBackground,


        bottomBar = {


            FarmerBottomBar(

                currentRoute =
                    FarmerRoute.ORDERS,


                onNavigate =
                    onNavigate

            )


        }



    ){ padding ->




        Column(

            modifier =
                Modifier
                    .padding(padding)
                    .padding(horizontal = 20.dp)
                    .fillMaxSize()

        ){



            Spacer(
                Modifier.height(20.dp)
            )




            Row(

                modifier =
                    Modifier.fillMaxWidth(),


                horizontalArrangement =
                    Arrangement.SpaceBetween,


                verticalAlignment =
                    Alignment.CenterVertically

            ){



                Text(

                    text =
                        "Orders",

                    fontSize =
                        24.sp,


                    fontWeight =
                        FontWeight.Bold

                )




                IconButton(

                    onClick = ::loadOrders

                ){



                    Icon(

                        Icons.Default.Refresh,

                        contentDescription = null,

                        tint = OrderGreen

                    )


                }


            }







            Spacer(
                Modifier.height(15.dp)
            )






            Row(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(

                            Color(0xFFEDE7DC),

                            RoundedCornerShape(25.dp)

                        )
                        .padding(5.dp),



                horizontalArrangement =
                    Arrangement.spacedBy(5.dp)

            ){



                FarmerOrderTabs.forEach { tab ->



                    Button(

                        modifier =
                            Modifier.weight(1f),


                        onClick = {

                            selectedTab = tab

                        },



                        colors =
                            ButtonDefaults.buttonColors(

                                containerColor =

                                    if(selectedTab == tab)

                                        OrderGreen

                                    else

                                        Color.Gray

                            ),



                        shape =
                            RoundedCornerShape(20.dp)

                    ){



                        Text(

                            tab,

                            fontSize =
                                11.sp

                        )

                    }



                }


            }







            Spacer(
                Modifier.height(15.dp)
            )







            when{


                isLoading -> {



                    Box(

                        modifier =
                            Modifier.fillMaxSize(),


                        contentAlignment =
                            Alignment.Center

                    ){


                        CircularProgressIndicator(

                            color =
                                OrderGreen

                        )


                    }


                }






                errorMessage.isNotEmpty() -> {



                    Text(

                        text =
                            errorMessage,


                        color =
                            Color.Red

                    )


                }






                filteredOrders.isEmpty() -> {



                    Box(

                        modifier =
                            Modifier.fillMaxSize(),


                        contentAlignment =
                            Alignment.Center

                    ){


                        Text(

                            text =
                                "No $selectedTab Orders",


                            color =
                                Color.Gray

                        )


                    }


                }







                else -> {



                    LazyColumn(

                        verticalArrangement =
                            Arrangement.spacedBy(15.dp)

                    ){



                        items(filteredOrders){ order ->



                            FarmerOrderCard(

                                order = order,


                                onAccept = {


                                    updateOrderStatus(

                                        order,

                                        "Active"

                                    )


                                },


                                onDelivered = {


                                    updateOrderStatus(

                                        order,

                                        "Delivered"

                                    )


                                }


                            )



                        }



                    }



                }


            }




        }



    }



}








@Composable
private fun FarmerOrderCard(

    order:FarmerOrder,

    onAccept:()->Unit,

    onDelivered:()->Unit

){



    Card(

        modifier =
            Modifier.fillMaxWidth(),


        shape =
            RoundedCornerShape(20.dp)

    ){



        Column(

            modifier =
                Modifier.padding(15.dp)

        ){



            Text(

                text =
                    "Customer ID: ${order.customer_id ?: "Unknown"}",


                fontWeight =
                    FontWeight.Bold

            )





            Text(

                text =
                    "Product: ${order.product_name}",


                fontSize =
                    13.sp

            )





            Text(

                text =
                    "Quantity: ${order.quantity}",


                fontSize =
                    13.sp

            )





            Text(

                text =
                    "RM %.2f".format(

                        order.price

                    ),


                color =
                    OrderGreen,


                fontWeight =
                    FontWeight.Bold

            )





            Spacer(
                Modifier.height(10.dp)
            )






            when(order.status){



                "Pending" -> {



                    Button(

                        onClick =
                            onAccept,


                        colors =
                            ButtonDefaults.buttonColors(

                                containerColor =
                                    OrderGreen

                            )

                    ){


                        Text(
                            "Accept Order"
                        )


                    }


                }





                "Active" -> {



                    Button(

                        onClick =
                            onDelivered,


                        colors =
                            ButtonDefaults.buttonColors(

                                containerColor =
                                    Color(0xFFFFA726)

                            )

                    ){


                        Text(
                            "Mark Delivered"
                        )


                    }



                }



            }



        }



    }



}