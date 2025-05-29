package com.example.gotit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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

    fun agregarObjeto(objeto: ObjetoColeccion) {
        viewModelScope.launch {
            objetoDao.insertarObjeto(objeto)
        }
    }

    fun eliminarObjeto(objeto: ObjetoColeccion) {
        viewModelScope.launch {
            objetoDao.eliminarObjeto(objeto)
        }
    }

    fun actualizarObjeto(objeto: ObjetoColeccion) {
        viewModelScope.launch {
            objetoDao.actualizar(objeto)
        }
    }
}

