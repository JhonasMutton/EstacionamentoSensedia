package com.estacionamento.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.estacionamento.R
import com.estacionamento.databinding.LayoutActivityHomeBinding
import java.lang.Exception

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: LayoutActivityHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var viewModelFactory: HomeViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.layout_activity_home
        )
        viewModelFactory = HomeViewModelFactory(applicationContext)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        viewModel.homeLiveData.observe(this, Observer {
            binding.inputPlacaCarroInterno.text.toString()
        })
        binding.buttonRetirarCarro.setOnClickListener {
            viewCarToPickup(binding.inputPlacaCarroInterno.text.toString().toUpperCase())
        }

        binding.buttonDevolverCarro.setOnClickListener {
            sendCar(binding.inputPlacaCarroInterno.text.toString().toUpperCase())
        }

        viewModel.liveData.observe(this, Observer { viewState ->
            when (viewState) {
                HomeViewState.LoadingCarInfo -> showLoadingView()
                is HomeViewState.CarInfoLoadedDevolucao -> sendCarReturn()
                is HomeViewState.CarInfoLoadedRetirada -> sendCarPickup()
                is HomeViewState.Error -> showErrorView(viewState.exception)
            }
        })
    }

    private fun showLoadingView() {

    }

    private fun showErrorView(exception: Exception) {
        Log.d("debug", "showErrorView: exception - ${exception.message}")
        val errorMessage: String = when (exception.message) {
            "carroInvalido" -> "Carro não cadastrado"
            "carroRetirado" -> "Carro já retirado"
            "carroDevolvido" -> "Carro já devolvido"
            "placaInvalida" -> "Placa inválida"
            else -> "Ocorreu um erro na aplicação"
        }
        binding.inputPlacaCarro.error = errorMessage
    }

    private fun sendCarPickup() {
        val intent = getRetiradaIntent()
        intent.putExtra("carId", viewModel.getCarId())
        intent.putExtra(
            "carLocation",
            viewModel.getCarLocation()
        ) //TODO Verificar  as variaveis injetadas
        intent.putExtra("parkingSpace", viewModel.getParkingSpaceId())
        Log.d(
            "debug",
            "sendCarPickup: carId - ${viewModel.getCarId()} carLocation - ${viewModel.getCarLocation()} parkingSpace - ${viewModel.getParkingSpaceId()}"
        )
        startActivity(intent)
    }

    private fun sendCarReturn() {
        val intent = getCarReturnIntent()
        intent.putExtra("carId", viewModel.getCarId())
        Log.d("debug", "sendCarDevolucao: carId - ${viewModel.getCarId()}")
        startActivity(intent)
    }

    private fun viewCarToPickup(carLicencePlate: String) {
        if (!validaPlaca(carLicencePlate))
            viewModel.showPlacaInvalidaError()
        else {
            viewModel.setLicensePlate(carLicencePlate)
            viewModel.callCarPickup()
        }
    }

    private fun sendCar(placaCarro: String) {
        if (!validaPlaca(placaCarro)) {
            viewModel.showPlacaInvalidaError()
        } else {
            viewModel.setLicensePlate(placaCarro)
            viewModel.carReturn()
        }
    }

    override fun onResume() {
        super.onResume()

        binding.inputPlacaCarro.error = null
    }

    private fun validaPlaca(placaCarro: String): Boolean {

        //verifica o tamanho da string para saber se o número de caracteres da placa está correto
        if (placaCarro.length != 7) {
            return false
        }

        //TODO FAZER A VALIDAÇÃO INCLUINDO NOVO PADRÃO DE PLACAS
//        //verifica se os 3 primeiros caracteres digitados são letras
//        if (!placaCarro.get(0).isLetter() || !placaCarro.get(1).isLetter() || !placaCarro.get(2).isLetter()) {
//            return false
//        }
//
//        //verifica se os quatro últimos caracteres digitados são números
//        if (!placaCarro.get(3).isDigit() || !placaCarro.get(4).isDigit() || !placaCarro.get(5).isDigit() || !placaCarro.get(6).isDigit()) {
//            return false
//        }

        //retorna verdadeiro se perceber que a string tem o formato de uma placa
        return true
    }

    private fun getCarReturnIntent() = Intent(DEVOLUCAO_INTENT)

    private fun getRetiradaIntent() = Intent(RETIRADA_INTENT)

    companion object {
        private const val DEVOLUCAO_INTENT = "com.estacionamento.map.devolucao.START"
        private const val RETIRADA_INTENT = "com.estacionamento.map.retirada.START"
    }
}