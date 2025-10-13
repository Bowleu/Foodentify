package com.bowleu.foodentify.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

@JsonClass(generateAdapter = true)
data class ProductResponse(
    val code: String,
    val product: ProductDto?,
    val status: Int,
    @Json(name = "status_verbose") val statusVerbose: String
)

@JsonClass(generateAdapter = true)
data class ProductDto(
    val code: String?,
    @Json(name = "product_name") val productName: String?,
    val quantity: String?,
    @Json(name = "nutrient_levels") val nutrientLevels: NutrientLevelsDto?,
    @Json(name = "image_front_url") val imageFrontUrl: String?,
    val allergens: String?,
    val nutriments: NutrimentsDto?,
    val ingredients: List<IngredientDto>? = null
)

@JsonClass(generateAdapter = true)
data class NutrientLevelsDto(
    val fat: NutrientLevelDto?,
    val salt: NutrientLevelDto?,
    @Json(name = "saturated-fat") val saturatedFat: NutrientLevelDto?,
    val sugars: NutrientLevelDto?,
)

enum class NutrientLevelDto {
    @Json(name = "low")
    LOW,
    @Json(name = "moderate")
    MODERATE,
    @Json(name = "high")
    HIGH
}

data class IngredientDto(
    val id: String?,
    val text: String?,
    @Json(name = "percent_estimate") val percentEstimate: Double?,
    val vegan: String?,
    val vegetarian: String?,
    val ingredients: List<IngredientDto>? = null
)

@JsonClass(generateAdapter = true)
data class NutrimentsDto(
    @Json(name = "energy-kcal") val energyKcal: Double?,
    val fat: Double?,
    val proteins: Double?,
    val salt: Double?,
    val sugars: Double?,
    val carbohydrates: Double?
)

interface ProductApi {
    @GET("api/v2/product/{barcode}")
    suspend fun getProductById(
        @Path("barcode") barcode: String,
        @Query("product_type") productType: String = "food",
        @Query("fields") fields: String = "code,product_name,quantity,nutrient_levels,image_front_url,allergens,nutriments.energy-kcal,nutriments.fat,nutriments.proteins,nutriments.salt,nutriments.sugars,nutriments.carbohydrates,ingredients"
    ): Response<ProductResponse>
}