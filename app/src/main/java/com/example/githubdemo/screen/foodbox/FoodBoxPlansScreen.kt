package com.example.githubdemo.screen.foodbox

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import com.example.githubdemo.model.FoodBoxPlan
import com.example.githubdemo.viewmodel.foodbox.FoodBoxViewModel

@Composable
fun FoodBoxPlansScreen(
    foodBoxViewModel: FoodBoxViewModel,
    onViewPlanClick: (String) -> Unit,
    onManageClick: () -> Unit
) {
    val state by
    foodBoxViewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        FoodBoxFlowHeader(
            title = "Food Box Plans",
            currentStep = 1,
            onManageClick =
                onManageClick
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
                BillingCycleSelector(
                    selected =
                        state.billingCycle,

                    onSelected =
                        foodBoxViewModel::
                        selectBillingCycle
                )

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                Text(
                    text =
                        "Choose your box",

                    color =
                        FoodBoxMainText,

                    fontSize = 24.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }

            items(
                items =
                    FoodBoxData.plans,

                key = {
                        plan ->
                    plan.id
                }
            ) { plan ->
                PlanCard(
                    plan = plan,

                    billingCycle =
                        state.billingCycle,

                    onClick = {
                        onViewPlanClick(
                            plan.id
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun BillingCycleSelector(
    selected: BillingCycle,
    onSelected: (BillingCycle) -> Unit
) {
    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(20.dp),

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
        Row(
            modifier =
                Modifier.padding(12.dp),

            horizontalArrangement =
                Arrangement.spacedBy(
                    10.dp
                )
        ) {
            CycleButton(
                text = "Monthly",

                selected =
                    selected ==
                            BillingCycle.MONTHLY,

                modifier =
                    Modifier.weight(1f),

                onClick = {
                    onSelected(
                        BillingCycle.MONTHLY
                    )
                }
            )

            CycleButton(
                text = "Yearly",

                selected =
                    selected ==
                            BillingCycle.YEARLY,

                modifier =
                    Modifier.weight(1f),

                onClick = {
                    onSelected(
                        BillingCycle.YEARLY
                    )
                }
            )
        }
    }
}

@Composable
private fun CycleButton(
    text: String,
    selected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,

        modifier = modifier,

        shape =
            RoundedCornerShape(50),

        colors =
            ButtonDefaults.buttonColors(
                containerColor =
                    if (selected) {
                        FoodBoxPrimaryGreen
                    } else {
                        Color(0xFFEDE9DF)
                    },

                contentColor =
                    if (selected) {
                        Color.White
                    } else {
                        FoodBoxSecondaryText
                    }
            ),

        elevation =
            ButtonDefaults
                .buttonElevation(0.dp)
    ) {
        Text(
            text = text,
            fontWeight =
                FontWeight.Bold
        )
    }
}

@Composable
private fun PlanCard(
    plan: FoodBoxPlan,
    billingCycle: BillingCycle,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            ),

        shape =
            RoundedCornerShape(24.dp),

        border =
            BorderStroke(
                1.dp,
                FoodBoxBorder
            ),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            ),

        elevation =
            CardDefaults
                .cardElevation(3.dp)
    ) {
        Column {
            Box {
                FoodBoxArtwork(
                    modifier =
                        Modifier.height(170.dp)
                )

                Column(
                    modifier = Modifier
                        .align(
                            Alignment.BottomStart
                        )
                        .padding(18.dp)
                ) {
                    Text(
                        text = plan.name,
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(
                        text =
                            plan.description,
                        color = Color.White,
                        fontSize = 15.sp
                    )
                }

                Surface(
                    modifier = Modifier
                        .align(
                            Alignment.TopEnd
                        )
                        .padding(14.dp),

                    shape =
                        RoundedCornerShape(50),

                    color =
                        if (
                            plan.badge ==
                            "Best Value"
                        ) {
                            FoodBoxOrange
                        } else {
                            FoodBoxPrimaryGreen
                        }
                ) {
                    Text(
                        text = plan.badge,

                        modifier =
                            Modifier.padding(
                                horizontal =
                                    12.dp,

                                vertical =
                                    5.dp
                            ),

                        color =
                            Color.White,

                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                val price =
                    if (
                        billingCycle ==
                        BillingCycle.MONTHLY
                    ) {
                        plan.monthlyPrice
                    } else {
                        plan.yearlyPrice
                    }

                Text(
                    text =
                        formatMoney(price),

                    color =
                        FoodBoxPrimaryGreen,

                    fontSize = 28.sp,

                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text =
                        if (
                            billingCycle ==
                            BillingCycle.MONTHLY
                        ) {
                            "/month"
                        } else {
                            "/year"
                        },

                    color =
                        FoodBoxSecondaryText,

                    fontSize = 17.sp
                )

                Spacer(
                    modifier =
                        Modifier.weight(1f)
                )

                Text(
                    text = "View",
                    color =
                        FoodBoxPrimaryGreen,
                    fontWeight =
                        FontWeight.Bold
                )

                Icon(
                    imageVector =
                        Icons.Default
                            .KeyboardArrowRight,

                    contentDescription =
                        null,

                    tint =
                        FoodBoxPrimaryGreen
                )
            }
        }
    }
}