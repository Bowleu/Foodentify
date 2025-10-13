package com.bowleu.foodentify.data.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "products",
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val name: String,
    val quantity: String,
    @ColumnInfo(name = "image_front_url") val imageFrontUrl: String,
    @ColumnInfo(name = "fat_level") val fatLevel: String,
    @ColumnInfo(name = "salt_level") val saltLevel: String,
    @ColumnInfo(name = "saturated_fat_level") val saturatedFatLevel: String,
    @ColumnInfo(name = "sugars_level") val sugarsLevel: String,
    val allergens: String,
    @Embedded val nutriments: NutrimentsEntity,
    @ColumnInfo(name = "last_updated") val lastUpdated: Long = System.currentTimeMillis(),
    val ingredients: List<IngredientEntity>
)


data class NutrimentsEntity(
    @ColumnInfo(name = "energy_kcal") val energyKcal: Double,
    val fat: Double,
    val proteins: Double,
    val salt: Double,
    val sugars: Double,
    val carbohydrates: Double
)

data class IngredientEntity(
    val name: String,
    val percent: Double,
    val vegan: String,
    val vegetarian: String,
    val ingredients: List<IngredientEntity>
)