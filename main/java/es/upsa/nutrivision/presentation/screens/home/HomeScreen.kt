package es.upsa.nutrivision.presentation.screens.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import es.upsa.nutrivision.data.model.FoodWithQuantity
import es.upsa.nutrivision.data.model.UserData
import es.upsa.nutrivision.domain.usecase.NutritionalGoals
import es.upsa.nutrivision.presentation.widgets.CustomLinearProgressIndicator
import es.upsa.nutrivision.ui.theme.CustomFontFamily
import java.time.LocalTime
import java.time.format.DateTimeFormatter

val CarbsColor = Color(0xFFE79D2F)
val FatColor = Color(0xFF8C57EB)
val ProteinColor = Color(0xFF148DC4)

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    val context = LocalContext.current
    var isReceiverRegistered by remember { mutableStateOf(false) }


    val resetEventReceiver = remember {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                viewModel.loadDailyFoods()
            }
        }
    }

    DisposableEffect(Unit) {
        // Register the broadcast receiver when the composable enters the composition
        context.registerReceiver(
            resetEventReceiver,
            IntentFilter("es.upsa.nutrivision.CALORIES_RESET"),
            Context.RECEIVER_NOT_EXPORTED
        )
        onDispose {
            // Unregister the broadcast receiver when the composable exits the composition
            context.unregisterReceiver(resetEventReceiver)
        }
    }

    val userData by viewModel.userData.collectAsState()


    LaunchedEffect(userData) {
        userData?.let {
            viewModel.calculateGoals(
                weight = it.weight,
                height = it.height,
                age = it.age,
                gender = it.gender,
                activityLevel = it.physicalActivity,
                goal = it.goal
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadUserData()
        viewModel.loadDailyFoods()
    }

    val nutritionalGoals = viewModel.nutritionalGoals.collectAsState().value
    val caloriesConsumed = viewModel.caloriesConsumed.collectAsState().value
    val proteinsConsumed = viewModel.proteinsConsumed.collectAsState().value
    val fatsConsumed = viewModel.fatsConsumed.collectAsState().value
    val carbsConsumed = viewModel.carbsConsumed.collectAsState().value
    val dailyFoods by viewModel.dailyFoods.collectAsState()
    val currentTime = LocalTime.now()
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val formattedTime = currentTime.format(formatter)

    Log.d("HomeScreen", "Calories consumed: $caloriesConsumed")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        nutritionalGoals?.let { goals ->
            NutritionalGoalsSection(goals, caloriesConsumed)
            MacronutrientProgressSection(
                proteinsConsumed = proteinsConsumed,
                fatsConsumed = fatsConsumed,
                carbsConsumed = carbsConsumed,
                proteinGoal = goals.proteins,
                fatGoal = goals.fats,
                carbGoal = goals.carbs
            )

            Spacer(modifier = Modifier.height(16.dp))

            FoodSection(dailyFoods, formattedTime)
        } ?: Text(text = "Loading nutritional data...")
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NutritionalGoalsSection(goals: NutritionalGoals, caloriesConsumed: Double) {
    val progress = if (goals.calories > 0) (caloriesConsumed / goals.calories).toFloat() else 0f
    val consumedPercentage = progress * 100
    val remainingPercentage = 100 - consumedPercentage

    val slices = if (progress <= 1) {
        listOf(
            PieChartData.Slice("Kcal Consumed", consumedPercentage, Color(0xFF20BF55)),
            PieChartData.Slice("Rest", remainingPercentage, Color(0xFFE0E0E0))
        )
    } else {
        listOf(
            PieChartData.Slice("Kcal Overconsumed", 100f, Color(0xFFFFA500))
        )
    }

    val donutChartData = PieChartData(
        slices = slices,
        plotType = PlotType.Donut,
    )

    val donutChartConfig = PieChartConfig(
        backgroundColor = Color.Transparent,
        strokeWidth = 60f,
        isAnimationEnable = true,
        isClickOnSliceEnabled = false,
        labelVisible = true,
        labelType = PieChartConfig.LabelType.VALUE,
        labelColorType = PieChartConfig.LabelColorType.SLICE_COLOR
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = "Your Nutritional Journey",
            fontSize = 26.sp,
            style = TextStyle(fontFamily = CustomFontFamily, fontWeight = FontWeight.ExtraBold),
            textAlign = TextAlign.Start
        )
    }
    Spacer(modifier = Modifier.height(10.dp))

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(240.dp)
    ) {
        DonutPieChart(
            modifier = Modifier.background(Color.Transparent),
            pieChartData = donutChartData,
            pieChartConfig = donutChartConfig,
        )
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${caloriesConsumed.toInt()}/${goals.calories.toInt()}",
                fontSize = 22.sp,
                color = Color.White,
                style = TextStyle(fontFamily = CustomFontFamily, fontWeight = FontWeight.Black)
            )
            Text(
                text = "kcal",
                fontSize = 22.sp,
                color = Color.White,
                style = TextStyle(fontFamily = CustomFontFamily, fontWeight = FontWeight.Black)
            )
        }
    }
}


@Composable
fun MacronutrientProgressSection(
    proteinsConsumed: Double,
    fatsConsumed: Double,
    carbsConsumed: Double,
    proteinGoal: Double,
    fatGoal: Double,
    carbGoal: Double
) {
    NutrientProgress("Proteins", proteinsConsumed, proteinGoal, ProteinColor)
    NutrientProgress("Fats", fatsConsumed, fatGoal, FatColor)
    NutrientProgress("Carbohydrates", carbsConsumed, carbGoal, CarbsColor)
}

@Composable
fun NutrientProgress(name: String, consumed: Double, goal: Double, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
    ) {
        Text(
            text = "$name: ${consumed.toInt()}/${goal.toInt()}g",
            style = TextStyle(fontFamily = CustomFontFamily, fontWeight = FontWeight.Black)
        )
    }

    CustomLinearProgressIndicator(
        progress = (consumed / goal).toFloat(),
        progressColor = color,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}

@Composable
fun FoodSection(dailyFoods: List<FoodWithQuantity>, formattedTime: String) {
    // Row para "Mis comidas" y "Añadir"
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Your meals", fontSize = 26.sp,
            style = TextStyle(fontFamily = CustomFontFamily, fontWeight = FontWeight.ExtraBold)
        )
        Text(
            text = "Add",
            style = TextStyle(fontFamily = CustomFontFamily, fontWeight = FontWeight.Thin),
            fontSize = 18.sp,
            color = Color.Blue,
            modifier = Modifier.clickable {
                // Acción para añadir comida
            }
        )
    }

    // Carrusel de cards personalizadas
    if (dailyFoods.isNotEmpty()) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(0.dp, 6.dp, 6.dp, 0.dp),
        ) {
            items(dailyFoods) { foodWithQuantity ->
                val food = foodWithQuantity.food
                val quantity = foodWithQuantity.quantity
                val calories = (food.nutrients.ENERC_KCAL / 100) * quantity
                val protein = (food.nutrients.PROCNT / 100) * quantity
                val fat = (food.nutrients.FAT / 100) * quantity
                val carbs = (food.nutrients.CHOCDF / 100) * quantity

                FoodCard(
                    foodName = food.label,
                    kcal = calories.toInt(),
                    protein = protein.toInt(),
                    fat = fat.toInt(),
                    carbs = carbs.toInt(),
                    quantity = quantity,
                    time = formattedTime
                )
            }
        }
    }
}





