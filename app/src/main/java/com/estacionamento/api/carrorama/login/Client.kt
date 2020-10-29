package com.estacionamento.api.carrorama.login

import com.estacionamento.api.carrorama.NetworkConfig
import com.estacionamento.api.carrorama.login.model.LoginRequest
import com.estacionamento.api.carrorama.login.model.LoginResponse
import retrofit2.Call

class Client {

    private var routes: Routes = NetworkConfig
        .provideApi(Routes::class.java)

    public fun Login(loginRequest: LoginRequest): Call<LoginResponse> = routes.Login(loginRequest)

}
