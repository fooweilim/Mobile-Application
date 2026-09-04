package com.example.githubdemo.screen.foodbox

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubdemo.data.FoodBoxData
import com.example.githubdemo.model.BillingCycle
import com.example.githubdemo.model.FoodBoxReminderSettings
import com.example.githubdemo.model.FoodBoxSubscription
import com.example.githubdemo.viewmodel.foodbox.FoodBoxViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private const val PLAN_TAB =
    "Plan"

private const val PAYMENTS_TAB =
    "Payments"

private const val REMINDERS_TAB =
    "Reminders"

private val subscriptionTabs = listOf(
    PLAN_TAB,
    PAYMENTS_TAB,
    REMINDERS_TAB
)

@Composable
fun ManageSubscriptionScreen(
    foodBoxViewModel: FoodBoxViewModel,
    onBackClick: () -> Unit,
    onBrowsePlansClick: () -> Unit
) {
    val state by
    foodBoxViewModel.uiState

    val subscription =
        state.activeSubscription

    var selectedTab by
    rememberSaveable {
        mutableStateOf(PLAN_TAB)
    }

    var showDayDialog by
    rememberSaveable {
        mutableStateOf(false)
    }

    var showAddressDialog by
    rememberSaveable {
        mutableStateOf(false)
    }

    var showCancelDialog by
    rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                FoodBoxPageBackground
            )
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 8.dp,
                    vertical = 6.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector =
                        Icons.Default
                            .ArrowBack,

                    contentDescription =
                        "Back",

                    tint =
                        FoodBoxMainText
                )
            }

            Text(
                text =
                    "My Subscription",

                color =
                    FoodBoxMainText,

                fontSize = 22.sp,

                fontWeight =
                    FontWeight.Bold
            )
        }

        SubscriptionTabRow(
            selectedTab =
                selectedTab,

            onSelected = {
                selectedTab = it
            }
        )

        if (subscription == null) {
            NoSubscriptionContent(
                message =
                    state.message,

                onBrowsePlansClick =
                    onBrowsePlansClick
            )
        } else {
            when (selectedTab) {
                PLAN_TAB -> {
                    PlanContent(
                        subscription =
                            subscription,

                        message =
                            state.message,

                        onDeliveryDayClick = {
                            showDayDialog =
                                true
                        },

                        onAddressClick = {
                            showAddressDialog =
                                true
                        },

                        onCancelClick = {
                            showCancelDialog =
                                true
                        }
                    )
                }

                PAYMENTS_TAB -> {
                    PaymentsContent(
                        subscription =
                            subscription
                    )
                }

                REMINDERS_TAB -> {
                    RemindersContent(
                        settings =
                            state.reminderSettings,

                        onSettingsChanged =
                            foodBoxViewModel::
                            updateReminderSettings
                    )
                }
            }
        }
    }

    if (
        showDayDialog &&
        subscription != null
    ) {
        DeliveryDayUpdateDialog(
            currentDay =
                subscription.deliveryDay,

            onDismiss = {
                showDayDialog = false
            },

            onSave = { day ->
                foodBoxViewModel
                    .updateSubscriptionDeliveryDay(
                        day
                    )

                showDayDialog = false
            }
        )
    }

    if (
        showAddressDialog &&
        subscription != null
    ) {
        AddressEditDialog(
            currentAddress =
                subscription
                    .deliveryAddress,

            onDismiss = {
                showAddressDialog =
                    false
            },

            onSave = { address ->
                if (
                    foodBoxViewModel
                        .updateSubscriptionAddress(
                            address
                        )
                ) {
                    showAddressDialog =
                        false
                }
            }
        )
    }

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = {
                showCancelDialog = false
            },

            title = {
                Text(
                    "Cancel Subscription?"
                )
            },

            text = {
                Text(
                    "Your future Food Box deliveries " +
                            "will be cancelled."
                )
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        foodBoxViewModel
                            .cancelSubscription()

                        showCancelDialog =
                            false
                    }
                ) {
                    Text(
                        text =
                            "Cancel Subscription",

                        color =
                            Color(0xFFD34242)
                    )
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        showCancelDialog =
                            false
                    }
                ) {
                    Text(
                        "Keep Subscription"
                    )
                }
            }
        )
    }
}

@Composable
private fun SubscriptionTabRow(
    selectedTab: String,
    onSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(
                horizontal = 16.dp
            )
            .fillMaxWidth()
            .background(
                color =
                    Color(0xFFEDE9DF),

                shape =
                    RoundedCornerShape(
                        50
                    )
            )
            .padding(4.dp),

        horizontalArrangement =
            Arrangement.spacedBy(4.dp)
    ) {
        subscriptionTabs.forEach {
                tab ->

            val selected =
                tab == selectedTab

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color =
                            if (selected) {
                                Color.White
                            } else {
                                Color.Transparent
                            },

                        shape =
                            RoundedCornerShape(
                                50
                            )
                    )
                    .clickable {
                        onSelected(tab)
                    }
                    .padding(
                        vertical = 10.dp
                    ),

                contentAlignment =
                    Alignment.Center
            ) {
                Text(
                    text = tab,

                    color =
                        if (selected) {
                            FoodBoxMainText
                        } else {
                            FoodBoxPrimaryGreen
                        },

                    fontSize = 13.sp,

                    fontWeight =
                        if (selected) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Medium
                        }
                )
            }
        }
    }
}

@Composable
private fun PlanContent(
    subscription: FoodBoxSubscription,
    message: String?,
    onDeliveryDayClick: () -> Unit,
    onAddressClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    LazyColumn(
        modifier =
            Modifier.fillMaxSize(),

        contentPadding =
            PaddingValues(16.dp),

        verticalArrangement =
            Arrangement.spacedBy(14.dp)
    ) {
        item {
            ActivePlanCard(
                subscription =
                    subscription
            )
        }

        if (!message.isNullOrBlank()) {
            item {
                FoodBoxMessage(message)
            }
        }

        item {
            Card(
                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(
                        18.dp
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
                            "Manage Plan",

                        modifier =
                            Modifier.padding(
                                16.dp
                            ),

                        color =
                            FoodBoxMainText,

                        fontSize = 17.sp,

                        fontWeight =
                            FontWeight.Bold
                    )

                    ManageRow(
                        icon =
                            Icons.Outlined
                                .LocalShipping,

                        title =
                            "Update Delivery Day",

                        onClick =
                            onDeliveryDayClick
                    )

                    ManageDivider()

                    ManageRow(
                        icon =
                            Icons.Outlined
                                .LocationOn,

                        title =
                            "Update Address",

                        onClick =
                            onAddressClick
                    )

                    ManageDivider()

                    ManageRow(
                        icon =
                            Icons.Default.Close,

                        title =
                            "Cancel Subscription",

                        onClick =
                            onCancelClick,

                        color =
                            Color(0xFFD34242)
                    )
                }
            }
        }
    }
}

@Composable
private fun ActivePlanCard(
    subscription: FoodBoxSubscription
) {
    val plan =
        FoodBoxData.getPlan(
            subscription.planId
        )

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(22.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    FoodBoxPrimaryGreen
            )
    ) {
        Column(
            modifier =
                Modifier.padding(18.dp)
        ) {
            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.Top
            ) {
                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {
                    Text(
                        text =
                            "Active Plan",

                        color =
                            FoodBoxSoftGreen,

                        fontSize = 13.sp
                    )

                    Text(
                        text =
                            subscription
                                .planName,

                        color =
                            Color.White,

                        fontSize = 24.sp,

                        fontWeight =
                            FontWeight.Bold
                    )
                }

                Text(
                    text = "ACTIVE",

                    modifier = Modifier
                        .background(
                            color =
                                FoodBoxMintGreen,

                            shape =
                                RoundedCornerShape(
                                    50
                                )
                        )
                        .padding(
                            horizontal =
                                10.dp,

                            vertical =
                                4.dp
                        ),

                    color = Color.White,

                    fontSize = 11.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(
                        8.dp
                    )
            ) {
                PlanValue(
                    value =
                        formatMoney(
                            subscription
                                .totalPrice
                        ),

                    label =
                        "per delivery",

                    modifier =
                        Modifier.weight(1f)
                )

                PlanValue(
                    value =
                        plan?.suitablePax
                            .orEmpty(),

                    label =
                        "box size",

                    modifier =
                        Modifier.weight(1f)
                )

                PlanValue(
                    value =
                        if (
                            subscription
                                .billingCycle ==
                            BillingCycle.MONTHLY
                        ) {
                            "Monthly"
                        } else {
                            "Yearly"
                        },

                    label =
                        "frequency",

                    modifier =
                        Modifier.weight(1f)
                )
            }

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            Row(
                verticalAlignment =
                    Alignment.Top
            ) {
                Icon(
                    imageVector =
                        Icons.Outlined
                            .CalendarMonth,

                    contentDescription =
                        null,

                    tint =
                        FoodBoxSoftGreen,

                    modifier =
                        Modifier.size(18.dp)
                )

                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )

                Text(
                    text =
                        "Next delivery: " +
                                subscription
                                    .deliveryDay +
                                " · 9:00 AM – 12:00 PM",

                    color = Color.White,

                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun PlanValue(
    value: String,
    label: String,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .background(
                color =
                    Color.White.copy(
                        alpha = 0.14f
                    ),

                shape =
                    RoundedCornerShape(
                        16.dp
                    )
            )
            .padding(
                vertical = 12.dp,
                horizontal = 5.dp
            ),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight =
                FontWeight.Bold,
            maxLines = 1
        )

        Text(
            text = label,
            color =
                FoodBoxSoftGreen,
            fontSize = 9.sp,
            maxLines = 1
        )
    }
}

@Composable
private fun ManageRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    color: Color =
        FoodBoxPrimaryGreen
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            )
            .padding(
                horizontal = 16.dp,
                vertical = 15.dp
            ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier =
                Modifier.size(20.dp)
        )

        Text(
            text = title,

            modifier = Modifier
                .weight(1f)
                .padding(
                    start = 14.dp
                ),

            color =
                if (
                    color ==
                    Color(0xFFD34242)
                ) {
                    color
                } else {
                    FoodBoxMainText
                },

            fontWeight =
                FontWeight.SemiBold
        )

        Icon(
            imageVector =
                Icons.Default
                    .KeyboardArrowRight,

            contentDescription = null,

            tint =
                FoodBoxSecondaryText
        )
    }
}

@Composable
private fun ManageDivider() {
    HorizontalDivider(
        modifier =
            Modifier.padding(
                horizontal = 16.dp
            ),

        color =
            FoodBoxBorder
    )
}

@Composable
private fun PaymentsContent(
    subscription: FoodBoxSubscription
) {
    LazyColumn(
        modifier =
            Modifier.fillMaxSize(),

        contentPadding =
            PaddingValues(16.dp),

        verticalArrangement =
            Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(
                        18.dp
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
                        Modifier.padding(16.dp)
                ) {
                    Text(
                        text =
                            "Next billing date",

                        color =
                            FoodBoxSecondaryText,

                        fontSize = 13.sp
                    )

                    Text(
                        text =
                            nextBillingDate(
                                subscription
                            ),

                        color =
                            FoodBoxMainText,

                        fontSize = 19.sp,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(
                        text =
                            formatMoney(
                                subscription
                                    .totalPrice
                            ) +
                                    " via " +
                                    paymentMethodName(
                                        subscription
                                    ),

                        color =
                            FoodBoxPrimaryGreen,

                        fontWeight =
                            FontWeight.SemiBold
                    )
                }
            }
        }

        item {
            Text(
                text =
                    "Payment History",

                color =
                    FoodBoxMainText,

                fontSize = 19.sp,

                fontWeight =
                    FontWeight.Bold
            )
        }

        item {
            Card(
                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(
                        18.dp
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
                Row(
                    modifier =
                        Modifier.padding(14.dp),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
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
                                Icons.Default
                                    .CheckCircle,

                            contentDescription =
                                null,

                            tint =
                                FoodBoxPrimaryGreen
                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                start = 12.dp
                            )
                    ) {
                        Text(
                            text =
                                formatDate(
                                    subscription
                                        .createdAt
                                ),

                            color =
                                FoodBoxMainText,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            text =
                                paymentMethodName(
                                    subscription
                                ),

                            color =
                                FoodBoxSecondaryText,

                            fontSize = 12.sp
                        )
                    }

                    Column(
                        horizontalAlignment =
                            Alignment.End
                    ) {
                        Text(
                            text =
                                formatMoney(
                                    subscription
                                        .totalPrice
                                ),

                            color =
                                FoodBoxMainText,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            text = "Paid",

                            color =
                                FoodBoxPrimaryGreen,

                            fontSize = 11.sp,

                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RemindersContent(
    settings: FoodBoxReminderSettings,
    onSettingsChanged:
        (FoodBoxReminderSettings) -> Unit
) {
    LazyColumn(
        modifier =
            Modifier.fillMaxSize(),

        contentPadding =
            PaddingValues(16.dp),

        verticalArrangement =
            Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text =
                    "Manage delivery reminders & alerts",

                color =
                    FoodBoxSecondaryText,

                fontSize = 14.sp
            )
        }

        item {
            ReminderRow(
                title =
                    "Delivery Day Reminder",

                subtitle =
                    "1 day before delivery",

                checked =
                    settings
                        .deliveryDayReminder,

                onChanged = {
                    onSettingsChanged(
                        settings.copy(
                            deliveryDayReminder =
                                it
                        )
                    )
                }
            )
        }

        item {
            ReminderRow(
                title =
                    "Out for Delivery Alert",

                subtitle =
                    "When rider departs",

                checked =
                    settings
                        .outForDeliveryAlert,

                onChanged = {
                    onSettingsChanged(
                        settings.copy(
                            outForDeliveryAlert =
                                it
                        )
                    )
                }
            )
        }

        item {
            ReminderRow(
                title =
                    "Arrived Notification",

                subtitle =
                    "When box is delivered",

                checked =
                    settings
                        .arrivedNotification,

                onChanged = {
                    onSettingsChanged(
                        settings.copy(
                            arrivedNotification =
                                it
                        )
                    )
                }
            )
        }

        item {
            ReminderRow(
                title =
                    "Payment Due Reminder",

                subtitle =
                    "2 days before billing",

                checked =
                    settings
                        .paymentDueReminder,

                onChanged = {
                    onSettingsChanged(
                        settings.copy(
                            paymentDueReminder =
                                it
                        )
                    )
                }
            )
        }

        item {
            ReminderRow(
                title =
                    "Subscription Renewal",

                subtitle =
                    "7 days before renewal",

                checked =
                    settings
                        .subscriptionRenewal,

                onChanged = {
                    onSettingsChanged(
                        settings.copy(
                            subscriptionRenewal =
                                it
                        )
                    )
                }
            )
        }

        item {
            ReminderRow(
                title =
                    "New Box Available",

                subtitle =
                    "When new plans launch",

                checked =
                    settings
                        .newBoxAvailable,

                onChanged = {
                    onSettingsChanged(
                        settings.copy(
                            newBoxAvailable =
                                it
                        )
                    )
                }
            )
        }

        item {
            Card(
                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(
                        18.dp
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
                    Row(
                        modifier =
                            Modifier.padding(
                                16.dp
                            ),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector =
                                Icons.Outlined
                                    .SupportAgent,

                            contentDescription =
                                null,

                            tint =
                                FoodBoxPrimaryGreen
                        )

                        Text(
                            text =
                                "Need Help?",

                            modifier =
                                Modifier.padding(
                                    start =
                                        12.dp
                                ),

                            color =
                                FoodBoxMainText,

                            fontWeight =
                                FontWeight.Bold
                        )
                    }

                    HorizontalDivider(
                        color =
                            FoodBoxBorder
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {
                        Text(
                            text =
                                "Contact Support",

                            modifier =
                                Modifier.weight(
                                    1f
                                ),

                            color =
                                FoodBoxMainText
                        )

                        Icon(
                            imageVector =
                                Icons.Default
                                    .KeyboardArrowRight,

                            contentDescription =
                                null,

                            tint =
                                FoodBoxSecondaryText
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReminderRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onChanged: (Boolean) -> Unit
) {
    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(18.dp),

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
                Modifier.padding(13.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
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
                        Icons.Outlined
                            .NotificationsNone,

                    contentDescription =
                        null,

                    tint =
                        FoodBoxPrimaryGreen
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        horizontal = 12.dp
                    )
            ) {
                Text(
                    text = title,
                    color =
                        FoodBoxMainText,
                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text = subtitle,
                    color =
                        FoodBoxSecondaryText,
                    fontSize = 12.sp
                )
            }

            Switch(
                checked = checked,
                onCheckedChange =
                    onChanged,

                colors =
                    SwitchDefaults.colors(
                        checkedTrackColor =
                            FoodBoxPrimaryGreen
                    )
            )
        }
    }
}

@Composable
private fun NoSubscriptionContent(
    message: String?,
    onBrowsePlansClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        verticalArrangement =
            Arrangement.Center,

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector =
                Icons.Outlined.Inventory2,

            contentDescription =
                null,

            tint =
                FoodBoxPrimaryGreen,

            modifier =
                Modifier.size(72.dp)
        )

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )

        Text(
            text =
                "No Active Subscription",

            color =
                FoodBoxMainText,

            fontSize = 24.sp,

            fontWeight =
                FontWeight.Bold
        )

        Text(
            text =
                message
                    ?: "Choose a Food Box plan to begin.",

            color =
                FoodBoxSecondaryText
        )

        Spacer(
            modifier =
                Modifier.height(24.dp)
        )

        FoodBoxPrimaryButton(
            text =
                "Browse Food Box Plans",

            onClick =
                onBrowsePlansClick
        )
    }
}

@Composable
private fun DeliveryDayUpdateDialog(
    currentDay: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var selected by
    rememberSaveable(
        currentDay
    ) {
        mutableStateOf(
            currentDay
        )
    }

    AlertDialog(
        onDismissRequest =
            onDismiss,

        title = {
            Text(
                "Update Delivery Day"
            )
        },

        text = {
            Column {
                FoodBoxData
                    .deliveryDays
                    .forEach { day ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selected =
                                        day
                                }
                                .padding(
                                    vertical =
                                        3.dp
                                ),

                            verticalAlignment =
                                Alignment
                                    .CenterVertically
                        ) {
                            RadioButton(
                                selected =
                                    selected ==
                                            day,

                                onClick = {
                                    selected =
                                        day
                                }
                            )

                            Text(day)
                        }
                    }
            }
        },

        confirmButton = {
            TextButton(
                onClick = {
                    onSave(selected)
                }
            ) {
                Text(
                    text = "Update",
                    color =
                        FoodBoxPrimaryGreen
                )
            }
        },

        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}

private fun paymentMethodName(
    subscription: FoodBoxSubscription
): String {
    return when (
        subscription.paymentMethod.name
    ) {
        "E_WALLET" ->
            "E-Wallet"

        "ONLINE_BANKING" ->
            "Online Banking"

        "CARD" ->
            "Credit / Debit Card"

        else ->
            subscription
                .paymentMethod
                .name
    }
}

private fun formatDate(
    timestamp: Long
): String {
    return SimpleDateFormat(
        "d MMM yyyy",
        Locale.getDefault()
    ).format(
        Date(timestamp)
    )
}

private fun nextBillingDate(
    subscription: FoodBoxSubscription
): String {
    val calendar =
        Calendar.getInstance().apply {
            timeInMillis =
                subscription.createdAt

            add(
                if (
                    subscription
                        .billingCycle ==
                    BillingCycle.MONTHLY
                ) {
                    Calendar.MONTH
                } else {
                    Calendar.YEAR
                },
                1
            )
        }

    return SimpleDateFormat(
        "EEEE, d MMMM yyyy",
        Locale.getDefault()
    ).format(
        calendar.time
    )
}