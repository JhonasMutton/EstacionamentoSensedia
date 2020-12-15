package com.estacionamento.api.carrorama.reservation

import com.google.gson.annotations.SerializedName

data class ReservationObject (
    @SerializedName("id") val id : Int,
    @SerializedName("placa") val placa : String,
    @SerializedName("modeloVeiculo") val modeloVeiculo : String,
    @SerializedName("marcaVeiculo") val marcaVeiculo : String,
    @SerializedName("hodometro") val hodometro : Int,
    @SerializedName("diaSemanaRodizioSP") val diaSemanaRodizioSP : String
)