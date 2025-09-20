package com.bowleu.foodentify.data.repository

import com.bowleu.foodentify.data.local.ProductDao
import com.bowleu.foodentify.data.model.ProductEntity
import com.bowleu.foodentify.data.remote.Product
import com.bowleu.foodentify.data.remote.ProductApi
import com.bowleu.foodentify.data.remote.ProductResponse
import com.bowleu.foodentify.data.remote.networkBoundResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ProductRepository {
    fun getProduct(id: String): Flow<ProductEntity?>
}

class RoomRetrofitProductRepository @Inject constructor(
    private val api: ProductApi,
    private val dao: ProductDao
) : ProductRepository {

    override fun getProduct(id: String): Flow<ProductEntity?> {
        return networkBoundResource(
            query = { dao.getProductById(id.toLong()) },
            fetch = { api.getProductById(id) },
            saveFetchResult = { response ->
                if (response.status == 1 && response.product != null) {
                    val entity = ProductEntity.fromProduct(response.product)
                    dao.insertProduct(entity)
                }
            },
            shouldFetch = { local -> local == null }
        )
    }

}