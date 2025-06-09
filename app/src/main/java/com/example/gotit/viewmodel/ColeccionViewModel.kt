package com.example.gotit.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gotit.data.dao.ObjetoDao
import com.example.gotit.data.model.ObjetoColeccion
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ColeccionViewModel(private val objetoDao: ObjetoDao) : ViewModel() {

    val objetos: StateFlow<List<ObjetoColeccion>> = objetoDao
        .obtenerObjetos()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    // Lista mutable y reactiva para categorías
    private val _categoriasConocidas = mutableStateListOf<String>()
    val categoriasConocidas: SnapshotStateList<String> get() = _categoriasConocidas

    init {
        // Inicializamos la lista con categorías existentes (asumiendo método para obtener sin Flow)
        viewModelScope.launch {
            val listaObjetos = objetoDao.obtenerObjetosInstantaneo() // Debes implementar este método en tu DAO
            _categoriasConocidas.clear()
            _categoriasConocidas.addAll(
                listaObjetos.map { it.categoria }.distinct().sorted()
            )
        }
    }

    fun agregarObjeto(objeto: ObjetoColeccion) {
        viewModelScope.launch {
            objetoDao.insertarObjeto(objeto)

            if (!_categoriasConocidas.contains(objeto.categoria)) {
                _categoriasConocidas.add(objeto.categoria)
            }
        }
    }

    fun eliminarObjeto(objeto: ObjetoColeccion) {
        viewModelScope.launch {
            objetoDao.eliminarObjeto(objeto)
            // No eliminamos categoría para que siga disponible
        }
    }

    fun actualizarObjeto(objeto: ObjetoColeccion) {
        viewModelScope.launch {
            objetoDao.actualizar(objeto)

            if (!_categoriasConocidas.contains(objeto.categoria)) {
                _categoriasConocidas.add(objeto.categoria)
            }
        }
    }
}
