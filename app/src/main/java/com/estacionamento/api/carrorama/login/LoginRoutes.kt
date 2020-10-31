package com.estacionamento.api.carrorama.login

import com.estacionamento.api.carrorama.login.model.LoginRequest
import com.estacionamento.api.carrorama.login.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface LoginRoutes {
    @POST("acesso/login")
    fun login(@Body loginRequest: LoginRequest) : Call<LoginResponse>

    @POST("acesso/recuperarsenha/{email}")
    fun forgotPassword(@Path("email") email: String) : Call<LoginResponse>
}