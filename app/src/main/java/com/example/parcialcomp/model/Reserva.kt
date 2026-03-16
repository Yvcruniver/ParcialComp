package com.example.parcialcomponentes.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservas")
data class Reserva(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombreCliente: String,
    val numeroCancha: Int,
    val fecha: String,
    val hora: String,
    val estado: String = "Activa"
)