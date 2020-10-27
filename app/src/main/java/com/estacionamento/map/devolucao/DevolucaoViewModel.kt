package com.estacionamento.map.devolucao

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.estacionamento.database.MyDataBase
import com.estacionamento.database.VeiculoLocalizacao
import com.estacionamento.map.retirada.RetiradaViewState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import java.lang.Exception

class DevolucaoViewModel(
    private val context: Context
): ViewModel() {

    private val _liveData: MutableLiveData<DevolucaoViewState> = MutableLiveData()

    private var selectedIndex = -1

    private var carId = -1

    private val db = Room.databaseBuilder(context, MyDataBase::class.java,"banco")
        .build()

    private val veiculoLocalizacaoDao = db.getVeiculoLocalizacaoDao()
    private val veiculoDao = db.getVeiculoDao()
    private val localizacaoDao = db.getLocalizacaoDao()

    val liveData: LiveData<DevolucaoViewState>
        get() = _liveData

    fun load() {
        _liveData.value = DevolucaoViewState.LoadingMap
    }

    fun loadMap(){
        _liveData.value = DevolucaoViewState.FinishedMap
    }

    fun mapException(exception: Exception){
        _liveData.value = DevolucaoViewState.Error(exception)
    }

    fun sendCar(){
        _liveData.value = DevolucaoViewState.SendingCar
        try {
            GlobalScope.launch {
                if(carId == -1 || selectedIndex == -1)
                {
                    val exception = Exception("infoError")
                    _liveData.postValue(DevolucaoViewState.Error(exception))
                }
                else
                {
                    veiculoLocalizacaoDao.insert(VeiculoLocalizacao(null, carId, selectedIndex, 1))
                    _liveData.postValue(DevolucaoViewState.CarSent)
                }
            }
        }
        catch (ex: Exception){
            _liveData.value = DevolucaoViewState.Error(ex)
        }
    }

    fun getSelectedIndex(): Int = selectedIndex

    fun setSelectedIndex(index: Int){
        selectedIndex = index
        Log.d("debug", "setSelectedIndex: index - $selectedIndex")
    }

    fun getCarId(): Int = carId

    fun setCarId(id: Int){
        carId = id
        Log.d("debug", "setCarId: index - $carId")
    }

}