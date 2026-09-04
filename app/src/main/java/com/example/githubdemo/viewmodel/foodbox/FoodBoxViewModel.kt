package com.example.githubdemo.viewmodel.foodbox

import android.app.Application
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubdemo.data.FoodBoxData
import com.example.githubdemo.data.local.FoodBoxLocalStorage
import com.example.githubdemo.data.local.LocalAccountStorage
import com.example.githubdemo.model.BillingCycle
import com.example.githubdemo.model.FoodBoxAddOn
import com.example.githubdemo.model.FoodBoxItem
import com.example.githubdemo.model.FoodBoxPlan
import com.example.githubdemo.model.FoodBoxReminderSettings
import com.example.githubdemo.model.FoodBoxSubscription
import com.example.githubdemo.model.FoodBoxUiState
import com.example.githubdemo.model.PaymentMethod
import com.example.githubdemo.supabase.FoodBoxCloudRepository
import kotlinx.coroutines.launch

private const val MINIMUM_ADDRESS_LENGTH = 8

class FoodBoxViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val appContext: Context =
        application.applicationContext

    private val _uiState =
        mutableStateOf(createInitialState())

    val uiState: State<FoodBoxUiState> =
        _uiState

    init {
        refreshActiveSubscription()
    }

    fun getSelectedPlan(): FoodBoxPlan? {
        return FoodBoxData.getPlan(
            _uiState.value.selectedPlanId
        )
    }

    fun selectBillingCycle(
        billingCycle: BillingCycle
    ) {
        _uiState.value = _uiState.value.copy(
            billingCycle = billingCycle,
            message = null
        )

        saveDraft()
    }

    fun selectPlan(planId: String) {
        val plan =
            FoodBoxData.getPlan(planId)
                ?: return

        _uiState.value = _uiState.value.copy(
            selectedPlanId = plan.id,
            customizedItems = plan.items,
            selectedAddOns = emptyList(),
            itemBeingSwappedId = null,
            message = null
        )

        saveDraft()
    }

    fun showSwapOptions(itemId: String) {
        val item =
            _uiState.value
                .customizedItems
                .firstOrNull {
                    it.id == itemId
                }
                ?: return

        if (!item.swappable) {
            _uiState.value =
                _uiState.value.copy(
                    itemBeingSwappedId = null,
                    message =
                        "${item.name} is a fixed item."
                )

            return
        }

        _uiState.value =
            _uiState.value.copy(
                itemBeingSwappedId = itemId,
                message = null
            )
    }

    fun dismissSwapOptions() {
        _uiState.value =
            _uiState.value.copy(
                itemBeingSwappedId = null
            )
    }

    fun replaceItem(
        replacement: FoodBoxItem
    ) {
        val originalId =
            _uiState.value
                .itemBeingSwappedId
                ?: return

        val original =
            _uiState.value
                .customizedItems
                .firstOrNull {
                    it.id == originalId
                }
                ?: return

        val duplicate =
            _uiState.value
                .customizedItems
                .any {
                    it.id != originalId &&
                            it.name.equals(
                                replacement.name,
                                ignoreCase = true
                            )
                }

        if (duplicate) {
            _uiState.value =
                _uiState.value.copy(
                    message =
                        "${replacement.name} is already in your box."
                )

            return
        }

        val replacementWithOriginalWeight =
            replacement.copy(
                id = original.id,
                quantity = original.quantity,
                swappable = original.swappable,
                isSwapped = true
            )

        val updatedItems =
            _uiState.value
                .customizedItems
                .map { item ->
                    if (item.id == originalId) {
                        replacementWithOriginalWeight
                    } else {
                        item
                    }
                }

        _uiState.value =
            _uiState.value.copy(
                customizedItems = updatedItems,
                itemBeingSwappedId = null,
                message =
                    "${original.name} changed to " +
                            "${replacement.name}. " +
                            "Weight remains " +
                            "${original.quantity}."
            )

        saveDraft()
    }

    fun toggleAddOn(
        addOn: FoodBoxAddOn
    ) {
        val selected =
            _uiState.value
                .selectedAddOns
                .any {
                    it.id == addOn.id
                }

        val newAddOns =
            if (selected) {
                _uiState.value
                    .selectedAddOns
                    .filterNot {
                        it.id == addOn.id
                    }
            } else {
                _uiState.value
                    .selectedAddOns +
                        addOn
            }

        _uiState.value =
            _uiState.value.copy(
                selectedAddOns = newAddOns,
                message = null
            )

        saveDraft()
    }

    fun selectDeliveryDay(day: String) {
        if (day !in FoodBoxData.deliveryDays) {
            return
        }

        _uiState.value =
            _uiState.value.copy(
                deliveryDay = day,
                message = null
            )

        saveDraft()
    }

    fun updateDeliveryAddress(
        address: String
    ): Boolean {
        val cleanAddress =
            address.trim()

        if (
            cleanAddress.length <
            MINIMUM_ADDRESS_LENGTH
        ) {
            _uiState.value =
                _uiState.value.copy(
                    message =
                        "Please enter a complete delivery address."
                )

            return false
        }

        _uiState.value =
            _uiState.value.copy(
                deliveryAddress = cleanAddress,
                message = null
            )

        saveDraft()

        return true
    }

    fun selectPaymentMethod(
        paymentMethod: PaymentMethod
    ) {
        _uiState.value =
            _uiState.value.copy(
                paymentMethod = paymentMethod,
                message = null
            )

        saveDraft()
    }

    fun getBasePrice(): Double {
        val plan =
            getSelectedPlan()
                ?: return 0.0

        return if (
            _uiState.value.billingCycle ==
            BillingCycle.MONTHLY
        ) {
            plan.monthlyPrice
        } else {
            plan.yearlyPrice
        }
    }

    fun getAddOnPrice(): Double {
        return _uiState.value
            .selectedAddOns
            .sumOf {
                it.price
            }
    }

    fun getTotalPrice(): Double {
        return getBasePrice() +
                getAddOnPrice()
    }

    fun confirmSubscription(
        userId: String?,
        onFinished: () -> Unit
    ) {
        if (_uiState.value.isLoading) {
            return
        }

        val state =
            _uiState.value

        val activeSubscription =
            state.activeSubscription

        if (
            activeSubscription != null &&
            activeSubscription.status.equals(
                FoodBoxData.STATUS_ACTIVE,
                ignoreCase = true
            )
        ) {
            _uiState.value =
                state.copy(
                    message =
                        "You already have an active Food Box " +
                                "subscription. Cancel it before " +
                                "subscribing to another box."
                )

            return
        }

        val cleanUserId =
            userId.orEmpty().trim()

        if (cleanUserId.isBlank()) {
            _uiState.value =
                state.copy(
                    message =
                        "Please sign in before subscribing."
                )

            return
        }

        val plan =
            getSelectedPlan()

        if (plan == null) {
            _uiState.value =
                state.copy(
                    message =
                        "Please select a Food Box plan."
                )

            return
        }

        if (state.deliveryDay.isBlank()) {
            _uiState.value =
                state.copy(
                    message =
                        "Please select a delivery day."
                )

            return
        }

        if (state.deliveryAddress.isBlank()) {
            _uiState.value =
                state.copy(
                    message =
                        "Please enter a complete delivery address."
                )

            return
        }

        val createdAt =
            System.currentTimeMillis()

        val subscription =
            FoodBoxSubscription(
                id =
                    "food_box_$createdAt",

                userId =
                    cleanUserId,

                planId =
                    plan.id,

                planName =
                    plan.name,

                billingCycle =
                    state.billingCycle,

                customizedItems =
                    state.customizedItems,

                selectedAddOns =
                    state.selectedAddOns,

                deliveryDay =
                    state.deliveryDay,

                deliveryAddress =
                    state.deliveryAddress,

                paymentMethod =
                    state.paymentMethod,

                totalPrice =
                    getTotalPrice(),

                status =
                    FoodBoxData.STATUS_ACTIVE,

                createdAt =
                    createdAt
            )

        _uiState.value =
            state.copy(
                isLoading = true,
                message = null
            )

        viewModelScope.launch {
            FoodBoxCloudRepository
                .createSubscription(
                    subscription
                )
                .onSuccess {
                    FoodBoxLocalStorage
                        .saveSubscription(
                            appContext,
                            subscription
                        )

                    _uiState.value =
                        _uiState.value.copy(
                            activeSubscription =
                                subscription,

                            isLoading = false,
                            message = null
                        )

                    onFinished()
                }
                .onFailure { exception ->
                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,

                            message =
                                exception.message
                                    ?: "Unable to create the subscription."
                        )
                }
        }
    }

    fun updateSubscriptionDeliveryDay(
        day: String
    ) {
        if (day !in FoodBoxData.deliveryDays) {
            return
        }

        val subscription =
            _uiState.value
                .activeSubscription
                ?: return

        val updated =
            subscription.copy(
                deliveryDay = day
            )

        _uiState.value =
            _uiState.value.copy(
                activeSubscription = updated,
                deliveryDay = day,
                message =
                    "Delivery day updated to $day."
            )

        saveDraft()
        saveSubscriptionChange(updated)
    }

    fun updateSubscriptionAddress(
        address: String
    ): Boolean {
        val cleanAddress =
            address.trim()

        if (
            cleanAddress.length <
            MINIMUM_ADDRESS_LENGTH
        ) {
            _uiState.value =
                _uiState.value.copy(
                    message =
                        "Please enter a complete delivery address."
                )

            return false
        }

        val subscription =
            _uiState.value
                .activeSubscription
                ?: return false

        val updated =
            subscription.copy(
                deliveryAddress =
                    cleanAddress
            )

        _uiState.value =
            _uiState.value.copy(
                activeSubscription = updated,
                deliveryAddress = cleanAddress,
                message =
                    "Delivery address updated."
            )

        saveDraft()
        saveSubscriptionChange(updated)

        return true
    }

    fun updateReminderSettings(
        settings: FoodBoxReminderSettings
    ) {
        _uiState.value =
            _uiState.value.copy(
                reminderSettings = settings,
                message = null
            )

        FoodBoxLocalStorage
            .saveReminderSettings(
                appContext,
                settings
            )
    }

    fun cancelSubscription() {
        if (_uiState.value.isLoading) {
            return
        }

        val subscription =
            _uiState.value
                .activeSubscription
                ?: return

        _uiState.value =
            _uiState.value.copy(
                isLoading = true,
                message = null
            )

        viewModelScope.launch {
            FoodBoxCloudRepository
                .deleteSubscription(
                    subscription.id
                )
                .onSuccess {
                    FoodBoxLocalStorage
                        .clearSubscription(
                            appContext
                        )

                    _uiState.value =
                        _uiState.value.copy(
                            activeSubscription = null,
                            isLoading = false,
                            message =
                                "Subscription cancelled."
                        )
                }
                .onFailure { exception ->
                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,

                            message =
                                exception.message
                                    ?: "Unable to cancel the subscription."
                        )
                }
        }
    }

    fun clearMessage() {
        _uiState.value =
            _uiState.value.copy(
                message = null
            )
    }

    fun refreshActiveSubscription(
        userId: String? =
            LocalAccountStorage
                .getProfile(appContext)
                ?.id
    ) {
        val cleanUserId =
            userId.orEmpty().trim()

        if (cleanUserId.isBlank()) {
            _uiState.value =
                _uiState.value.copy(
                    activeSubscription = null,
                    isLoading = false
                )

            return
        }

        _uiState.value =
            _uiState.value.copy(
                isLoading = true,
                message = null
            )

        viewModelScope.launch {
            FoodBoxCloudRepository
                .getActiveSubscription(
                    cleanUserId
                )
                .onSuccess { subscription ->
                    if (subscription == null) {
                        FoodBoxLocalStorage
                            .clearSubscription(
                                appContext
                            )
                    } else {
                        FoodBoxLocalStorage
                            .saveSubscription(
                                appContext,
                                subscription
                            )
                    }

                    _uiState.value =
                        _uiState.value.copy(
                            activeSubscription =
                                subscription,

                            isLoading = false,
                            message = null
                        )
                }
                .onFailure { exception ->
                    val savedSubscription =
                        FoodBoxLocalStorage
                            .loadSubscription(
                                appContext
                            )
                            ?.takeIf {
                                it.userId ==
                                        cleanUserId &&
                                        it.status.equals(
                                            FoodBoxData.STATUS_ACTIVE,
                                            ignoreCase = true
                                        )
                            }

                    _uiState.value =
                        _uiState.value.copy(
                            activeSubscription =
                                savedSubscription,

                            isLoading = false,

                            message =
                                exception.message
                                    ?: "Unable to refresh the subscription."
                        )
                }
        }
    }

    private fun saveSubscriptionChange(
        subscription: FoodBoxSubscription
    ) {
        FoodBoxLocalStorage
            .saveSubscription(
                appContext,
                subscription
            )

        viewModelScope.launch {
            FoodBoxCloudRepository
                .updateSubscription(
                    subscription
                )
                .onFailure { exception ->
                    _uiState.value =
                        _uiState.value.copy(
                            message =
                                exception.message
                                    ?: "Unable to save the subscription change."
                        )
                }
        }
    }

    private fun saveDraft() {
        FoodBoxLocalStorage.saveDraft(
            appContext,
            _uiState.value
        )
    }

    private fun createInitialState():
            FoodBoxUiState {
        val defaultPlan =
            FoodBoxData.plans.first()

        val savedDraft =
            FoodBoxLocalStorage
                .loadDraft(appContext)

        val savedSubscription =
            FoodBoxLocalStorage
                .loadSubscription(
                    appContext
                )

        val reminders =
            FoodBoxLocalStorage
                .loadReminderSettings(
                    appContext
                )

        val validSavedPlan =
            savedDraft
                ?.selectedPlanId
                ?.let(
                    FoodBoxData::getPlan
                )

        return if (
            savedDraft != null &&
            validSavedPlan != null
        ) {
            savedDraft.copy(
                activeSubscription =
                    savedSubscription,

                reminderSettings =
                    reminders,

                itemBeingSwappedId = null,
                isLoading = false,
                message = null
            )
        } else {
            FoodBoxUiState(
                selectedPlanId =
                    defaultPlan.id,

                customizedItems =
                    defaultPlan.items,

                activeSubscription =
                    savedSubscription,

                reminderSettings =
                    reminders
            )
        }
    }
}