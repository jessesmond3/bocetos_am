package com.example.camara_permiso.pantallas

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGrayscaleFilter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun PantallaCam() {
    val contexto = LocalContext.current
    val cicloVida = LocalLifecycleOwner.current
    val vistaPreview = remember { PreviewView(contexto) }
    val capturador = remember { ImageCapture.Builder().build() }

    var filtroActual by remember { mutableStateOf<GPUImageFilter>(GPUImageFilter()) }

    // Vincular cámara
    LaunchedEffect(true) {
        val proveedor = contexto.obtenerProvedorDeCamara()
        proveedor.unbindAll()

        val preview = Preview.Builder().build()
        val selector = CameraSelector.DEFAULT_BACK_CAMERA

        preview.setSurfaceProvider(vistaPreview.surfaceProvider)

        try {
            proveedor.bindToLifecycle(cicloVida, selector, preview, capturador)
        } catch (e: Exception) {
            Log.e("PantallaCam", "Error al vincular la cámara", e)
            // Aquí podrías mostrar un mensaje al usuario indicando que la cámara no está disponible
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        AndroidView(
            factory = { vistaPreview },
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = { filtroActual = GPUImageGrayscaleFilter() }) {
                    Text("Gris")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(onClick = {
                capturarYGuardarImagenConFiltro(contexto, capturador, filtroActual)
            }) {
                Text("Capturar con filtro")
            }
        }
    }
}

private suspend fun Context.obtenerProvedorDeCamara(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { future ->
            future.addListener({
                continuation.resume(future.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }

private fun capturarYGuardarImagenConFiltro(
    context: Context,
    capturador: ImageCapture,
    filtro: GPUImageFilter
) {
    val nombre = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val archivoTemporal = File.createTempFile(nombre, ".jpg", context.cacheDir)

    val opciones = ImageCapture.OutputFileOptions.Builder(archivoTemporal).build()

    capturador.takePicture(
        opciones,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                try {
                    // Convertir a bitmap y aplicar filtro
                    val bitmapOriginal = BitmapFactory.decodeFile(archivoTemporal.absolutePath)
                    val gpuImage = GPUImage(context)
                    gpuImage.setImage(bitmapOriginal)
                    gpuImage.setFilter(filtro)
                    val imagenFiltrada = gpuImage.bitmapWithFilterApplied

                    guardarImagenFiltrada(context, imagenFiltrada)
                } catch (e: Exception) {
                    Log.e("CAPTURA_ERROR", "Error al procesar la imagen: ${e.message}")
                } finally {
                    archivoTemporal.delete() // Limpiar el archivo temporal
                }
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("CAPTURA_ERROR", "Error al capturar: ${exception.message}")
                archivoTemporal.delete() // Limpiar el archivo temporal en caso de error
            }
        }
    )
}

private fun guardarImagenFiltrada(context: Context, bitmap: Bitmap) {
    val nombreArchivo = "IMG_${System.currentTimeMillis()}.jpg"
    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, nombreArchivo)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/ConFiltros")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
    }

    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    uri?.let { imageUri ->
        var outputStream: OutputStream? = null
        try {
            outputStream = resolver.openOutputStream(imageUri)
            outputStream?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.clear()
                    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                    resolver.update(imageUri, contentValues, null, null)
                }
                Log.d("GUARDADO", "Imagen guardada con filtro en: $imageUri")
            } ?: run {
                Log.e("GUARDADO_ERROR", "No se pudo abrir el OutputStream para la URI: $imageUri")
                resolver.delete(imageUri, null, null)
            }
        } catch (e: Exception) {
            Log.e("GUARDADO_ERROR", "Error al guardar la imagen: ${e.message}")
            resolver.delete(imageUri, null, null)
        } finally {
            outputStream?.close()
        }
    } ?: run {
        Log.e("GUARDADO_ERROR", "Error al obtener la URI para guardar la imagen")
    }
}