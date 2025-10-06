package com.bowleu.foodentify.ui.scanner

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.bowleu.foodentify.R
import com.bowleu.foodentify.ui.common.DefaultScreenScaffold
import com.bowleu.foodentify.ui.common.Screen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.journeyapps.barcodescanner.ScanContract
import timber.log.Timber
import kotlin.math.abs

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScannerScreen(navController: NavController, viewModel: ScannerViewModel) {
    val cameraPermission = rememberPermissionState(permission = Manifest.permission.CAMERA)
    val scanOptions by viewModel.scanOptions.collectAsState()
    val barcodeLauncher = rememberLauncherForActivityResult(ScanContract()) {
        result -> if (result.contents.isNullOrEmpty()) {
        Timber.w("Barcode is null or empty.")
        } else {
            viewModel.onBarcodeScanned(result.contents, navController)
        }
    }
    BackHandler {
        navController.navigate(Screen.Main.route)
    }
    DefaultScreenScaffold {
        Box(modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
            detectDragGestures(
                onDragEnd = {},
                onDrag = { change, dragAmount ->
                    val (dx, dy) = dragAmount
                    val absDx = abs(dx)
                    val absDy = abs(dy)

                    if (absDx > 50 || absDy > 50) {
                        if (absDx > absDy) {
                            viewModel.switchCamera()

                        } else {
                            viewModel.switchFlashlight()
                        }
                        change.consume()
                    }
                }
            )
        }) {
            LaunchedEffect(Unit) {
                cameraPermission.launchPermissionRequest()
            }

            LaunchedEffect(scanOptions) {
                if (cameraPermission.status.isGranted) {
                    barcodeLauncher.launch(scanOptions)
                }
            }

            when {
                cameraPermission.status.isGranted -> {
                    Timber.d("Camera permission is granted")
                }

                cameraPermission.status.shouldShowRationale -> {
                    Text(stringResource(R.string.camera_permission_required))
                }

                else -> {
                    Text(stringResource(R.string.camera_permission_declined))
                }
            }
        }
    }
}


@SuppressLint("ViewModelConstructorInComposable")
@Composable
@Preview
fun ScannerScreenPreview() {
    ScannerScreen(NavController(LocalContext.current), ScannerViewModel())
}