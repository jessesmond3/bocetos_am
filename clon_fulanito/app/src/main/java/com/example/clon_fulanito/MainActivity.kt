// MainActivity.kt (Styling Enhancements)
package com.example.clon_fulanito

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.clon_fulanito.modelos.swapi.NaveEspacial
import com.example.clon_fulanito.ui.theme.Clon_fulanitoTheme
import com.example.clon_fulanito.vista_modelos.FulanitoViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults

class MainActivity : ComponentActivity() {
    private val modelo_app = FulanitoViewModel()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Clon_fulanitoTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("Star Wars Ships", style = MaterialTheme.typography.headlineMedium) },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                ) { innerPadding ->
                    PantallaNavegadora(Modifier.padding(innerPadding), vm_fulanito = modelo_app)
                }
            }
        }
    }
}

@Composable
fun PantallaNavegadora(modifier: Modifier = Modifier, vm_fulanito: FulanitoViewModel) {
    // Dummy data for now
    val ships = remember {
        mutableStateListOf(
            NaveEspacial("X-wing", "T-65B", "Incom Corporation", "1", "1"),
            NaveEspacial("Millennium Falcon", "YT-1300 light freighter", "Corellian Engineering Corporation", "4", "6"),
            NaveEspacial("TIE Fighter", "TIE/ln space superiority fighter", "Sienar Fleet Systems", "1", "0")
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp) // Add spacing between items
    ) {
        items(ships) { ship ->
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp) // Add spacing within the card
                ) {
                    Text(
                        text = ship.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "Model: ${ship.model}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Manufacturer: ${ship.manufacturer}", style = MaterialTheme.typography.bodyMedium)
                    Row {
                        Text(text = "Crew: ", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                        Text(text = ship.crew, style = MaterialTheme.typography.bodyMedium)
                    }
                    Row {
                        Text(text = "Passengers: ", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                        Text(text = ship.passengers, style = MaterialTheme.typography.bodyMedium)
                    }
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