package es.upsa.nutrivision.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import es.upsa.nutrivision.ui.theme.CustomFontFamily

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FoodCard(foodName: String, kcal: Int, protein: Int, fat: Int, carbs: Int, quantity: Float, time: String
) {
    Box(
        modifier = Modifier
            .width(300.dp)
            .height(150.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            FoodCardTitle(foodName)
            FoodCardContent(protein, fat, carbs)
            FoodCardFooter(kcal, time)
        }
    }
}

@Composable
fun FoodCardTitle(foodName: String) {
    Text(
        text = foodName,
        fontSize = 20.sp,
        color = Color.Black,
        style = TextStyle(fontFamily = CustomFontFamily, fontWeight = FontWeight.Bold),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun FoodCardContent(protein: Int, fat: Int, carbs: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        FoodCardChart(protein, fat, carbs)
        FoodCardNutrients(protein, fat, carbs)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FoodCardChart(protein: Int, fat: Int, carbs: Int) {
    Box(
        modifier = Modifier.size(70.dp),
        contentAlignment = Alignment.Center
    ) {
        DonutPieChart(
            modifier = Modifier.background(Color.LightGray),
            pieChartData = PieChartData(
                slices = listOf(
                    PieChartData.Slice("Protein", protein.toFloat(), Color(ProteinColor.value)),
                    PieChartData.Slice("Fat", fat.toFloat(), Color(FatColor.value)),
                    PieChartData.Slice("Carbohydrates", carbs.toFloat(), Color(CarbsColor.value))
                ),
                plotType = PlotType.Donut
            ),
            pieChartConfig = PieChartConfig(
                backgroundColor = Color.LightGray,
                strokeWidth = 20f,
                activeSliceAlpha = .9f,
                isAnimationEnable = true
            )
        )
    }
}

@Composable
fun FoodCardNutrients(protein: Int, fat: Int, carbs: Int) {
    Column {
        NutrientRow("Protein", protein, ProteinColor)
        NutrientRow("Fat", fat, FatColor)
        NutrientRow("Carbohydrates", carbs, CarbsColor)
    }
}

@Composable
fun NutrientRow(nutrientName: String, amount: Int, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(17.dp)
                .background(color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "${amount}g $nutrientName",
            color = Color.Black,
            style = TextStyle(fontFamily = CustomFontFamily, fontWeight = FontWeight.Normal)
        )
    }
}

@Composable
fun FoodCardFooter(kcal: Int, time: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "$kcal kcal",
            fontSize = 16.sp,
            color = Color.Black,
            style = TextStyle(fontFamily = CustomFontFamily, fontWeight = FontWeight.Normal)
        )
        Text(
            text = time,
            fontSize = 14.sp,
            color = Color.Gray,
            style = TextStyle(fontFamily = CustomFontFamily, fontWeight = FontWeight.Normal)
        )
    }
}
