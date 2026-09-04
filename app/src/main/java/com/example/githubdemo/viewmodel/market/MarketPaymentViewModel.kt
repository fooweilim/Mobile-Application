package com.example.githubdemo.viewmodel.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubdemo.model.PaymentMethod
import com.example.githubdemo.model.market.CartProduct
import com.example.githubdemo.model.market.MarketOrderSummary
import com.example.githubdemo.model.market.MarketPaymentUiState
import com.example.githubdemo.supabase.MarketOrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val PAYMENT_PASSWORD_LENGTH = 6
private const val MINIMUM_ACCOUNT_LENGTH = 8
private const val MAXIMUM_ACCOUNT_LENGTH = 18
private const val CARD_NUMBER_LENGTH = 16

class MarketPaymentViewModel : ViewModel() {

    private val repository =
        MarketOrderRepository()

    private val _uiState =
        MutableStateFlow(
            MarketPaymentUiState()
        )

    val uiState: StateFlow<MarketPaymentUiState> =
        _uiState.asStateFlow()

    fun selectPaymentMethod(
        paymentMethod: PaymentMethod
    ) {
        _uiState.value =
            _uiState.value.copy(
                selectedMethod = paymentMethod,

                message = null,

                phoneError = null,
                eWalletPasswordError = null,

                accountError = null,
                bankingPasswordError = null,

                cardError = null,
                cardPasswordError = null
            )
    }

    fun prefillPhoneNumber(
        phoneNumber: String
    ) {
        if (
            _uiState.value
                .phoneNumber
                .isBlank()
        ) {
            updatePhoneNumber(
                phoneNumber
            )
        }
    }

    fun updatePhoneNumber(
        value: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                phoneNumber =
                    value
                        .filter(
                            Char::isDigit
                        )
                        .take(11),

                phoneError = null,

                message = null
            )
    }

    fun updateEWalletPassword(
        value: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                eWalletPassword =
                    value
                        .filter(
                            Char::isDigit
                        )
                        .take(
                            PAYMENT_PASSWORD_LENGTH
                        ),

                eWalletPasswordError = null,

                message = null
            )
    }

    fun updateAccountNumber(
        value: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                accountNumber =
                    value
                        .filter(
                            Char::isDigit
                        )
                        .take(
                            MAXIMUM_ACCOUNT_LENGTH
                        ),

                accountError = null,

                message = null
            )
    }

    fun updateBankingPassword(
        value: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                bankingPassword =
                    value
                        .filter(
                            Char::isDigit
                        )
                        .take(
                            PAYMENT_PASSWORD_LENGTH
                        ),

                bankingPasswordError = null,

                message = null
            )
    }

    fun updateCardNumber(
        value: String
    ) {
        val digits =
            value
                .filter(
                    Char::isDigit
                )
                .take(
                    CARD_NUMBER_LENGTH
                )

        _uiState.value =
            _uiState.value.copy(
                cardNumber =
                    digits
                        .chunked(4)
                        .joinToString(
                            separator = " "
                        ),

                cardError = null,

                message = null
            )
    }

    fun updateCardPassword(
        value: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                cardPassword =
                    value
                        .filter(
                            Char::isDigit
                        )
                        .take(
                            PAYMENT_PASSWORD_LENGTH
                        ),

                cardPasswordError = null,

                message = null
            )
    }

    fun submitOrder(
        customerId: String,
        items: List<CartProduct>
    ) {
        if (
            _uiState.value.isLoading
        ) {
            return
        }

        val paymentMethod =
            _uiState.value
                .selectedMethod

        if (paymentMethod == null) {
            _uiState.value =
                _uiState.value.copy(
                    message =
                        "Please select a payment method."
                )

            return
        }

        if (
            !validate(
                paymentMethod
            )
        ) {
            return
        }

        if (items.isEmpty()) {
            _uiState.value =
                _uiState.value.copy(
                    message =
                        "Your checkout is empty. " +
                                "Please return to the cart."
                )

            return
        }

        viewModelScope.launch {
            _uiState.value =
                _uiState.value.copy(
                    isLoading = true,

                    message = null
                )

            try {
                repository.placeOrder(
                    customerId =
                        customerId,

                    items =
                        items,

                    paymentMethod =
                        paymentMethod
                )

                _uiState.value =
                    MarketPaymentUiState(
                        orderSummary =
                            MarketOrderSummary(
                                itemCount =
                                    items.sumOf {
                                        it.quantity
                                    },

                                totalPrice =
                                    items.sumOf {
                                        it.product.price *
                                                it.quantity
                                    },

                                paymentMethod =
                                    paymentMethod
                            )
                    )
            } catch (
                exception: Exception
            ) {
                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false,

                        message =
                            exception.message
                                ?: "Unable to place the order. " +
                                "Please try again."
                    )
            }
        }
    }

    fun reset() {
        _uiState.value =
            MarketPaymentUiState()
    }

    private fun validate(
        paymentMethod: PaymentMethod
    ): Boolean {
        val state =
            _uiState.value

        return when (
            paymentMethod
        ) {
            PaymentMethod.E_WALLET -> {
                validateEWallet(
                    state
                )
            }

            PaymentMethod.ONLINE_BANKING -> {
                validateOnlineBanking(
                    state
                )
            }

            PaymentMethod.CARD -> {
                validateCard(
                    state
                )
            }
        }
    }

    private fun validateEWallet(
        state: MarketPaymentUiState
    ): Boolean {
        val validPhone =
            state.phoneNumber
                .matches(
                    Regex(
                        "^01\\d{8,9}$"
                    )
                )

        val validPassword =
            isValidPaymentPassword(
                state.eWalletPassword
            )

        _uiState.value =
            state.copy(
                phoneError =
                    if (validPhone) {
                        null
                    } else {
                        "Enter a valid Malaysian phone number, " +
                                "for example 0123456789."
                    },

                eWalletPasswordError =
                    getPasswordError(
                        validPassword
                    ),

                message =
                    if (
                        validPhone &&
                        validPassword
                    ) {
                        null
                    } else {
                        "Please correct the invalid " +
                                "Touch 'n Go details."
                    }
            )

        return validPhone &&
                validPassword
    }

    private fun validateOnlineBanking(
        state: MarketPaymentUiState
    ): Boolean {
        val validAccount =
            state.accountNumber.length in
                    MINIMUM_ACCOUNT_LENGTH..
                    MAXIMUM_ACCOUNT_LENGTH &&
                    state.accountNumber
                        .all(
                            Char::isDigit
                        )

        val validPassword =
            isValidPaymentPassword(
                state.bankingPassword
            )

        _uiState.value =
            state.copy(
                accountError =
                    if (validAccount) {
                        null
                    } else {
                        "Account number must contain " +
                                "$MINIMUM_ACCOUNT_LENGTH to " +
                                "$MAXIMUM_ACCOUNT_LENGTH digits."
                    },

                bankingPasswordError =
                    getPasswordError(
                        validPassword
                    ),

                message =
                    if (
                        validAccount &&
                        validPassword
                    ) {
                        null
                    } else {
                        "Please correct the invalid " +
                                "online banking details."
                    }
            )

        return validAccount &&
                validPassword
    }

    private fun validateCard(
        state: MarketPaymentUiState
    ): Boolean {
        val cardDigits =
            state.cardNumber
                .filter(
                    Char::isDigit
                )

        val validCard =
            cardDigits.length ==
                    CARD_NUMBER_LENGTH

        val validPassword =
            isValidPaymentPassword(
                state.cardPassword
            )

        _uiState.value =
            state.copy(
                cardError =
                    if (validCard) {
                        null
                    } else {
                        "Card number must contain " +
                                "exactly $CARD_NUMBER_LENGTH digits."
                    },

                cardPasswordError =
                    getPasswordError(
                        validPassword
                    ),

                message =
                    if (
                        validCard &&
                        validPassword
                    ) {
                        null
                    } else {
                        "Please correct the invalid " +
                                "card details."
                    }
            )

        return validCard &&
                validPassword
    }

    private fun isValidPaymentPassword(
        password: String
    ): Boolean {
        return password.length ==
                PAYMENT_PASSWORD_LENGTH &&
                password.all(
                    Char::isDigit
                )
    }

    private fun getPasswordError(
        valid: Boolean
    ): String? {
        return if (valid) {
            null
        } else {
            "Password must contain exactly " +
                    "$PAYMENT_PASSWORD_LENGTH digits."
        }
    }
}