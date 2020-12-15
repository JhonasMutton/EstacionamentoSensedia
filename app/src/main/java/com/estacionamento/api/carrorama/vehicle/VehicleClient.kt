package com.estacionamento.api.carrorama.vehicle

import com.estacionamento.api.carrorama.NetworkConfig
import retrofit2.Call

class VehicleClient {
    private var vehicleRoutes: VehicleRoutes = NetworkConfig
        .provideApi(VehicleRoutes::class.java)

    fun getVehicleId(accessHash: String, plate: String): Call<VehicleResponse> =
        vehicleRoutes.getVehicleId(accessHash, plate)
}
