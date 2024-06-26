package es.upsa.nutrivision.presentation.screens.scan

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.google.gson.Gson
import es.upsa.nutrivision.R
import es.upsa.nutrivision.data.model.FoodResponse
import es.upsa.nutrivision.presentation.navigation.NutriVisionScreens
import es.upsa.nutrivision.ui.theme.CustomFontFamily


@Composable
fun ScanFoodScreen(navController: NavController, viewModel: ScanFoodViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val lifecycleOwner = context as LifecycleOwner
    val previewViewState = remember { mutableStateOf<PreviewView?>(null) }
    val showDialog = remember { mutableStateOf(false) }
    val foodSearchResult by viewModel.foodSearchResult.collectAsState()
    val foodSearchError by viewModel.foodSearchError.collectAsState()

    HandlePermissions(viewModel, lifecycleOwner)
    HandleFoodSearchResults(viewModel, navController, foodSearchResult)
    HandleFoodSearchError(viewModel, foodSearchError, showDialog)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Header()
        PreviewImage()
        CameraPreview(previewViewState, viewModel, lifecycleOwner)
    }
}

@Composable
fun HandlePermissions(viewModel: ScanFoodViewModel, lifecycleOwner: LifecycleOwner) {
    val context = LocalContext.current
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.onPermissionResult(isGranted, lifecycleOwner)
    }

    LaunchedEffect(Unit) {
        viewModel.resetState()
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.onPermissionResult(true, lifecycleOwner)
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
}

@Composable
fun HandleFoodSearchResults(
    viewModel: ScanFoodViewModel,
    navController: NavController,
    foodSearchResult: FoodResponse?
) {
    LaunchedEffect(foodSearchResult) {
        foodSearchResult?.let { foodResponse ->
            val hintFood = foodResponse.hints.map { hint ->
                hint.food.copy(measures = hint.measures)
            }
            val foodJson = Uri.encode(Gson().toJson(hintFood.firstOrNull()))
            navController.navigate("addFood/$foodJson") {
                popUpTo(NutriVisionScreens.ScanFoodScreen.name) { inclusive = true }
            }
            viewModel.resetState()
        }
    }
}

@Composable
fun HandleFoodSearchError(
    viewModel: ScanFoodViewModel,
    foodSearchError: String?,
    showDialog: MutableState<Boolean>
) {
    LaunchedEffect(foodSearchError) {
        if (foodSearchError != null) {
            showDialog.value = true
        }
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = "Food not found") },
            text = { Text("This food is not in our database.Do you want to try again?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog.value = false
                        viewModel.shouldContinueScanning.value = true
                        viewModel.resetError()
                    }
                ) {
                    Text("Retry")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog.value = false
                        viewModel.shouldContinueScanning.value = true
                        viewModel.resetError()
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun Header() {
    Spacer(modifier = Modifier.height(20.dp))
    Text(
        text = "Try scanning a food barcode",
        style = TextStyle(
            fontFamily = CustomFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 25.sp
        )
    )
}

@Composable
fun PreviewImage() {
    Image(
        painter = painterResource(id = R.drawable.barcode),
        contentDescription = "Your Image",
        modifier = Modifier.width(150.dp),
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
    )
}

@Composable
fun CameraPreview(
    previewViewState: MutableState<PreviewView?>,
    viewModel: ScanFoodViewModel,
    lifecycleOwner: LifecycleOwner
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).also { previewViewState.value = it }
            },
            modifier = Modifier.size(width = 250.dp, height = 250.dp)
        )

        LaunchedEffect(previewViewState.value) {
            previewViewState.value?.let { previewView ->
                viewModel.previewView.value = previewView
                if (viewModel.isCameraPermissionGranted.value) {
                    viewModel.startCamera(lifecycleOwner)
                }
            }
        }
    }
}

