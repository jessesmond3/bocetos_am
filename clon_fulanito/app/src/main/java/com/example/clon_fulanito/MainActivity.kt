package com.example.clon_fulanito

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.clon_fulanito.ui.pantallas.PantallaNavegadora
import com.example.clon_fulanito.ui.theme.Clon_fulanitoTheme
import com.example.clon_fulanito.vista_modelos.FulanitoViewModel

class MainActivity : ComponentActivity() {
    private val modelo_app = FulanitoViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Clon_fulanitoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    //PantallaDePublicaciones(modifier = Modifier.padding(innerPadding), vm_fulanito = modelo_app)
                    PantallaNavegadora(Modifier.padding(innerPadding), vm_fulanito = modelo_app)
                }
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Clon_fulanitoTheme {
        //Greeting("Android")
    }
}