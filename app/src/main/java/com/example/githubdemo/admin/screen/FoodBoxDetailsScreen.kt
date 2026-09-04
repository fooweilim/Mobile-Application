package com.example.githubdemo.admin.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.githubdemo.admin.model.FoodItem
import com.example.githubdemo.admin.viewmodel.FoodBoxViewModel
import com.example.githubdemo.ui.theme.AdminBackground
import com.example.githubdemo.ui.theme.HarvestGreen
import java.util.Locale

@Composable
fun FoodBoxDetailsScreen(
    foodBoxId: String,
    navController: NavHostController,
    viewModel: FoodBoxViewModel,
    onBack: () -> Unit
) {
    val foodBox =
        viewModel.foodBoxes.find {
                currentFoodBox ->
            currentFoodBox.id ==
                    foodBoxId
        }

    val foodItems =
        viewModel.foodItems

    var itemToDelete by remember {
        mutableStateOf<FoodItem?>(null)
    }

    LaunchedEffect(foodBoxId) {
        if (viewModel.foodBoxes.isEmpty()) {
            viewModel.loadFoodBoxes()
        }

        viewModel.loadFoodItems(
            foodBoxId
        )
    }

    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    AdminBackground
                ),
        contentPadding =
            PaddingValues(20.dp),
        verticalArrangement =
            Arrangement.spacedBy(
                12.dp
            )
    ) {
        item {
            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onBack
                ) {
                    Text("Back")
                }

                Text(
                    text =
                        "Food Box Details",
                    style =
                        MaterialTheme
                            .typography
                            .titleLarge
                )
            }
        }

        item {
            Card(
                modifier =
                    Modifier.fillMaxWidth(),
                shape =
                    RoundedCornerShape(
                        16.dp
                    ),
                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            Color.White
                    )
            ) {
                Column(
                    modifier =
                        Modifier.padding(20.dp)
                ) {
                    Icon(
                        imageVector =
                            Icons.Default
                                .Inventory,
                        contentDescription =
                            null,
                        tint =
                            HarvestGreen
                    )

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    Text(
                        text =
                            foodBox?.name
                                ?: "Food Box",
                        style =
                            MaterialTheme
                                .typography
                                .headlineSmall,
                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(
                        text =
                            String.format(
                                Locale.US,
                                "RM %.2f",
                                foodBox?.price
                                    ?: 0.0
                            ),
                        color =
                            HarvestGreen,
                        fontWeight =
                            FontWeight.Bold
                    )

                    if (
                        !foodBox
                            ?.description
                            .isNullOrBlank()
                    ) {
                        Spacer(
                            modifier =
                                Modifier.height(
                                    8.dp
                                )
                        )

                        Text(
                            text =
                                foodBox
                                    ?.description
                                    .orEmpty()
                        )
                    }
                }
            }
        }

        item {
            Button(
                modifier =
                    Modifier.fillMaxWidth(),
                colors =
                    ButtonDefaults
                        .buttonColors(
                            containerColor =
                                HarvestGreen
                        ),
                onClick = {
                    navController.navigate(
                        "foodItemDetailEdit/" +
                                foodBoxId
                    )
                }
            ) {
                Icon(
                    imageVector =
                        Icons.Default.Edit,
                    contentDescription =
                        null
                )

                Spacer(
                    modifier =
                        Modifier.padding(
                            horizontal = 4.dp
                        )
                )

                Text("Manage Items")
            }
        }

        item {
            Text(
                text =
                    "Included Items",
                style =
                    MaterialTheme
                        .typography
                        .titleMedium
            )
        }

        if (
            foodItems.isEmpty() &&
            !viewModel.isLoading
        ) {
            item {
                Text(
                    text =
                        "No item is assigned to this food box."
                )
            }
        }

        items(
            items = foodItems,
            key = { item ->
                item.id
            }
        ) { item ->
            Card(
                modifier =
                    Modifier.fillMaxWidth(),
                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            Color.White
                    )
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {
                    Column(
                        modifier =
                            Modifier.weight(1f)
                    ) {
                        Text(
                            text =
                                item.name,
                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            text =
                                item.weight
                                    .orEmpty()
                                    .ifBlank {
                                        "Weight not set"
                                    },
                            color =
                                Color.Gray
                        )
                    }

                    IconButton(
                        onClick = {
                            itemToDelete = item
                        }
                    ) {
                        Icon(
                            imageVector =
                                Icons.Default
                                    .Delete,
                            contentDescription =
                                "Delete item",
                            tint =
                                Color.Red
                        )
                    }
                }
            }
        }

        if (
            !viewModel.errorMessage
                .isNullOrBlank()
        ) {
            item {
                Text(
                    text =
                        viewModel
                            .errorMessage
                            .orEmpty(),
                    color =
                        MaterialTheme
                            .colorScheme
                            .error
                )
            }
        }
    }

    itemToDelete?.let { selectedItem ->
        AlertDialog(
            onDismissRequest = {
                itemToDelete = null
            },
            title = {
                Text("Remove item?")
            },
            text = {
                Text(
                    "${selectedItem.name} will be removed from this food box."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteFoodItem(
                            selectedItem.id
                        )

                        itemToDelete = null
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        itemToDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}