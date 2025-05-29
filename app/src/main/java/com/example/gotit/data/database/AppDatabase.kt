package com.example.gotit.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gotit.data.dao.ObjetoDao
import com.example.gotit.data.model.ObjetoColeccion

@Database(entities = [ObjetoColeccion::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun objetoDao(): ObjetoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "coleccion_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
