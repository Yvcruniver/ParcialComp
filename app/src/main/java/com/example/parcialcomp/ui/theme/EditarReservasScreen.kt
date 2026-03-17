package com.example.parcialcomponentes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parcialcomponentes.model.Reserva
import com.example.parcialcomponentes.viewmodel.ReservaViewModel

@Composable
fun EditarReservaScreen(reserva: Reserva, viewModel: ReservaViewModel, onNavigateBack: () -> Unit) {
    var nombreCliente by remember { mutableStateOf(reserva.nombreCliente) }
    var numeroCancha by remember { mutableStateOf(reserva.numeroCancha.toString()) }
    var fecha by remember { mutableStateOf(reserva.fecha) }
    var hora by remember { mutableStateOf(reserva.hora) }
    var estado by remember { mutableStateOf(reserva.estado) }
    var mensajeError by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("Editar Reserva", fontSize = 26.sp, fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E), modifier = Modifier.padding(bottom = 4.dp))
        Text("Modifica los datos de la reserva", fontSize = 14.sp,
            color = Color(0xFF757575), modifier = Modifier.padding(bottom = 24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                OutlinedTextField(value = nombreCliente, onValueChange = { nombreCliente = it },
                    label = { Text("Nombre del cliente") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    singleLine = true, shape = RoundedCornerShape(10.dp))
                OutlinedTextField(value = numeroCancha, onValueChange = { numeroCancha = it },
                    label = { Text("Número de cancha") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    singleLine = true, shape = RoundedCornerShape(10.dp))
                OutlinedTextField(value = fecha, onValueChange = { fecha = it },
                    label = { Text("Fecha (DD/MM/AAAA)") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    singleLine = true, shape = RoundedCornerShape(10.dp))
                OutlinedTextField(value = hora, onValueChange = { hora = it },
                    label = { Text("Hora (HH:MM)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true, shape = RoundedCornerShape(10.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Estado de la reserva", fontSize = 14.sp, fontWeight = FontWeight.SemiBold,
            color = Color(0xFF424242), modifier = Modifier.padding(bottom = 8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(bottom = 16.dp)) {
            Button(
                onClick = { estado = "Activa" },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (estado == "Activa") Color(0xFF2E7D32) else Color(0xFFEEEEEE),
                    contentColor = if (estado == "Activa") Color.White else Color(0xFF757575)
                )
            ) { Text("Activa", fontWeight = FontWeight.SemiBold) }

            Button(
                onClick = { estado = "Cancelada" },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (estado == "Cancelada") Color(0xFFC62828) else Color(0xFFEEEEEE),
                    contentColor = if (estado == "Cancelada") Color.White else Color(0xFF757575)
                )
            ) { Text("Cancelada", fontWeight = FontWeight.SemiBold) }
        }

        if (mensajeError.isNotEmpty()) {
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))) {
                Text(mensajeError, color = Color(0xFFC62828),
                    modifier = Modifier.padding(12.dp), fontSize = 14.sp)
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(onClick = onNavigateBack, modifier = Modifier.weight(1f).height(52.dp),
                shape = RoundedCornerShape(12.dp)) {
                Text("Cancelar", fontWeight = FontWeight.SemiBold)
            }
            Button(
                onClick = {
                    val reservaActualizada = reserva.copy(
                        nombreCliente = nombreCliente,
                        numeroCancha = numeroCancha.toIntOrNull() ?: reserva.numeroCancha,
                        fecha = fecha, hora = hora, estado = estado
                    )
                    viewModel.actualizarReserva(reservaActualizada) { exitoso ->
                        if (exitoso) onNavigateBack()
                        else mensajeError = "Ya existe una reserva activa en esa cancha, fecha y hora"
                    }
                },
                modifier = Modifier.weight(1f).height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E))
            ) { Text("Guardar", fontWeight = FontWeight.SemiBold) }
        }
    }
}