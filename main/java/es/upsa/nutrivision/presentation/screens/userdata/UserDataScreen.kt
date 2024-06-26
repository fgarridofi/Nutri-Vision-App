package es.upsa.nutrivision.presentation.screens.userdata

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import es.upsa.nutrivision.R
import es.upsa.nutrivision.presentation.navigation.NutriVisionScreens
import es.upsa.nutrivision.presentation.widgets.CustomDropdownMenu
import es.upsa.nutrivision.ui.theme.CustomFontFamily

@Composable
fun UserDataScreen(navController: NavController, userDataViewModel: UserDataViewModel = hiltViewModel()) {
    val userData by userDataViewModel.userData.collectAsState(initial = null)

    var weight by remember { mutableStateOf(userData?.weight?.toString() ?: "70") }
    var height by remember { mutableStateOf(userData?.height?.toString() ?: "170") }
    var gender by remember { mutableStateOf(userData?.gender ?: "Male") }
    var age by remember { mutableStateOf(userData?.age?.toString() ?: "30") }
    var physicalActivity by remember { mutableStateOf(userData?.physicalActivity ?: "Sedentary") }
    var goal by remember { mutableStateOf(userData?.goal ?: "Keep weight") }

    var weightError by remember { mutableStateOf(false) }
    var heightError by remember { mutableStateOf(false) }
    var genderError by remember { mutableStateOf(false) }
    var ageError by remember { mutableStateOf(false) }
    var physicalActivityError by remember { mutableStateOf(false) }
    var goalError by remember { mutableStateOf(false) }

    val weightOptions = (45..170).map { it.toString() }
    val heightOptions = (130..230).map { it.toString() }
    val ageOptions = (15..90).map { it.toString() }
    val genderOptions = listOf("Male", "Female")
    val activityOptions = listOf("Sedentary", "Somewhat active", "Active", "Very active")
    val goalOptions = listOf("Lose weight", "Gain muscle mass", "Maintain weight")

    LaunchedEffect(userData) {
        userData?.let {
            weight = it.weight.toString()
            height = it.height.toString()
            gender = it.gender
            age = it.age.toString()
            physicalActivity = it.physicalActivity
            goal = it.goal
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Fill in these details to personalize your nutritional plan!",
                fontSize = 24.sp,
                style = TextStyle(fontFamily = CustomFontFamily, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
                modifier = Modifier.weight(4f)
            )
            Spacer(modifier = Modifier.width(1.dp))
            Image(
                painter = painterResource(id = R.drawable.userdata_img),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .weight(3f)
                    .height(126.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        CustomDropdownMenu(
            label = "Weight (kg)",
            options = weightOptions,
            selectedOption = weight,
            onOptionSelected = {
                weight = it
                weightError = weight.isBlank()
            },
            isError = weightError
        )
        Spacer(modifier = Modifier.height(8.dp))

        CustomDropdownMenu(
            label = "Height (cm)",
            options = heightOptions,
            selectedOption = height,
            onOptionSelected = {
                height = it
                heightError = height.isBlank()
            },
            isError = heightError
        )
        Spacer(modifier = Modifier.height(8.dp))

        CustomDropdownMenu(
            label = "Gender",
            options = genderOptions,
            selectedOption = gender,
            onOptionSelected = {
                gender = it
                genderError = gender.isBlank()
            },
            isError = genderError
        )
        Spacer(modifier = Modifier.height(8.dp))
        CustomDropdownMenu(
            label = "Age",
            options = ageOptions,
            selectedOption = age,
            onOptionSelected = {
                age = it
                ageError = age.isBlank()
            },
            isError = ageError
        )
        Spacer(modifier = Modifier.height(8.dp))

        CustomDropdownMenu(
            label = "Physical activity level",
            options = activityOptions,
            selectedOption = physicalActivity,
            onOptionSelected = {
                physicalActivity = it
                physicalActivityError = physicalActivity.isBlank()
            },
            isError = physicalActivityError
        )
        Spacer(modifier = Modifier.height(8.dp))

        CustomDropdownMenu(
            label = "Goal",
            options = goalOptions,
            selectedOption = goal,
            onOptionSelected = {
                goal = it
                goalError = goal.isBlank()
            },
            isError = goalError
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            weightError = weight.isBlank()
            heightError = height.isBlank()
            genderError = gender.isBlank()
            physicalActivityError = physicalActivity.isBlank()
            goalError = goal.isBlank()

            if (!weightError && !heightError && !genderError && !physicalActivityError && !goalError) {
                userDataViewModel.saveUserData(weight.toInt(), height.toInt(), gender, age.toInt(), physicalActivity, goal)
                navController.navigate(NutriVisionScreens.HomeScreen.name) {
                    popUpTo(NutriVisionScreens.UserDataScreen.name) { inclusive = true }
                }
            }
        }) {
            Text("Save")
        }
    }
}



