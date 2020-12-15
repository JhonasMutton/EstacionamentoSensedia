package com.estacionamento.map.retirada

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.estacionamento.api.carrorama.reservation.ReservationClient
import com.estacionamento.api.parking.park.ParkingClient
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
    private var locationId = -1
    private var parkingSpaceId = -1

    val liveData: LiveData<RetiradaViewState>
        get() = _liveData

    fun load() {
        _liveData.value = RetiradaViewState.LoadingMap
    }

    fun loadMap(){
        _liveData.value = RetiradaViewState.FinishedMap
    }

    fun sendCarToPickup() {
        _liveData.value = RetiradaViewState.SendingCar
        try {
            GlobalScope.launch {
                if (parkingSpaceId == -1) {
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

    fun getLocationId(): Int = locationId

    fun getParkingSpace(): Int = parkingSpaceId

    fun setLocationId(id: Int){
        locationId = id
        Log.d("debug", "setLocationId: index - $locationId")
    }

    fun setParkingSpace(id: Int){
        parkingSpaceId = id
        Log.d("debug", "setCarLocationId: index - $locationId")
    }

    private fun deleteParkingSpaceByCar(): Boolean {
        val parkingSpace = parkingClient.deleteCarInParkingSpace(carId).execute()
        return parkingSpace.isSuccessful
    }

}