package com.example.camara_permiso

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Surface
import androidx.core.content.ContextCompat
import com.example.camara_permiso.pantallas.PantallaCam
import com.example.camara_permiso.ui.theme.Camara_permisoTheme

class MainActivity : ComponentActivity() {

    private val solicitudDePermiso =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { estaGarantizado ->
            if (estaGarantizado) {
                mostrarVistaCamara()
            } else {
                // Permiso denegado: podrías mostrar un mensaje o cerrar la app.
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) -> {
                mostrarVistaCamara()
            }

            else -> {
                solicitudDePermiso.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun mostrarVistaCamara() {
        setContent {
            Camara_permisoTheme {
                Surface {
                    PantallaCam() // Aquí se carga la cámara con filtros
                }
            }
        }
    }
}
