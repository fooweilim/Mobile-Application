package com.example.githubdemo.screen.foodbox

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubdemo.model.BillingCycle
import com.example.githubdemo.model.PaymentMethod
import com.example.githubdemo.viewmodel.foodbox.FoodBoxViewModel

@Composable
fun FoodBoxCheckoutScreen(
    foodBoxViewModel: FoodBoxViewModel,
    onBackClick: () -> Unit,
    onSubscribeClick: () -> Unit
) {
    val state by
    foodBoxViewModel.uiState

    val plan =
        foodBoxViewModel
            .getSelectedPlan()

    Column(
        modifier =
            Modifier.fillMaxSize()
    ) {
        FoodBoxFlowHeader(
            title = "Checkout",
            currentStep = 5,
            onBackClick =
                onBackClick
        )

        LazyColumn(
            modifier =
                Modifier.fillMaxSize(),

            contentPadding =
                PaddingValues(20.dp),

            verticalArrangement =
                Arrangement.spacedBy(
                    16.dp
                )
        ) {
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
                                "Order Summary",

                            color =
                                FoodBoxMainText,

                            fontSize = 22.sp,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Spacer(
                            modifier =
                                Modifier.height(
                                    14.dp
                                )
                        )

                        Text(
                            text =
                                plan?.name.orEmpty(),

                            color =
                                FoodBoxMainText,

                            fontSize = 20.sp,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            text =
                                "${plan?.suitablePax.orEmpty()} · " +
                                        if (
                                            state.billingCycle ==
                                            BillingCycle.MONTHLY
                                        ) {
                                            "Monthly"
                                        } else {
                                            "Yearly"
                                        },

                            color =
                                FoodBoxSecondaryText
                        )

                        Text(
                            text =
                                state.deliveryDay,

                            color =
                                FoodBoxSecondaryText
                        )

                        HorizontalDivider(
                            modifier =
                                Modifier.padding(
                                    vertical =
                                        16.dp
                                ),

                            color =
                                FoodBoxBorder
                        )

                        PriceRow(
                            label =
                                "Box price",

                            amount =
                                foodBoxViewModel
                                    .getBasePrice()
                        )

                        PriceRow(
                            label =
                                "Add-ons",

                            amount =
                                foodBoxViewModel
                                    .getAddOnPrice()
                        )

                        PriceRow(
                            label =
                                "Delivery",

                            amount = 0.0,

                            free = true
                        )

                        HorizontalDivider(
                            modifier =
                                Modifier.padding(
                                    vertical =
                                        12.dp
                                ),

                            color =
                                FoodBoxBorder
                        )

                        Row(
                            modifier =
                                Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Total",

                                modifier =
                                    Modifier.weight(
                                        1f
                                    ),

                                color =
                                    FoodBoxMainText,

                                fontSize =
                                    20.sp,

                                fontWeight =
                                    FontWeight.Bold
                            )

                            Text(
                                text =
                                    formatMoney(
                                        foodBoxViewModel
                                            .getTotalPrice()
                                    ),

                                color =
                                    FoodBoxPrimaryGreen,

                                fontSize =
                                    22.sp,

                                fontWeight =
                                    FontWeight.Bold
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text =
                        "Payment Method",

                    color =
                        FoodBoxMainText,

                    fontSize = 22.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }

            item {
                PaymentOption(
                    title =
                        "E-Wallet",

                    subtitle =
                        "Touch 'n Go, Boost, GrabPay",

                    icon =
                        Icons.Outlined
                            .AccountBalanceWallet,

                    selected =
                        state.paymentMethod ==
                                PaymentMethod
                                    .E_WALLET,

                    onClick = {
                        foodBoxViewModel
                            .selectPaymentMethod(
                                PaymentMethod
                                    .E_WALLET
                            )
                    }
                )
            }

            item {
                PaymentOption(
                    title =
                        "Online Banking (FPX)",

                    subtitle =
                        "Maybank, CIMB, RHB",

                    icon =
                        Icons.Outlined
                            .AccountBalance,

                    selected =
                        state.paymentMethod ==
                                PaymentMethod
                                    .ONLINE_BANKING,

                    onClick = {
                        foodBoxViewModel
                            .selectPaymentMethod(
                                PaymentMethod
                                    .ONLINE_BANKING
                            )
                    }
                )
            }

            item {
                PaymentOption(
                    title =
                        "Credit / Debit Card",

                    subtitle =
                        "Visa, Mastercard",

                    icon =
                        Icons.Outlined
                            .CreditCard,

                    selected =
                        state.paymentMethod ==
                                PaymentMethod.CARD,

                    onClick = {
                        foodBoxViewModel
                            .selectPaymentMethod(
                                PaymentMethod.CARD
                            )
                    }
                )
            }

            if (
                !state.message
                    .isNullOrBlank()
            ) {
                item {
                    FoodBoxMessage(
                        message =
                            state.message
                    )
                }
            }

            item {
                FoodBoxPrimaryButton(
                    text =
                        if (state.isLoading) {
                            "Saving..."
                        } else {
                            "Confirm & Subscribe"
                        },

                    enabled =
                        !state.isLoading,

                    onClick =
                        onSubscribeClick
                )
            }
        }
    }
}

@Composable
private fun PriceRow(
    label: String,
    amount: Double,
    free: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 4.dp
            )
    ) {
        Text(
            text = label,

            modifier =
                Modifier.weight(1f),

            color =
                FoodBoxSecondaryText
        )

        Text(
            text =
                if (free) {
                    "Free"
                } else {
                    formatMoney(amount)
                },

            color =
                FoodBoxMainText,

            fontWeight =
                FontWeight.SemiBold
        )
    }
}

@Composable
private fun PaymentOption(
    title: String,
    subtitle: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            ),

        shape =
            RoundedCornerShape(20.dp),

        border =
            BorderStroke(
                width = 1.dp,

                color =
                    if (selected) {
                        FoodBoxPrimaryGreen
                    } else {
                        FoodBoxBorder
                    }
            ),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    if (selected) {
                        FoodBoxSoftGreen
                    } else {
                        Color.White
                    }
            )
    ) {
        Row(
            modifier =
                Modifier.padding(18.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint =
                    FoodBoxPrimaryGreen,
                modifier =
                    Modifier.size(30.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        start = 14.dp
                    )
            ) {
                Text(
                    text = title,
                    color =
                        FoodBoxMainText,
                    fontSize = 18.sp,
                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text = subtitle,
                    color =
                        FoodBoxSecondaryText,
                    fontSize = 13.sp
                )
            }

            if (selected) {
                Icon(
                    imageVector =
                        Icons.Default
                            .CheckCircle,

                    contentDescription =
                        null,

                    tint =
                        FoodBoxPrimaryGreen
                )
            }
        }
    }
}