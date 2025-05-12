package com.example.camara_permiso.pantallas

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.camara_permiso.R
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGrayscaleFilter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun PantallaCam() {
    val contexto = LocalContext.current
    val cicloVida = LocalLifecycleOwner.current

    var lenteUsado by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
    val vistaPrevia = remember { PreviewView(contexto) }
    val capturadorImagen = remember { ImageCapture.Builder().build() }
    var filtroActual by remember { mutableStateOf<GPUImageFilter?>(null) } // Nullable filter

    // State to hold the last captured image (for display/further processing)
    val imagenCapturada = remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(lenteUsado) {
        val proveedorCamara = contexto.obtenerProvedorDeCamara()
        proveedorCamara.unbindAll()

        val selectorCamara = CameraSelector.Builder().requireLensFacing(lenteUsado).build()
        proveedorCamara.bindToLifecycle(
            cicloVida,
            selectorCamara,
            Preview.Builder().build().apply { setSurfaceProvider(vistaPrevia.surfaceProvider) },
            capturadorImagen
        )
    }

    Box(Modifier.fillMaxSize()) {
        AndroidView(
            factory = { vistaPrevia },
            modifier = Modifier.fillMaxSize()
        )

        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = { filtroActual = GPUImageGrayscaleFilter() }) {
                    Text("Gris")
                }
                // Add more filter buttons here
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        tomarFoto(
                            contexto,
                            capturadorImagen,
                            filtroActual
                        ) { bitmap -> imagenCapturada.value = bitmap }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                ) {
                    Icon(
                        painter = painterResource(id = R.mipmap.foto1),
                        contentDescription = "Tomar Foto",
                        tint = Color.White,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = { alternarCamara(lenteUsado) { newLens -> lenteUsado = newLens } },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                ) {
                    Icon(
                        painter = painterResource(id = R.mipmap.foto2),
                        contentDescription = "Cambiar Cámara",
                        tint = Color.White,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = { abrirGaleria(contexto) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                ) {
                    Icon(
                        painter = painterResource(id = R.mipmap.foto3),
                        contentDescription = "Abrir Galería",
                        tint = Color.White,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                }
            }
        }
    }
}

private suspend fun Context.obtenerProvedorDeCamara(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { provider ->
            provider.addListener({
                continuation.resume(provider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }

private fun tomarFoto(
    contexto: Context,
    capturadorImagen: ImageCapture,
    filtro: GPUImageFilter?,
    onImagenCapturada: (Bitmap) -> Unit // Callback for captured image
) {
    val nombreArchivo = "Captura_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}.jpg"
    val valoresImagen = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, nombreArchivo)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/nuestra_app")
            put(MediaStore.Images.Media.IS_PENDING, 1) // Flag for pending write
        }
    }

    val opcionesSalida = ImageCapture.OutputFileOptions.Builder(
        contexto.contentResolver,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        valoresImagen
    ).build()

    capturadorImagen.takePicture(
        opcionesSalida,
        ContextCompat.getMainExecutor(contexto),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                Log.d("CameraX", "Imagen guardada en: ${outputFileResults.savedUri}")
                // Load the saved image, apply the filter, and pass it back
                outputFileResults.savedUri?.let { uri ->
                    val bitmap = BitmapFactory.decodeStream(contexto.contentResolver.openInputStream(uri))
                    val imagenFiltrada = filtro?.let { applyFilter(contexto, bitmap, it) } ?: bitmap
                    onImagenCapturada(imagenFiltrada)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        valoresImagen.clear()
                        valoresImagen.put(MediaStore.Images.Media.IS_PENDING, 0)
                        contexto.contentResolver.update(uri, valoresImagen, null, null) // Clear pending flag
                    }
                }
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("CameraX", "Error al tomar la foto: ${exception.message}", exception)
            }
        }
    )
}

private fun applyFilter(context: Context, bitmap: Bitmap, filter: GPUImageFilter): Bitmap {
    val gpuImage = GPUImage(context).apply {
        setImage(bitmap)
        setFilter(filter)
    }
    return gpuImage.bitmapWithFilterApplied
}

private fun alternarCamara(currentLens: Int, onLensChange: (Int) -> Unit) {
    onLensChange(
        if (currentLens == CameraSelector.LENS_FACING_BACK)
            CameraSelector.LENS_FACING_FRONT
        else
            CameraSelector.LENS_FACING_BACK
    )
}

private fun abrirGaleria(contexto: Context) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        type = "image/*"
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    contexto.startActivity(intent)
}