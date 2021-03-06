package com.estacionamento.home

import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.estacionamento.api.carrorama.vehicle.VehicleClient
import com.estacionamento.api.carrorama.vehicle.VehicleObject
import com.estacionamento.api.parking.park.ParkingClient
import com.estacionamento.api.parking.park.model.ParkedCar
import com.estacionamento.session.SessionManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class HomeViewModel(
    private val context: Context
) : ViewModel() {

    val homeLiveData: MutableLiveData<String> = MutableLiveData()
    private val _liveData: MutableLiveData<HomeViewState> = MutableLiveData()
    private val vehicleClient: VehicleClient = VehicleClient()
    private val parkingClient: ParkingClient = ParkingClient()
    private val sessionManager: SessionManager = SessionManager(context)

    val liveData: LiveData<HomeViewState>
        get() = _liveData
    private var carId = -1
    private var carLocation = -1
    private var parkingSpaceId = -1
    private lateinit var licensePlate: String

    fun getLicensePlate(): String {
        return licensePlate
    }

    fun getCarId(): Int {
        return carId
    }

    fun getCarLocation(): Int {
        return carLocation
    }

    fun getParkingSpaceId(): Int {
        return parkingSpaceId
    }

    fun setLicensePlate(carLicencePlate: String) {
        licensePlate = carLicencePlate
    }

    fun callCarPickup() {
        _liveData.value = HomeViewState.LoadingCarInfo
        try {
            GlobalScope.launch {
                val vehicleObject = getVehicle()
                vehicleObject ?: run {
                    _liveData.postValue(HomeViewState.Error(Resources.NotFoundException("carroInvalido")))
                    return@launch
                }

                carId = vehicleObject.id
                if (validateCar()) {
                    Log.d("debug", "getCarId: carId - $carId")

                    val parkingSpaceCar = getParkingSpaceByCar()
                        ?: run {
                            _liveData.postValue(HomeViewState.Error(Resources.NotFoundException("carroRetirado")))
                            return@launch
                        }

                    carLocation = parkingSpaceCar.locationId

                    parkingSpaceCar.id ?: run {
                        _liveData.postValue(HomeViewState.Error(Resources.NotFoundException("carroRetirado")))
                        return@launch
                    }
                    parkingSpaceId = parkingSpaceCar.id
                    /*TODO FAZER UMA SEGUNDA AVALIAÇÃO VIA CARRORAMA  E FAZER A RETIRADA DO CARRO NO MESMO*/
                    _liveData.postValue(HomeViewState.CarInfoLoadedRetirada) //TODO CHAMA A RETIRADA
                }
            }
        } catch (e: Exception) {
            _liveData.value = HomeViewState.Error(e)
        }
    }

    fun carReturn() {
        _liveData.value = HomeViewState.LoadingCarInfo
        try {
            GlobalScope.launch {
                val vehicleObject = getVehicle()
                vehicleObject ?: run {
                    _liveData.postValue(HomeViewState.Error(Resources.NotFoundException("carroInvalido")))
                    return@launch
                }

                carId = vehicleObject.id
                if (validateCar()) {
                    Log.d("debug", "getCarId: carId - $carId")
                    val parkingSpaceCar = null //TODO RETIRAR MOCK getParkingSpaceByCar()

                    if (parkingSpaceCar == null) {
                        _liveData.postValue(HomeViewState.CarInfoLoadedDevolucao)
                    } else {
                        val exception = Exception("carroDevolvido")
                        _liveData.postValue(HomeViewState.Error(exception))
                    }
                }
            }
        } catch (e: Exception) {
            _liveData.value = HomeViewState.Error(e)
        }
    }

    private suspend fun validateCar(): Boolean {
        if (carId == 0) {
            val exception = Exception("carroInvalido")
            _liveData.postValue(HomeViewState.Error(exception))
            return false
        }
        return true
    }

    private suspend fun validateLocation(): Boolean {
        if (carLocation == 0) {
            val exception = Exception("carroRetirado")
            _liveData.postValue(HomeViewState.Error(exception))
            return false
        }
        return true
    }

    private suspend fun validateLocationCarReturn(): Boolean {
        if (carLocation != 0) {
            val exception = Exception("carroDevolvido")
            _liveData.postValue(HomeViewState.Error(exception))
            return false
        }
        return true
    }

    fun showPlacaInvalidaError() {
        val exception = Exception("placaInvalida")
        _liveData.value = HomeViewState.Error(exception)
    }

    private fun getVehicle(): VehicleObject? {
        //TODO TIRAR O MOCK
//        val vehicleRequest = vehicleClient.getVehicleId(sessionManager.getHash(), licensePlate).execute()
//
//        if (vehicleRequest.code() == 200) {
//            return vehicleRequest.body()?.vehicleObject
//        }
//        return null

        return VehicleObject(10, "AAA1111", "Gol", "Volkswagen", 1, "seg");
    }

    private fun getParkingSpaceByCar(): ParkedCar? {
        val parkingSpace = parkingClient.getParkingSpaceByCar(carId).execute()

        if (parkingSpace.code() == 200) {

            return parkingSpace.body()?.let {
                if (it.isNotEmpty()) {
                    it[0]
                } else {
                    null
                }
            }

        }
        return null
    }
}