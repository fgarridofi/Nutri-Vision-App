package es.upsa.nutrivision.presentation.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import es.upsa.nutrivision.data.model.Food
import es.upsa.nutrivision.data.model.Nutrients
import es.upsa.nutrivision.presentation.navigation.NutriVisionScreens
import es.upsa.nutrivision.presentation.screens.home.HomeViewModel
import java.util.Date

@Composable
fun AddCustomFoodScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    var label by remember { mutableStateOf("") }
    var image by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    //var quantity by remember { mutableStateOf(1f) }
    var quantity = remember { mutableStateOf(1f) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = label,
            onValueChange = { label = it },
            label = { Text("Food Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = calories,
            onValueChange = { calories = it },
            label = { Text("Calories (per 100g)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = protein,
            onValueChange = { protein = it },
            label = { Text("Protein (per 100g)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = fat,
            onValueChange = { fat = it },
            label = { Text("Fat (per 100g)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = carbs,
            onValueChange = { carbs = it },
            label = { Text("Carbs (per 100g)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
/*
        OutlinedTextField(
            value = quantity.toString(),
            onValueChange = { quantity = it.toFloatOrNull() ?: 1f },
            label = { Text("Quantity (in grams)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )*/
        val displayQuantity = remember { mutableStateOf(quantity.value.toString()) }

        OutlinedTextField(
            value = displayQuantity.value,
            onValueChange = { newValue ->
                displayQuantity.value = newValue
                newValue.toFloatOrNull()?.let {
                    quantity.value = it
                }
            },
            label = { Text("Quantity (in grams)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Button(
            onClick = {
                val customFood = Food(
                    foodId = "custom_${System.currentTimeMillis()}",
                    label = label,
                    knownAs = "",
                    nutrients = Nutrients(
                        ENERC_KCAL = calories.toFloatOrNull() ?: 0f,
                        PROCNT = protein.toFloatOrNull() ?: 0f,
                        FAT = fat.toFloatOrNull() ?: 0f,
                        CHOCDF = carbs.toFloatOrNull() ?: 0f,
                        FIBTG = 0f
                    ),
                    category = "Custom",
                    categoryLabel = "Custom Food",
                    image = null,
                    measures = null
                )
                val totalWeight = quantity.value
                val caloriesConsumed = (calories.toFloatOrNull() ?: 0f) / 100 * totalWeight
                val proteinsConsumed = (protein.toFloatOrNull() ?: 0f) / 100 * totalWeight
                val fatsConsumed = (fat.toFloatOrNull() ?: 0f) / 100 * totalWeight
                val carbsConsumed = (carbs.toFloatOrNull() ?: 0f) / 100 * totalWeight

                viewModel.addCaloriesConsumed(caloriesConsumed.toDouble())
                viewModel.addProteinsConsumed(proteinsConsumed.toDouble())
                viewModel.addFatsConsumed(fatsConsumed.toDouble())
                viewModel.addCarbsConsumed(carbsConsumed.toDouble())
                viewModel.addFood(customFood, totalWeight)

                navController.navigate(NutriVisionScreens.HomeScreen.name) {
                    popUpTo(NutriVisionScreens.HomeScreen.name) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Custom Food")
        }
    }
}
