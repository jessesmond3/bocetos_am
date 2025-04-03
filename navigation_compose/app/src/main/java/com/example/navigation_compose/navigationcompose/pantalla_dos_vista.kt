package com.example.navigation_compose.navigationcompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun PantallaDosVista(navegar_hacia_pantalla_3: () -> Unit = {}){
    Column(modifier = Modifier.fillMaxSize().background(Color.DarkGray),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.weight(1f))
        Text("HOla desde la pantalla dos")
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = navegar_hacia_pantalla_3) {
            Text("Ir a la pantalla 3")
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}