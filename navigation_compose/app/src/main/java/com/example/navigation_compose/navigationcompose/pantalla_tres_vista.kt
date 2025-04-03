package com.example.navigation_compose.navigationcompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun PantallaTresVista(navegar_hacia_home: () -> Unit){
    Column(modifier = Modifier.fillMaxSize().background(Color.Red)) {
        Text("Hola desde la pantalla tres")
        Button(onClick = navegar_hacia_home) {
            Text("Regresar a apntalla uno")
        }
    }
}