package com.estacionamento.api.carrorama.login

import com.estacionamento.api.carrorama.NetworkConfig
import com.estacionamento.api.carrorama.login.model.LoginRequest
import com.estacionamento.api.carrorama.login.model.LoginResponse
import retrofit2.Call

class LoginClient {

    private var loginRoutes: LoginRoutes = NetworkConfig
        .provideApi(LoginRoutes::class.java)

    fun login(loginRequest: LoginRequest): Call<LoginResponse> = loginRoutes.login(loginRequest)

    fun forgotPassword(email: String): Call<LoginResponse> = loginRoutes.forgotPassword(email)
}
