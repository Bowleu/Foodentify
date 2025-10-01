package com.bowleu.foodentify.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [
        ProductEntity::class,
        NutrientLevelEntity::class
    ],
    exportSchema = false
)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun getProductDao() : ProductDao

}