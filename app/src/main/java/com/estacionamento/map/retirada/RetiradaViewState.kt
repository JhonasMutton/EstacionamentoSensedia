package com.estacionamento.map.retirada

import java.lang.Exception

sealed class RetiradaViewState {

    object LoadingMap: RetiradaViewState()

    object FinishedMap : RetiradaViewState()

    data class Error(val exception: Exception) : RetiradaViewState()

    object SendingCar: RetiradaViewState()

    object CarSent: RetiradaViewState()

    data class ErrorCarSent(val exception: Exception) : RetiradaViewState()

}