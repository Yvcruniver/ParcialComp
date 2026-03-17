package com.example.parcialcomponentes

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.parcialcomponentes.data.ReservaDatabase
import com.example.parcialcomponentes.model.Reserva
import com.example.parcialcomponentes.repository.ReservaRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ReservaRepositoryTest {

    private lateinit var database: ReservaDatabase
    private lateinit var repository: ReservaRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, ReservaDatabase::class.java)
            .allowMainThreadQueries().build()
        repository = ReservaRepository(database.reservaDao())
    }

    @After
    fun tearDown() { database.close() }

    @Test
    fun insertarYObtener() = runBlocking {
        repository.guardarReserva(Reserva(nombreCliente = "Carlos Perez", numeroCancha = 1, fecha = "15/03/2026", hora = "10:00", estado = "Activa"))
        val resultado = repository.obtenerTodas()
        assertEquals(1, resultado.size)
        assertEquals("Carlos Perez", resultado[0].nombreCliente)
    }

    @Test
    fun detectarConflicto() = runBlocking {
        repository.guardarReserva(Reserva(nombreCliente = "Ana Torres", numeroCancha = 2, fecha = "16/03/2026", hora = "09:00", estado = "Activa"))
        val resultado = repository.guardarReserva(Reserva(nombreCliente = "Luis Gomez", numeroCancha = 2, fecha = "16/03/2026", hora = "09:00", estado = "Activa"))
        assertFalse(resultado)
    }

    @Test
    fun noConflictoHoraDistinta() = runBlocking {
        repository.guardarReserva(Reserva(nombreCliente = "Maria Lopez", numeroCancha = 3, fecha = "17/03/2026", hora = "10:00", estado = "Activa"))
        val resultado = repository.guardarReserva(Reserva(nombreCliente = "Pedro Ruiz", numeroCancha = 3, fecha = "17/03/2026", hora = "11:00", estado = "Activa"))
        assertTrue(resultado)
    }

    @Test
    fun buscarPorNombreParcial() = runBlocking {
        repository.guardarReserva(Reserva(nombreCliente = "Juan Perez", numeroCancha = 1, fecha = "18/03/2026", hora = "14:00", estado = "Activa"))
        val resultado = repository.buscarPorCliente("Juan")
        assertEquals(1, resultado.size)
        assertEquals("Juan Perez", resultado[0].nombreCliente)
    }

    @Test
    fun eliminarReserva() = runBlocking {
        repository.guardarReserva(Reserva(nombreCliente = "Sofia Castro", numeroCancha = 4, fecha = "19/03/2026", hora = "16:00", estado = "Activa"))
        val reservaGuardada = repository.obtenerTodas()[0]
        repository.eliminar(reservaGuardada)
        assertEquals(0, repository.obtenerTodas().size)
    }
}