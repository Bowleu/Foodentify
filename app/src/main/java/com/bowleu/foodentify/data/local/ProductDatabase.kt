package com.bowleu.foodentify.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    version = 1,
    entities = [
        ProductEntity::class
    ],
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun getProductDao() : ProductDao

}