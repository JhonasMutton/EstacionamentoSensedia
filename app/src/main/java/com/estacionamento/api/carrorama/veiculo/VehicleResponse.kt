package com.estacionamento.api.carrorama.veiculo

import com.estacionamento.api.carrorama.login.model.Objeto
import com.google.gson.annotations.SerializedName


data class VehicleResponse (
    @SerializedName("success") val success : Boolean,
    @SerializedName("messages") val messages : List<String>,
    @SerializedName("object") val objeto : List<Objeto>
)