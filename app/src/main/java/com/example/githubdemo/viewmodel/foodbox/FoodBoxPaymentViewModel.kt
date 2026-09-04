package com.example.githubdemo.viewmodel.foodbox

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.githubdemo.data.local.LocalAccountStorage
import com.example.githubdemo.model.PaymentMethod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val PAYMENT_PASSWORD_LENGTH = 6
private const val MINIMUM_ACCOUNT_LENGTH = 8
private const val MAXIMUM_ACCOUNT_LENGTH = 18
private const val CARD_NUMBER_LENGTH = 16
private const val MAXIMUM_PHONE_LENGTH = 11

data class FoodBoxPaymentUiState(
    val selectedMethod: PaymentMethod? = null,

    val phoneNumber: String = "",
    val eWalletPassword: String = "",

    val accountNumber: String = "",
    val bankingPassword: String = "",

    val cardNumber: String = "",
    val cardPassword: String = "",

    val phoneError: String? = null,
    val eWalletPasswordError: String? = null,

    val accountError: String? = null,
    val bankingPasswordError: String? = null,

    val cardError: String? = null,
    val cardPasswordError: String? = null,

    val message: String? = null
)

class FoodBoxPaymentViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val savedPhoneNumber =
        LocalAccountStorage
            .getProfile(application.applicationContext)
            ?.phoneNumber
            .orEmpty()
            .filter(Char::isDigit)
            .take(MAXIMUM_PHONE_LENGTH)

    private val _uiState = MutableStateFlow(
        FoodBoxPaymentUiState(
            phoneNumber = savedPhoneNumber
        )
    )

    val uiState: StateFlow<FoodBoxPaymentUiState> =
        _uiState.asStateFlow()

    fun selectPaymentMethod(
        paymentMethod: PaymentMethod
    ) {
        _uiState.value = _uiState.value.copy(
            selectedMethod = paymentMethod,

            phoneError = null,
            eWalletPasswordError = null,

            accountError = null,
            bankingPasswordError = null,

            cardError = null,
            cardPasswordError = null,

            message = null
        )
    }

    fun updatePhoneNumber(value: String) {
        _uiState.value = _uiState.value.copy(
            phoneNumber = value
                .filter(Char::isDigit)
                .take(MAXIMUM_PHONE_LENGTH),

            phoneError = null,
            message = null
        )
    }

    fun updateEWalletPassword(value: String) {
        _uiState.value = _uiState.value.copy(
            eWalletPassword =
                filterPassword(value),

            eWalletPasswordError = null,
            message = null
        )
    }

    fun updateAccountNumber(value: String) {
        _uiState.value = _uiState.value.copy(
            accountNumber = value
                .filter(Char::isDigit)
                .take(MAXIMUM_ACCOUNT_LENGTH),

            accountError = null,
            message = null
        )
    }

    fun updateBankingPassword(value: String) {
        _uiState.value = _uiState.value.copy(
            bankingPassword =
                filterPassword(value),

            bankingPasswordError = null,
            message = null
        )
    }

    fun updateCardNumber(value: String) {
        val digits = value
            .filter(Char::isDigit)
            .take(CARD_NUMBER_LENGTH)

        _uiState.value = _uiState.value.copy(
            cardNumber = digits
                .chunked(4)
                .joinToString(" "),

            cardError = null,
            message = null
        )
    }

    fun updateCardPassword(value: String) {
        _uiState.value = _uiState.value.copy(
            cardPassword =
                filterPassword(value),

            cardPasswordError = null,
            message = null
        )
    }

    fun validateForSubmission(): PaymentMethod? {
        val currentState = _uiState.value
        val paymentMethod =
            currentState.selectedMethod

        if (paymentMethod == null) {
            _uiState.value = currentState.copy(
                message =
                    "Please select a payment method."
            )

            return null
        }

        val valid = when (paymentMethod) {
            PaymentMethod.E_WALLET ->
                validateEWallet(currentState)

            PaymentMethod.ONLINE_BANKING ->
                validateOnlineBanking(currentState)

            PaymentMethod.CARD ->
                validateCard(currentState)
        }

        return paymentMethod.takeIf {
            valid
        }
    }

    private fun validateEWallet(
        currentState: FoodBoxPaymentUiState
    ): Boolean {
        val validPhone =
            currentState.phoneNumber.matches(
                Regex("^01\\d{8,9}$")
            )

        val validPassword =
            isPasswordValid(
                currentState.eWalletPassword
            )

        _uiState.value = currentState.copy(
            phoneError =
                if (validPhone) {
                    null
                } else {
                    "Enter a valid Malaysian phone number."
                },

            eWalletPasswordError =
                getPasswordError(validPassword),

            message =
                if (
                    validPhone &&
                    validPassword
                ) {
                    null
                } else {
                    "Please correct the invalid Touch 'n Go details."
                }
        )

        return validPhone &&
                validPassword
    }

    private fun validateOnlineBanking(
        currentState: FoodBoxPaymentUiState
    ): Boolean {
        val validAccount =
            currentState.accountNumber.length in
                    MINIMUM_ACCOUNT_LENGTH..
                    MAXIMUM_ACCOUNT_LENGTH &&
                    currentState.accountNumber.all(
                        Char::isDigit
                    )

        val validPassword =
            isPasswordValid(
                currentState.bankingPassword
            )

        _uiState.value = currentState.copy(
            accountError =
                if (validAccount) {
                    null
                } else {
                    "Account number must contain 8 to 18 digits."
                },

            bankingPasswordError =
                getPasswordError(validPassword),

            message =
                if (
                    validAccount &&
                    validPassword
                ) {
                    null
                } else {
                    "Please correct the invalid online banking details."
                }
        )

        return validAccount &&
                validPassword
    }

    private fun validateCard(
        currentState: FoodBoxPaymentUiState
    ): Boolean {
        val validCard =
            currentState.cardNumber
                .filter(Char::isDigit)
                .length ==
                    CARD_NUMBER_LENGTH

        val validPassword =
            isPasswordValid(
                currentState.cardPassword
            )

        _uiState.value = currentState.copy(
            cardError =
                if (validCard) {
                    null
                } else {
                    "Card number must contain exactly 16 digits."
                },

            cardPasswordError =
                getPasswordError(validPassword),

            message =
                if (
                    validCard &&
                    validPassword
                ) {
                    null
                } else {
                    "Please correct the invalid card details."
                }
        )

        return validCard &&
                validPassword
    }

    private fun filterPassword(
        value: String
    ): String {
        return value
            .filter(Char::isDigit)
            .take(PAYMENT_PASSWORD_LENGTH)
    }

    private fun isPasswordValid(
        value: String
    ): Boolean {
        return value.length ==
                PAYMENT_PASSWORD_LENGTH &&
                value.all(Char::isDigit)
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