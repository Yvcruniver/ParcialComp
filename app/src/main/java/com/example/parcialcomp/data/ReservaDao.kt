package com.example.parcialcomponentes.data

import androidx.room.*
import com.example.parcialcomponentes.model.Reserva

@Dao
interface ReservaDao {

    @Insert
    suspend fun insertar(reserva: Reserva)

    @Query("SELECT * FROM reservas ORDER BY fecha DESC")
    suspend fun obtenerTodas(): List<Reserva>

    @Delete
    suspend fun eliminar(reserva: Reserva)

    @Update
    suspend fun actualizar(reserva: Reserva)

    @Query("SELECT * FROM reservas WHERE nombreCliente LIKE '%' || :nombre || '%'")
    suspend fun buscarPorCliente(nombre: String): List<Reserva>

    @Query("SELECT COUNT(*) FROM reservas WHERE numeroCancha = :cancha AND fecha = :fecha AND hora = :hora AND estado = 'Activa' AND id != :idExcluir")
    suspend fun existeConflicto(cancha: Int, fecha: String, hora: String, idExcluir: Int): Int
}
