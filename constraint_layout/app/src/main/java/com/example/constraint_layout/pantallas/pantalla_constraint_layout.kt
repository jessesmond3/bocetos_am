package com.example.constriant_layout.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.constriant_layout.ui.theme.Purple80

// https://developer.android.com/develop/ui/compose/layouts/constraintlayout
// Informacion sobre el constraint layout
@Composable
fun PantallaDeCuadros(){
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val gradiente = listOf(Color.Cyan, Color.Magenta, Color.Green)

        val (caja_a, caja_b, caja_c, caja_d, caja_e, caja_f ) = createRefs()
        val (caja_g, caja_h, caja_i ) = createRefs()

        val tamaño_de_cajas = 75.dp

        val cajas_tercer_fila_chain = createHorizontalChain(caja_e, caja_d, caja_f,
            chainStyle = ChainStyle.Spread)

        Box(modifier = Modifier.size(tamaño_de_cajas).background(Color.Black).constrainAs(caja_a){
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }) {
            Text("Esto es un texto de prueba sumamente largo", style = TextStyle(
                brush = Brush.linearGradient(
                    colors = gradiente
                )
            ))
        }

        Box(modifier = Modifier
            .size(tamaño_de_cajas)
            .background(Brush.linearGradient(gradiente))
            .constrainAs(caja_b) {
                top.linkTo(caja_a.bottom, margin = 15.dp)
                start.linkTo(parent.start)
                end.linkTo(caja_c.start)
            }) {
            Text("B")
        }

        Button(onClick = {}, modifier = Modifier
            .constrainAs(caja_c) {
                top.linkTo(caja_a.bottom, margin = 15.dp)
                start.linkTo(caja_b.end)
                end.linkTo(parent.end)
            }
            .background(Color.Red)) { Text("C") }

        Button(onClick = {}, modifier = Modifier
            .constrainAs(caja_d) {
                top.linkTo(caja_b.bottom, margin = 50.dp)
                start.linkTo(parent.start)
                end.linkTo(caja_e.end)
            }
            .background(Color.Red)) { Text("D") }

        Button(onClick = {}, modifier = Modifier
            .constrainAs(caja_e) {
                top.linkTo(caja_b.bottom, margin = 50.dp)
                start.linkTo(caja_d.end)
                end.linkTo(caja_f.start)
            }
            .background(Color.Red)) { Text("E") }

        Button(onClick = {}, modifier = Modifier
            .constrainAs(caja_f) {
                top.linkTo(caja_b.bottom, margin = 50.dp)
                start.linkTo(caja_e.end)
                end.linkTo(parent.end)
            }
            .background(Color.Red)) { Text("F") }

        Button(onClick = {}, modifier = Modifier
            .constrainAs(caja_g) {
                start.linkTo(caja_h.start)
                top.linkTo(caja_i.top)

            }
            .background(Color.Red)) { Text("G") }

        Button(onClick = {}, modifier = Modifier
            .constrainAs(caja_h) {
                start.linkTo(parent.start)

                bottom.linkTo(caja_i.bottom)

            }
            .background(Color.Red)) { Text("H") }

        Button(onClick = {}, modifier = Modifier
            .constrainAs(caja_i) {
                top.linkTo(caja_f.bottom, margin = 50.dp)
                start.linkTo(caja_h.end)

                end.linkTo(parent.end)

            }
            .fillMaxHeight(0.3f)) { Text("I") }

    }
}

@Preview(showBackground = true)
@Composable
fun Previsualizacion(){
    PantallaDeCuadros()
}