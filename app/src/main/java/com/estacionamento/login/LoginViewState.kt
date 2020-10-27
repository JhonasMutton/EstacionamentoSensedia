package com.estacionamento.home

import java.lang.Exception

sealed class LoginViewState {

    object LoadingCarInfo: LoginViewState()

    object CarInfoLoadedRetirada : LoginViewState()

    object CarInfoLoadedDevolucao : LoginViewState()

    data class Error(val exception: Exception) : LoginViewState()

}