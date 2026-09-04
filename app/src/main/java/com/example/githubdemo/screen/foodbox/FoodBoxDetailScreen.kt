package com.example.githubdemo.screen.foodbox

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubdemo.data.FoodBoxData
import com.example.githubdemo.model.BillingCycle
import com.example.githubdemo.viewmodel.foodbox.FoodBoxViewModel

@Composable
fun FoodBoxDetailScreen(
    planId: String,
    foodBoxViewModel: FoodBoxViewModel,
    onBackClick: () -> Unit,
    onCustomizeClick: () -> Unit,
    onManageClick: () -> Unit
) {
    val plan =
        FoodBoxData.getPlan(planId)
            ?: return

    val state by
    foodBoxViewModel.uiState

    val cycle =
        state.billingCycle

    val alreadySubscribed =
        state.activeSubscription != null

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        FoodBoxFlowHeader(
            title = plan.name,
            currentStep = 2,
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier =
                Modifier.fillMaxSize(),

            contentPadding =
                PaddingValues(20.dp),

            verticalArrangement =
                Arrangement.spacedBy(
                    18.dp
                )
        ) {
            item {
                Card(
                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(
                            24.dp
                        ),

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                FoodBoxSoftGreen
                        )
                ) {
                    Column(
                        modifier =
                            Modifier.padding(
                                22.dp
                            )
                    ) {
                        val price =
                            if (
                                cycle ==
                                BillingCycle.MONTHLY
                            ) {
                                plan.monthlyPrice
                            } else {
                                plan.yearlyPrice
                            }

                        Text(
                            text =
                                formatMoney(price) +
                                        if (
                                            cycle ==
                                            BillingCycle.MONTHLY
                                        ) {
                                            "/month"
                                        } else {
                                            "/year"
                                        },

                            color =
                                FoodBoxPrimaryGreen,

                            fontSize = 30.sp,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            text =
                                "Suitable for " +
                                        plan.suitablePax,

                            color =
                                FoodBoxSecondaryText,

                            fontSize = 17.sp
                        )
                    }
                }
            }

            item {
                Card(
                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(
                            22.dp
                        ),

                    border =
                        BorderStroke(
                            1.dp,
                            FoodBoxBorder
                        ),

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                Color.White
                        )
                ) {
                    Column(
                        modifier =
                            Modifier.padding(
                                20.dp
                            )
                    ) {
                        Text(
                            text =
                                "Weekly Nutrition",

                            color =
                                FoodBoxMainText,

                            fontSize = 21.sp,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Spacer(
                            modifier =
                                Modifier.height(
                                    16.dp
                                )
                        )

                        Row(
                            modifier =
                                Modifier.fillMaxWidth(),

                            horizontalArrangement =
                                Arrangement
                                    .SpaceEvenly
                        ) {
                            NutritionValue(
                                value =
                                    plan.calories,

                                label =
                                    "Calories"
                            )

                            NutritionValue(
                                value =
                                    plan.protein,

                                label =
                                    "Protein"
                            )

                            NutritionValue(
                                value =
                                    plan.vitamins,

                                label =
                                    "Vitamins"
                            )
                        }
                    }
                }
            }

            item {
                Card(
                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(
                            22.dp
                        ),

                    border =
                        BorderStroke(
                            1.dp,
                            FoodBoxBorder
                        ),

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                Color.White
                        )
                ) {
                    Column {
                        Text(
                            text =
                                "What's Inside",

                            modifier =
                                Modifier.padding(
                                    18.dp
                                ),

                            color =
                                FoodBoxMainText,

                            fontSize = 21.sp,

                            fontWeight =
                                FontWeight.Bold
                        )

                        plan.items
                            .forEachIndexed {
                                    index,
                                    item ->

                                FoodBoxItemRow(
                                    item = item
                                )

                                if (
                                    index <
                                    plan.items
                                        .lastIndex
                                ) {
                                    HorizontalDivider(
                                        modifier =
                                            Modifier.padding(
                                                horizontal =
                                                    16.dp
                                            ),

                                        color =
                                            FoodBoxBorder
                                    )
                                }
                            }
                    }
                }
            }

            item {
                if (alreadySubscribed) {
                    FoodBoxMessage(
                        message =
                            "You already have an active Food Box " +
                                    "subscription. You can manage or " +
                                    "cancel it before subscribing to " +
                                    "another box."
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                12.dp
                            )
                    )
                }

                FoodBoxPrimaryButton(
                    text =
                        if (alreadySubscribed) {
                            "Manage Current Subscription"
                        } else {
                            "Customize This Box"
                        },

                    onClick =
                        if (alreadySubscribed) {
                            onManageClick
                        } else {
                            onCustomizeClick
                        }
                )
            }
        }
    }
}

@Composable
private fun NutritionValue(
    value: String,
    label: String
) {
    Column(
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            color = FoodBoxMainText,
            fontSize = 20.sp,
            fontWeight =
                FontWeight.Bold
        )

        Text(
            text = label,
            color =
                FoodBoxSecondaryText,
            fontSize = 13.sp
        )
    }
}