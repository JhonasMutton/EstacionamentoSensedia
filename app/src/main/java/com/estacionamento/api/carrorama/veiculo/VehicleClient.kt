package com.estacionamento.api.carrorama.veiculo

import com.estacionamento.api.carrorama.NetworkConfig
import com.estacionamento.api.carrorama.login.model.LoginRequest
import com.estacionamento.api.carrorama.login.model.LoginResponse
import retrofit2.Call

class VehicleClient {
    private var vehicleRoutes: VehicleRoutes = NetworkConfig
        .provideApi(VehicleRoutes::class.java)

    fun getVehicleId(accessHash: String, plate: String): Call<VehicleResponse> =
        vehicleRoutes.getVehicleId(accessHash, plate)
}
