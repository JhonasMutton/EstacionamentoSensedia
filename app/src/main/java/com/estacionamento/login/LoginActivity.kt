package com.estacionamento.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.estacionamento.R
import com.estacionamento.api.carrorama.login.Client
import com.estacionamento.api.carrorama.login.model.LoginRequest
import com.estacionamento.api.carrorama.login.model.LoginResponse
import com.estacionamento.databinding.LayoutActivityHomeBinding
import com.estacionamento.databinding.LayoutActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import java.net.Inet4Address
import java.net.NetworkInterface

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LayoutActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var viewModelFactory: LoginViewModelFactory
    private lateinit var loginClient: Client
    private var logingIn: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.layout_activity_login
        )
        viewModelFactory = LoginViewModelFactory(applicationContext)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        viewModel.homeLiveData.observe(this, Observer {
            binding.inputUsuarioInterno.text.toString()
        })
        viewModel.homeLiveData.observe(this, Observer {
            binding.inputSenhaInterno.text.toString()
        })


        binding.buttonDevolverCarro.setOnClickListener {
            login(
                binding.inputUsuarioInterno.text.toString().toUpperCase(),
                binding.inputSenhaInterno.text.toString().toUpperCase()
            )
        }

        loginClient = Client(applicationContext)
    }

    private fun login(usuario: String, senha: String) {
        when {
            usuario == "" -> binding.inputUsuarioInterno.error = "Preencha o campo de usuario"
            senha == "" -> binding.inputSenhaInterno.error = "Preencha o campo de senha"
            else -> makeLogin(usuario, senha)
        }
    }

    private fun getHomeIntent() = Intent(HOME_INTENT)

    companion object {
        private const val HOME_INTENT = "com.estacionamento.home.START"
    }

    private fun makeLogin(usuario: String, senha: String) {
        val loginRequest = LoginRequest(usuario, senha, getIpv4HostAddress(), "android", "teste", 2)
        if (logingIn) {
            return
        }
        logingIn = true
        loginClient.Login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable): Unit {
                binding.loginError.text = "Ocorreu um erro interno na autenticação!"
                logingIn = false
            }
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.e("login", "Autenticação feita e com código:" + response.code())
                Log.e("login", "Autenticação feita e com código:" +  response.body().toString())
                when {
                    response.isSuccessful -> startActivity(getHomeIntent()) //TODO colocar body no session manager
                    response.code() == 401 -> binding.loginError.text = "Usuário ou senha errados!"//TODO ADICIONAR AO DICIONARIO
                    else -> binding.loginError.text = "Ocorreu alfgum erro na autenticação!"
                }
                logingIn = false
            }
        })


    }

    fun getIpv4HostAddress(): String {
        NetworkInterface.getNetworkInterfaces()?.toList()?.map { networkInterface ->
            networkInterface.inetAddresses?.toList()?.find {
                !it.isLoopbackAddress && it is Inet4Address
            }?.let { return it.hostAddress }
        }
        return ""
    }
}