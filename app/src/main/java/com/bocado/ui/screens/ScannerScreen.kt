package com.bocado.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.bocado.viewmodel.MenuViewModel

@Composable
fun ScannerScreen(
    menuViewModel: MenuViewModel,
    onScanSuccess: () -> Unit,
    onNavigateBack: () -> Unit // Agregamos botón de retroceder
) {
    val context = LocalContext.current
    var hasCameraPermission by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        hasCameraPermission = isGranted
    }

    // Pedir permiso automáticamente al entrar
    LaunchedEffect(Unit) {
        if (!hasCameraPermission) launcher.launch(Manifest.permission.CAMERA)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (hasCameraPermission) {
            // MUESTRA LA CÁMARA EN VIVO
            CameraPreview()

            // Overlay oscuro semitransparente para que se lea el texto
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)))

            // Texto e instrucciones arriba
            Text(
                "Apunta al código QR de la mesa",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 48.dp)
            )

            // Botones abajo
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(32.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = onNavigateBack, // Retrocede
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    )
                ) {
                    Text("Retroceder")
                }

                Button(
                    onClick = {
                        // AL ESCANEAR, SE COMUNICA CON LA API FICTICIA QUE ESTÁ, PERO NO UNIDO A UN QR NO SE LLEGÓ A INVESTIGAR ESO UNIR LA API EXTERNA CON UN QR RANDOM, EXEDE ESTE TRABAJO
                        menuViewModel.loadDishes()
                        onScanSuccess() // La cámara desaparece porque navega al Menú
                    }
                ) {
                    Text("Escanear QR", color= Color.White)
                }
            }
        } else {
            // UI si no hay permisos
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Permiso de cámara denegado.", color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { launcher.launch(Manifest.permission.CAMERA) }) {
                    Text("Pedir permiso de nuevo")
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(onClick = onNavigateBack) {
                    Text("Volver")
                }
            }
        }
    }
}

@Composable
fun CameraPreview() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val executor = ContextCompat.getMainExecutor(ctx)
            cameraProviderFuture.addListener({
                try {
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    val selector = CameraSelector.DEFAULT_BACK_CAMERA
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(lifecycleOwner, selector, preview)
                } catch (e: Exception) {
                    Log.e("CameraX", "Error al iniciar cámara", e)
                }
            }, executor)
            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
}
