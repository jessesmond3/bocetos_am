package com.example.modelo_vista_vistamodelo.modelos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mvvvm.modelos.ModeloCajonTexto
import com.example.mvvvm.ui.theme.MvvvmTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MvvvmTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding),
                        modelo_vista = ModeloCajonTexto()
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier, modelo_vista: ModeloCajonTexto) {
    // val cajon_de_texto: String = modelo_vista.cajon_de_texto.observerAsState
    // val cajon_de_texto: String by modelo_vista.cajon_de_texto.observeAsState(initial = "Valor inicial del modelo")
    val cajon_de_texto: String by modelo_vista.cajon_de_texto.observeAsState(initial = "Valor inicial de este lado")

    Column(modifier = modifier) {
        Text(
            text = "Hello ${cajon_de_texto}!",
            modifier = Modifier.fillMaxHeight(0.05f)
        )

        TextField(onValueChange = {
            modelo_vista.pasar_nuevo_texto(it)
        }, value = cajon_de_texto)
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MvvvmTheme {
        Greeting(Modifier.fillMaxSize(), modelo_vista = ModeloCajonTexto())
    }
}