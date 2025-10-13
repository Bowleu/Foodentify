package com.bowleu.foodentify.ui.product

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.bowleu.foodentify.R
import com.bowleu.foodentify.domain.model.Ingredient
import com.bowleu.foodentify.domain.model.NutrientLevel
import com.bowleu.foodentify.domain.model.NutrientLevels
import com.bowleu.foodentify.domain.model.Nutriments
import com.bowleu.foodentify.domain.model.Product
import com.bowleu.foodentify.ui.common.DefaultScreenScaffold
import com.bowleu.foodentify.ui.common.ErrorScreen
import com.bowleu.foodentify.ui.common.LoadingScreen
import com.bowleu.foodentify.ui.theme.FoodentifyTheme
import timber.log.Timber

@Composable
fun ProductScreen(
    navController: NavController,
    viewModel: ProductViewModel
) {
    val product by viewModel.productState.collectAsState()
    val animateTrigger = remember { mutableStateOf(false) }
    Timber.d("Image Url: ${product?.imageFrontUrl ?: "empty"}")

    LaunchedEffect(product) {
        if (product != null) {
            animateTrigger.value = true
        }
    }

    when (viewModel.uiLoadingState.collectAsState().value) {
        UiLoadingState.IDLE -> {
            ErrorScreen()
            Timber.e("IDLE SCREEN ERROR")
        }
        UiLoadingState.LOADING -> LoadingScreen()
        UiLoadingState.ERROR -> ErrorScreen()
        UiLoadingState.SUCCESS ->
            product.let {
                if (it != null) {
                    DefaultScreenScaffold(
                        shouldShowTitle = true,
                        shouldShowBackButton = true,
                        navController = navController
                    ) { paddingValues ->
                        ProductDetails(it, paddingValues, animateTrigger.value)
                    }
                }
            }
    }
}

@Composable
fun ProductDetails(
    product: Product,
    padding: PaddingValues,
    animateTrigger: Boolean
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues = padding)) {
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(10.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
        ) {

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Фото продукта
                    Image(
                        painter = rememberAsyncImagePainter(product.imageFrontUrl),
                        contentDescription = stringResource(R.string.product_image_content_description),
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.LightGray)
                            .size(120.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Информация о продукте
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${stringResource(R.string.volume)}: ${product.quantity}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "${stringResource(R.string.food_energy)}: ${product.nutriments.energyKcal} ${stringResource(R.string.kcal)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            item {
                CardItem(rounded = 16.dp, elevation = 4.dp, modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp))
                {
                    Text(
                        stringResource(R.string.product_composition),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val data = listOf(
                            Triple(R.string.proteins, product.nutriments.proteins, Color(0xFF4CAF50)),
                            Triple(R.string.fats, product.nutriments.fat, Color(0xFFFF9800)),
                            Triple(R.string.carbohydrates, product.nutriments.carbohydrates, Color(0xFF2196F3))
                        )
                        PieChart(
                            data = data,
                            modifier = Modifier.size(150.dp),
                            animateTrigger = animateTrigger
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            NutrimentRow(R.string.proteins, product.nutriments.proteins, Color(0xFF4CAF50))
                            NutrimentRow(R.string.fats, product.nutriments.fat, Color(0xFFFF9800))
                            NutrimentRow(R.string.carbohydrates, product.nutriments.carbohydrates, Color(0xFF2196F3))
                        }
                    }
                }
            }

            item {
                CardItem(rounded = 16.dp, elevation = 4.dp, modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp))
                {
                    Text(
                        text = stringResource(R.string.food_safety_header),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    val nutrients = listOf(
                        R.string.sugars to product.nutrientLevels.sugars,
                        R.string.salt to product.nutrientLevels.salt,
                        R.string.fats to product.nutrientLevels.fat,
                        R.string.saturated_fats to product.nutrientLevels.saturatedFat
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        nutrients.forEach { (name, level) ->
                            SafetyLevelRow(name, level, animateTrigger)
                        }
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    HorizontalDivider(
                        modifier = Modifier.padding(bottom = 12.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.surfaceVariant
                    )

                    Text(
                        text = stringResource(R.string.ingredients_header),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    IngredientsList(product.ingredients)

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(R.string.allergens),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val allergens = product.allergens.ifEmpty {
                        stringResource(R.string.no_allergens)
                    }
                    Text(
                        text = allergens,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun NutrimentRow(name: Int, value: Double, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("${stringResource(name)}: $value г", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun SafetyLevelRow(name: Int, level: NutrientLevel, animateTrigger: Boolean) {
    val (color, progress, levelName) = when (level) {
        NutrientLevel.LOW -> Triple(Color(0xFF81C784), 0.33f, R.string.low_level)
        NutrientLevel.MODERATE -> Triple(Color(0xFFFFB74D),0.66f, R.string.moderate_level)
        NutrientLevel.HIGH -> Triple(Color(0xFFE57373), 1f, R.string.high_level)
    }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(name), style = MaterialTheme.typography.bodyMedium)
            Text(stringResource(levelName),
                style = MaterialTheme.typography.bodyMedium,
                color = color)
        }

        val animatedProgress by animateFloatAsState(
            targetValue = if (animateTrigger) progress else 0f,
            animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            label = "progressAnim"
        )

        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = Color(0xFFE0E0E0),
            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        )
    }
}

@Composable
fun PieChart(
    data: List<Triple<Int, Double, Color>>,
    modifier: Modifier = Modifier,
    animateTrigger: Boolean
) {

    val animationProgress by animateFloatAsState(
        targetValue = if (animateTrigger) 1f else 0f,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing)
    )

    Canvas(modifier = modifier) {
        val total = 100.0
        var startAngle = -90f
        var sum = 0.0

        for (nutriment in data) {
            val sweepAngle = (nutriment.second / total * 360 * animationProgress).toFloat()
            drawArc(
                color = nutriment.third,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true
            )
            startAngle += sweepAngle
            sum += nutriment.second
        }
        val others = 100 - sum
        if (others > 0) {
            val sweepAngle = (others / total * 360 * animationProgress).toFloat()
            drawArc(
                color = Color(0xFFBA68C8),
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true
            )
        }
    }
}


@Composable
fun IngredientsList(ingredients: List<Ingredient>, level: Int = 0) {
    Column(modifier = Modifier.fillMaxWidth()) {
        ingredients.forEach { ingredient ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = (level * 12).dp, top = 4.dp, bottom = 4.dp)
            ) {
                // Иконка веган/вегетарианец
                val icon = when (ingredient.vegan.lowercase()) {
                    "yes" -> "🌱"
                    "maybe" -> "🥕"
                    else -> "⚪"
                }
                Text(icon, modifier = Modifier.width(24.dp))

                Spacer(modifier = Modifier.width(4.dp))

                // Название и процент
                Text(
                    text = "${ingredient.name} — ${ingredient.percent}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Вложенные ингредиенты
            if (ingredient.ingredients.isNotEmpty()) {
                IngredientsList(ingredient.ingredients, level + 1)
            }
        }
    }
}

@Composable
fun CardItem(rounded: Dp, elevation: Dp, modifier: Modifier = Modifier, content: @Composable (ColumnScope.() -> Unit)) {
    Card(
        shape = RoundedCornerShape(rounded),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}

@Preview
@Composable
fun ProductDetailsPreview() {
    FoodentifyTheme {
        DefaultScreenScaffold(shouldShowBackButton = true) { paddingValues ->
            ProductDetails(
                Product(
                    id = 0,
                    name = "Тараллуччи",
                    quantity = "350 g",
                    nutrientLevels = NutrientLevels(
                        fat = NutrientLevel.MODERATE,
                        salt = NutrientLevel.LOW,
                        saturatedFat = NutrientLevel.LOW,
                        sugars = NutrientLevel.HIGH
                    ),
                    imageFrontUrl = "https://images.openfoodfacts.org/images/products/301/762/042/2003/front_en.399.400.jpg",
                    allergens = "молоко, мёд",
                    nutriments = Nutriments(
                        energyKcal = 467.0,
                        fat = 17.0,
                        proteins = 7.0,
                        salt = 0.68,
                        sugars = 23.0,
                        carbohydrates = 70.0
                    ),
                    ingredients = listOf(
                        Ingredient(
                            name = "Мука хлебопекарная",
                            percent = 60.1,
                            vegan = "yes",
                            vegetarian = "yes",
                            ingredients = emptyList()
                        ),
                        Ingredient(
                            name = "Сахар",
                            percent = 23.1,
                            vegan = "yes",
                            vegetarian = "yes",
                            ingredients = emptyList()
                        ),
                        Ingredient(
                            name = "Песок",
                            percent = 6.0,
                            vegan = "yes",
                            vegetarian = "yes",
                            ingredients = emptyList()
                        ),
                    )
                ), padding = paddingValues,
                animateTrigger = true
            )
        }
    }
}