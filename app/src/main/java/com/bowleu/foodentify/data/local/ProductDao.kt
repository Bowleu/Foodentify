package com.bowleu.foodentify.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(entity = ProductEntity::class)
    fun insertProduct(product: ProductEntity)

    @Update(entity = ProductEntity::class)
    fun updateProduct(product: ProductEntity)

    @Query("DELETE FROM products WHERE id = :id")
    fun deleteProduct(id: Long)
    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    fun getProductById(id: Long): Flow<ProductEntity?>

}