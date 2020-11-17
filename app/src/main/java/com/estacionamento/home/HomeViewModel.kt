package com.estacionamento.home

import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.estacionamento.api.carrorama.vehicle.VehicleClient
import com.estacionamento.api.carrorama.vehicle.VehicleObject
import com.estacionamento.database.DB
import com.estacionamento.database.LocalizacaoDaoImpl
import com.estacionamento.database.VeiculoLocalizacaoDaoImpl
import com.estacionamento.database.model.Localizacao
import com.estacionamento.database.model.VeiculoLocalizacao
import com.estacionamento.session.SessionManager
import kotlinx.coroutines.*
import java.lang.Exception


class HomeViewModel(
    private val context: Context
) : ViewModel() {

    val homeLiveData: MutableLiveData<String> = MutableLiveData()
    private val _liveData: MutableLiveData<HomeViewState> = MutableLiveData()
    private val vehicleClient: VehicleClient = VehicleClient()
    private val sessionManager: SessionManager = SessionManager(context)

    val liveData: LiveData<HomeViewState>
        get() = _liveData
    private var carId = -1
    private var carLocation = -1
    private var veiculoLocalizacao = -1
    private lateinit var chapa: String

    private val db = DB()

    init {

    }

    val veiculoLocalizacaoDao = VeiculoLocalizacaoDaoImpl(db)
    val localizacaoDao = LocalizacaoDaoImpl(db)

    fun getPlaca(): String {
        return chapa
    }

    fun getCarId(): Int {
        return carId
    }

    fun getCarLocation(): Int {
        return carLocation
    }

    fun getVeiculoLocalizacao(): Int {
        return veiculoLocalizacao
    }

    fun setPlaca(chapaVeiculo: String) {
        chapa = chapaVeiculo
    }

    fun connectDb() = GlobalScope.launch {
        db.connect()
        //Salva as localizações
        //TODO Adicionar trecho em um migrador, como Liquibase
        for(x in 0 until 480)
        {
            val localizacao = Localizacao(null,"X")
            localizacaoDao.insert(localizacao)
        }
        val veiculoLocalizacaoSecreta = VeiculoLocalizacao(null,0,0, 0)
        veiculoLocalizacaoDao.insert(veiculoLocalizacaoSecreta)
        val veiculoLocalizacao = VeiculoLocalizacao(null,1,230, 1)
        veiculoLocalizacaoDao.insert(veiculoLocalizacao)

        Log.d("debug", "Banco Connectado")
    }

    fun sendCar() {
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
                    //TODO Verificar disponibilidade via API
                    carLocation = veiculoLocalizacaoDao.veiculoDisponivel(carId)
                    if (validateLocation()) {
                        veiculoLocalizacao =
                            veiculoLocalizacaoDao.getVeiculoLocalizacao(carId, carLocation)
                        _liveData.postValue(HomeViewState.CarInfoLoadedRetirada)
                    }
                }
            }
        } catch (e: Exception) {
            _liveData.value = HomeViewState.Error(e)
        }
    }

    fun sendCarDevolucao() {
        _liveData.value = HomeViewState.LoadingCarInfo
        try {
            GlobalScope.launch {
//                carId = veiculoDao.getVeiculo(chapa TODO PEGAR O CAR ID PELA API
                if (validateCar()) {
                    Log.d("debug", "getCarId: carId - $carId")
                    carLocation = veiculoLocalizacaoDao.veiculoDisponivel(carId)
                    if (validateLocationDevolucao()) {
                        _liveData.postValue(HomeViewState.CarInfoLoadedDevolucao)
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


    private suspend fun validateLocationDevolucao(): Boolean {
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
        val vehicleRequest = vehicleClient.getVehicleId(sessionManager.getHash(), chapa).execute()

        if (vehicleRequest.code() == 200) {
            return vehicleRequest.body()?.vehicleObject
        }
        return null
    }
}