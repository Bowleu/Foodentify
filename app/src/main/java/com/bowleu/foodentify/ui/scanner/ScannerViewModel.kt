package com.bowleu.foodentify.ui.scanner

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(): ViewModel() {
    private var cameraId = 0
    private var flashlightEnabled = false

    private var _scanOptions = MutableStateFlow(ScanOptions().apply {
        setCameraId(cameraId)
        setDesiredBarcodeFormats(ScanOptions.EAN_13)
        setTorchEnabled(flashlightEnabled)
    }
    )
    val scanOptions = _scanOptions.asStateFlow()
    var scannedBarcode by mutableStateOf("")
        private set

    fun onBarcodeScanned(value: String, navController: NavController) {
        scannedBarcode = value
        navController.navigate("product/$value")
    }

    fun switchCamera() {
        cameraId = if (cameraId == 0) 1 else 0
        updateScanOptions()
    }

    fun switchFlashlight() {
        flashlightEnabled = !flashlightEnabled
        updateScanOptions()
    }

    private fun updateScanOptions() {
        _scanOptions.value = ScanOptions().apply {
            setCameraId(cameraId)
            setDesiredBarcodeFormats(ScanOptions.EAN_13)
            setTorchEnabled(flashlightEnabled)
        }
    }
}