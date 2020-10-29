package com.estacionamento.home

import android.content.Intent
import android.os.Build.DEVICE
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.estacionamento.R
import com.estacionamento.api.carrorama.NetworkConfig
import com.estacionamento.api.carrorama.login.Client
import com.estacionamento.api.carrorama.login.model.LoginRequest
import com.estacionamento.api.carrorama.login.model.LoginResponse
import com.estacionamento.databinding.LayoutActivityLoginBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import java.net.Inet4Address
import java.net.NetworkInterface


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LayoutActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var viewModelFactory: LoginViewModelFactory
    private lateinit var loginClient: Client
    private var loggingIn: Boolean = false
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

        loginClient = Client()
    }

    private fun login(usuario: String, senha: String) {
        when {
            usuario == "" -> binding.inputUsuarioInterno.error = "Preencha o campo de usuario"
            senha == "" -> binding.inputSenhaInterno.error = "Preencha o campo de senha"
            else -> doLogin(usuario, senha)
        }
    }

    private fun getHomeIntent() = Intent(HOME_INTENT)

    companion object {
        private const val HOME_INTENT = "com.estacionamento.home.START"
    }

    private fun doLogin(usuario: String, senha: String) {
        val loginRequest = LoginRequest(usuario, senha, getIpv4HostAddress(), DEVICE, "app", 2)
        if (loggingIn) {
            return
        }
        loggingIn = true
        DisableLoginButton(loggingIn)
        loginClient.Login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable): Unit {
                binding.loginError.text = "Ocorreu um erro interno durante a autenticação!"
                loggingIn = false
                DisableLoginButton(loggingIn)
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.e("login", "Autenticação feita e com código:" + response.code())
                when {
                    response.isSuccessful -> startActivity(getHomeIntent()) //TODO colocar body no session manager
                    response.code() == 401 -> binding.loginError.text =
                        extractMessageError(response, "Usuário ou senha incorretos!")
                    else -> binding.loginError.text =
                        extractMessageError(response, "Ocorreu algum erro durante a autenticação!")
                }
                loggingIn = false
                DisableLoginButton(loggingIn)
            }
        })

    }

    private fun DisableLoginButton(isDisable: Boolean) {
        binding.buttonDevolverCarro.isEnabled = !isDisable
    }

    private fun extractMessageError(
        response: Response<LoginResponse>,
        alternativeMessage: String
    ): String {
        return response.errorBody()?.let {
            val errorConverter: Converter<ResponseBody, LoginResponse> =
                NetworkConfig.getRetrofitInstance().responseBodyConverter(
                    LoginResponse::class.java,
                    arrayOfNulls<Annotation>(0)
                )
            val error: LoginResponse? = errorConverter.convert(it)
            error?.messages?.get(0) ?: alternativeMessage
        } ?: alternativeMessage
    }

    private fun getIpv4HostAddress(): String {
        NetworkInterface.getNetworkInterfaces()?.toList()?.map { networkInterface ->
            networkInterface.inetAddresses?.toList()?.find {
                !it.isLoopbackAddress && it is Inet4Address
            }?.let { return it.hostAddress }
        }
        return ""
    }
}