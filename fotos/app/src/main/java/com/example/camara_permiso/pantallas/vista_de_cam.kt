package com.example.camara_permiso.pantallas

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OutputFileResults
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import java.lang.reflect.Modifier
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun PantallaCam(){
    val lente_usado = CameraSelector.LENS_FACING_BACK
    val ciclo_vida_dueño = LocalLifecycleOwner.current

    val contexto = LocalContext.current

    val prevista = Preview.Builder().build()
    val vista_prev = remember {
        PreviewView(contexto)
    }

    val camarax_selector = CameraSelector.Builder().requireLensFacing(lente_usado).build()

    val capturador_de_imagen = remember { ImageCapture.Builder().build() }

    LaunchedEffect(lente_usado) {
        val proveedor_local_camara = contexto.obtenerProvedorDeCamara()
        proveedor_local_camara.unbindAll()

        proveedor_local_camara.bindToLifecycle(ciclo_vida_dueño, camarax_selector,prevista, capturador_de_imagen)

        prevista.setSurfaceProvider(vista_prev.surfaceProvider)
    }

    Box(contentAlignment = Alignment.BottomCenter){
        AndroidView(factory = { vista_prev }, modifier = androidx.compose.ui.Modifier.fillMaxSize())
        Button(onClick = {tomar_foto(capturador_de_imagen, contexto)}) {
            Text("hola mundo")
        }
    }


}

private suspend fun Context.obtenerProvedorDeCamara(): ProcessCameraProvider =
    suspendCoroutine { continuacion ->
        ProcessCameraProvider.getInstance(this).also { proveedo_cam ->
            proveedo_cam.addListener({
                continuacion.resume(proveedo_cam.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }

private fun tomar_foto(capturador_imagen: ImageCapture, contexto: Context){
    val nombre_archivo = "CapturaFoto.jpeg"

    val valores_del_contenido = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, nombre_archivo)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P){
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/nuestra_app")
        }
    }

    val salida_foto = ImageCapture.OutputFileOptions.Builder(
        contexto.contentResolver,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        valores_del_contenido
    ).build()

    capturador_imagen.takePicture(
        salida_foto,
        ContextCompat.getMainExecutor(contexto),
        object: ImageCapture.OnImageSavedCallback{
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults){
                Log.v("CAPTURA_EXITO", "Exito, no ha pasado nada")
            }

            override fun onError (exception: ImageCaptureException){
                Log.v("CAPTURE_ERROR", "Se identifica el siguiente error: ${exception.message}")
            }
        }
    )

}