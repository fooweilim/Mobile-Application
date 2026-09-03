package com.example.githubdemo.screen.meals

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubdemo.data.meals.FavouriteManager
import com.example.githubdemo.data.meals.MealData

private val FavouriteDarkGreen =
    Color(0xFF174C39)

@Composable
fun FavouriteScreen(
    onBack: () -> Unit,
    onViewDetails: (Int) -> Unit
) {
    val favouriteMeals =
        MealData.meals.filter {
            FavouriteManager.isFavourite(it.id)
        }

    Scaffold(
        containerColor = Color.White
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            FavouriteHeader(
                onBack = onBack
            )

            if (favouriteMeals.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No Favourite Meal ❤️",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),

                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            horizontal = 18.dp,
                            vertical = 12.dp
                        ),

                    horizontalArrangement =
                        Arrangement.spacedBy(12.dp),

                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = favouriteMeals,
                        key = { meal -> meal.id }
                    ) { meal ->

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(13.dp),

                            elevation =
                                CardDefaults.cardElevation(
                                    defaultElevation = 3.dp
                                )
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Box {
                                    Image(
                                        painter =
                                            painterResource(
                                                meal.imageRes
                                            ),

                                        contentDescription =
                                            meal.name,

                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(140.dp)
                                            .clip(
                                                RoundedCornerShape(
                                                    12.dp
                                                )
                                            ),

                                        contentScale =
                                            ContentScale.Crop
                                    )

                                    Surface(
                                        modifier = Modifier
                                            .padding(7.dp)
                                            .size(30.dp)
                                            .align(
                                                Alignment.TopEnd
                                            ),

                                        shape = CircleShape,
                                        color = Color.White
                                    ) {
                                        Box(
                                            contentAlignment =
                                                Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector =
                                                    Icons.Default.Favorite,

                                                contentDescription =
                                                    null,

                                                tint = Color.Red
                                            )
                                        }
                                    }
                                }

                                Spacer(
                                    modifier =
                                        Modifier.height(8.dp)
                                )

                                Text(
                                    text = meal.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text =
                                        "RM %.2f".format(
                                            meal.price
                                        ),

                                    color = Color(0xFF329444),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(8.dp)
                                )

                                Button(
                                    onClick = {
                                        onViewDetails(meal.id)
                                    },

                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(32.dp),

                                    colors =
                                        ButtonDefaults.buttonColors(
                                            containerColor =
                                                Color.Black
                                        )
                                ) {
                                    Text(
                                        text = "View Details",
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FavouriteHeader(
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = FavouriteDarkGreen,

                shape = RoundedCornerShape(
                    bottomStart = 30.dp,
                    bottomEnd = 30.dp
                )
            )
            .padding(
                vertical = 18.dp,
                horizontal = 15.dp
            ),

        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack
        ) {
            Icon(
                imageVector =
                    Icons.AutoMirrored.Filled.ArrowBack,

                contentDescription = "Back",
                tint = Color.White
            )
        }

        Text(
            text = "My Favourite",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
    }
}