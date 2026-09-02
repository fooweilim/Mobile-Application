package com.example.githubdemo.data

import android.util.Patterns

object AuthValidation {

    fun isValidName(
        fullName: String
    ): Boolean {
        return fullName.trim().length >= 2
    }

    fun isValidEmail(
        email: String
    ): Boolean {
        return email.isNotBlank() &&
                Patterns.EMAIL_ADDRESS
                    .matcher(email.trim())
                    .matches()
    }

    fun isValidPhoneNumber(
        phoneNumber: String
    ): Boolean {
        val cleanedPhoneNumber =
            phoneNumber.trim()

        return cleanedPhoneNumber.length in 9..12 &&
                cleanedPhoneNumber.all {
                        character ->

                    character.isDigit()
                }
    }

    fun isValidAdditionalInformation(
        information: String
    ): Boolean {
        return information.trim().length >= 2
    }

    fun isValidPassword(
        password: String
    ): Boolean {
        return password.length >= 8 &&
                password.any { character ->
                    character.isLetter()
                } &&
                password.any { character ->
                    character.isDigit()
                }
    }

    fun passwordsMatch(
        password: String,
        confirmPassword: String
    ): Boolean {
        return password == confirmPassword
    }
}