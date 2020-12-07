package com.estacionamento.api.parking.park

import com.estacionamento.api.parking.NetworkConfig
import com.estacionamento.api.parking.park.model.ParkedCar
import retrofit2.Call
import java.util.*

class ParkingClient {

    private var parkingRoutes: ParkingRoutes = NetworkConfig
        .provideApi(ParkingRoutes::class.java)

    fun getParkingLots(): Call<List<ParkedCar>> = parkingRoutes.getParkingLots()

    fun parkingACar(parkedCar: ParkedCar): Call<ParkedCar> = parkingRoutes.parkingACar(parkedCar)

    fun getParkingSpaceByCar(carId: Int): Call<List<ParkedCar>> =
        parkingRoutes.getParkingSpaceByCar(carId) //TODO REVISAR RETORNO

    fun deleteCarInParkingSpace(carId: Int): Call<Objects> =
        parkingRoutes.deleteCarInParkingSpace(carId)

    fun getParkingSpaceByLocation(locationId: Int): Call<ParkedCar> =
        parkingRoutes.getParkingSpaceByLocation(locationId)

}