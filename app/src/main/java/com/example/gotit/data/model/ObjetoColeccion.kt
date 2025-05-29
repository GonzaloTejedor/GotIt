
package com.example.gotit.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "objetos")
data class ObjetoColeccion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val categoria: String,
    val fecha: String,
    val imagenUri: String,
    val precio: Double = 0.0
)

