package com.example.githubdemo.screen.market

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.githubdemo.ui.theme.PrimaryGreen
import com.example.githubdemo.viewmodel.market.CartViewModel
import com.example.githubdemo.viewmodel.market.MarketViewModel


@Composable
fun MarketScreen(
    onNavigate:(String)->Unit = {},
    marketViewModel: MarketViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel()
){

    val products =
        marketViewModel.products.value


    val cartItems by
    cartViewModel.cartProducts.collectAsState()



    var searchText by remember {

        mutableStateOf("")

    }



    val filteredProducts =

        products.filter {

            it.name.contains(
                searchText,
                ignoreCase = true
            )

        }



    val cartCount =

        cartItems.sumOf {

            it.quantity

        }



    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    Color(0xFFF4F6EE)
                )

    ){



        Surface(

            color = PrimaryGreen,

            shape =
                RoundedCornerShape(
                    bottomStart = 35.dp,
                    bottomEnd = 35.dp
                )

        ){


            Column(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 35.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 20.dp
                        )

            ){



                Row(

                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.SpaceBetween,

                    verticalAlignment =
                        Alignment.CenterVertically

                ){



                    Column{


                        Text(

                            text = "Market",

                            color = Color.White,

                            style =
                                MaterialTheme.typography
                                    .headlineMedium

                        )


                        Text(

                            text =
                                "📍 Current Location",

                            color = Color.White

                        )


                    }




                    Box{


                        IconButton(

                            onClick = {

                                onNavigate(
                                    "cart"
                                )

                            }

                        ){

                            Icon(

                                Icons.Default.ShoppingCart,

                                contentDescription = null,

                                tint = Color.White

                            )


                        }




                        if(cartCount > 0){


                            Box(

                                modifier =
                                    Modifier
                                        .size(20.dp)
                                        .align(
                                            Alignment.TopEnd
                                        )
                                        .background(
                                            Color.Red,
                                            RoundedCornerShape(
                                                50
                                            )
                                        )

                            ){


                                Text(

                                    text =
                                        cartCount.toString(),

                                    color =
                                        Color.White,

                                    fontSize =
                                        12.sp,

                                    modifier =
                                        Modifier.align(
                                            Alignment.Center
                                        )

                                )


                            }


                        }


                    }



                }





                Spacer(
                    Modifier.height(15.dp)
                )




                OutlinedTextField(

                    value = searchText,


                    onValueChange = {

                        searchText = it

                    },


                    modifier =
                        Modifier.fillMaxWidth(),


                    placeholder = {

                        Text(
                            "Search product"
                        )

                    },


                    leadingIcon = {

                        Icon(

                            Icons.Default.Search,

                            contentDescription = null

                        )

                    },


                    shape =
                        RoundedCornerShape(
                            18.dp
                        ),



                    colors =
                        OutlinedTextFieldDefaults.colors(

                            focusedContainerColor =
                                Color.White,


                            unfocusedContainerColor =
                                Color.White,


                            focusedBorderColor =
                                PrimaryGreen,


                            unfocusedBorderColor =
                                Color.LightGray


                        )

                )



            }



        }







        LazyVerticalGrid(

            columns =
                GridCells.Fixed(2),


            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(12.dp),


            verticalArrangement =
                Arrangement.spacedBy(
                    12.dp
                ),


            horizontalArrangement =
                Arrangement.spacedBy(
                    12.dp
                )


        ){



            items(filteredProducts){ product ->



                Card(

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(
                                300.dp
                            ),


                    shape =
                        RoundedCornerShape(
                            18.dp
                        ),


                    colors =
                        CardDefaults.cardColors(

                            containerColor =
                                Color.White

                        )


                ){



                    Column(

                        modifier =
                            Modifier.padding(
                                10.dp
                            )

                    ){



                        AsyncImage(

                            model =
                                product.image_url,


                            contentDescription =
                                product.name,


                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(
                                        120.dp
                                    )

                        )





                        Spacer(
                            Modifier.height(8.dp)
                        )





                        Text(

                            text =
                                product.name,


                            maxLines = 1,


                            style =
                                MaterialTheme.typography
                                    .titleMedium

                        )





                        Text(

                            text =
                                product.category,


                            color =
                                Color.Gray

                        )





                        Spacer(
                            Modifier.height(5.dp)
                        )





                        Text(

                            text =
                                "RM %.2f"
                                    .format(
                                        product.price
                                    ),


                            color =
                                PrimaryGreen

                        )





                        Spacer(
                            Modifier.weight(1f)
                        )





                        Button(

                            onClick = {


                                product.id?.let {


                                    cartViewModel
                                        .addToCart(
                                            it
                                        )


                                }


                            },


                            modifier =
                                Modifier
                                    .fillMaxWidth(),


                            colors =
                                ButtonDefaults.buttonColors(

                                    containerColor =
                                        PrimaryGreen

                                ),


                            shape =
                                RoundedCornerShape(
                                    12.dp
                                )

                        ){



                            Text(
                                "Add Cart"
                            )



                        }



                    }


                }



            }



        }



    }



}