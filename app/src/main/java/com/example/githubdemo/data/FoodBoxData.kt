package com.example.githubdemo.data

import com.example.githubdemo.model.FoodBoxAddOn
import com.example.githubdemo.model.FoodBoxItem
import com.example.githubdemo.model.FoodBoxPlan

object FoodBoxData {

    const val STATUS_ACTIVE =
        "active"

    val deliveryDays = listOf(
        "Monday",
        "Wednesday",
        "Friday",
        "Saturday"
    )

    val plans = listOf(
        FoodBoxPlan(
            id = "basic_box",
            name = "Basic Box",
            description =
                "Essentials for small families",
            suitablePax = "2–3 pax",
            monthlyPrice = 20.0,
            yearlyPrice = 216.0,
            calories = "1850",
            protein = "42g",
            vitamins = "A, C, K",
            badge = "Popular",
            items = listOf(
                FoodBoxItem(
                    id = "basic_1",
                    name = "Kangkung",
                    quantity = "500g",
                    swappable = true
                ),
                FoodBoxItem(
                    id = "basic_2",
                    name = "Bayam",
                    quantity = "300g",
                    swappable = true
                ),
                FoodBoxItem(
                    id = "basic_3",
                    name = "Tomato",
                    quantity = "500g",
                    swappable = false
                ),
                FoodBoxItem(
                    id = "basic_4",
                    name = "Papaya",
                    quantity = "1kg",
                    swappable = true
                ),
                FoodBoxItem(
                    id = "basic_5",
                    name = "Carrot",
                    quantity = "300g",
                    swappable = false
                )
            )
        ),
        FoodBoxPlan(
            id = "family_box",
            name = "Family Box",
            description =
                "Complete nutrition for the whole family",
            suitablePax = "4–6 pax",
            monthlyPrice = 40.0,
            yearlyPrice = 432.0,
            calories = "3600",
            protein = "86g",
            vitamins = "A, B, C, K",
            badge = "Best Value",
            items = listOf(
                FoodBoxItem(
                    id = "family_1",
                    name = "Kangkung",
                    quantity = "1kg",
                    swappable = true
                ),
                FoodBoxItem(
                    id = "family_2",
                    name = "Bayam",
                    quantity = "600g",
                    swappable = true
                ),
                FoodBoxItem(
                    id = "family_3",
                    name = "Tomato",
                    quantity = "1kg",
                    swappable = false
                ),
                FoodBoxItem(
                    id = "family_4",
                    name = "Papaya",
                    quantity = "2kg",
                    swappable = true
                ),
                FoodBoxItem(
                    id = "family_5",
                    name = "Carrot",
                    quantity = "600g",
                    swappable = false
                ),
                FoodBoxItem(
                    id = "family_6",
                    name = "Cucumber",
                    quantity = "1kg",
                    swappable = true
                )
            )
        ),
        FoodBoxPlan(
            id = "premium_box",
            name = "Premium Box",
            description =
                "Organic, curated weekly harvest",
            suitablePax = "6–8 pax",
            monthlyPrice = 60.0,
            yearlyPrice = 648.0,
            calories = "5200",
            protein = "118g",
            vitamins = "A, B, C, E, K",
            badge = "Organic",
            items = listOf(
                FoodBoxItem(
                    id = "premium_1",
                    name = "Kailan",
                    quantity = "1kg",
                    swappable = true
                ),
                FoodBoxItem(
                    id = "premium_2",
                    name = "Spinach",
                    quantity = "800g",
                    swappable = true
                ),
                FoodBoxItem(
                    id = "premium_3",
                    name = "Cherry Tomato",
                    quantity = "1kg",
                    swappable = false
                ),
                FoodBoxItem(
                    id = "premium_4",
                    name = "Papaya",
                    quantity = "2kg",
                    swappable = true
                ),
                FoodBoxItem(
                    id = "premium_5",
                    name = "Carrot",
                    quantity = "1kg",
                    swappable = false
                ),
                FoodBoxItem(
                    id = "premium_6",
                    name = "Bittergourd",
                    quantity = "1kg",
                    swappable = true
                ),
                FoodBoxItem(
                    id = "premium_7",
                    name = "Pumpkin",
                    quantity = "2kg",
                    swappable = true
                )
            )
        )
    )

    val replacementItems = listOf(
        FoodBoxItem(
            id = "replacement_bayam",
            name = "Bayam",
            quantity = "300g",
            swappable = true
        ),
        FoodBoxItem(
            id = "replacement_spinach",
            name = "Spinach",
            quantity = "300g",
            swappable = true
        ),
        FoodBoxItem(
            id = "replacement_kailan",
            name = "Kailan",
            quantity = "300g",
            swappable = true
        ),
        FoodBoxItem(
            id = "replacement_bittergourd",
            name = "Bittergourd",
            quantity = "500g",
            swappable = true
        ),
        FoodBoxItem(
            id = "replacement_pumpkin",
            name = "Pumpkin",
            quantity = "1kg",
            swappable = true
        ),
        FoodBoxItem(
            id = "replacement_cucumber",
            name = "Cucumber",
            quantity = "500g",
            swappable = true
        )
    )

    val addOns = listOf(
        FoodBoxAddOn(
            id = "eggs",
            name = "Extra Eggs (6 pcs)",
            price = 3.0
        ),
        FoodBoxAddOn(
            id = "tofu",
            name = "Tofu Pack 400g",
            price = 2.5
        )
    )

    fun getPlan(
        planId: String
    ): FoodBoxPlan? {
        return plans.firstOrNull {
            it.id == planId
        }
    }
}