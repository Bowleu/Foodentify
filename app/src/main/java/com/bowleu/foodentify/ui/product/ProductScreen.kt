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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.bowleu.foodentify.domain.model.NutrientLevel
import com.bowleu.foodentify.domain.model.NutrientLevels
import com.bowleu.foodentify.domain.model.Nutriments
import com.bowleu.foodentify.domain.model.Product
import com.bowleu.foodentify.ui.common.DefaultScreenScaffold
import com.bowleu.foodentify.ui.theme.FoodentifyTheme

@Composable
fun ProductScreen(
    navController: NavController,
    viewModel: ProductViewModel
) {
    val product by viewModel.productState.collectAsState()
    DefaultScreenScaffold {
        paddingValues ->
        product.let {
            if (it != null) {
                ProductDetails(it, paddingValues)
            } else {
                ProductError()
            }
        }
    }
}

@Composable
fun ProductDetails(
    product: Product,
    padding: PaddingValues
) {
    Column(modifier = Modifier.fillMaxSize()
        .padding(paddingValues = padding)) {
        Spacer(modifier = Modifier.fillMaxWidth()
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
                        contentDescription = "Product image",
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.LightGray)
                            .size(120.dp), // можно уменьшить размер, чтобы текст помещался
                        contentScale = ContentScale.Crop
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
                            text = "Объем: ${product.quantity}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Энергетическая ценность: ${product.nutriments.energyKcal} ккал",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Состав (БЖУ)",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PieChart(
                                data = mapOf(
                                    "Белки" to product.nutriments.proteins,
                                    "Жиры" to product.nutriments.fat,
                                    "Углеводы" to product.nutriments.carbohydrates
                                ),
                                modifier = Modifier.size(150.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                NutrimentRow("Белки", product.nutriments.proteins, Color(0xFF4CAF50))
                                NutrimentRow("Жиры", product.nutriments.fat, Color(0xFFFF9800))
                                NutrimentRow("Углеводы", product.nutriments.carbohydrates, Color(0xFF2196F3))
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Пищевая безопасность",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        val nutrients = listOf(
                            "Сахар" to product.nutrientLevels.sugars,
                            "Соль" to product.nutrientLevels.salt,
                            "Жиры" to product.nutrientLevels.fat,
                            "Насыщенные жиры" to product.nutrientLevels.saturatedFat
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            nutrients.forEach { (name, level) ->
                                SafetyLevelRow(name, level)
                            }
                        }
                    }
                }
            }

            item {
                // Ингредиенты
                Text(
                    "Ингредиенты",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text(
                    "ыыы", // TODO
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item {
                // Аллергены
                Text(
                    "Аллергены",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text(
                    product.allergens,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
fun NutrimentRow(name: String, value: Double, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("$name: $value г", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun SafetyLevelRow(name: String, level: Int) {
    val color = when (level) {
        in 0..2 -> Color(0xFF81C784) // низкий — зелёный
        in 3..5 -> Color(0xFFFFB74D) // средний — оранжевый
        else -> Color(0xFFE57373)    // высокий — красный
    }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(name, style = MaterialTheme.typography.bodyMedium)
            Text("$level", style = MaterialTheme.typography.bodyMedium, color = color)
        }
        LinearProgressIndicator(
            progress = (level / 10f).coerceIn(0f, 1f),
            color = color,
            trackColor = Color(0xFFE0E0E0),
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
        )
    }
}

@Composable
fun PieChart(
    data: Map<String, Double>,
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(
        Color(0xFF81C784), // зелёный
        Color(0xFFFFB74D), // оранжевый
        Color(0xFF64B5F6), // синий
        Color(0xFFE57373), // красный
        Color(0xFFBA68C8)  // фиолетовый
    )
) {
    // Анимируем прогресс раскрытия от 0 до 1
    val animationProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
    )

    Canvas(modifier = modifier) {
        val total = data.values.sum()
        var startAngle = -90f

        data.entries.forEachIndexed { index, entry ->
            val sweepAngle = (entry.value / total * 360 * animationProgress).toFloat()
            drawArc(
                color = colors.getOrElse(index) { Color.Gray },
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true
            )
            startAngle += sweepAngle
        }
    }
}

@Composable
fun SafetyLevelRow(name: String, level: NutrientLevel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(name)
        Text(
            text = when (level) {
                NutrientLevel.LOW -> "Низкое"
                NutrientLevel.MODERATE -> "Среднее"
                NutrientLevel.HIGH -> "Высокое"
            },
            color = when (level) {
                NutrientLevel.LOW -> Color.Green
                NutrientLevel.MODERATE -> Color.Yellow
                NutrientLevel.HIGH -> Color.Red
            }
        )
    }
}


@Composable
fun ProductError() {

}

@Preview
@Composable
fun ProductErrorPreview() {
    ProductError()
}

@Preview
@Composable
fun ProductDetailsPreview() {
    FoodentifyTheme {
        DefaultScreenScaffold { paddingValues ->
            ProductDetails(
                Product(
                    id = 0,
                    name = "Тараллуччи",
                    quantity = 350.0,
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
                    )
                ), padding = paddingValues
            )
        }
    }
}