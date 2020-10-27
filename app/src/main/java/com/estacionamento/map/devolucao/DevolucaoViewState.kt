package com.estacionamento.map.devolucao

import java.lang.Exception

sealed class DevolucaoViewState {

    object LoadingMap: DevolucaoViewState()

    object FinishedMap : DevolucaoViewState()

    data class Error(val exception: Exception) : DevolucaoViewState()

    object SendingCar: DevolucaoViewState()

    object CarSent: DevolucaoViewState()

}