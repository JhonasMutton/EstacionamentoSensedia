package com.estacionamento.api.carrorama.login

import com.estacionamento.api.carrorama.login.model.LoginRequest
import com.estacionamento.api.carrorama.login.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Routes {
    @POST("acesso/login")
    fun Login(@Body loginRequest: LoginRequest) : Call<LoginResponse>
}