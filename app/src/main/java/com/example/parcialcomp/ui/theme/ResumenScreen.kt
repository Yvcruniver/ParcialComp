package com.example.parcialcomponentes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.parcialcomponentes.viewmodel.ReservaViewModel

@Composable
fun ResumenScreen(viewModel: ReservaViewModel) {
    val reservas by viewModel.reservas.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) { viewModel.cargarReservas() }

    val activas = reservas.filter { it.estado == "Activa" }
    val canceladas = reservas.filter { it.estado == "Cancelada" }
    val porCancha = activas.groupBy { it.numeroCancha }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .padding(20.dp)
    ) {
        Text("Resumen", fontSize = 26.sp, fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E), modifier = Modifier.padding(bottom = 4.dp))
        Text("Ocupación de canchas", fontSize = 14.sp,
            color = Color(0xFF757575), modifier = Modifier.padding(bottom = 20.dp))

        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Activas", activas.size.toString(), Color(0xFF1A237E), Modifier.weight(1f))
            StatCard("Canceladas", canceladas.size.toString(), Color(0xFFC62828), Modifier.weight(1f))
            StatCard("Total", reservas.size.toString(), Color(0xFF37474F), Modifier.weight(1f))
        }

        if (activas.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay reservas activas", color = Color(0xFF9E9E9E), fontSize = 16.sp)
            }
        } else {
            Text("Por cancha", fontSize = 16.sp, fontWeight = FontWeight.SemiBold,
                color = Color(0xFF424242), modifier = Modifier.padding(bottom = 12.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(porCancha.entries.toList()) { (cancha, lista) ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Cancha $cancha", fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                                Text("${lista.size} reservas activas", fontSize = 13.sp,
                                    color = Color(0xFF757575))
                            }
                            Surface(shape = RoundedCornerShape(20.dp),
                                color = Color(0xFF1A237E)) {
                                Text("${lista.size}", color = Color.White,
                                    fontWeight = FontWeight.Bold, fontSize = 16.sp,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = color)
            Text(label, fontSize = 12.sp, color = Color(0xFF757575))
        }
    }
}