package com.example.githubdemo.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.githubdemo.data.meals.Meal
import com.example.githubdemo.data.meals.MealData

private fun getMealPriceByPax(
    mealName: String,
    pax: Int
): String {
    val prices =
        when (mealName) {
            "Tomato Egg Stir Fry" -> mapOf(
                1 to 2.00,
                2 to 2.50,
                4 to 3.00,
                6 to 4.50,
                8 to 6.00
            )

            "Vegetable Rice" -> mapOf(
                1 to 2.50,
                2 to 3.50,
                4 to 5.00,
                6 to 7.00,
                8 to 9.00
            )

            "Mushroom Omelette" -> mapOf(
                1 to 2.50,
                2 to 3.50,
                4 to 4.00,
                6 to 5.50,
                8 to 7.00
            )

            "Carrot & Potato Soup" -> mapOf(
                1 to 2.50,
                2 to 3.50,
                4 to 5.00,
                6 to 7.00,
                8 to 9.00
            )

            "Banana Oatmeal" -> mapOf(
                1 to 2.00,
                2 to 3.00,
                4 to 4.00,
                6 to 5.50,
                8 to 7.00
            )

            "Tuna Sandwich" -> mapOf(
                1 to 3.50,
                2 to 4.50,
                4 to 6.00,
                6 to 8.00,
                8 to 10.00
            )

            "Vegetable Pasta" -> mapOf(
                1 to 4.00,
                2 to 5.00,
                4 to 7.00,
                6 to 9.00,
                8 to 12.00
            )

            "Chicken Fried Rice" -> mapOf(
                1 to 4.50,
                2 to 6.00,
                4 to 8.00,
                6 to 10.00,
                8 to 13.00
            )

            "Chicken Vegetable Soup" -> mapOf(
                1 to 4.50,
                2 to 6.00,
                4 to 8.00,
                6 to 10.00,
                8 to 13.00
            )

            "Fish Rice Bowl" -> mapOf(
                1 to 5.00,
                2 to 7.00,
                4 to 10.00,
                6 to 13.00,
                8 to 16.00
            )

            else -> emptyMap()
        }

    val selectedPrice = prices[pax]

    return if (selectedPrice != null) {
        "RM %.2f".format(selectedPrice)
    } else {
        "Unavailable"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDetailScreen(
    meal: Meal,
    onBack: () -> Unit
) {
    var selectedPax by remember(meal.id) {
        mutableStateOf(meal.pax)
    }

    var favourite by remember(meal.id) {
        mutableStateOf(
            FavouriteManager.isFavourite(meal.id)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(meal.name)
                },

                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector =
                                Icons.Default.ArrowBack,

                            contentDescription = "Back"
                        )
                    }
                },

                actions = {
                    IconButton(
                        onClick = {
                            FavouriteManager.toggleFavourite(
                                meal.id
                            )

                            favourite =
                                FavouriteManager.isFavourite(
                                    meal.id
                                )
                        }
                    ) {
                        Icon(
                            imageVector =
                                Icons.Default.Favorite,

                            contentDescription = "Favourite",

                            tint =
                                if (favourite) {
                                    Color.Red
                                } else {
                                    Color.LightGray
                                }
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(meal.imageRes),
                contentDescription = meal.name,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(
                        RoundedCornerShape(16.dp)
                    ),

                contentScale = ContentScale.Crop
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = meal.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = getMealPriceByPax(
                    mealName = meal.name,
                    pax = selectedPax
                ),

                color = Color(0xFF329444),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(15.dp)
            )

            Text(
                text = "Number of Pax",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Row(
                modifier = Modifier.horizontalScroll(
                    rememberScrollState()
                ),

                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    1,
                    2,
                    4,
                    6,
                    8
                ).forEach { pax ->

                    Button(
                        onClick = {
                            selectedPax = pax
                        },

                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    if (selectedPax == pax) {
                                        Color(0xFF174C39)
                                    } else {
                                        Color.LightGray
                                    }
                            )
                    ) {
                        Text("$pax")
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            val ingredients =
                MealData.getIngredientsByPax(
                    meal = meal,
                    pax = selectedPax
                )

            Text(
                text = "Ingredients ($selectedPax Pax)",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            if (ingredients.isEmpty()) {
                Text(
                    text = "Unavailable",
                    fontSize = 15.sp,
                    color = Color.Gray
                )
            } else {
                ingredients.forEach { ingredient ->
                    Text(
                        text = "• $ingredient",
                        fontSize = 15.sp,

                        modifier = Modifier.padding(
                            vertical = 4.dp
                        )
                    )
                }

                Spacer(
                    modifier = Modifier.height(15.dp)
                )

                Text(
                    text = "Seasonings",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                meal.seasonings.forEach { seasoning ->
                    Text(
                        text = "• $seasoning",
                        fontSize = 15.sp,

                        modifier = Modifier.padding(
                            vertical = 3.dp
                        )
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "Nutrition",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            NutritionDetail(
                meal = meal
            )

            Spacer(
                modifier = Modifier.height(30.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onBack,

                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        ),

                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "Back",
                        color = Color.White
                    )
                }

                Button(
                    onClick = {
                        FavouriteManager.toggleFavourite(
                            meal.id
                        )

                        favourite =
                            FavouriteManager.isFavourite(
                                meal.id
                            )
                    },

                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                if (favourite) {
                                    Color.Gray
                                } else {
                                    Color(0xFF24C93D)
                                }
                        ),

                    shape = RoundedCornerShape(50)
                ) {
                    Icon(
                        imageVector =
                            Icons.Default.Favorite,

                        contentDescription = null,
                        tint = Color.White
                    )

                    Spacer(
                        modifier = Modifier.width(5.dp)
                    )

                    Text(
                        text =
                            if (favourite) {
                                "Added Favourite"
                            } else {
                                "Add to Favourite"
                            },

                        color = Color.White
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(30.dp)
            )
        }
    }
}

@Composable
private fun NutritionDetail(
    meal: Meal
) {
    Row(
        modifier = Modifier.fillMaxWidth(),

        horizontalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {
        NutritionBox(
            modifier = Modifier.weight(1f),
            title = "Protein",
            value = meal.protein
        )

        NutritionBox(
            modifier = Modifier.weight(1f),
            title = "Calories",
            value = meal.calories
        )

        NutritionBox(
            modifier = Modifier.weight(1f),
            title = "Vitamins",
            value = meal.vitamins
        )
    }
}

@Composable
private fun NutritionBox(
    modifier: Modifier,
    title: String,
    value: String
) {
    Column(
        modifier = modifier
            .background(
                color = Color(0xFFDFF4E1),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(10.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = value,
            fontSize = 10.sp
        )
    }
}