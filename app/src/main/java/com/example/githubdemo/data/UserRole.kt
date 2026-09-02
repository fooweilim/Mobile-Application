package com.example.githubdemo.data

object UserRole {

    val BUYER = "buyer"
    val FARMER = "farmer"
    val ADMIN = "admin"

    fun isValidRole(
        userRole: String
    ): Boolean {
        return userRole == BUYER ||
                userRole == FARMER ||
                userRole == ADMIN
    }

    fun canSignUp(
        userRole: String
    ): Boolean {
        return userRole == BUYER ||
                userRole == FARMER
    }

    fun getRoleName(
        userRole: String
    ): String {
        return when (userRole) {
            BUYER -> "Buyer"
            FARMER -> "Farmer"
            ADMIN -> "Admin"
            else -> "User"
        }
    }

    fun getAdditionalFieldLabel(
        userRole: String
    ): String {
        return when (userRole) {
            BUYER -> "Delivery Address"
            FARMER -> "IC Number"
            ADMIN -> "Admin ID"
            else -> "Additional Information"
        }
    }
}