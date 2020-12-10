package com.estacionamento.api.parking.park.model

import com.google.gson.annotations.SerializedName
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

data class ParkedCar(
    @SerializedName("id") val id: Int?,
    @SerializedName("location") val location: Int?,
    @SerializedName("locationId") val locationId: Int,
    @SerializedName("carId") val carId: Int,
    @SerializedName("parkDate") val parkDate: String?
) {
    constructor(locationId: Int, carId: Int) : this(null, null, locationId, carId, null)
}

