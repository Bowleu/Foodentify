package com.bowleu.foodentify.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "ingredients",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"]
        ),
        ForeignKey (
            entity = IngredientEntity::class,
            parentColumns = ["id"],
            childColumns = ["ingredient_id"]
        )
    ]
)
data class IngredientEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "product_id") val productId: Long,
    @ColumnInfo(name = "ingredient_id") val ingredientId: Long,
    val percent: Double,
    val vegan: Boolean,
    val vegetarian: Boolean,
)
