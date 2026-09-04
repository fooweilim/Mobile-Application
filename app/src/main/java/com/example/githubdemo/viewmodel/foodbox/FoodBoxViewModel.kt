package com.example.githubdemo.viewmodel.foodbox

import android.app.Application
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

private const val MINIMUM_ADDRESS_LENGTH =
    10

class FoodBoxViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val appContext =
        application.applicationContext

    private val _uiState =
        mutableStateOf(
            createInitialState()
        )

    val uiState: State<FoodBoxUiState> =
        _uiState

    init {
        refreshActiveSubscription()
    }

    fun getSelectedPlan():
            FoodBoxPlan? {

        return FoodBoxData.getPlan(
            _uiState.value.selectedPlanId
        )
    }

    fun selectBillingCycle(
        billingCycle: BillingCycle
    ) {
        _uiState.value =
            _uiState.value.copy(
                billingCycle = billingCycle,
                message = null
            )

        saveDraft()
    }

    fun selectPlan(
        planId: String
    ) {
        val plan =
            FoodBoxData.getPlan(planId)
                ?: return

        _uiState.value =
            _uiState.value.copy(
                selectedPlanId = plan.id,
                customizedItems = plan.items,
                selectedAddOns = emptyList(),
                itemBeingSwappedId = null,
                message = null
            )

        saveDraft()
    }

    fun showSwapOptions(
        itemId: String
    ) {
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

        /*
         * The replacement keeps the
         * original/default item weight.
         */
        val replacementWithOriginalWeight =
            replacement.copy(
                id = original.id,
                quantity = original.quantity,
                swappable = original.swappable,
                isSwapped = true
            )

        _uiState.value =
            _uiState.value.copy(
                customizedItems =
                    _uiState.value
                        .customizedItems
                        .map { item ->
                            if (
                                item.id ==
                                originalId
                            ) {
                                replacementWithOriginalWeight
                            } else {
                                item
                            }
                        },
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
                    .selectedAddOns + addOn
            }

        _uiState.value =
            _uiState.value.copy(
                selectedAddOns = newAddOns,
                message = null
            )

        saveDraft()
    }

    fun selectDeliveryDay(
        day: String
    ) {
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
        val clean =
            address.trim()

        if (
            clean.length <
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
                deliveryAddress = clean,
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
        val state =
            _uiState.value

        val plan =
            getSelectedPlan()

        if (
            state.activeSubscription?.status ==
            FoodBoxData.STATUS_ACTIVE
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

        if (userId.isNullOrBlank()) {
            _uiState.value =
                state.copy(
                    message =
                        "Please sign in before subscribing."
                )

            return
        }

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

        if (
            state.deliveryAddress.length <
            MINIMUM_ADDRESS_LENGTH
        ) {
            _uiState.value =
                state.copy(
                    message =
                        "Please enter a complete delivery address."
                )

            return
        }

        val subscription =
            FoodBoxSubscription(
                id =
                    System.currentTimeMillis()
                        .toString(),
                userId = userId,
                planId = plan.id,
                planName = plan.name,
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
                    System.currentTimeMillis()
            )

        _uiState.value =
            state.copy(
                isLoading = true,
                message = null
            )

        viewModelScope.launch {
            val result =
                FoodBoxCloudRepository
                    .createSubscription(
                        subscription
                    )

            result.onSuccess {
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
            }.onFailure { exception ->
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
                deliveryDay = day,
                activeSubscription = updated,
                message =
                    "Delivery day updated to $day."
            )

        saveSubscriptionChange(updated)
    }

    fun updateSubscriptionAddress(
        address: String
    ): Boolean {
        val clean =
            address.trim()

        if (
            clean.length <
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
                deliveryAddress = clean
            )

        _uiState.value =
            _uiState.value.copy(
                deliveryAddress = clean,
                activeSubscription = updated,
                message =
                    "Delivery address updated."
            )

        saveSubscriptionChange(updated)

        return true
    }

    fun updateReminderSettings(
        settings: FoodBoxReminderSettings
    ) {
        _uiState.value =
            _uiState.value.copy(
                reminderSettings = settings
            )

        FoodBoxLocalStorage
            .saveReminderSettings(
                appContext,
                settings
            )

        saveDraft()
    }

    fun cancelSubscription() {
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
            val result =
                FoodBoxCloudRepository
                    .deleteSubscription(
                        subscription.id
                    )

            result.onSuccess {
                FoodBoxLocalStorage
                    .clearSubscription(
                        appContext
                    )

                _uiState.value =
                    _uiState.value.copy(
                        activeSubscription = null,
                        isLoading = false,
                        message =
                            "Your subscription has been cancelled."
                    )
            }.onFailure { exception ->
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

    private fun saveSubscriptionChange(
        subscription: FoodBoxSubscription
    ) {
        FoodBoxLocalStorage
            .saveSubscription(
                appContext,
                subscription
            )

        saveDraft()

        viewModelScope.launch {
            val result =
                FoodBoxCloudRepository
                    .updateSubscription(
                        subscription
                    )

            result
                .exceptionOrNull()
                ?.message
                ?.let { errorMessage ->
                    _uiState.value =
                        _uiState.value.copy(
                            message =
                                errorMessage
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

    fun refreshActiveSubscription(
        userId: String? =
            LocalAccountStorage
                .getProfile(appContext)
                ?.id
    ) {
        if (userId.isNullOrBlank()) {
            return
        }

        viewModelScope.launch {
            FoodBoxCloudRepository
                .getActiveSubscription(
                    userId
                )
                .onSuccess {
                        cloudSubscription ->

                    if (
                        cloudSubscription ==
                        null
                    ) {
                        FoodBoxLocalStorage
                            .clearSubscription(
                                appContext
                            )
                    } else {
                        FoodBoxLocalStorage
                            .saveSubscription(
                                appContext,
                                cloudSubscription
                            )
                    }

                    _uiState.value =
                        _uiState.value.copy(
                            activeSubscription =
                                cloudSubscription
                        )
                }
        }
    }

    private fun createInitialState():
            FoodBoxUiState {

        val savedDraft =
            FoodBoxLocalStorage
                .loadDraft(appContext)

        val subscription =
            FoodBoxLocalStorage
                .loadSubscription(
                    appContext
                )

        val reminders =
            FoodBoxLocalStorage
                .loadReminderSettings(
                    appContext
                )

        if (savedDraft != null) {
            return savedDraft.copy(
                activeSubscription =
                    subscription,
                reminderSettings =
                    reminders,
                itemBeingSwappedId =
                    null,
                isLoading = false,
                message = null
            )
        }

        val plan =
            FoodBoxData.plans.first()

        return FoodBoxUiState(
            selectedPlanId = plan.id,
            customizedItems = plan.items,
            activeSubscription =
                subscription,
            reminderSettings = reminders
        )
    }
}