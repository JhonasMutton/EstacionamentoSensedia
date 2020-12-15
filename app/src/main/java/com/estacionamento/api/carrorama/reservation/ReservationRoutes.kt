package com.estacionamento.api.carrorama.reservation

import retrofit2.Call
import retrofit2.http.*

interface ReservationRoutes {
    @GET("/reservapool/{accessHash}")
    fun reserve(@Path("accessHash") accessHash: String, @Body reservationRequest: ReservationRequest) : Call<ReservationResponse>
}