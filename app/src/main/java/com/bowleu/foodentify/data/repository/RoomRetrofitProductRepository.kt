package com.bowleu.foodentify.data.repository

import androidx.compose.ui.text.toUpperCase
import com.bowleu.foodentify.data.local.IngredientEntity
import com.bowleu.foodentify.data.local.NutrimentsEntity
import com.bowleu.foodentify.data.local.ProductDao
import com.bowleu.foodentify.data.local.ProductEntity
import com.bowleu.foodentify.data.remote.IngredientDto
import com.bowleu.foodentify.data.remote.ProductApi
import com.bowleu.foodentify.data.remote.ProductDto
import com.bowleu.foodentify.data.remote.networkBoundResource
import com.bowleu.foodentify.domain.model.Ingredient
import com.bowleu.foodentify.domain.model.NutrientLevel
import com.bowleu.foodentify.domain.model.NutrientLevels
import com.bowleu.foodentify.domain.model.Nutriments
import com.bowleu.foodentify.domain.model.Product
import com.bowleu.foodentify.domain.repository.ProductRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import kotlin.collections.map

class RoomRetrofitProductRepository @Inject constructor(
    private val api: ProductApi,
    private val dao: ProductDao
) : ProductRepository {

    companion object {
        private const val CACHE_TIMEOUT = 60 * 60 * 1000L
    }

    override fun getProduct(id: Long): Flow<Product?> {
        val entityFlow = networkBoundResource(
            query = { dao.getProductById(id) },
            fetch = { api.getProductById(id.toString()) },
            saveFetchResult = { response ->
                if (response.isSuccessful) {
                    val productResponse = response.body()
                    if ((productResponse?.status ?: 0) == 1 && productResponse?.product != null) {
                        val entity = response.body()?.product?.toEntity()
                        entity.let {
                            if (it != null)
                                dao.insertProduct(it)
                        }
                    }
                } else {
                    Timber.e("Ошибка запроса: ${response.code()}")
                }
            },
            shouldFetch = { local ->
                val now = System.currentTimeMillis()
                val isCacheStale = local == null || (now - local.lastUpdated) > CACHE_TIMEOUT

                isCacheStale //&& networkChecker.isOnline()
            }
        )
        return entityFlow.map { entity -> entity?.toDomain() }
    }
}

fun ProductDto.toEntity(): ProductEntity = ProductEntity(
    id = (code ?: "0").toLong(),
    name = productName ?: "",
    quantity = quantity ?: "0 g",
    imageFrontUrl = imageFrontUrl ?: "",
    allergens = allergens ?: "",
    nutriments = NutrimentsEntity(
        energyKcal = nutriments?.energyKcal ?: 0.0,
        fat = nutriments?.fat ?: 0.0,
        proteins = nutriments?.proteins ?: 0.0,
        salt = nutriments?.salt ?: 0.0,
        sugars = nutriments?.sugars ?: 0.0,
        carbohydrates = nutriments?.carbohydrates ?: 0.0
    ),
    lastUpdated = System.currentTimeMillis(),
    fatLevel = nutrientLevels?.fat?.name ?: "LOW",
    saltLevel = nutrientLevels?.salt?.name ?: "LOW",
    saturatedFatLevel = nutrientLevels?.saturatedFat?.name ?: "LOW",
    sugarsLevel = nutrientLevels?.sugars?.name ?: "LOW",
    ingredients = ingredients?.map { it.toEntity() }.orEmpty(),
)

fun IngredientDto.toEntity(): IngredientEntity = IngredientEntity(
    name = text ?: "",
    percent = percentEstimate ?: 0.0,
    vegan = vegan ?: "",
    vegetarian = vegetarian ?: "",
    ingredients = ingredients?.map { it.toEntity() }.orEmpty()
)

fun ProductEntity.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        quantity = quantity,
        nutrientLevels = NutrientLevels(
            fat = NutrientLevel.valueOf(fatLevel.uppercase()),
            salt = NutrientLevel.valueOf(saltLevel.uppercase()),
            saturatedFat = NutrientLevel.valueOf(saturatedFatLevel.uppercase()),
            sugars = NutrientLevel.valueOf(sugarsLevel.uppercase())
        ),
        imageFrontUrl = imageFrontUrl,
        allergens = allergens,
        nutriments = Nutriments(
            energyKcal = nutriments.energyKcal,
            fat = nutriments.fat,
            proteins = nutriments.proteins,
            salt = nutriments.salt,
            sugars = nutriments.sugars,
            carbohydrates = nutriments.carbohydrates
        ),
        ingredients = ingredients.map { it.toDomain() }
    )
}

fun IngredientEntity.toDomain(): Ingredient = Ingredient(
    name = name,
    percent = percent,
    vegan = vegan,
    vegetarian = vegetarian,
    ingredients = ingredients.map { it.toDomain() }
)