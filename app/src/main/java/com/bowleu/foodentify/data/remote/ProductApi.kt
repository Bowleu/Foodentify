package com.bowleu.foodentify.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

data class ProductResponse(
    val product: Product?,
    val status: Int,
    val code: String
)

data class Product(
    val id: Long,
    val name: String,
    val quantity: Double,
    val quantityUnit: String,
    val energyKcal: Double,
    val proteins: Double,
    val fat: Double,
    val carbohydrates: Double,
    val sugars: Double,
    val salt: Double,
    val sodium: Double,
    // TODO Переделать под структуру JSON.
)

interface ProductApi {
    @GET("api/v2/product/{id}.json")
    // TODO Переделать под требования API
    suspend fun getProductById(@Path("id") id: String): ProductResponse
}