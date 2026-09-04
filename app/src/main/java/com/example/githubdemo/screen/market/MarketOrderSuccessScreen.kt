package com.example.githubdemo.screen.market

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubdemo.model.PaymentMethod
import com.example.githubdemo.model.market.MarketOrderSummary
import com.example.githubdemo.ui.theme.PrimaryGreen

@Composable
fun MarketOrderSuccessScreen(
    summary: MarketOrderSummary,
    onContinueShopping: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFFF4F6EE)
            )
            .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(108.dp)
                .background(
                    color =
                        Color(0xFFE3F3E8),

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
                    "Order successful",

                tint =
                    PrimaryGreen,

                modifier =
                    Modifier.size(64.dp)
            )
        }

        Spacer(
            modifier =
                Modifier.height(24.dp)
        )

        Text(
            text =
                "Order Successful!",

            color =
                Color(0xFF173B2E),

            fontSize =
                30.sp,

            fontWeight =
                FontWeight.Bold,

            textAlign =
                TextAlign.Center
        )

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        Text(
            text =
                "Your market order has been placed. " +
                        "The farmer can now see it in Farmer Orders.",

            color =
                Color.Gray,

            style =
                MaterialTheme.typography
                    .bodyLarge,

            textAlign =
                TextAlign.Center
        )

        Spacer(
            modifier =
                Modifier.height(22.dp)
        )

        Row(
            horizontalArrangement =
                Arrangement.spacedBy(
                    8.dp
                )
        ) {
            MarketSuccessChip(
                text =
                    "${summary.itemCount} item" +
                            if (
                                summary.itemCount == 1
                            ) {
                                ""
                            } else {
                                "s"
                            }
            )

            MarketSuccessChip(
                text =
                    summary
                        .paymentMethod
                        .displayName()
            )
        }

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        MarketSuccessChip(
            text =
                "RM %.2f".format(
                    summary.totalPrice
                )
        )

        Spacer(
            modifier =
                Modifier.height(32.dp)
        )

        Button(
            onClick =
                onContinueShopping,

            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),

            shape =
                RoundedCornerShape(
                    16.dp
                ),

            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        PrimaryGreen
                )
        ) {
            Text(
                text =
                    "Continue Shopping",

                color =
                    Color.White,

                fontWeight =
                    FontWeight.Bold,

                fontSize =
                    17.sp
            )
        }
    }
}

@Composable
private fun MarketSuccessChip(
    text: String
) {
    Surface(
        shape =
            RoundedCornerShape(50),

        color =
            Color(0xFFE3F3E8),

        border =
            BorderStroke(
                width = 1.dp,

                color =
                    Color(0xFFBFD7C7)
            )
    ) {
        Text(
            text =
                text,

            modifier =
                Modifier.padding(
                    horizontal = 12.dp,

                    vertical = 7.dp
                ),

            color =
                PrimaryGreen,

            fontWeight =
                FontWeight.SemiBold,

            maxLines = 1
        )
    }
}

private fun PaymentMethod
        .displayName(): String {

    return when (this) {
        PaymentMethod.E_WALLET ->
            "Touch 'n Go"

        PaymentMethod.ONLINE_BANKING ->
            "Online Banking"

        PaymentMethod.CARD ->
            "Card"
    }
}