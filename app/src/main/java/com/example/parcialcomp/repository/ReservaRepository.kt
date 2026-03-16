package com.example.parcialcomponentes.repository

import com.example.parcialcomponentes.data.ReservaDao
import com.example.parcialcomponentes.model.Reserva

class ReservaRepository(private val dao: ReservaDao) {

    suspend fun guardarReserva(reserva: Reserva): Boolean {
        val conflicto = dao.existeConflicto(reserva.numeroCancha, reserva.fecha, reserva.hora, -1)
        if (conflicto > 0) return false
        dao.insertar(reserva)
        return true
    }

    suspend fun obtenerTodas(): List<Reserva> = dao.obtenerTodas()

    suspend fun eliminar(reserva: Reserva) = dao.eliminar(reserva)

    suspend fun actualizar(reserva: Reserva): Boolean {
        val conflicto = dao.existeConflicto(reserva.numeroCancha, reserva.fecha, reserva.hora, reserva.id)
        if (conflicto > 0) return false
        dao.actualizar(reserva)
        return true
    }

    suspend fun buscarPorCliente(nombre: String): List<Reserva> = dao.buscarPorCliente(nombre)
}
