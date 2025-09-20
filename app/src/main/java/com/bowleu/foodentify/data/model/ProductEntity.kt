package com.bowleu.foodentify.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bowleu.foodentify.data.remote.Product

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
    ) {
    companion object {
        fun fromProduct(product: Product): ProductEntity {
            return ProductEntity(
                id = product.id,
                name = product.name,
                quantity = product.quantity,
                quantityUnit = product.quantityUnit,
                energyKcal = product.energyKcal,
                proteins = product.proteins,
                fat = product.fat,
                carbohydrates = product.carbohydrates,
                sugars = product.sugars,
                salt = product.salt,
                sodium = product.sodium
            )
        }
    }
}