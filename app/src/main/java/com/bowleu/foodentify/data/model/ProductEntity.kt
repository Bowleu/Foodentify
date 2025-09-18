package com.bowleu.foodentify.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val name: String,
    val quantity: Double,
    @ColumnInfo(name = "quantity_unit") val quantityUnit: String,
    @ColumnInfo(name = "energy_kcal") val energyKcal: Double,
    val proteins: Double,
    val fat: Double,
    val carbohydrates: Double,
    val sugars: Double,
    val salt: Double,
    val sodium: Double,
    )