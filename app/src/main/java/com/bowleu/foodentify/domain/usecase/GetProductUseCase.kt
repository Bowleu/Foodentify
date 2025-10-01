package com.bowleu.foodentify.domain.usecase

import com.bowleu.foodentify.domain.model.Product
import com.bowleu.foodentify.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(id: Long): Flow<Product?> {
        return repository.getProduct(id)
    }
}