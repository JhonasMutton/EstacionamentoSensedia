package com.estacionamento.api.carrorama.vehicle

import retrofit2.Call
import retrofit2.http.*

interface VehicleRoutes {
    @GET("/veiculo/placa/{accessHash}")
    fun getVehicleId(@Path("accessHash") accessHash: String, @Query("placa") plate : String ) : Call<VehicleResponse>
}