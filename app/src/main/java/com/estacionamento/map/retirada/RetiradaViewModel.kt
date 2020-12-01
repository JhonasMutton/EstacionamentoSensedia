package com.estacionamento.map.retirada

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.estacionamento.api.carrorama.reservation.ReservationClient
import com.estacionamento.api.parking.park.ParkingClient
import com.estacionamento.api.parking.park.model.ParkedCar
import com.estacionamento.session.SessionManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class RetiradaViewModel(
    private val context: Context
): ViewModel() {

    private val _liveData: MutableLiveData<RetiradaViewState> = MutableLiveData()
    private val reservationClient: ReservationClient = ReservationClient()
    private val parkingClient: ParkingClient = ParkingClient()
    private val sessionManager: SessionManager = SessionManager(context)
    private var carId = -1
    private var carLocation = -1
    private var parkingSpaceId = -1

    val liveData: LiveData<RetiradaViewState>
        get() = _liveData

    fun load() {
        _liveData.value = RetiradaViewState.LoadingMap
    }

    fun loadMap(){
        _liveData.value = RetiradaViewState.FinishedMap
    }

    fun mapException(exception: Exception){
        _liveData.value = RetiradaViewState.Error(exception)
    }

    fun sendCarToPickup() {
        _liveData.value = RetiradaViewState.SendingCar
        try {
            GlobalScope.launch {
                if (parkingSpaceId == -1) { //TODO modificar essa logica
                    val exception = Exception("infoError")
                    _liveData.postValue(RetiradaViewState.Error(exception))
                } else {
                    if(!deleteParkingSpaceByCar()){
                        val exception = Exception("infoError")
                        _liveData.postValue(RetiradaViewState.Error(exception))
                    }
                    _liveData.postValue(RetiradaViewState.CarSent)
                }
            }
        }
        catch (e: Exception)
        {
            _liveData.value = RetiradaViewState.Error(e)
        }
    }

    fun getCarId(): Int = carId

    fun setCarId(id: Int){
        carId = id
        Log.d("debug", "setCarId: index - $carId")
    }

    fun getCarLocation(): Int = carLocation

    fun getParkingSpace(): Int = parkingSpaceId

    fun setCarLocation(id: Int){
        carLocation = id
        Log.d("debug", "setCarLocation: index - $carLocation")
    }

    fun setParkingSpace(id: Int){
        parkingSpaceId = id
        Log.d("debug", "setCarLocationId: index - $carLocation")
    }

    private fun deleteParkingSpaceByCar(): Boolean {
        val parkingSpace = parkingClient.deleteCarInParkingSpace(carId).execute()
        return parkingSpace.isSuccessful
    }

}