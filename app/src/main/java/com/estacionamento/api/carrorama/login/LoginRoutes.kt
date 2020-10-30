package com.estacionamento.api.carrorama.login

import com.estacionamento.api.carrorama.login.model.LoginRequest
import com.estacionamento.api.carrorama.login.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginRoutes {
    @POST("acesso/login")
    fun login(@Body loginRequest: LoginRequest) : Call<LoginResponse>
}