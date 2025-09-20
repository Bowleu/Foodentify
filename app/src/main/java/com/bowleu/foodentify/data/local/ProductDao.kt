package com.bowleu.foodentify.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.bowleu.foodentify.data.model.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(entity = ProductEntity::class)
    fun insertProduct(product: ProductEntity)

    @Query("DELETE FROM products WHERE id = :productId")
    fun deleteProduct(productId: Long)
    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    fun getProductById(id: Long): Flow<ProductEntity?>

}