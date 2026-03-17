package com.example.parcialcomponentes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.parcialcomponentes.model.Reserva
import com.example.parcialcomponentes.ui.*
import com.example.parcialcomp.ui.theme.ParcialCompTheme
import com.example.parcialcomponentes.viewmodel.ReservaViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: ReservaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParcialCompTheme {
                AppNavigation(viewModel)
            }
        }
    }
}

@Composable
fun AppNavigation(viewModel: ReservaViewModel) {
    val navController = rememberNavController()
    var reservaSeleccionada by remember { mutableStateOf<Reserva?>(null) }
    var rutaActual by remember { mutableStateOf("agregar") }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF1A237E)) {
                NavigationBarItem(
                    selected = rutaActual == "agregar",
                    onClick = { rutaActual = "agregar"; navController.navigate("agregar") },
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    label = { Text("Agregar") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF1A237E),
                        selectedTextColor = Color.White,
                        indicatorColor = Color.White,
                        unselectedIconColor = Color(0xFFB0BEC5),
                        unselectedTextColor = Color(0xFFB0BEC5)
                    )
                )
                NavigationBarItem(
                    selected = rutaActual == "lista",
                    onClick = { rutaActual = "lista"; navController.navigate("lista") },
                    icon = { Icon(Icons.Default.List, contentDescription = null) },
                    label = { Text("Listado") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF1A237E),
                        selectedTextColor = Color.White,
                        indicatorColor = Color.White,
                        unselectedIconColor = Color(0xFFB0BEC5),
                        unselectedTextColor = Color(0xFFB0BEC5)
                    )
                )
                NavigationBarItem(
                    selected = rutaActual == "resumen",
                    onClick = { rutaActual = "resumen"; navController.navigate("resumen") },
                    icon = { Icon(Icons.Default.Star, contentDescription = null) },
                    label = { Text("Resumen") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF1A237E),
                        selectedTextColor = Color.White,
                        indicatorColor = Color.White,
                        unselectedIconColor = Color(0xFFB0BEC5),
                        unselectedTextColor = Color(0xFFB0BEC5)
                    )
                )
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = "agregar",
            modifier = Modifier.padding(innerPadding)) {
            composable("agregar") {
                AgregarReservaScreen(viewModel) { navController.navigateUp() }
            }
            composable("lista") {
                ListaReservasScreen(viewModel) { reserva ->
                    reservaSeleccionada = reserva
                    navController.navigate("editar")
                }
            }
            composable("resumen") { ResumenScreen(viewModel) }
            composable("editar") {
                reservaSeleccionada?.let { reserva ->
                    EditarReservaScreen(reserva, viewModel) { navController.navigateUp() }
                }
            }
        }
    }
}