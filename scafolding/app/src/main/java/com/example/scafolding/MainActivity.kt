package com.example.scafolding

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scafolding.ui.theme.ScafoldingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScafoldingTheme {

                }
            }
        }
    }


@Preview(showBackground = true)
@Composable
fun prevista(){
    Aplicacion(modifier = Modifier.fillMaxSize())
}

@Composable
fun Aplicacion(modifier: Modifier){
    fun al_pulsar_el_carrito(){
        Log.v("Aplicacion", "Se ha pulsado el boton y utilizado la funcion al pusar el carrito")
    }
    Scaffold(modifier = modifier,
        topBar = {
            BarraSuperior()
            //Greeting("Hola", modifier = Modifier.fillMaxWidth(0.2F).background(Color.Red))
        },
        bottomBar = {
            BarraInferior()
           // Greeting("Hola", modifier = Modifier.fillMaxWidth(0.2F).background(Color.Gray))
        },
        floatingActionButton = {
            BotonFlotante(al_pusho_picar = {
                al_pulsar_el_carrito()
                al_pulsar_el_carrito()
            })
            //Text("--------")
        })

    { padding_interior ->
        Column(modifier = Modifier.padding(padding_interior).background(Color.Cyan)) {
            Text("Esto es un mensaje",
                modifier = Modifier
                    .fillMaxHeight(0.1f)
                    .background(color = Color.Blue)
                )

            Greeting("Esto es la columna")
            Greeting("Esto es la columna")
            Greeting("Esto es la columna")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior(){
    TopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
    ),
                title = {
        Text("Titulo de la barra", modifier = Modifier)
    })
}

@Composable
fun BarraInferior(){
    fun boton_pulsado_inferior(){
        Log.v("Aplicacion", "Se ha pulsado el boton y utilizado la funcion al pulsar el carrito")
    }
    BottomAppBar (
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.primary
    ){
        Row(verticalAlignment = Alignment.CenterVertically){
            Text("Aqui va un aopion")
            Spacer(modifier =  Modifier.width(15.dp))
            Icon(Icons.Rounded.ShoppingCart, contentDescription = "Boton de carrito de compras")
            BotonFlotante(al_pusho_picar = {boton_pulsado_inferior()})
        }
    }
}

@Composable
fun BotonFlotante(al_pusho_picar: () -> Unit){
    FloatingActionButton(onClick = al_pusho_picar) {
        Icon(Icons.Rounded.ShoppingCart, contentDescription = "")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}