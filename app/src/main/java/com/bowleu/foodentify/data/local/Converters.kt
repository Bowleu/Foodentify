package com.bowleu.foodentify.data.local

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

object Converters {
    private val moshi = Moshi.Builder().build()
    private val type = Types.newParameterizedType(List::class.java, IngredientEntity::class.java)
    private val adapter = moshi.adapter<List<IngredientEntity>>(type)

    @TypeConverter
    @JvmStatic
    fun fromIngredientList(value: List<IngredientEntity>?): String? {
        return value?.let { adapter.toJson(it) }
    }

    @TypeConverter
    @JvmStatic
    fun toIngredientList(value: String?): List<IngredientEntity>? {
        return value?.let { adapter.fromJson(it) }
    }
}