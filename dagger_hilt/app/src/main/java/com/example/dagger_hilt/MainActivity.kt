package com.example.daggerhilt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.daggerhilt.ui.theme.DaggerHiltTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DaggerHiltTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column {
                        Greeting(
                            modifier = Modifier.padding(innerPadding)
                        )

                        Adios()
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier, vm_publicaicones: VistaModeloPublicaciones = hiltViewModel()) {
    Text(
        text = "Hello ${vm_publicaicones.publicaicones.value}!",
        modifier = modifier
    )
}

@Composable
fun Adios(vm_publicaicones: VistaModeloPublicaciones = hiltViewModel()){
    Column {
        TextField(value = vm_publicaicones.publicaicones.value,
            onValueChange = {valor_nuevo -> vm_publicaicones.publicaicones.value = valor_nuevo})
        vista_3()
    }

}

@Composable
fun vista_3(vm_comentarios: VistaModeloComentarios = hiltViewModel()){
    Text("Informacion de comentarios: ${vm_comentarios.comentarios.value}")
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DaggerHiltTheme {
        Greeting()
    }
}