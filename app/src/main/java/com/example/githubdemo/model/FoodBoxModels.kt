package com.example.githubdemo.model

import kotlinx.serialization.Serializable

@Serializable
enum class BillingCycle {
    MONTHLY,
    YEARLY
}

@Serializable
enum class PaymentMethod {
    E_WALLET,
    ONLINE_BANKING,
    CARD
}

@Serializable
data class FoodBoxItem(
    val id: String,
    val name: String,
    val quantity: String,
    val swappable: Boolean,
    val isSwapped: Boolean = false
)

@Serializable
data class FoodBoxAddOn(
    val id: String,
    val name: String,
    val price: Double
)

@Serializable
data class FoodBoxPlan(
    val id: String,
    val name: String,
    val description: String,
    val suitablePax: String,
    val monthlyPrice: Double,
    val yearlyPrice: Double,
    val calories: String,
    val protein: String,
    val vitamins: String,
    val badge: String,
    val items: List<FoodBoxItem>
)

@Serializable
data class FoodBoxSubscription(
    val id: String,
    val userId: String,
    val planId: String,
    val planName: String,
    val billingCycle: BillingCycle,
    val customizedItems: List<FoodBoxItem>,
    val selectedAddOns: List<FoodBoxAddOn>,
    val deliveryDay: String,
    val deliveryAddress: String,
    val paymentMethod: PaymentMethod,
    val totalPrice: Double,
    val status: String,
    val createdAt: Long
)

@Serializable
data class FoodBoxReminderSettings(
    val deliveryDayReminder: Boolean = true,
    val outForDeliveryAlert: Boolean = true,
    val arrivedNotification: Boolean = true,
    val paymentDueReminder: Boolean = false,
    val subscriptionRenewal: Boolean = true,
    val newBoxAvailable: Boolean = false
)

@Serializable
data class FoodBoxUiState(
    val selectedPlanId: String = "",
    val billingCycle: BillingCycle =
        BillingCycle.MONTHLY,
    val customizedItems: List<FoodBoxItem> =
        emptyList(),
    val selectedAddOns: List<FoodBoxAddOn> =
        emptyList(),
    val deliveryDay: String = "",
    val deliveryAddress: String = "",
    val paymentMethod: PaymentMethod =
        PaymentMethod.E_WALLET,
    val activeSubscription: FoodBoxSubscription? =
        null,
    val reminderSettings: FoodBoxReminderSettings =
        FoodBoxReminderSettings(),
    val itemBeingSwappedId: String? = null,
    val isLoading: Boolean = false,
    val message: String? = null
)