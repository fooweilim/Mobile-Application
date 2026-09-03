package com.example.githubdemo.data.meals

import com.example.githubdemo.R

object MealData {

    val meals = listOf(
        Meal(
            id = 1,
            name = "Tomato Egg Stir Fry",
            price = 3.00,
            pax = 4,
            supportedPax = listOf(1, 2, 4, 6, 8),
            protein = "12g",
            calories = "220 kcal",
            vitamins = "A, C",
            imageRes = R.drawable.tomato_egg_stir_fry,

            ingredients = listOf(
                "For 1 Pax:",
                "Tomatoes - 1 medium",
                "Eggs - 2",
                "Garlic - 1 clove",
                "Onion - 1/4",

                "For 2 Pax:",
                "Tomatoes - 2 medium",
                "Eggs - 3",
                "Garlic - 2 cloves",
                "Onion - 1/2",

                "For 4 Pax:",
                "Tomatoes - 4 medium",
                "Eggs - 6",
                "Garlic - 3 cloves",
                "Onion - 1 small",

                "For 6 Pax:",
                "Tomatoes - 6 medium",
                "Eggs - 9",
                "Garlic - 5 cloves",
                "Onion - 1.5",

                "For 8 Pax:",
                "Tomatoes - 8 medium",
                "Eggs - 12",
                "Garlic - 6 cloves",
                "Onion - 2"
            ),

            seasonings = listOf(
                "Salt",
                "Black pepper",
                "Tomato ketchup",
                "Soy sauce",
                "Cooking oil"
            )
        ),

        Meal(
            id = 2,
            name = "Vegetable Rice",
            price = 5.00,
            pax = 4,
            supportedPax = listOf(2, 4, 6, 8),
            protein = "8g",
            calories = "250 kcal",
            vitamins = "A, B6, C",
            imageRes = R.drawable.vegetable_rice,

            ingredients = listOf(
                "For 2 Pax:",
                "Rice - 2 bowls",
                "Carrot - 1/2",
                "Green peas - 1/4 cup",
                "Corn - 1/4 cup",
                "Egg - 1",

                "For 4 Pax:",
                "Rice - 4 bowls",
                "Carrot - 1",
                "Green peas - 1/2 cup",
                "Corn - 1/2 cup",
                "Eggs - 2",

                "For 6 Pax:",
                "Rice - 6 bowls",
                "Carrot - 2",
                "Green peas - 1 cup",
                "Corn - 1 cup",
                "Eggs - 3",

                "For 8 Pax:",
                "Rice - 8 bowls",
                "Carrot - 3",
                "Green peas - 1.5 cups",
                "Corn - 1.5 cups",
                "Eggs - 4"
            ),

            seasonings = listOf(
                "Salt",
                "Soy sauce",
                "Black pepper",
                "Cooking oil"
            )
        ),

        Meal(
            id = 3,
            name = "Mushroom Omelette",
            price = 3.00,
            pax = 4,
            supportedPax = listOf(1, 2, 4),
            protein = "15g",
            calories = "220 kcal",
            vitamins = "A, B12, D",
            imageRes = R.drawable.mushroom_omelette,

            ingredients = listOf(
                "For 1 Pax:",
                "Eggs - 1",
                "Mushroom - 50g",
                "Onion - small amount",

                "For 2 Pax:",
                "Eggs - 3",
                "Mushroom - 100g",
                "Onion - 1/4",

                "For 4 Pax:",
                "Eggs - 6",
                "Mushroom - 200g",
                "Onion - 1/2",
                "Spring onion"
            ),

            seasonings = listOf(
                "Salt",
                "Black pepper",
                "Cooking oil"
            )
        ),

        Meal(
            id = 4,
            name = "Carrot & Potato Soup",
            price = 5.00,
            pax = 4,
            supportedPax = listOf(2, 4, 6, 8),
            protein = "3.5g",
            calories = "170 kcal",
            vitamins = "A, B6, K, C",
            imageRes = R.drawable.carrot_potato_soup,

            ingredients = listOf(
                "For 2 Pax:",
                "Potato - 1",
                "Carrot - 1",
                "Onion - 1/2",
                "Garlic - 1 clove",

                "For 4 Pax:",
                "Potato - 3",
                "Carrot - 2",
                "Onion - 1",
                "Garlic - 2 cloves",

                "For 6 Pax:",
                "Potato - 5",
                "Carrot - 3",
                "Onion - 1.5",
                "Garlic - 4 cloves",

                "For 8 Pax:",
                "Potato - 6",
                "Carrot - 4",
                "Onion - 2",
                "Garlic - 5 cloves"
            ),

            seasonings = listOf(
                "Salt",
                "Black pepper",
                "Chicken stock"
            )
        ),

        Meal(
            id = 5,
            name = "Banana Oatmeal",
            price = 4.00,
            pax = 2,
            supportedPax = listOf(1, 2),
            protein = "6g",
            calories = "280 kcal",
            vitamins = "B6, C",
            imageRes = R.drawable.banana_oatmeal,

            ingredients = listOf(
                "For 1 Pax:",
                "Oats - 1/2 cup",
                "Banana - 1",
                "Milk - 100ml",
                "Honey - 1 tsp",

                "For 2 Pax:",
                "Oats - 1 cup",
                "Banana - 2",
                "Milk - 200ml",
                "Honey - 2 tsp"
            ),

            seasonings = listOf(
                "Cinnamon",
                "Honey"
            )
        ),

        Meal(
            id = 6,
            name = "Tuna Sandwich",
            price = 5.00,
            pax = 4,
            supportedPax = listOf(1, 2, 4),
            protein = "18g",
            calories = "320 kcal",
            vitamins = "A, D",
            imageRes = R.drawable.tuna_sandwich,

            ingredients = listOf(
                "For 1 Pax:",
                "Bread - 2 slices",
                "Tuna - 50g",
                "Lettuce",
                "Cucumber",

                "For 2 Pax:",
                "Bread - 4 slices",
                "Tuna - 100g",
                "Lettuce",
                "Cucumber",

                "For 4 Pax:",
                "Bread - 8 slices",
                "Tuna - 200g",
                "Lettuce",
                "Cucumber"
            ),

            seasonings = listOf(
                "Black pepper",
                "Salt",
                "Mayonnaise"
            )
        ),

        Meal(
            id = 7,
            name = "Vegetable Pasta",
            price = 7.00,
            pax = 4,
            supportedPax = listOf(2, 4, 6),
            protein = "10g",
            calories = "350 kcal",
            vitamins = "A, C, K",
            imageRes = R.drawable.vegetable_pasta,

            ingredients = listOf(
                "For 2 Pax:",
                "Pasta - 150g",
                "Broccoli - 50g",
                "Tomato - 1",
                "Carrot - 1/2",

                "For 4 Pax:",
                "Pasta - 300g",
                "Broccoli - 100g",
                "Tomato - 2",
                "Carrot - 1",

                "For 6 Pax:",
                "Pasta - 450g",
                "Broccoli - 150g",
                "Tomato - 3",
                "Carrot - 2"
            ),

            seasonings = listOf(
                "Salt",
                "Black pepper",
                "Olive oil"
            )
        ),

        Meal(
            id = 8,
            name = "Chicken Fried Rice",
            price = 8.00,
            pax = 4,
            supportedPax = listOf(2, 4, 6, 8),
            protein = "25g",
            calories = "450 kcal",
            vitamins = "B6, C",
            imageRes = R.drawable.chicken_fried_rice,

            ingredients = listOf(
                "For 2 Pax:",
                "Rice - 2 bowls",
                "Chicken - 100g",
                "Eggs - 1",
                "Carrot - 1/2",

                "For 4 Pax:",
                "Rice - 4 bowls",
                "Chicken - 200g",
                "Eggs - 2",
                "Carrot - 1",

                "For 6 Pax:",
                "Rice - 6 bowls",
                "Chicken - 300g",
                "Eggs - 3",
                "Carrot - 2",

                "For 8 Pax:",
                "Rice - 8 bowls",
                "Chicken - 400g",
                "Eggs - 4",
                "Carrot - 3"
            ),

            seasonings = listOf(
                "Soy sauce",
                "Salt",
                "Black pepper"
            )
        ),

        Meal(
            id = 9,
            name = "Chicken Vegetable Soup",
            price = 8.00,
            pax = 6,
            supportedPax = listOf(4, 6, 8),
            protein = "22g",
            calories = "300 kcal",
            vitamins = "A, B6, C",
            imageRes = R.drawable.chicken_vegetable_soup,

            ingredients = listOf(
                "For 4 Pax:",
                "Chicken - 200g",
                "Carrot - 2",
                "Potato - 2",
                "Onion - 1",

                "For 6 Pax:",
                "Chicken - 300g",
                "Carrot - 3",
                "Potato - 3",
                "Onion - 1.5",

                "For 8 Pax:",
                "Chicken - 400g",
                "Carrot - 4",
                "Potato - 4",
                "Onion - 2"
            ),

            seasonings = listOf(
                "Salt",
                "Black pepper",
                "Chicken stock"
            )
        ),

        Meal(
            id = 10,
            name = "Fish Rice Bowl",
            price = 10.00,
            pax = 4,
            supportedPax = listOf(2, 4),
            protein = "28g",
            calories = "480 kcal",
            vitamins = "D, B12",
            imageRes = R.drawable.fish_rice_bowl,

            ingredients = listOf(
                "For 2 Pax:",
                "Rice - 2 bowls",
                "Fish fillet - 100g",
                "Vegetables",
                "Egg - 1",

                "For 4 Pax:",
                "Rice - 4 bowls",
                "Fish fillet - 200g",
                "Vegetables",
                "Eggs - 2"
            ),

            seasonings = listOf(
                "Soy sauce",
                "Black pepper",
                "Salt"
            )
        )
    )

    fun getMealById(id: Int): Meal? {
        return meals.find {
            it.id == id
        }
    }

    fun filterMeal(
        budget: Double,
        pax: Int
    ): List<Meal> {
        return meals.filter {
            it.price <= budget &&
                    it.supportedPax.contains(pax)
        }
    }

    fun getIngredientsByPax(
        meal: Meal,
        pax: Int
    ): List<String> {
        val title = "For $pax Pax:"
        val index = meal.ingredients.indexOf(title)

        if (index == -1) {
            return emptyList()
        }

        return meal.ingredients
            .drop(index + 1)
            .takeWhile {
                !it.startsWith("For ")
            }
    }
}