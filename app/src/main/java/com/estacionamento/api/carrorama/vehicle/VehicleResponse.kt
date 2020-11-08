package com.estacionamento.api.carrorama.vehicle

import com.google.gson.annotations.SerializedName


data class VehicleResponse (
    @SerializedName("success") val success : Boolean,
    @SerializedName("messages") val messages : List<String>,
    @SerializedName("object") val vehicleObject : List<VehicleObject>
)