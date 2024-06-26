package es.upsa.nutrivision.domain.model


import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import es.upsa.nutrivision.presentation.screens.scan.ScanFoodViewModel

class BarcodeAnalyzer(private val viewModel: ScanFoodViewModel) : ImageAnalysis.Analyzer {
    private val scanner = BarcodeScanning.getClient()

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        if (!viewModel.shouldContinueScanning.value) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        viewModel.shouldContinueScanning.value = false
                        viewModel.searchFoodByUPC(barcode.rawValue ?: "")
                    }
                }
                .addOnFailureListener {
                    // Task failed with an exception
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }
}