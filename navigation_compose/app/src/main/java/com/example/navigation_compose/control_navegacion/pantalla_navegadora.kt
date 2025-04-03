package com.example.navigation_compose.control_navegacion

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.navigationcompose.pantallas.PantallaDosVista
import com.example.navigationcompose.pantallas.PantallaTresVista
import com.example.navigationcompose.pantallas.PantallaUnoVista

@Preview
@Composable
fun PantallaNavegadora(){
    val control_de_navegacion = rememberNavController()

    NavHost(navController = control_de_navegacion, startDestination = Pantalla1){
        composable<Pantalla1>{
            PantallaUnoVista(navegar_hacia_pantalla_dos = {
                control_de_navegacion.navigate(Pantalla2)
            })
        }

        composable<Pantalla2> {
            PantallaDosVista(navegar_hacia_pantalla_3 = {
                control_de_navegacion.navigate(Pantalla3)
            })
        }

        composable<Pantalla3> {
            PantallaTresVista(navegar_hacia_home = {
                control_de_navegacion.navigate(Pantalla1)
            })
        }
    }
}