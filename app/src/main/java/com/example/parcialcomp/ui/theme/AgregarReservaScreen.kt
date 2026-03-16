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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.parcialcomponentes.model.Reserva
import com.example.parcialcomponentes.viewmodel.ReservaViewModel

@Composable
fun AgregarReservaScreen(viewModel: ReservaViewModel, onNavigateBack: () -> Unit) {
    var nombreCliente by remember { mutableStateOf("") }
    var numeroCancha by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var mensajeLocal by remember { mutableStateOf("") }
    var guardadoExitoso by remember { mutableStateOf(false) }
    val mensajeError by viewModel.mensajeError.collectAsStateWithLifecycle()

    DisposableEffect(Unit) { onDispose { viewModel.limpiarError() } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Nueva Reserva",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Completa los datos para registrar",
            fontSize = 14.sp,
            color = Color(0xFF757575),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                OutlinedTextField(
                    value = nombreCliente,
                    onValueChange = { nombreCliente = it },
                    label = { Text("Nombre del cliente") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )
                OutlinedTextField(
                    value = numeroCancha,
                    onValueChange = { numeroCancha = it },
                    label = { Text("Número de cancha") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )
                OutlinedTextField(
                    value = fecha,
                    onValueChange = { fecha = it },
                    label = { Text("Fecha (DD/MM/AAAA)") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )
                OutlinedTextField(
                    value = hora,
                    onValueChange = { hora = it },
                    label = { Text("Hora (HH:MM)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (mensajeLocal.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
            ) {
                Text(mensajeLocal, color = Color(0xFFC62828),
                    modifier = Modifier.padding(12.dp), fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (!mensajeError.isNullOrEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
            ) {
                Text(mensajeError!!, color = Color(0xFFC62828),
                    modifier = Modifier.padding(12.dp), fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (guardadoExitoso) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
            ) {
                Text("Reserva guardada correctamente", color = Color(0xFF2E7D32),
                    modifier = Modifier.padding(12.dp), fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                mensajeLocal = ""
                guardadoExitoso = false
                if (nombreCliente.isBlank() || numeroCancha.isBlank() || fecha.isBlank() || hora.isBlank()) {
                    mensajeLocal = "Todos los campos son obligatorios"
                    return@Button
                }
                val reserva = Reserva(
                    nombreCliente = nombreCliente,
                    numeroCancha = numeroCancha.toIntOrNull() ?: 0,
                    fecha = fecha, hora = hora, estado = "Activa"
                )
                viewModel.guardarReserva(reserva) { exitoso ->
                    if (exitoso) {
                        nombreCliente = ""; numeroCancha = ""; fecha = ""; hora = ""
                        guardadoExitoso = true
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E))
        ) {
            Text("Guardar Reserva", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
