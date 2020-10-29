package com.estacionamento.api.carrorama.login

import android.content.Context
import com.estacionamento.api.carrorama.Config
import com.estacionamento.api.carrorama.login.model.LoginRequest
import com.estacionamento.api.carrorama.login.model.LoginResponse
import com.google.android.material.internal.ContextUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class Client(context: Context) {

    private var routes: Routes = Config
        .provideApi(Routes::class.java, context)

    public fun Login(loginRequest: LoginRequest): Call<LoginResponse> = routes.Login(loginRequest)

}
