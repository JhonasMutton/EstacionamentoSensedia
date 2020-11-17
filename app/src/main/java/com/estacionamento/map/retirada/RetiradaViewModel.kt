package com.estacionamento.map.retirada

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.estacionamento.api.carrorama.reservation.ReservationClient
import com.estacionamento.api.carrorama.reservation.ReservationRequest
import com.estacionamento.session.SessionManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class RetiradaViewModel(
    private val context: Context
): ViewModel() {

    private val _liveData: MutableLiveData<RetiradaViewState> = MutableLiveData()
    private val reservationClient: ReservationClient = ReservationClient()
    private val sessionManager: SessionManager = SessionManager(context)
    private var carId = -1
    private var carLocation = -1
    private var carLocationId = -1

    private val db = Room.databaseBuilder(context, MyDataBase::class.java,"banco")
        .build()

    private val veiculoLocalizacaoDao = db.getVeiculoLocalizacaoDao()
    private val veiculoDao = db.getVeiculoDao()
    private val localizacaoDao = db.getLocalizacaoDao()

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

    fun sendCar() {
        _liveData.value = RetiradaViewState.SendingCar
        try {
            GlobalScope.launch {
                if (carLocationId == -1) {
                    val exception = Exception("infoError")
                    _liveData.postValue(RetiradaViewState.Error(exception))
                } else {
                    //Faz a reserva TODO AJUSTAR onde é chamado e como é montado a request
                    val reservation = ReservationRequest(
                        0,
                        Calendar.getInstance().time.toString(),
                        Calendar.getInstance().time.toString(),
                        false,
                        carId,
                        sessionManager.getConductorId(),
                        0,
                        carLocation.toString(),
                        carLocation.toString(),
                        "",
                        ""
                    )

                   val reserveResponse = reservationClient.reserve(sessionManager.getHash(), reservation).execute()
                    if (!reserveResponse.isSuccessful){
                        val exception = Exception("infoError")
                        _liveData.postValue(RetiradaViewState.Error(exception))
                        return@launch
                    }
                    veiculoLocalizacaoDao.updateDisponibilidade(carLocationId)
                    db.close()
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

    fun getCarLocationId(): Int = carLocationId

    fun setCarLocation(id: Int){
        carLocation = id
        Log.d("debug", "setCarLocation: index - $carLocation")
    }

    fun setCarLocationId(id: Int){
        carLocationId = id
        Log.d("debug", "setCarLocationId: index - $carLocation")
    }

}