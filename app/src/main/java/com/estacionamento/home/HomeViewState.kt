package com.estacionamento.home

import java.lang.Exception

sealed class HomeViewState {

    object LoadingCarInfo: HomeViewState()

    object CarInfoLoadedRetirada : HomeViewState()

    object CarInfoLoadedDevolucao : HomeViewState()

    data class Error(val exception: Exception) : HomeViewState()

}