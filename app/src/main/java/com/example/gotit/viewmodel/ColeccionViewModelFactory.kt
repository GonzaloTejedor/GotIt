package com.example.gotit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gotit.data.dao.ObjetoDao

class ColeccionViewModelFactory(
    private val objetoDao: ObjetoDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ColeccionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ColeccionViewModel(objetoDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
