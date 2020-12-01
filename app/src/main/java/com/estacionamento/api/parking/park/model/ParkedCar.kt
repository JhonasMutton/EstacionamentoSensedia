package com.estacionamento.api.parking.park.model

import com.google.gson.annotations.SerializedName

data class ParkedCar (
    @SerializedName("id") val id : Int,
    @SerializedName("location") val location : Int,
    @SerializedName("locationId") val locationId : Int,
    @SerializedName("carId") val carId : Int,
    @SerializedName("parkDate") val parkDate : String
)