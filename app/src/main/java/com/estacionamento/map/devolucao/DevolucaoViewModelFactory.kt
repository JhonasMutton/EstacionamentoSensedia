package com.estacionamento.map.devolucao

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DevolucaoViewModelFactory(
    private val context: Context
): ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DevolucaoViewModel::class.java)) {
            return DevolucaoViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}