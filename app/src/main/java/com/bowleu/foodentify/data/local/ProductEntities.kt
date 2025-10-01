package com.bowleu.foodentify.data.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = NutrientLevelEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_fat_level"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = NutrientLevelEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_salt_level"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = NutrientLevelEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_saturated_fat_level"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = NutrientLevelEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_sugars_level"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("id_fat_level"),
        Index("id_salt_level"),
        Index("id_saturated_fat_level"),
        Index("id_sugars_level")
    ]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val name: String,
    val quantity: Double,
    @ColumnInfo(name = "image_front_url") val imageFrontUrl: String,
    @ColumnInfo(name = "id_fat_level") val fatLevelId: Long,
    @ColumnInfo(name = "id_salt_level") val saltLevelId: Long,
    @ColumnInfo(name = "id_saturated_fat_level") val saturatedFatLevelId: Long,
    @ColumnInfo(name = "id_sugars_level") val sugarsLevelId: Long,
    val allergens: String,
    @Embedded val nutriments: NutrimentsEntity,
    @ColumnInfo(name = "last_updated") val lastUpdated: Long = System.currentTimeMillis(),
)


data class NutrimentsEntity(
    @ColumnInfo(name = "energy_kcal") val energyKcal: Double,
    val fat: Double,
    val proteins: Double,
    val salt: Double,
    val sugars: Double,
    val carbohydrates: Double
)

@Entity(
    tableName = "nutrient_levels",
    indices = [Index("name", unique = true)]
)
data class NutrientLevelEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo("name") val name: String
)