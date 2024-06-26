package es.upsa.nutrivision.presentation.screens.scan

import android.app.Application
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.lifecycle.HiltViewModel
import es.upsa.nutrivision.data.api.FoodApi
import es.upsa.nutrivision.data.model.FoodResponse
import es.upsa.nutrivision.domain.model.BarcodeAnalyzer
import es.upsa.nutrivision.domain.usecase.SearchFoodByUPCUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

@HiltViewModel
class ScanFoodViewModel @Inject constructor(
    application: Application,
    private val searchFoodByUPCUseCase: SearchFoodByUPCUseCase
) : AndroidViewModel(application) {

    val previewView: MutableState<PreviewView?> = mutableStateOf(null)
    private val cameraProviderFuture = ProcessCameraProvider.getInstance(application)
    private val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    val isCameraPermissionGranted = mutableStateOf(false)
    private val _foodSearchResult = MutableStateFlow<FoodResponse?>(null)
    val foodSearchResult: StateFlow<FoodResponse?> = _foodSearchResult
    val shouldContinueScanning = mutableStateOf(true)
    private val _foodSearchError = MutableStateFlow<String?>(null)
    val foodSearchError: StateFlow<String?> = _foodSearchError

    fun startCamera(lifecycleOwner: LifecycleOwner) {
        val context = getApplication<Application>().applicationContext
        val previewView = previewView.value
        if (previewView == null) {
            Log.e("ScanFoodViewModel", "PreviewView is null")
            return
        }
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val imageCapture = ImageCapture.Builder().build()

            val imageAnalyzer = ImageAnalysis.Builder().build().also {
                it.setAnalyzer(cameraExecutor, BarcodeAnalyzer(this))
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture,
                    imageAnalyzer
                )
            } catch (exc: Exception) {
                Log.e("ScanFoodViewModel", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    fun onPermissionResult(isGranted: Boolean, lifecycleOwner: LifecycleOwner) {
        isCameraPermissionGranted.value = isGranted
        if (isGranted) {
            shouldContinueScanning.value = true
            startCamera(lifecycleOwner)
        }
    }

    fun resetError() {
        _foodSearchError.value = null
    }

    fun searchFoodByUPC(upc: String) {
        viewModelScope.launch {
            searchFoodByUPCUseCase.searchFoodByUPC(upc, _foodSearchResult, _foodSearchError)
            shouldContinueScanning.value = false
        }
    }

    fun resetState() {
        _foodSearchResult.value = null
        shouldContinueScanning.value = true
        resetError()
    }

    override fun onCleared() {
        super.onCleared()
        cameraExecutor.shutdown()
    }
}
