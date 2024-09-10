@file:Suppress("NAME_SHADOWING")

package com.mmi.paymentapp.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.mmi.paymentapp.R


val IsProcessingPayment = mutableStateOf(false)
var creatingTransaction = mutableStateOf(false)

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("SourceLockedOrientationActivity")
@Composable
fun PaymentTabView() {
    val permissionCameraState = rememberPermissionState(permission = android.Manifest.permission.CAMERA)

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    var showBottomSheet by remember { mutableStateOf(false) }

    val localContext = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    (localContext as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(localContext)
    }

    if (permissionCameraState.status.isGranted) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                val previewView = PreviewView(context)
                val preview = androidx.camera.core.Preview.Builder().build()
                val selector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()

                preview.setSurfaceProvider(previewView.surfaceProvider)

                val imageAnalysis = ImageAnalysis.Builder().build()
                imageAnalysis.setAnalyzer(
                    ContextCompat.getMainExecutor(context),
                    BarcodeAnalyzer(context)
                )
                runCatching {
                    cameraProviderFuture.get().bindToLifecycle(
                        lifecycleOwner,
                        selector,
                        preview,
                        imageAnalysis
                    )
                }.onFailure {
                    Log.e("CAMERA", "Camera bind error ${it.localizedMessage}", it)
                }
                previewView
            }
        )
    } else if (permissionCameraState.status.shouldShowRationale) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState,
            modifier = Modifier
                .fillMaxHeight()
        ) {
            Text(text = "You Dont Have Camera Permission, You can change it from settings")
        }
    } else {
        SideEffect {
            permissionCameraState.run { launchPermissionRequest() }
        }
    }

    Column {
        if(isPaymentComplete.value){
            LaunchedEffect(Unit) {
                creatingTransaction.value = false
                IsProcessingPayment.value = false
                sheetState.hide()
            }
        }
        if(IsProcessingPayment.value){
            MakeTransaction()
            val sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true,
            )
            ModalBottomSheet(
                onDismissRequest = {
                    IsProcessingPayment.value = false
                },
                sheetState = sheetState,
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                PaymentConfrimation()
            }

            if (creatingTransaction.value){
                ProcessQrisPayment()
            }
        }
    }
}

@Composable
fun PaymentConfrimation() {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxHeight()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 16.dp, 16.dp, 0.dp),
            colors = CardColors(
                colorScheme.primaryContainer, colorScheme.primary,
                colorScheme.inversePrimary, colorScheme.inverseSurface
            ),
            shape = MaterialTheme.shapes.medium,
            onClick = {}
        ) {
            Text(
                text = "Amount",
                style = TextStyle(
                    fontSize = 16.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 8.dp, 0.dp, 0.dp)
            )
            Text(
                text = "Rp. " + Transaction?.amount,
                style = TextStyle(
                    fontSize = 25.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 16.dp, 0.dp, 16.dp)
            )
        }
        Column {
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 8.dp)
            ){
                Text(text = "Merchant")
                Text(text = Transaction?.merchant!!)
            }
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 8.dp)
            ){
                Text(text = "Transaction ID")
                Text(text = Transaction?.paymentInfo!!)
            }
            Button(
                onClick = { creatingTransaction.value = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 8.dp, 16.dp, 24.dp)
            ) {
                Text(text = "BAYAR")
            }
        }
    }
}