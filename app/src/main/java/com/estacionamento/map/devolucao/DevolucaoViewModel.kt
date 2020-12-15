package com.estacionamento.map.devolucao

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.estacionamento.api.parking.park.ParkingClient
import com.estacionamento.api.parking.park.model.ParkedCar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class DevolucaoViewModel(
    private val context: Context
) : ViewModel() {

    private val _liveData: MutableLiveData<DevolucaoViewState> = MutableLiveData()
    private val parkingClient: ParkingClient = ParkingClient()
    private var locationId = -1
    private var carId = -1

    val liveData: LiveData<DevolucaoViewState>
        get() = _liveData

    fun load() {
        _liveData.value = DevolucaoViewState.LoadingMap
    }

    fun loadMap() {
        _liveData.value = DevolucaoViewState.FinishedMap
    }

    fun sendCar() {
        _liveData.value = DevolucaoViewState.SendingCar
        try {
            GlobalScope.launch {
                if (carId == -1 || locationId == -1) {
                    val exception = Exception("infoError")
                    _liveData.postValue(DevolucaoViewState.Error(exception))
                } else {
                    val parkedCar = ParkedCar(locationId, carId)
                    setParkingSpace(parkedCar) ?: run {
                        val exception = Exception("infoError")
                        _liveData.postValue(DevolucaoViewState.Error(exception))
                        return@launch
                    }
                    _liveData.postValue(DevolucaoViewState.CarSent)
                }
            }
        } catch (ex: Exception) {
            _liveData.value = DevolucaoViewState.Error(ex)
        }
    }

    fun getLocationId(): Int = locationId

    fun setLocationId(locationId: Int) {
        this.locationId = locationId
        Log.d("debug", "setLocationId: index - ${this.locationId}")
    }

    fun getCarId(): Int = carId

    fun setCarId(id: Int) {
        carId = id
        Log.d("debug", "setCarId: index - $carId")
    }

    private fun setParkingSpace(parkedCar: ParkedCar): ParkedCar? {
        val parkingSpace = parkingClient.parkingACar(parkedCar).execute()
        if (parkingSpace.isSuccessful) {
            return parkingSpace.body()
        }
        return null
    }

}