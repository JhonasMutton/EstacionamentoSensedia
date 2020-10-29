package com.estacionamento.api.carrorama.login.model

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body

data class LoginRequest(
    @SerializedName("usuario")
    var usuario: String,
    @SerializedName("senha")
    var senha: String,
    @SerializedName("ip")
    var ip: String,
    @SerializedName("dispositivo")
    var dispositivo: String,
    @SerializedName("acesso")
    var acesso: String,
    @SerializedName("versaoCodigo")
    var versaoCodigo: Int
)