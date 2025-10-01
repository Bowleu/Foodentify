package com.bowleu.foodentify.ui.product

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.bowleu.foodentify.domain.model.NutrientLevel
import com.bowleu.foodentify.domain.model.NutrientLevels
import com.bowleu.foodentify.domain.model.Nutriments
import com.bowleu.foodentify.domain.model.Product
import com.bowleu.foodentify.ui.common.DefaultScreenScaffold

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
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            item {
                // Фото продукта
                Image(
                    painter = rememberAsyncImagePainter(product.imageFrontUrl),
                    contentDescription = "Product image",
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )
            }

            item {
                // Круговая диаграмма БЖУ
                Text(
                    "Состав (БЖУ)",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    PieChart(
                        data = mapOf(
                            "Белки" to product.nutriments.proteins,
                            "Жиры" to product.nutriments.fat,
                            "Углеводы" to product.nutriments.carbohydrates
                        )
                    )
                }
            }

            item {
                // Безопасность
                Text(
                    "Пищевая безопасность",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        "Сахар" to product.nutrientLevels.sugars,
                        "Соль" to product.nutrientLevels.salt,
                        "Жиры" to product.nutrientLevels.fat,
                        "Насыщенные жиры" to product.nutrientLevels.saturatedFat
                    ).forEach { (name, level) ->
                        SafetyLevelRow(name, level)
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
fun PieChart(
    data: Map<String, Double>,
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Cyan)
) {
    Canvas(modifier = modifier) {
        val total = data.values.sum()
        var startAngle = -90f

        data.entries.forEachIndexed { index, entry ->
            val sweepAngle = (entry.value / total * 360).toFloat()
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
    DefaultScreenScaffold {
        paddingValues ->
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
                imageFrontUrl = "none",
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