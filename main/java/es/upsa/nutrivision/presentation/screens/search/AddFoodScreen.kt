package es.upsa.nutrivision.presentation.screens.search

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.*
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import coil.compose.rememberAsyncImagePainter
import es.upsa.nutrivision.data.model.Food
import es.upsa.nutrivision.data.model.Measure
import es.upsa.nutrivision.presentation.navigation.NutriVisionScreens
import es.upsa.nutrivision.presentation.screens.home.CarbsColor
import es.upsa.nutrivision.presentation.screens.home.FatColor
import es.upsa.nutrivision.presentation.screens.home.HomeViewModel
import es.upsa.nutrivision.presentation.screens.home.ProteinColor
import es.upsa.nutrivision.ui.theme.CustomFontFamily

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun AddFoodScreen(navController: NavController, food: Food, viewModel: HomeViewModel = hiltViewModel() ) {
    val selectedMeasure = remember { mutableStateOf<Measure?>(null) }
    val quantity = remember { mutableStateOf(1f) }
    val expanded = remember { mutableStateOf(false) }

    val showMeasureError = remember { mutableStateOf(false) }
    val calculatedNutrients = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        FoodDetailsHeader(food)
        Spacer(modifier = Modifier.height(16.dp))
        NutritionalInformation(food)
        Spacer(modifier = Modifier.height(16.dp))
        MeasureSelector(food, selectedMeasure, expanded, showMeasureError)
        Spacer(modifier = Modifier.height(16.dp))
        QuantityInput(quantity)
        Spacer(modifier = Modifier.height(16.dp))
        AddFoodButton(navController, viewModel, food, selectedMeasure, quantity, showMeasureError, calculatedNutrients)
        Spacer(modifier = Modifier.height(16.dp))
        DisplayCalculatedNutrients(calculatedNutrients)
    }
}

@Composable
fun FoodDetailsHeader(food: Food) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = food.label,
            style = TextStyle(fontFamily = CustomFontFamily, fontWeight = FontWeight.ExtraBold),
            modifier = Modifier.padding(bottom = 8.dp),
            fontSize = 22.sp
        )

        food.image?.let {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "food image",
                    modifier = Modifier
                        .size(128.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NutritionalInformation(food: Food) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Nutritional Information",
            style = TextStyle(fontFamily = CustomFontFamily, fontWeight = FontWeight.ExtraBold),
            fontSize = 19.sp
        )
        Text(
            text = "${String.format("%.1f", food.nutrients.ENERC_KCAL)} kcal/100g",
            style = TextStyle(fontFamily = CustomFontFamily, fontWeight = FontWeight.Normal),
            color = Color.Gray,
            fontSize = 15.sp
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            NutrientRow("Protein", food.nutrients.PROCNT, ProteinColor)
            NutrientRow("Fat", food.nutrients.FAT, FatColor)
            NutrientRow("Carbohydrates", food.nutrients.CHOCDF, CarbsColor)
            NutrientRow("Fiber", food.nutrients.FIBTG, Color.Gray)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Box(
            modifier = Modifier.size(90.dp),
            contentAlignment = Alignment.Center
        ) {
            DonutPieChart(
                modifier = Modifier.background(Color.Transparent),
                pieChartData = PieChartData(
                    slices = listOf(
                        PieChartData.Slice("Proteins", food.nutrients.PROCNT, Color(ProteinColor.value)),
                        PieChartData.Slice("Fat", food.nutrients.FAT, Color(FatColor.value)),
                        PieChartData.Slice("Carbohydrates", food.nutrients.CHOCDF, Color(CarbsColor.value)),
                        PieChartData.Slice("Fiber", food.nutrients.FIBTG, Color.Gray)
                    ),
                    plotType = PlotType.Donut
                ),
                pieChartConfig = PieChartConfig(
                    backgroundColor = Color.Transparent,
                    strokeWidth = 28f,
                    activeSliceAlpha = .9f,
                    isAnimationEnable = true
                )
            )
        }
    }
}

@Composable
fun NutrientRow(label: String, value: Float, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(17.dp)
                .background(color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "$label: ${String.format("%.1f", value)} g",
            style = TextStyle(fontFamily = CustomFontFamily, fontWeight = FontWeight.Normal)
        )
    }
    Spacer(modifier = Modifier.height(2.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeasureSelector(
    food: Food,
    selectedMeasure: MutableState<Measure?>,
    expanded: MutableState<Boolean>,
    showMeasureError: MutableState<Boolean>
) {
    Text(
        text = "Select Measure",
        style = TextStyle(fontFamily = CustomFontFamily, fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(bottom = 8.dp),
        fontSize = 19.sp
    )

    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = {
            expanded.value = !expanded.value
        }
    ) {
        OutlinedTextField(
            value = if (selectedMeasure.value != null) "${selectedMeasure.value!!.label} (${String.format("%.1f", selectedMeasure.value!!.weight)}g)" else "Select Measure",
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
            },
            isError = showMeasureError.value,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            food.measures?.forEach { measure ->
                DropdownMenuItem(
                    text = { Text("${measure.label} (${String.format("%.1f", measure.weight)} g)") },
                    onClick = {
                        selectedMeasure.value = measure
                        expanded.value = false
                        showMeasureError.value = false
                    }
                )
            }
        }
    }
}

@Composable
fun QuantityInput(quantity: MutableState<Float>) {
    Text(
        text = "Quantity",
        style = TextStyle(fontFamily = CustomFontFamily, fontWeight = FontWeight.ExtraBold),
        modifier = Modifier.padding(bottom = 8.dp),
        fontSize = 19.sp
    )

    val displayQuantity = remember { mutableStateOf(quantity.value.toString()) }

    OutlinedTextField(
        value = displayQuantity.value,
        onValueChange = { newValue ->
            displayQuantity.value = newValue
            newValue.toFloatOrNull()?.let {
                quantity.value = it
            }
        },
        label = { Text("Quantity") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        maxLines = 1,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun AddFoodButton(
    navController: NavController,
    viewModel: HomeViewModel,
    food: Food,
    selectedMeasure: MutableState<Measure?>,
    quantity: MutableState<Float>,
    showMeasureError: MutableState<Boolean>,
    calculatedNutrients: MutableState<String>
) {
    Button(
        onClick = {
            if (selectedMeasure.value == null) {
                showMeasureError.value = true
            } else {
                showMeasureError.value = false
                val measureWeight = selectedMeasure.value?.weight ?: 0f
                val totalWeight = measureWeight * quantity.value
                val calories = (food.nutrients.ENERC_KCAL / 100) * totalWeight
                val protein = (food.nutrients.PROCNT / 100) * totalWeight
                val fat = (food.nutrients.FAT / 100) * totalWeight
                val carbs = (food.nutrients.CHOCDF / 100) * totalWeight
                val fiber = (food.nutrients.FIBTG / 100) * totalWeight

                // Update consumed nutrients
                viewModel.addCaloriesConsumed(calories.toDouble())
                viewModel.addProteinsConsumed(protein.toDouble())
                viewModel.addFatsConsumed(fat.toDouble())
                viewModel.addCarbsConsumed(carbs.toDouble())

                // Add food to daily list
                viewModel.addFood(food, totalWeight)

                // Navigate to Home
                navController.navigate(NutriVisionScreens.HomeScreen.name) {
                    popUpTo(NutriVisionScreens.HomeScreen.name) { inclusive = true }
                }
            }
        },
        //modifier = Modifier.align(Alignment.CenterHorizontally)
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(text = "Add Food")
    }
}

@Composable
fun DisplayCalculatedNutrients(calculatedNutrients: MutableState<String>) {
    if (calculatedNutrients.value.isNotEmpty()) {
        Text(
            text = calculatedNutrients.value,
            style = TextStyle(fontFamily = CustomFontFamily, fontWeight = FontWeight.Normal),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}