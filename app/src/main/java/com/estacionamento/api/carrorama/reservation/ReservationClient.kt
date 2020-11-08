package com.estacionamento.api.carrorama.reservation

import com.estacionamento.api.carrorama.NetworkConfig
import retrofit2.Call

class ReservationClient {
    private var vehicleRoutes: ReservationRoutes = NetworkConfig
        .provideApi(ReservationRoutes::class.java)

    fun reserve(
        accessHash: String,
        reservationRequest: ReservationRequest
    ): Call<ReservationResponse> =
        vehicleRoutes.reserve(accessHash, reservationRequest)
}
