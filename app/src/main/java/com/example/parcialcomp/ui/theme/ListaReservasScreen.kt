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
import com.example.parcialcomponentes.model.Reserva
import com.example.parcialcomponentes.viewmodel.ReservaViewModel

@Composable
fun ListaReservasScreen(viewModel: ReservaViewModel, onEditarReserva: (Reserva) -> Unit) {
    val reservas by viewModel.reservas.collectAsStateWithLifecycle()
    var textoBusqueda by remember { mutableStateOf("") }
    var reservaAEliminar by remember { mutableStateOf<Reserva?>(null) }

    LaunchedEffect(Unit) { viewModel.cargarReservas() }
    LaunchedEffect(textoBusqueda) {
        if (textoBusqueda.isEmpty()) viewModel.cargarReservas()
        else viewModel.buscarPorCliente(textoBusqueda)
    }

    reservaAEliminar?.let { reserva ->
        AlertDialog(
            onDismissRequest = { reservaAEliminar = null },
            title = { Text("Eliminar reserva", fontWeight = FontWeight.Bold) },
            text = { Text("¿Deseas eliminar la reserva de ${reserva.nombreCliente}?") },
            confirmButton = {
                Button(
                    onClick = { viewModel.eliminarReserva(reserva); reservaAEliminar = null },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
                    shape = RoundedCornerShape(8.dp)
                ) { Text("Eliminar") }
            },
            dismissButton = {
                OutlinedButton(onClick = { reservaAEliminar = null },
                    shape = RoundedCornerShape(8.dp)) { Text("Cancelar") }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .padding(20.dp)
    ) {
        Text("Reservas", fontSize = 26.sp, fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E), modifier = Modifier.padding(bottom = 4.dp))
        Text("Gestiona todas las reservas", fontSize = 14.sp,
            color = Color(0xFF757575), modifier = Modifier.padding(bottom = 16.dp))

        OutlinedTextField(
            value = textoBusqueda,
            onValueChange = { textoBusqueda = it },
            label = { Text("Buscar por nombre") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        if (reservas.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Sin reservas", fontSize = 18.sp, fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF9E9E9E))
                    Text("No hay reservas registradas aún", fontSize = 14.sp, color = Color(0xFFBDBDBD))
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(reservas) { reserva ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(reserva.nombreCliente, fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = if (reserva.estado == "Activa") Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                                ) {
                                    Text(
                                        text = reserva.estado,
                                        color = if (reserva.estado == "Activa") Color(0xFF2E7D32) else Color(0xFFC62828),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(color = Color(0xFFF0F0F0))
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(modifier = Modifier.fillMaxWidth()) {
                                InfoChip("Cancha ${reserva.numeroCancha}", modifier = Modifier.weight(1f))
                                InfoChip(reserva.fecha, modifier = Modifier.weight(1f))
                                InfoChip(reserva.hora, modifier = Modifier.weight(1f))
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(
                                    onClick = { onEditarReserva(reserva) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp)
                                ) { Text("Editar") }
                                Button(
                                    onClick = { reservaAEliminar = reserva },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
                                ) { Text("Eliminar") }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoChip(text: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.padding(end = 4.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFF5F7FA)
    ) {
        Text(text, fontSize = 12.sp, color = Color(0xFF616161),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
    }
}