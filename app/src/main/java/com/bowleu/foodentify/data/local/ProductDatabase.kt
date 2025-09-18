package com.bowleu.foodentify.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bowleu.foodentify.data.model.IngredientEntity
import com.bowleu.foodentify.data.model.ProductEntity

@Database(
    version = 1,
    entities = [
        ProductEntity::class,
        IngredientEntity::class
    ]
)
abstract class ProductDatabase : RoomDatabase() {

    abstract fun getProductDao() : ProductDao

}