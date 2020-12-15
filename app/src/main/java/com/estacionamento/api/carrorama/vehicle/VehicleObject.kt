package com.estacionamento.api.carrorama.vehicle

import com.google.gson.annotations.SerializedName

data class VehicleObject (
    @SerializedName("id") val id : Int,
    @SerializedName("placa") val placa : String,
    @SerializedName("modeloVeiculo") val modeloVeiculo : String,
    @SerializedName("marcaVeiculo") val marcaVeiculo : String,
    @SerializedName("hodometro") val hodometro : Int,
    @SerializedName("diaSemanaRodizioSP") val diaSemanaRodizioSP : String
)