package com.example.parcialcomponentes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.parcialcomponentes.model.Reserva

@Database(entities = [Reserva::class], version = 1)
abstract class ReservaDatabase : RoomDatabase() {

    abstract fun reservaDao(): ReservaDao

    companion object {
        @Volatile
        private var INSTANCE: ReservaDatabase? = null

        fun getInstance(context: Context): ReservaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ReservaDatabase::class.java,
                    "reservas_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
