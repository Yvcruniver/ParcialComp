package com.example.parcialcomponentes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcialcomponentes.data.ReservaDatabase
import com.example.parcialcomponentes.model.Reserva
import com.example.parcialcomponentes.repository.ReservaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReservaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ReservaRepository

    private val _reservas = MutableStateFlow<List<Reserva>>(emptyList())
    val reservas: StateFlow<List<Reserva>> = _reservas.asStateFlow()

    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError.asStateFlow()

    init {
        val dao = ReservaDatabase.getInstance(application).reservaDao()
        repository = ReservaRepository(dao)
    }

    fun cargarReservas() {
        viewModelScope.launch {
            _reservas.value = repository.obtenerTodas()
        }
    }

    fun guardarReserva(reserva: Reserva, onResultado: (Boolean) -> Unit) {
        viewModelScope.launch {
            val exitoso = repository.guardarReserva(reserva)
            if (!exitoso) {
                _mensajeError.value = "Ya existe una reserva activa en esa cancha, fecha y hora"
            } else {
                _mensajeError.value = null
                cargarReservas()
            }
            onResultado(exitoso)
        }
    }

    fun limpiarError() { _mensajeError.value = null }

    fun eliminarReserva(reserva: Reserva) {
        viewModelScope.launch {
            repository.eliminar(reserva)
            cargarReservas()
        }
    }

    fun actualizarReserva(reserva: Reserva, onResultado: (Boolean) -> Unit) {
        viewModelScope.launch {
            val exitoso = repository.actualizar(reserva)
            onResultado(exitoso)
            if (exitoso) cargarReservas()
        }
    }

    fun buscarPorCliente(nombre: String) {
        viewModelScope.launch {
            _reservas.value = repository.buscarPorCliente(nombre)
        }
    }
}