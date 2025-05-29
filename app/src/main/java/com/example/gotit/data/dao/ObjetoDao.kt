package com.example.gotit.data.dao

import androidx.room.*
import com.example.gotit.data.model.ObjetoColeccion
import kotlinx.coroutines.flow.Flow

@Dao
interface ObjetoDao {
    @Query("SELECT * FROM objetos ORDER BY fecha DESC")
    fun obtenerObjetos(): Flow<List<ObjetoColeccion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarObjeto(objeto: ObjetoColeccion)

    @Delete
    suspend fun eliminarObjeto(objeto: ObjetoColeccion)

    @Update
    suspend fun actualizar(objeto: ObjetoColeccion)

}
