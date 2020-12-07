package com.estacionamento.api.parking.park

import com.estacionamento.api.parking.park.model.ParkedCar
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface ParkingRoutes {

    @GET(Companion.basePath)
    fun getParkingLots(): Call<List<ParkedCar>>

    @POST(Companion.basePath)
    fun parkingACar(@Body parkedCar: ParkedCar): Call<ParkedCar>

    @GET("${Companion.basePath}/cars/{carId}")
    fun getParkingSpaceByCar(@Path("carId") carId: Int): Call<List<ParkedCar>>

    @DELETE("${Companion.basePath}/cars/{carId}")
    fun deleteCarInParkingSpace(@Path("carId") carId: Int): Call<Objects>

    @GET("${Companion.basePath}/locations/{locationId}")
    fun getParkingSpaceByLocation(@Path("locationId") locationId: Int): Call<ParkedCar>

    companion object {
        const val basePath  = "/parking"
    }
}
