package com.estacionamento.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.estacionamento.database.*
import kotlinx.coroutines.*
import java.lang.Exception


class LoginViewModel(
    private val context: Context
) : ViewModel() {

    val homeLiveData: MutableLiveData<String> = MutableLiveData()

    private val _liveData: MutableLiveData<LoginViewState> = MutableLiveData()

    val liveData: LiveData<LoginViewState>
        get() = _liveData

    private var carId = -1

    private var carLocation = -1

    private var veiculoLocalizacao = -1

    private lateinit var chapa: String

    private val db = Room.databaseBuilder(context, MyDataBase::class.java,"banco")
    .build()

    val veiculoDao = db.getVeiculoDao()
    val veiculoLocalizacaoDao = db.getVeiculoLocalizacaoDao()
    val localizacaoDao = db.getLocalizacaoDao()

    fun getPlaca(): String{
        return chapa
    }

    fun getCarId(): Int{
        return carId
    }

    fun getCarLocation(): Int{
        return carLocation
    }

    fun getVeiculoLocalizacao(): Int{
        return veiculoLocalizacao
    }

    fun setPlaca(chapaVeiculo : String){
        chapa = chapaVeiculo
    }

    fun startDb(){
            GlobalScope.launch {
                val veiculo1 = Veiculo(null,"AAA1111")
                veiculoDao.insert(veiculo1)
                val veiculo2 = Veiculo(null,"ABC1234")
                veiculoDao.insert(veiculo2)
                val veiculo3 = Veiculo(null,"EDE1188")
                veiculoDao.insert(veiculo3)
                for(x in 0 until 480)
                {
                    val localizacao = Localizacao(null,"X")
                    localizacaoDao.insert(localizacao)
                }
                val veiculoLocalizacaoSecreta = VeiculoLocalizacao(null,0,0, 0)
                veiculoLocalizacaoDao.insert(veiculoLocalizacaoSecreta)
                val veiculoLocalizacao = VeiculoLocalizacao(null,1,230, 1)
                veiculoLocalizacaoDao.insert(veiculoLocalizacao)
                Log.d("debug", "BANCO CRIADO")
            }
    }

    fun sendCar() {
        _liveData.value = LoginViewState.LoadingCarInfo
        try {
            GlobalScope.launch {
                carId = veiculoDao.getVeiculo(chapa)
                if(validateCar()){
                    Log.d("debug", "getCarId: carId - $carId")
                    carLocation = veiculoLocalizacaoDao.veiculoDisponivel(carId)
                    if(validateLocation())
                    {
                        veiculoLocalizacao = veiculoLocalizacaoDao.getVeiculoLocalizacao(carId,carLocation)
                        _liveData.postValue(LoginViewState.CarInfoLoadedRetirada)
                    }
                }
            }
        }
        catch (e: Exception)
        {
            _liveData.value = LoginViewState.Error(e)
        }
    }

    fun sendCarDevolucao() {
        _liveData.value = LoginViewState.LoadingCarInfo
        try {
            GlobalScope.launch {
                carId = veiculoDao.getVeiculo(chapa)
                if(validateCar()){
                    Log.d("debug", "getCarId: carId - $carId")
                    carLocation = veiculoLocalizacaoDao.veiculoDisponivel(carId)
                    if(validateLocationDevolucao())
                    {
                        _liveData.postValue(LoginViewState.CarInfoLoadedDevolucao)
                    }
                }
            }
        }
        catch (e: Exception)
        {
            _liveData.value = LoginViewState.Error(e)
        }
    }

    private suspend fun validateCar(): Boolean{
        if(carId == 0){
            val exception = Exception("carroInvalido")
            _liveData.postValue(LoginViewState.Error(exception))
            return false
        }
        return true
    }

    private suspend fun validateLocation(): Boolean{
        if(carLocation == 0){
            val exception = Exception("carroRetirado")
            _liveData.postValue(LoginViewState.Error(exception))
            return false
        }
        return true
    }


    private suspend fun validateLocationDevolucao(): Boolean{
        if(carLocation != 0){
            val exception = Exception("carroDevolvido")
            _liveData.postValue(LoginViewState.Error(exception))
            return false
        }
        return true
    }

    fun showPlacaInvalidaError(){
        val exception = Exception("placaInvalida")
        _liveData.value  = LoginViewState.Error(exception)
    }
}