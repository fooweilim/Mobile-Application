package com.example.githubdemo.screen.meals

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubdemo.data.meals.FavouriteManager
import com.example.githubdemo.data.meals.Meal
import com.example.githubdemo.data.meals.MealData

private val DarkGreen =
    Color(0xFF174C39)

private val LightGreen =
    Color(0xFFD9FBDD)

@Composable
fun MealsScreen(
    onViewDetails: (Int) -> Unit,
    onFavouriteClick: () -> Unit
) {
    var budget by remember {
        mutableStateOf(10.0)
    }

    var selectedPax by remember {
        mutableStateOf(4)
    }

    var searchText by remember {
        mutableStateOf("")
    }

    var showBudgetDialog by remember {
        mutableStateOf(false)
    }

    var showPaxMenu by remember {
        mutableStateOf(false)
    }

    val meals =
        MealData.filterMeal(
            budget = budget,
            pax = selectedPax
        ).filter { meal ->
            searchText.isEmpty() ||
                    meal.name.contains(
                        searchText,
                        ignoreCase = true
                    ) ||
                    meal.ingredients.any {
                        it.contains(
                            searchText,
                            ignoreCase = true
                        )
                    }
        }

    if (showBudgetDialog) {
        BudgetDialog(
            currentBudget = budget,

            onSave = {
                budget = it
                showBudgetDialog = false
            },

            onCancel = {
                showBudgetDialog = false
            }
        )
    }

    Scaffold(
        containerColor = Color.White
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            MealsHeader(
                searchText = searchText,

                onSearchChange = {
                    searchText = it
                },

                onFavouriteClick =
                    onFavouriteClick
            )

            BudgetSection(
                budget = budget,
                pax = selectedPax,

                onEdit = {
                    showBudgetDialog = true
                },

                onPaxClick = {
                    showPaxMenu = !showPaxMenu
                },

                showMenu = showPaxMenu,

                onSelectPax = {
                    selectedPax = it
                    showPaxMenu = false
                }
            )

            Text(
                text = "Recommended for You",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,

                modifier = Modifier.padding(
                    start = 20.dp,
                    top = 5.dp,
                    bottom = 10.dp
                )
            )

            if (meals.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No meals found",
                        fontSize = 17.sp,
                        color = Color.Gray
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),

                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 18.dp),

                    horizontalArrangement =
                        Arrangement.spacedBy(12.dp),

                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = meals,
                        key = { meal -> meal.id }
                    ) { meal ->

                        MealCard(
                            meal = meal,
                            onViewDetails = onViewDetails
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MealsHeader(
    searchText: String,
    onSearchChange: (String) -> Unit,
    onFavouriteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = DarkGreen,

                shape = RoundedCornerShape(
                    bottomStart = 30.dp,
                    bottomEnd = 30.dp
                )
            )
            .padding(22.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Meals",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = onFavouriteClick
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favourite meals",
                    tint = Color.Red
                )
            }
        }

        Spacer(
            modifier = Modifier.height(15.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White.copy(
                        alpha = 0.12f
                    ),

                    shape = RoundedCornerShape(15.dp)
                )
                .padding(12.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color.White
            )

            Spacer(
                modifier = Modifier.width(10.dp)
            )

            BasicTextField(
                value = searchText,
                onValueChange = onSearchChange,
                singleLine = true,

                modifier = Modifier.weight(1f),

                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 12.sp
                ),

                decorationBox = { innerTextField ->
                    if (searchText.isEmpty()) {
                        Text(
                            text =
                                "Search recipes or ingredients",

                            color = Color.White.copy(
                                alpha = 0.6f
                            ),

                            fontSize = 12.sp
                        )
                    }

                    innerTextField()
                }
            )
        }
    }
}

@Composable
private fun BudgetSection(
    budget: Double,
    pax: Int,
    onEdit: () -> Unit,
    onPaxClick: () -> Unit,
    showMenu: Boolean,
    onSelectPax: (Int) -> Unit
) {
    Box(
        modifier = Modifier.padding(
            horizontal = 20.dp,
            vertical = 10.dp
        )
    ) {
        Surface(
            color = LightGreen,
            shape = RoundedCornerShape(13.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),

                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Daily Budget",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {
                        Text(
                            text = "RM %.2f".format(budget),
                            fontSize = 17.sp,
                            color = DarkGreen,
                            fontWeight = FontWeight.Bold
                        )

                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit budget",

                            modifier = Modifier
                                .size(18.dp)
                                .clickable {
                                    onEdit()
                                }
                        )
                    }
                }

                Box {
                    Row(
                        modifier = Modifier.clickable {
                            onPaxClick()
                        },

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = null,
                            tint = Color(0xFF329444)
                        )

                        Spacer(
                            modifier = Modifier.width(4.dp)
                        )

                        Text(
                            text = "$pax pax",
                            fontWeight = FontWeight.Bold
                        )

                        Icon(
                            imageVector =
                                Icons.Default.KeyboardArrowDown,

                            contentDescription =
                                "Select number of pax"
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,

                        onDismissRequest = {
                            onPaxClick()
                        }
                    ) {
                        listOf(
                            1,
                            2,
                            4,
                            6,
                            8
                        ).forEach { number ->

                            DropdownMenuItem(
                                text = {
                                    Text("$number pax")
                                },

                                onClick = {
                                    onSelectPax(number)
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
private fun MealCard(
    meal: Meal,
    onViewDetails: (Int) -> Unit
) {
    var favourite by remember(meal.id) {
        mutableStateOf(
            FavouriteManager.isFavourite(meal.id)
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(13.dp),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column {
            Box {
                Image(
                    painter = painterResource(meal.imageRes),
                    contentDescription = meal.name,

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 13.dp,
                                topEnd = 13.dp
                            )
                        ),

                    contentScale = ContentScale.Crop
                )

                Surface(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(30.dp)
                        .align(Alignment.TopEnd)
                        .clickable {
                            FavouriteManager.toggleFavourite(
                                meal.id
                            )

                            favourite =
                                FavouriteManager.isFavourite(
                                    meal.id
                                )
                        },

                    shape = CircleShape,
                    color = Color.White
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector =
                                Icons.Default.Favorite,

                            contentDescription =
                                "Add favourite",

                            tint =
                                if (favourite) {
                                    Color.Red
                                } else {
                                    Color(0xFFFF8A80)
                                }
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = meal.name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "RM %.2f".format(meal.price),
                    color = Color(0xFF329444),
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Button(
                    onClick = {
                        onViewDetails(meal.id)
                    },

                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .height(32.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
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

@Composable
private fun BudgetDialog(
    currentBudget: Double,
    onSave: (Double) -> Unit,
    onCancel: () -> Unit
) {
    var input by remember {
        mutableStateOf(
            currentBudget.toString()
        )
    }

    AlertDialog(
        onDismissRequest = onCancel,

        title = {
            Text("Daily Budget")
        },

        text = {
            OutlinedTextField(
                value = input,

                onValueChange = {
                    input = it
                },

                label = {
                    Text("Enter Budget (RM)")
                },

                singleLine = true
            )
        },

        confirmButton = {
            Button(
                onClick = {
                    val value =
                        input.toDoubleOrNull()

                    if (value != null && value >= 0) {
                        onSave(value)
                    }
                }
            ) {
                Text("Save")
            }
        },

        dismissButton = {
            TextButton(
                onClick = onCancel
            ) {
                Text("Cancel")
            }
        }
    )
}