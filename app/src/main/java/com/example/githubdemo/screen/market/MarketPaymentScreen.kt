package com.example.githubdemo.screen.market

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.githubdemo.data.market.SelectedCartManager
import com.example.githubdemo.ui.theme.PrimaryGreen

@Composable
fun MarketPaymentScreen(
    onBack:()->Unit = {},
    onPaymentSuccess:()->Unit = {}
){

    val selectedItems = remember {
        SelectedCartManager.getSelectedCart()
    }

    var selectedMethod by remember {
        mutableStateOf("")
    }

    var showError by remember {
        mutableStateOf(false)
    }

    val total = selectedItems.sumOf {
        it.product.price * it.quantity
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFFF4F6EE)
            )
    ){

        Surface(
            color = PrimaryGreen,
            shape = RoundedCornerShape(
                bottomStart = 35.dp,
                bottomEnd = 35.dp
            )
        ){

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 35.dp,
                        start = 10.dp,
                        end = 10.dp,
                        bottom = 20.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ){

                IconButton(
                    onClick = onBack
                ){

                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = Color.White
                    )

                }

                Text(
                    "Payment",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium
                )

            }

        }


        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ){

            item{

                Text(
                    "Order Summary",
                    style = MaterialTheme.typography.titleLarge
                )

            }


            items(selectedItems){ item ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ){

                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){

                        AsyncImage(
                            model = item.product.image_url,
                            contentDescription = item.product.name,
                            modifier = Modifier.size(80.dp)
                        )

                        Spacer(
                            Modifier.width(12.dp)
                        )


                        Column{

                            Text(
                                item.product.name,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                "Quantity: ${item.quantity}"
                            )

                            Text(
                                "RM %.2f".format(
                                    item.product.price *
                                            item.quantity
                                ),
                                color = PrimaryGreen
                            )

                        }

                    }

                }

            }


            item{

                Spacer(
                    Modifier.height(20.dp)
                )


                Text(
                    "Payment Method",
                    style = MaterialTheme.typography.titleLarge
                )


                PaymentMethodCard(
                    title = "Cash",
                    selected = selectedMethod == "Cash"
                ){

                    selectedMethod = "Cash"
                    showError = false

                }


                PaymentMethodCard(
                    title = "Credit Card",
                    selected = selectedMethod == "Credit Card"
                ){

                    selectedMethod = "Credit Card"
                    showError = false

                }


                PaymentMethodCard(
                    title = "Online Banking",
                    selected = selectedMethod == "Online Banking"
                ){

                    selectedMethod = "Online Banking"
                    showError = false

                }


            }


        }


        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(
                topStart = 25.dp,
                topEnd = 25.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ){

            Column(
                modifier = Modifier.padding(16.dp)
            ){

                Text(
                    "Total: RM %.2f".format(total),
                    color = Color.Black,
                    style = MaterialTheme.typography.titleMedium
                )


                if(showError){

                    Text(
                        "Please select payment method",
                        color = Color.Red
                    )

                }


                Spacer(
                    Modifier.height(10.dp)
                )


                Button(
                    onClick = {

                        if(selectedMethod.isEmpty()){

                            showError = true

                        }else{

                            SelectedCartManager.clear()

                            onPaymentSuccess()

                        }

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen
                    )
                ){

                    Text(
                        "Pay Now",
                        color = Color.White
                    )

                }


            }

        }

    }

}



@Composable
fun PaymentMethodCard(
    title:String,
    selected:Boolean,
    onClick:()->Unit
){

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        onClick = onClick
    ){

        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ){

            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = PrimaryGreen
                )
            )


            Text(
                title
            )

        }

    }

}