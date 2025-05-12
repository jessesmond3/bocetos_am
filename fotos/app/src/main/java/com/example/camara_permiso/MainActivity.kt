package com.example.camara_permiso

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.camara_permiso.pantallas.PantallaCam
import com.example.camara_permiso.ui.theme.Camara_permisoTheme
import androidx.compose.foundation.layout.fillMaxSize

class MainActivity : ComponentActivity() {

    private val tienePermisoCamara = mutableStateOf(false)

    private val solicitudDePermiso =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { estaGarantizado ->
            tienePermisoCamara.value = estaGarantizado
            if (!estaGarantizado) {
                // Permiso denegado: Handle appropriately (e.g., show a message)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        verificarPermisoCamara() // Check permission on create

        setContent {
            Camara_permisoTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    if (tienePermisoCamara.value) {
                        val contexto = LocalContext.current
                        PantallaCam() // Pass context if needed
                    } else {
                        // Show UI for no permission
                    }
                }
            }
        }
    }

    private fun verificarPermisoCamara() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            tienePermisoCamara.value = true
        } else {
            solicitudDePermiso.launch(Manifest.permission.CAMERA)
        }
    }
}