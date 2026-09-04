package com.example.githubdemo.admin.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.githubdemo.admin.model.FoodItem
import com.example.githubdemo.admin.viewmodel.FoodBoxViewModel
import com.example.githubdemo.ui.theme.AdminBackground
import java.util.Locale

@Composable
fun FoodItemEditScreen(
    viewModel: FoodBoxViewModel,
    foodBoxId: String = "basic",
    onBack: () -> Unit = {}
) {
    val foodItems =
        viewModel.foodItems

    var itemToDelete by remember {
        mutableStateOf<FoodItem?>(null)
    }

    LaunchedEffect(foodBoxId) {
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
                        "Manage Food Box Items",
                    style =
                        MaterialTheme
                            .typography
                            .titleLarge
                )
            }
        }

        item {
            Text(
                text =
                    "Items assigned to: $foodBoxId",
                style =
                    MaterialTheme
                        .typography
                        .bodyMedium
            )
        }

        if (
            foodItems.isEmpty() &&
            !viewModel.isLoading
        ) {
            item {
                Text(
                    text =
                        "No items are assigned to this food box."
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
                shape =
                    RoundedCornerShape(
                        10.dp
                    ),
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
                                    } +
                                        " • " +
                                        String.format(
                                            Locale.US,
                                            "RM %.2f",
                                            item.price
                                        ),
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
                Text("Delete item?")
            },
            text = {
                Text(
                    "${selectedItem.name} will be permanently deleted."
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