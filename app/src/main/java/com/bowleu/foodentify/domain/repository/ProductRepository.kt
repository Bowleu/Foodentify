package com.bowleu.foodentify.domain.repository

import com.bowleu.foodentify.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProduct(id: Long): Flow<Product?>
}