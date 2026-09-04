package com.example.githubdemo.screen.foodbox

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubdemo.model.FoodBoxItem

val FoodBoxDarkGreen =
    Color(0xFF153F32)

val FoodBoxPrimaryGreen =
    Color(0xFF2E765B)

val FoodBoxMintGreen =
    Color(0xFF52C798)

val FoodBoxSoftGreen =
    Color(0xFFE7F4E9)

val FoodBoxOrange =
    Color(0xFFFFA31A)

val FoodBoxPageBackground =
    Color(0xFFF8F6F0)

val FoodBoxMainText =
    Color(0xFF1E2E20)

val FoodBoxSecondaryText =
    Color(0xFF71806D)

val FoodBoxBorder =
    Color(0xFFD8E2DC)

private val stepLabels = listOf(
    "Browse",
    "Details",
    "Customize",
    "Schedule",
    "Checkout"
)

@Composable
fun FoodBoxFlowHeader(
    title: String,
    currentStep: Int,
    onBackClick: (() -> Unit)? = null,
    onManageClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(FoodBoxDarkGreen)
            .statusBarsPadding()
            .padding(
                start = 10.dp,
                end = 10.dp,
                top = 10.dp,
                bottom = 16.dp
            )
    ) {
        Row(
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            if (onBackClick != null) {
                IconButton(
                    onClick = onBackClick
                ) {
                    Icon(
                        imageVector =
                            Icons.Default.ArrowBack,
                        contentDescription =
                            "Back",
                        tint = Color.White
                    )
                }
            }

            Text(
                text = title,
                modifier =
                    Modifier.weight(1f),
                color = Color.White,
                fontSize = 28.sp,
                fontWeight =
                    FontWeight.Bold
            )

            if (onManageClick != null) {
                TextButton(
                    onClick =
                        onManageClick,

                    colors =
                        ButtonDefaults
                            .textButtonColors(
                                contentColor =
                                    FoodBoxSoftGreen
                            )
                ) {
                    Text(
                        text = "Manage",
                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }
        }

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),

            verticalAlignment =
                Alignment.Top
        ) {
            stepLabels.forEachIndexed {
                    index,
                    label ->

                FoodBoxStep(
                    number = index + 1,
                    label = label,
                    currentStep =
                        currentStep
                )

                if (
                    index <
                    stepLabels.lastIndex
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                horizontal = 2.dp,
                                vertical = 15.dp
                            )
                            .height(3.dp)
                            .clip(
                                RoundedCornerShape(
                                    50
                                )
                            )
                            .background(
                                if (
                                    index + 1 <
                                    currentStep
                                ) {
                                    FoodBoxMintGreen
                                } else {
                                    Color(
                                        0xFFEDEAE2
                                    )
                                }
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun FoodBoxStep(
    number: Int,
    label: String,
    currentStep: Int
) {
    val completed =
        number < currentStep

    val current =
        number == currentStep

    val circleColor =
        when {
            completed ->
                FoodBoxMintGreen

            current ->
                FoodBoxOrange

            else ->
                Color(0xFFEDEAE2)
        }

    val labelColor =
        when {
            completed ->
                FoodBoxMintGreen

            current ->
                FoodBoxOrange

            else ->
                FoodBoxSecondaryText
        }

    Column(
        modifier =
            Modifier.width(52.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(circleColor),

            contentAlignment =
                Alignment.Center
        ) {
            if (completed) {
                Icon(
                    imageVector =
                        Icons.Default.Check,

                    contentDescription =
                        "Completed",

                    tint = Color.White,

                    modifier =
                        Modifier.size(20.dp)
                )
            } else {
                Text(
                    text =
                        number.toString(),

                    color =
                        if (current) {
                            Color.White
                        } else {
                            FoodBoxSecondaryText
                        },

                    fontSize = 14.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(5.dp)
        )

        Text(
            text = label,

            modifier =
                Modifier.fillMaxWidth(),

            color = labelColor,

            fontSize = 8.sp,

            lineHeight = 9.sp,

            fontWeight =
                if (current) {
                    FontWeight.Bold
                } else {
                    FontWeight.Medium
                },

            textAlign =
                TextAlign.Center,

            maxLines = 1,

            softWrap = false,

            overflow =
                TextOverflow.Visible
        )
    }
}

@Composable
fun FoodBoxPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,

        modifier = modifier
            .fillMaxWidth()
            .height(58.dp),

        enabled = enabled,

        shape =
            RoundedCornerShape(18.dp),

        colors =
            ButtonDefaults.buttonColors(
                containerColor =
                    FoodBoxPrimaryGreen,

                disabledContainerColor =
                    Color(0xFFA8B8AF)
            )
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight =
                FontWeight.Bold
        )
    }
}

@Composable
fun FoodBoxOutlineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,

        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),

        shape =
            RoundedCornerShape(18.dp),

        border =
            BorderStroke(
                1.dp,
                FoodBoxBorder
            )
    ) {
        Text(
            text = text,
            color = FoodBoxMainText,
            fontWeight =
                FontWeight.SemiBold
        )
    }
}

@Composable
fun FoodBoxItemRow(
    item: FoodBoxItem,
    showSwapButton: Boolean = false,
    onSwapClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(
                    if (item.isSwapped) {
                        Color(0xFFFFF0D9)
                    } else {
                        FoodBoxSoftGreen
                    }
                ),

            contentAlignment =
                Alignment.Center
        ) {
            Icon(
                imageVector =
                    Icons.Outlined.Eco,

                contentDescription = null,

                tint =
                    if (item.isSwapped) {
                        FoodBoxOrange
                    } else {
                        FoodBoxPrimaryGreen
                    }
            )
        }

        Spacer(
            modifier =
                Modifier.width(14.dp)
        )

        Column(
            modifier =
                Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Text(
                    text = item.name,
                    color = FoodBoxMainText,
                    fontWeight =
                        FontWeight.Bold
                )

                if (item.isSwapped) {
                    Spacer(
                        modifier =
                            Modifier.width(5.dp)
                    )

                    Text(
                        text = "SWAPPED",
                        color =
                            FoodBoxOrange,
                        fontSize = 9.sp,
                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }

            Text(
                text = item.quantity,
                color =
                    FoodBoxSecondaryText,
                fontSize = 13.sp
            )
        }

        if (
            showSwapButton &&
            item.swappable &&
            onSwapClick != null
        ) {
            OutlinedButton(
                onClick =
                    onSwapClick,

                shape =
                    RoundedCornerShape(50),

                border =
                    BorderStroke(
                        1.dp,
                        FoodBoxBorder
                    ),

                contentPadding =
                    PaddingValues(
                        horizontal = 12.dp,
                        vertical = 4.dp
                    )
            ) {
                Text(
                    text = "↻ Swap",
                    color =
                        FoodBoxPrimaryGreen,
                    fontSize = 12.sp
                )
            }
        } else if (showSwapButton) {
            Text(
                text = "Fixed",
                color =
                    FoodBoxSecondaryText,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun FoodBoxArtwork(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(190.dp)
            .background(
                Color(0xFF4A8F67)
            ),

        contentAlignment =
            Alignment.Center
    ) {
        Icon(
            imageVector =
                Icons.Outlined.Eco,

            contentDescription = null,

            tint = Color.White,

            modifier =
                Modifier.size(90.dp)
        )
    }
}

@Composable
fun FoodBoxMessage(
    message: String?
) {
    if (!message.isNullOrBlank()) {
        Card(
            modifier =
                Modifier.fillMaxWidth(),

            shape =
                RoundedCornerShape(14.dp),

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        FoodBoxSoftGreen
                )
        ) {
            Text(
                text = message,

                modifier =
                    Modifier.padding(14.dp),

                color =
                    FoodBoxPrimaryGreen
            )
        }
    }
}

fun formatMoney(
    amount: Double
): String {
    return "RM %.2f".format(amount)
}