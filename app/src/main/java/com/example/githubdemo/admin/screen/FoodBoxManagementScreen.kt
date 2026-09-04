package com.example.githubdemo.admin.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.sp
import com.example.githubdemo.admin.model.FoodBox
import com.example.githubdemo.admin.viewmodel.FoodBoxViewModel
import com.example.githubdemo.ui.theme.AdminBackground
import com.example.githubdemo.ui.theme.HarvestGreen
import com.example.githubdemo.ui.theme.LightGreen
import java.util.Locale

@Composable
fun FoodBoxManagementScreen(
    viewModel: FoodBoxViewModel,
    onFoodBoxClick: (String) -> Unit
) {
    var selectedPlan by remember {
        mutableStateOf("Monthly")
    }

    val foodBoxes =
        viewModel.foodBoxes

    LaunchedEffect(Unit) {
        viewModel.loadFoodBoxes()
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    AdminBackground
                )
    ) {
        Text(
            text =
                "Food Box Management",
            modifier =
                Modifier.padding(
                    start = 24.dp,
                    top = 20.dp,
                    end = 24.dp
                ),
            style =
                MaterialTheme
                    .typography
                    .titleLarge
        )

        BillingCycleSelector(
            selectedPlan =
                selectedPlan,
            onPlanSelected = {
                selectedPlan = it
            }
        )

        Text(
            text = "Food Box Plans",
            modifier =
                Modifier.padding(
                    horizontal = 24.dp,
                    vertical = 12.dp
                ),
            fontSize = 20.sp,
            fontWeight =
                FontWeight.Bold
        )

        if (viewModel.isLoading) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(30.dp),
                contentAlignment =
                    Alignment.Center
            ) {
                CircularProgressIndicator(
                    color =
                        HarvestGreen
                )
            }
        } else if (
            !viewModel.errorMessage
                .isNullOrBlank()
        ) {
            Text(
                text =
                    viewModel
                        .errorMessage
                        .orEmpty(),
                modifier =
                    Modifier.padding(
                        horizontal = 24.dp
                    ),
                color =
                    MaterialTheme
                        .colorScheme
                        .error
            )
        } else if (foodBoxes.isEmpty()) {
            Text(
                text =
                    "No food box plans available.",
                modifier =
                    Modifier.padding(
                        horizontal = 24.dp
                    )
            )
        } else {
            LazyColumn(
                modifier =
                    Modifier.fillMaxSize(),
                contentPadding =
                    PaddingValues(
                        start = 24.dp,
                        end = 24.dp,
                        bottom = 100.dp
                    ),
                verticalArrangement =
                    Arrangement.spacedBy(
                        18.dp
                    )
            ) {
                items(
                    items = foodBoxes,
                    key = { foodBox ->
                        foodBox.id
                    }
                ) { foodBox ->
                    FoodBoxCard(
                        foodBox =
                            foodBox,
                        billingCycle =
                            selectedPlan,
                        onClick = {
                            onFoodBoxClick(
                                foodBox.id
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun BillingCycleSelector(
    selectedPlan: String,
    onPlanSelected: (String) -> Unit
) {
    Row(
        modifier =
            Modifier
                .padding(
                    start = 24.dp,
                    top = 18.dp,
                    end = 24.dp
                )
                .fillMaxWidth()
                .height(52.dp)
                .background(
                    color = Color.White,
                    shape =
                        RoundedCornerShape(
                            26.dp
                        )
                )
                .padding(4.dp)
    ) {
        listOf(
            "Monthly",
            "Yearly"
        ).forEach { plan ->
            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(
                            color =
                                if (
                                    selectedPlan ==
                                    plan
                                ) {
                                    HarvestGreen
                                } else {
                                    Color.Transparent
                                },
                            shape =
                                RoundedCornerShape(
                                    22.dp
                                )
                        )
                        .clickable {
                            onPlanSelected(
                                plan
                            )
                        },
                contentAlignment =
                    Alignment.Center
            ) {
                Text(
                    text = plan,
                    color =
                        if (
                            selectedPlan ==
                            plan
                        ) {
                            Color.White
                        } else {
                            Color.Gray
                        },
                    fontWeight =
                        FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun FoodBoxCard(
    foodBox: FoodBox,
    billingCycle: String,
    onClick: () -> Unit
) {
    val displayPrice =
        if (
            billingCycle == "Yearly"
        ) {
            foodBox.price * 12
        } else {
            foodBox.price
        }

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = onClick
                ),
        shape =
            RoundedCornerShape(18.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
    ) {
        Column {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(
                            LightGreen
                        ),
                contentAlignment =
                    Alignment.Center
            ) {
                Icon(
                    imageVector =
                        Icons.Default
                            .Inventory,
                    contentDescription =
                        null,
                    modifier =
                        Modifier.size(70.dp),
                    tint =
                        HarvestGreen
                )

                Surface(
                    modifier =
                        Modifier
                            .align(
                                Alignment.TopEnd
                            )
                            .padding(10.dp),
                    shape =
                        RoundedCornerShape(
                            10.dp
                        ),
                    color =
                        HarvestGreen
                ) {
                    Text(
                        text =
                            billingCycle,
                        color =
                            Color.White,
                        fontSize = 11.sp,
                        modifier =
                            Modifier.padding(
                                horizontal =
                                    8.dp,
                                vertical =
                                    4.dp
                            )
                    )
                }
            }

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {
                    Text(
                        text =
                            foodBox.name,
                        fontWeight =
                            FontWeight.Bold,
                        fontSize = 17.sp
                    )

                    Text(
                        text =
                            String.format(
                                Locale.US,
                                "RM %.2f/%s",
                                displayPrice,
                                if (
                                    billingCycle ==
                                    "Yearly"
                                ) {
                                    "year"
                                } else {
                                    "month"
                                }
                            ),
                        color =
                            HarvestGreen,
                        fontWeight =
                            FontWeight.Bold
                    )

                    if (
                        foodBox.description
                            .isNotBlank()
                    ) {
                        Spacer(
                            modifier =
                                Modifier.height(
                                    4.dp
                                )
                        )

                        Text(
                            text =
                                foodBox.description,
                            style =
                                MaterialTheme
                                    .typography
                                    .bodySmall,
                            color =
                                Color.Gray,
                            maxLines = 2
                        )
                    }
                }

                Icon(
                    imageVector =
                        Icons.Default
                            .ChevronRight,
                    contentDescription =
                        "View food box",
                    tint =
                        HarvestGreen
                )
            }
        }
    }
}