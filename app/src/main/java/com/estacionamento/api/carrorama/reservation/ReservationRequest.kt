package com.estacionamento.api.carrorama.reservation

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body

data class ReservationRequest (
    @SerializedName("id") val id : Int,
    @SerializedName("dataHoraReserva") val dataHoraReserva : String,
    @SerializedName("dataHoraPrevisaoDevolucao") val dataHoraPrevisaoDevolucao : String,
    @SerializedName("circularaSP") val circularaSP : Boolean,
    @SerializedName("veiculoID") val veiculoID : Int,
    @SerializedName("condutorID") val condutorID : Int,
    @SerializedName("motivoReservaPoolID") val motivoReservaPoolID : Int,
    @SerializedName("localRetirada") val localRetirada : String,
    @SerializedName("localDevolucao") val localDevolucao : String,
    @SerializedName("voucherAutorizacao") val voucherAutorizacao : String,
    @SerializedName("observacao") val observacao : String
)