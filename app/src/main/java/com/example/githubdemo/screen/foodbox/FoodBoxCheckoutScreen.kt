package com.example.githubdemo.screen.foodbox

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.githubdemo.model.BillingCycle
import com.example.githubdemo.model.PaymentMethod
import com.example.githubdemo.viewmodel.foodbox.FoodBoxPaymentViewModel
import com.example.githubdemo.viewmodel.foodbox.FoodBoxViewModel

@Composable
fun FoodBoxCheckoutScreen(
    foodBoxViewModel: FoodBoxViewModel,
    onBackClick: () -> Unit,
    onSubscribeClick: () -> Unit,
    paymentViewModel: FoodBoxPaymentViewModel =
        viewModel()
) {
    val foodBoxState by
    foodBoxViewModel.uiState

    val paymentState by
    paymentViewModel
        .uiState
        .collectAsState()

    val selectedPlan =
        foodBoxViewModel
            .getSelectedPlan()

    Column(
        modifier =
            Modifier.fillMaxSize()
    ) {
        FoodBoxFlowHeader(
            title = "Checkout",
            currentStep = 5,
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier =
                Modifier.fillMaxSize(),

            contentPadding =
                PaddingValues(20.dp),

            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {
            item {
                FoodBoxOrderSummary(
                    planName =
                        selectedPlan
                            ?.name
                            .orEmpty(),

                    suitablePax =
                        selectedPlan
                            ?.suitablePax
                            .orEmpty(),

                    billingCycle =
                        foodBoxState
                            .billingCycle,

                    deliveryDay =
                        foodBoxState
                            .deliveryDay,

                    boxPrice =
                        foodBoxViewModel
                            .getBasePrice(),

                    addOnPrice =
                        foodBoxViewModel
                            .getAddOnPrice(),

                    totalPrice =
                        foodBoxViewModel
                            .getTotalPrice()
                )
            }

            item {
                Text(
                    text =
                        "Payment Method",

                    color =
                        FoodBoxMainText,

                    fontSize =
                        22.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }

            item {
                FoodBoxPaymentOption(
                    title =
                        "E-Wallet",

                    subtitle =
                        "Touch 'n Go only",

                    icon =
                        Icons.Outlined
                            .AccountBalanceWallet,

                    selected =
                        paymentState
                            .selectedMethod ==
                                PaymentMethod
                                    .E_WALLET,

                    onClick = {
                        paymentViewModel
                            .selectPaymentMethod(
                                PaymentMethod
                                    .E_WALLET
                            )
                    }
                )
            }

            if (
                paymentState.selectedMethod ==
                PaymentMethod.E_WALLET
            ) {
                item {
                    FoodBoxCredentialCard(
                        heading =
                            "Touch 'n Go details"
                    ) {
                        FoodBoxCredentialField(
                            value =
                                paymentState
                                    .phoneNumber,

                            onValueChange =
                                paymentViewModel::
                                updatePhoneNumber,

                            label =
                                "Phone number",

                            placeholder =
                                "0123456789",

                            keyboardType =
                                KeyboardType.Phone,

                            error =
                                paymentState
                                    .phoneError
                        )

                        FoodBoxPasswordField(
                            value =
                                paymentState
                                    .eWalletPassword,

                            onValueChange =
                                paymentViewModel::
                                updateEWalletPassword,

                            error =
                                paymentState
                                    .eWalletPasswordError
                        )
                    }
                }
            }

            item {
                FoodBoxPaymentOption(
                    title =
                        "Online Banking",

                    subtitle =
                        "Account number and 6-digit password",

                    icon =
                        Icons.Outlined
                            .AccountBalance,

                    selected =
                        paymentState
                            .selectedMethod ==
                                PaymentMethod
                                    .ONLINE_BANKING,

                    onClick = {
                        paymentViewModel
                            .selectPaymentMethod(
                                PaymentMethod
                                    .ONLINE_BANKING
                            )
                    }
                )
            }

            if (
                paymentState.selectedMethod ==
                PaymentMethod.ONLINE_BANKING
            ) {
                item {
                    FoodBoxCredentialCard(
                        heading =
                            "Online banking details"
                    ) {
                        FoodBoxCredentialField(
                            value =
                                paymentState
                                    .accountNumber,

                            onValueChange =
                                paymentViewModel::
                                updateAccountNumber,

                            label =
                                "Account number",

                            placeholder =
                                "Enter 8 to 18 digits",

                            keyboardType =
                                KeyboardType.Number,

                            error =
                                paymentState
                                    .accountError
                        )

                        FoodBoxPasswordField(
                            value =
                                paymentState
                                    .bankingPassword,

                            onValueChange =
                                paymentViewModel::
                                updateBankingPassword,

                            error =
                                paymentState
                                    .bankingPasswordError
                        )
                    }
                }
            }

            item {
                FoodBoxPaymentOption(
                    title =
                        "Credit / Debit Card",

                    subtitle =
                        "16-digit card number and password",

                    icon =
                        Icons.Outlined
                            .CreditCard,

                    selected =
                        paymentState
                            .selectedMethod ==
                                PaymentMethod.CARD,

                    onClick = {
                        paymentViewModel
                            .selectPaymentMethod(
                                PaymentMethod.CARD
                            )
                    }
                )
            }

            if (
                paymentState.selectedMethod ==
                PaymentMethod.CARD
            ) {
                item {
                    FoodBoxCredentialCard(
                        heading = "Card details"
                    ) {
                        FoodBoxCredentialField(
                            value =
                                paymentState
                                    .cardNumber,

                            onValueChange =
                                paymentViewModel::
                                updateCardNumber,

                            label =
                                "Card number",

                            placeholder =
                                "1234 5678 9012 3456",

                            keyboardType =
                                KeyboardType.Number,

                            error =
                                paymentState
                                    .cardError
                        )

                        FoodBoxPasswordField(
                            value =
                                paymentState
                                    .cardPassword,

                            onValueChange =
                                paymentViewModel::
                                updateCardPassword,

                            error =
                                paymentState
                                    .cardPasswordError
                        )
                    }
                }
            }

            item {
                FoodBoxPaymentNotice()
            }

            if (
                !paymentState.message
                    .isNullOrBlank()
            ) {
                item {
                    FoodBoxMessage(
                        message =
                            paymentState.message
                    )
                }
            }

            if (
                !foodBoxState.message
                    .isNullOrBlank()
            ) {
                item {
                    FoodBoxMessage(
                        message =
                            foodBoxState.message
                    )
                }
            }

            item {
                FoodBoxPrimaryButton(
                    text =
                        if (
                            foodBoxState
                                .isLoading
                        ) {
                            "Saving..."
                        } else {
                            "Confirm & Subscribe"
                        },

                    enabled =
                        !foodBoxState
                            .isLoading,

                    onClick = {
                        val paymentMethod =
                            paymentViewModel
                                .validateForSubmission()

                        if (
                            paymentMethod != null
                        ) {
                            foodBoxViewModel
                                .selectPaymentMethod(
                                    paymentMethod
                                )

                            onSubscribeClick()
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun FoodBoxOrderSummary(
    planName: String,
    suitablePax: String,
    billingCycle: BillingCycle,
    deliveryDay: String,
    boxPrice: Double,
    addOnPrice: Double,
    totalPrice: Double
) {
    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(22.dp),

        border =
            BorderStroke(
                width = 1.dp,
                color = FoodBoxBorder
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
            Text(
                text =
                    "Order Summary",

                color =
                    FoodBoxMainText,

                fontSize =
                    22.sp,

                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            Text(
                text = planName,

                color =
                    FoodBoxMainText,

                fontSize =
                    20.sp,

                fontWeight =
                    FontWeight.Bold
            )

            Text(
                text =
                    "$suitablePax · " +
                            if (
                                billingCycle ==
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
                text = deliveryDay,

                color =
                    FoodBoxSecondaryText
            )

            HorizontalDivider(
                modifier =
                    Modifier.padding(
                        vertical = 16.dp
                    ),

                color =
                    FoodBoxBorder
            )

            FoodBoxPriceRow(
                label =
                    "Box price",

                amount =
                    boxPrice
            )

            FoodBoxPriceRow(
                label =
                    "Add-ons",

                amount =
                    addOnPrice
            )

            FoodBoxPriceRow(
                label =
                    "Delivery",

                amount =
                    0.0,

                free =
                    true
            )

            HorizontalDivider(
                modifier =
                    Modifier.padding(
                        vertical = 12.dp
                    ),

                color =
                    FoodBoxBorder
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth()
            ) {
                Text(
                    text =
                        "Total",

                    modifier =
                        Modifier.weight(1f),

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
                            totalPrice
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

@Composable
private fun FoodBoxPriceRow(
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
private fun FoodBoxPaymentOption(
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
                width =
                    if (selected) {
                        1.5.dp
                    } else {
                        1.dp
                    },

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

                    fontSize =
                        18.sp,

                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text = subtitle,

                    color =
                        FoodBoxSecondaryText,

                    fontSize =
                        13.sp
                )
            }

            if (selected) {
                Icon(
                    imageVector =
                        Icons.Default
                            .CheckCircle,

                    contentDescription =
                        "Selected",

                    tint =
                        FoodBoxPrimaryGreen
                )
            }
        }
    }
}

@Composable
private fun FoodBoxCredentialCard(
    heading: String,

    content:
    @Composable
    ColumnScope.() -> Unit
) {
    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(20.dp),

        border =
            BorderStroke(
                width = 1.dp,
                color = FoodBoxBorder
            ),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            verticalArrangement =
                Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = heading,

                color =
                    FoodBoxMainText,

                fontWeight =
                    FontWeight.Bold
            )

            content()
        }
    }
}

@Composable
private fun FoodBoxCredentialField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardType: KeyboardType,
    error: String?
) {
    OutlinedTextField(
        value = value,

        onValueChange =
            onValueChange,

        label = {
            Text(label)
        },

        placeholder = {
            Text(placeholder)
        },

        keyboardOptions =
            KeyboardOptions(
                keyboardType =
                    keyboardType
            ),

        singleLine =
            true,

        isError =
            error != null,

        supportingText = {
            if (error != null) {
                Text(error)
            }
        },

        modifier =
            Modifier.fillMaxWidth()
    )
}

@Composable
private fun FoodBoxPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    error: String?
) {
    var passwordVisible by remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        value = value,

        onValueChange =
            onValueChange,

        label = {
            Text(
                "6-digit password"
            )
        },

        placeholder = {
            Text("123456")
        },

        visualTransformation =
            if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },

        keyboardOptions =
            KeyboardOptions(
                keyboardType =
                    KeyboardType.NumberPassword
            ),

        trailingIcon = {
            IconButton(
                onClick = {
                    passwordVisible =
                        !passwordVisible
                }
            ) {
                Icon(
                    imageVector =
                        if (passwordVisible) {
                            Icons.Default
                                .VisibilityOff
                        } else {
                            Icons.Default
                                .Visibility
                        },

                    contentDescription =
                        if (passwordVisible) {
                            "Hide password"
                        } else {
                            "Show password"
                        }
                )
            }
        },

        singleLine =
            true,

        isError =
            error != null,

        supportingText = {
            if (error != null) {
                Text(error)
            } else {
                Text(
                    "${value.length}/6 digits"
                )
            }
        },

        modifier =
            Modifier.fillMaxWidth()
    )
}

@Composable
private fun FoodBoxPaymentNotice() {
    Surface(
        modifier =
            Modifier.fillMaxWidth(),

        color =
            Color(0xFFFFF5DE),

        shape =
            RoundedCornerShape(14.dp)
    ) {
        Text(
            text =
                "Enter " +
                        "e-wallet, bank or card password. " +
                        "These payment details are not saved " +
                        "locally or sent to Supabase.",

            modifier =
                Modifier.padding(14.dp),

            color =
                Color(0xFF795500),

            style =
                MaterialTheme
                    .typography
                    .bodySmall
        )
    }
}