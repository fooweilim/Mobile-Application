package com.example.githubdemo.screen.market

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.githubdemo.data.local.LocalAccountStorage
import com.example.githubdemo.data.market.SelectedCartManager
import com.example.githubdemo.model.PaymentMethod
import com.example.githubdemo.ui.theme.PrimaryGreen
import com.example.githubdemo.viewmodel.market.CartViewModel
import com.example.githubdemo.viewmodel.market.MarketPaymentViewModel

private val MarketPaymentBackground =
    Color(0xFFF4F6EE)

private val MarketPaymentBorder =
    Color(0xFFD8E3DC)

@Composable
fun MarketPaymentScreen(
    onBack: () -> Unit = {},
    onPaymentSuccess: () -> Unit = {},
    cartViewModel: CartViewModel,
    paymentViewModel: MarketPaymentViewModel =
        viewModel()
) {
    val context =
        LocalContext.current

    val profile =
        remember(context) {
            LocalAccountStorage
                .getProfile(
                    context
                )
        }

    val selectedItems =
        remember {
            SelectedCartManager
                .getSelectedCart()
        }

    val uiState by
    paymentViewModel
        .uiState
        .collectAsState()

    val orderSummary =
        uiState.orderSummary

    LaunchedEffect(
        profile?.phoneNumber
    ) {
        paymentViewModel
            .prefillPhoneNumber(
                profile
                    ?.phoneNumber
                    .orEmpty()
            )
    }

    LaunchedEffect(
        orderSummary
    ) {
        if (orderSummary != null) {
            SelectedCartManager.clear()

            cartViewModel.loadCart()
        }
    }

    if (orderSummary != null) {
        MarketOrderSuccessScreen(
            summary =
                orderSummary,

            onContinueShopping = {
                paymentViewModel.reset()

                onPaymentSuccess()
            }
        )

        return
    }

    val total =
        selectedItems.sumOf {
            it.product.price *
                    it.quantity
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MarketPaymentBackground
            )
    ) {
        MarketPaymentHeader(
            onBack =
                onBack
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),

            contentPadding =
                PaddingValues(
                    16.dp
                ),

            verticalArrangement =
                Arrangement.spacedBy(
                    12.dp
                )
        ) {
            item {
                Text(
                    text =
                        "Order Summary",

                    style =
                        MaterialTheme
                            .typography
                            .titleLarge,

                    fontWeight =
                        FontWeight.Bold
                )
            }

            if (
                selectedItems.isEmpty()
            ) {
                item {
                    EmptyCheckoutCard()
                }
            }

            items(
                items =
                    selectedItems,

                key = {
                    it.cartId
                }
            ) { item ->
                MarketCheckoutItemCard(
                    name =
                        item.product.name,

                    imageUrl =
                        item.product.image_url,

                    quantity =
                        item.quantity,

                    lineTotal =
                        item.product.price *
                                item.quantity
                )
            }

            item {
                HorizontalDivider(
                    modifier =
                        Modifier.padding(
                            vertical = 4.dp
                        ),

                    color =
                        MarketPaymentBorder
                )

                Text(
                    text =
                        "Payment Method",

                    style =
                        MaterialTheme
                            .typography
                            .titleLarge,

                    fontWeight =
                        FontWeight.Bold
                )
            }

            item {
                MarketPaymentMethodCard(
                    title =
                        "E-Wallet",

                    subtitle =
                        "Touch 'n Go only",

                    icon =
                        Icons.Outlined
                            .AccountBalanceWallet,

                    selected =
                        uiState.selectedMethod ==
                                PaymentMethod.E_WALLET,

                    onClick = {
                        paymentViewModel
                            .selectPaymentMethod(
                                PaymentMethod.E_WALLET
                            )
                    }
                )
            }

            if (
                uiState.selectedMethod ==
                PaymentMethod.E_WALLET
            ) {
                item {
                    CredentialCard(
                        heading =
                            "Touch 'n Go details"
                    ) {
                        CredentialTextField(
                            value =
                                uiState.phoneNumber,

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
                                uiState.phoneError
                        )

                        PaymentPasswordField(
                            value =
                                uiState
                                    .eWalletPassword,

                            onValueChange =
                                paymentViewModel::
                                updateEWalletPassword,

                            error =
                                uiState
                                    .eWalletPasswordError
                        )
                    }
                }
            }

            item {
                MarketPaymentMethodCard(
                    title =
                        "Online Banking",

                    subtitle =
                        "FPX demo checkout",

                    icon =
                        Icons.Outlined
                            .AccountBalance,

                    selected =
                        uiState.selectedMethod ==
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
                uiState.selectedMethod ==
                PaymentMethod.ONLINE_BANKING
            ) {
                item {
                    CredentialCard(
                        heading =
                            "Online banking details"
                    ) {
                        CredentialTextField(
                            value =
                                uiState.accountNumber,

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
                                uiState.accountError
                        )

                        PaymentPasswordField(
                            value =
                                uiState
                                    .bankingPassword,

                            onValueChange =
                                paymentViewModel::
                                updateBankingPassword,

                            error =
                                uiState
                                    .bankingPasswordError
                        )
                    }
                }
            }

            item {
                MarketPaymentMethodCard(
                    title =
                        "Credit / Debit Card",

                    subtitle =
                        "16-digit card number",

                    icon =
                        Icons.Outlined
                            .CreditCard,

                    selected =
                        uiState.selectedMethod ==
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
                uiState.selectedMethod ==
                PaymentMethod.CARD
            ) {
                item {
                    CredentialCard(
                        heading =
                            "Card details"
                    ) {
                        CredentialTextField(
                            value =
                                uiState.cardNumber,

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
                                uiState.cardError
                        )

                        PaymentPasswordField(
                            value =
                                uiState.cardPassword,

                            onValueChange =
                                paymentViewModel::
                                updateCardPassword,

                            error =
                                uiState
                                    .cardPasswordError
                        )
                    }
                }
            }

            item {
                PaymentSecurityNotice()
            }

            if (
                !uiState.message
                    .isNullOrBlank()
            ) {
                item {
                    Text(
                        text =
                            uiState.message
                                .orEmpty(),

                        color =
                            MaterialTheme
                                .colorScheme
                                .error,

                        fontWeight =
                            FontWeight.SemiBold
                    )
                }
            }
        }

        MarketPaymentBottomSection(
            total =
                total,

            isLoading =
                uiState.isLoading,

            enabled =
                !uiState.isLoading &&
                        selectedItems
                            .isNotEmpty(),

            onPlaceOrder = {
                paymentViewModel
                    .submitOrder(
                        customerId =
                            profile
                                ?.id
                                .orEmpty(),

                        items =
                            selectedItems
                    )
            }
        )
    }
}

@Composable
private fun MarketPaymentHeader(
    onBack: () -> Unit
) {
    Surface(
        color =
            PrimaryGreen,

        shape =
            RoundedCornerShape(
                bottomStart = 35.dp,

                bottomEnd = 35.dp
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 35.dp,

                    start = 10.dp,

                    end = 16.dp,

                    bottom = 20.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {
            IconButton(
                onClick =
                    onBack
            ) {
                Icon(
                    imageVector =
                        Icons.Default
                            .ArrowBack,

                    contentDescription =
                        "Back",

                    tint =
                        Color.White
                )
            }

            Text(
                text =
                    "Payment",

                color =
                    Color.White,

                style =
                    MaterialTheme
                        .typography
                        .headlineMedium,

                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}

@Composable
private fun EmptyCheckoutCard() {
    Card(
        modifier =
            Modifier.fillMaxWidth(),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            )
    ) {
        Text(
            text =
                "Your checkout is empty. " +
                        "Return to the cart and select " +
                        "at least one item.",

            color =
                Color.Red,

            modifier =
                Modifier.padding(
                    16.dp
                )
        )
    }
}

@Composable
private fun MarketCheckoutItemCard(
    name: String,
    imageUrl: String?,
    quantity: Int,
    lineTotal: Double
) {
    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                18.dp
            ),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            )
    ) {
        Row(
            modifier =
                Modifier.padding(
                    12.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {
            AsyncImage(
                model =
                    imageUrl,

                contentDescription =
                    name,

                modifier =
                    Modifier.size(
                        76.dp
                    )
            )

            Spacer(
                modifier =
                    Modifier.width(
                        12.dp
                    )
            )

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {
                Text(
                    text =
                        name,

                    style =
                        MaterialTheme
                            .typography
                            .titleMedium,

                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text =
                        "Quantity: $quantity",

                    color =
                        Color.Gray
                )

                Text(
                    text =
                        "RM %.2f".format(
                            lineTotal
                        ),

                    color =
                        PrimaryGreen,

                    fontWeight =
                        FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun MarketPaymentMethodCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                18.dp
            ),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    if (selected) {
                        Color(0xFFE7F4EB)
                    } else {
                        Color.White
                    }
            ),

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
                        PrimaryGreen
                    } else {
                        MarketPaymentBorder
                    }
            ),

        onClick =
            onClick
    ) {
        Row(
            modifier =
                Modifier.padding(
                    14.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color =
                            Color(0xFFE7F4EB),

                        shape =
                            RoundedCornerShape(
                                12.dp
                            )
                    ),

                contentAlignment =
                    Alignment.Center
            ) {
                Icon(
                    imageVector =
                        icon,

                    contentDescription =
                        null,

                    tint =
                        PrimaryGreen
                )
            }

            Spacer(
                modifier =
                    Modifier.width(
                        12.dp
                    )
            )

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {
                Text(
                    text =
                        title,

                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text =
                        subtitle,

                    color =
                        Color.Gray,

                    style =
                        MaterialTheme
                            .typography
                            .bodySmall
                )
            }

            RadioButton(
                selected =
                    selected,

                onClick =
                    onClick,

                colors =
                    RadioButtonDefaults
                        .colors(
                            selectedColor =
                                PrimaryGreen
                        )
            )
        }
    }
}

@Composable
private fun CredentialCard(
    heading: String,

    content:
    @Composable
    ColumnScope.() -> Unit
) {
    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                18.dp
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
                .padding(
                    14.dp
                ),

            verticalArrangement =
                Arrangement.spacedBy(
                    4.dp
                )
        ) {
            Text(
                text =
                    heading,

                fontWeight =
                    FontWeight.Bold
            )

            content()
        }
    }
}

@Composable
private fun CredentialTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType,
    error: String?,
    placeholder: String = ""
) {
    OutlinedTextField(
        value =
            value,

        onValueChange =
            onValueChange,

        label = {
            Text(
                text =
                    label
            )
        },

        placeholder = {
            if (
                placeholder.isNotEmpty()
            ) {
                Text(
                    text =
                        placeholder
                )
            }
        },

        keyboardOptions =
            KeyboardOptions(
                keyboardType =
                    keyboardType
            ),

        singleLine = true,

        isError =
            error != null,

        supportingText = {
            if (error != null) {
                Text(
                    text =
                        error
                )
            }
        },

        modifier =
            Modifier.fillMaxWidth()
    )
}

@Composable
private fun PaymentPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    error: String?
) {
    var passwordVisible by
    remember {
        mutableStateOf(
            false
        )
    }

    OutlinedTextField(
        value =
            value,

        onValueChange =
            onValueChange,

        label = {
            Text(
                text =
                    "6-digit password"
            )
        },

        placeholder = {
            Text(
                text =
                    "123456"
            )
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
                        if (
                            passwordVisible
                        ) {
                            Icons.Default
                                .VisibilityOff
                        } else {
                            Icons.Default
                                .Visibility
                        },

                    contentDescription =
                        if (
                            passwordVisible
                        ) {
                            "Hide password"
                        } else {
                            "Show password"
                        }
                )
            }
        },

        singleLine = true,

        isError =
            error != null,

        supportingText = {
            if (error != null) {
                Text(
                    text =
                        error
                )
            } else {
                Text(
                    text =
                        "${value.length}/6 digits"
                )
            }
        },

        modifier =
            Modifier.fillMaxWidth()
    )
}

@Composable
private fun PaymentSecurityNotice() {
    Surface(
        modifier =
            Modifier.fillMaxWidth(),

        color =
            Color(0xFFFFF5DE),

        shape =
            RoundedCornerShape(
                14.dp
            )
    ) {
        Text(
            text =
                "Classroom demo only: never enter a real " +
                        "e-wallet, bank, or card password. " +
                        "The 6-digit value is checked only " +
                        "in memory and is not saved to Supabase.",

            modifier =
                Modifier.padding(
                    14.dp
                ),

            color =
                Color(0xFF795500),

            style =
                MaterialTheme
                    .typography
                    .bodySmall
        )
    }
}

@Composable
private fun MarketPaymentBottomSection(
    total: Double,
    isLoading: Boolean,
    enabled: Boolean,
    onPlaceOrder: () -> Unit
) {
    Surface(
        modifier =
            Modifier.fillMaxWidth(),

        color =
            Color.White,

        shadowElevation =
            8.dp,

        shape =
            RoundedCornerShape(
                topStart = 24.dp,

                topEnd = 24.dp
            )
    ) {
        Column(
            modifier =
                Modifier.padding(
                    16.dp
                )
        ) {
            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement
                        .SpaceBetween,

                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Text(
                    text =
                        "Total",

                    style =
                        MaterialTheme
                            .typography
                            .titleMedium,

                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text =
                        "RM %.2f".format(
                            total
                        ),

                    color =
                        PrimaryGreen,

                    style =
                        MaterialTheme
                            .typography
                            .titleLarge,

                    fontWeight =
                        FontWeight.Bold
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        10.dp
                    )
            )

            Button(
                onClick =
                    onPlaceOrder,

                enabled =
                    enabled,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        52.dp
                    ),

                shape =
                    RoundedCornerShape(
                        15.dp
                    ),

                colors =
                    ButtonDefaults
                        .buttonColors(
                            containerColor =
                                PrimaryGreen
                        )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier =
                            Modifier.size(
                                22.dp
                            ),

                        color =
                            Color.White,

                        strokeWidth =
                            2.dp
                    )
                } else {
                    Text(
                        text =
                            "Place Order",

                        color =
                            Color.White,

                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }
        }
    }
}