package com.example.githubdemo.screen.foodbox

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubdemo.model.BillingCycle
import com.example.githubdemo.viewmodel.foodbox.FoodBoxViewModel

@Composable
fun SubscriptionSuccessScreen(
    foodBoxViewModel: FoodBoxViewModel,
    onManageClick: () -> Unit,
    onBrowseMoreClick: () -> Unit
) {
    val state by
    foodBoxViewModel.uiState

    val subscription =
        state.activeSubscription

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                FoodBoxPageBackground
            )
            .padding(18.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(104.dp)
                .background(
                    color =
                        FoodBoxSoftGreen,

                    shape =
                        CircleShape
                ),

            contentAlignment =
                Alignment.Center
        ) {
            Icon(
                imageVector =
                    Icons.Default.Check,

                contentDescription =
                    "Subscribed",

                tint =
                    FoodBoxPrimaryGreen,

                modifier =
                    Modifier.size(62.dp)
            )
        }

        Spacer(
            modifier =
                Modifier.height(22.dp)
        )

        Text(
            text = "Subscribed!",
            color = FoodBoxMainText,
            fontSize = 30.sp,
            fontWeight =
                FontWeight.Bold
        )

        Text(
            text =
                "Your " +
                        (subscription?.planName
                            ?: "Food Box") +
                        " is confirmed.",

            color =
                FoodBoxSecondaryText,

            fontSize = 16.sp,

            textAlign =
                TextAlign.Center
        )

        if (subscription != null) {
            Text(
                text =
                    "First delivery: Next " +
                            subscription
                                .deliveryDay,

                color =
                    FoodBoxPrimaryGreen,

                fontSize = 17.sp,

                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(18.dp)
            )

            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(
                        7.dp
                    )
            ) {
                SuccessChip(
                    text =
                        subscription.planName
                )

                SuccessChip(
                    text =
                        if (
                            subscription
                                .billingCycle ==
                            BillingCycle.MONTHLY
                        ) {
                            "Monthly"
                        } else {
                            "Yearly"
                        }
                )

                SuccessChip(
                    text =
                        formatMoney(
                            subscription
                                .totalPrice
                        )
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(30.dp)
        )

        FoodBoxPrimaryButton(
            text =
                "Manage My Subscription",

            onClick =
                onManageClick
        )

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        FoodBoxOutlineButton(
            text =
                "Browse More Boxes",

            onClick =
                onBrowseMoreClick
        )
    }
}

@Composable
private fun SuccessChip(
    text: String
) {
    Surface(
        shape =
            RoundedCornerShape(50),

        color =
            FoodBoxSoftGreen,

        border =
            BorderStroke(
                1.dp,
                FoodBoxBorder
            )
    ) {
        Text(
            text = text,

            modifier =
                Modifier.padding(
                    horizontal = 10.dp,
                    vertical = 6.dp
                ),

            color =
                FoodBoxPrimaryGreen,

            fontSize = 11.sp,

            fontWeight =
                FontWeight.SemiBold,

            maxLines = 1
        )
    }
}