package com.bowleu.foodentify.domain.model

data class Product(
    val id: Long,
    val name: String,
    val quantity: String,
    val nutrientLevels: NutrientLevels,
    val imageFrontUrl: String,
    val allergens: String,
    val nutriments: Nutriments,
    val ingredients: List<Ingredient>
)

data class NutrientLevels(
    val fat: NutrientLevel,
    val salt: NutrientLevel,
    val saturatedFat: NutrientLevel,
    val sugars: NutrientLevel,
)

enum class NutrientLevel {
    HIGH,
    MODERATE,
    LOW
}

data class Nutriments(
    val energyKcal: Double,
    val fat: Double,
    val proteins: Double,
    val salt: Double,
    val sugars: Double,
    val carbohydrates: Double
)

data class Ingredient(
    val name: String,
    val percent: Double,
    val vegan: String,
    val vegetarian: String,
    val ingredients: List<Ingredient>
)
