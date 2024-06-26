package es.upsa.nutrivision.presentation.screens.splash

import android.content.Context
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import es.upsa.nutrivision.R
import es.upsa.nutrivision.presentation.navigation.NutriVisionScreens
import es.upsa.nutrivision.presentation.screens.userdata.UserDataViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController,userDataViewModel: UserDataViewModel = hiltViewModel()) {

    val scale = remember {
        Animatable(0f)
    }
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)


    LaunchedEffect(key1 = true, block = {
        scale.animateTo(
            targetValue = 0.9f,
            animationSpec = spring(
                dampingRatio = 0.3f,
                stiffness = 500f
            )
        )
        delay(600L)
        userDataViewModel.isUserDataSaved { isSaved ->
            if (isSaved) {
                navController.navigate(NutriVisionScreens.HomeScreen.name) {
                    popUpTo(NutriVisionScreens.SplashScreen.name) { inclusive = true }
                }
            } else {
                navController.navigate(NutriVisionScreens.UserDataScreen.name) {
                    popUpTo(NutriVisionScreens.SplashScreen.name) { inclusive = true }
                }
            }
        }
    })


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary
    ) {
        Column(
            modifier = Modifier.padding(5.dp).scale(scale.value),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(250.dp),
                painter = painterResource(id = R.drawable.logo_app),
                contentDescription = "App logo",
                contentScale = ContentScale.Fit
            )
        }
    }
}