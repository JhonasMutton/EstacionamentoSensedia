package com.estacionamento.login

import android.content.Intent
import android.os.Build.MODEL
import android.os.Bundle
import android.util.Log
import android.util.Patterns.EMAIL_ADDRESS
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.estacionamento.R
import com.estacionamento.api.carrorama.NetworkConfig
import com.estacionamento.api.carrorama.login.LoginClient
import com.estacionamento.api.carrorama.login.model.LoginRequest
import com.estacionamento.api.carrorama.login.model.LoginResponse
import com.estacionamento.databinding.LayoutActivityLoginBinding
import com.estacionamento.session.SessionManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import java.net.Inet4Address
import java.net.NetworkInterface


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LayoutActivityLoginBinding
    private lateinit var loginClient: LoginClient
    private lateinit var sessionManager: SessionManager
    private var loggingIn: Boolean = false

    companion object {
        private const val HOME_INTENT = "com.estacionamento.home.START"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.layout_activity_login
        )


        binding.buttonDevolverCarro.setOnClickListener {
            login(
                binding.inputUsuarioInterno.text.toString(),
                binding.inputSenhaInterno.text.toString()
            )
        }

        binding.esqueceuSenha.setOnClickListener{
            startActivity(Intent("com.login.forgotpassword.START"))
            finish()
        }

        loginClient = LoginClient()
        sessionManager = SessionManager(applicationContext)
    }

    private fun login(usuario: String, senha: String) = when {
        usuario == "" -> binding.inputUsuarioInterno.error = "Preencha o campo de usuario"
        !EMAIL_ADDRESS.matcher(usuario).matches() ->
            binding.inputUsuarioInterno.error = "Endereço de email de formato errado!"
        senha == "" -> binding.inputSenhaInterno.error = "Preencha o campo de senha"
        else -> doLogin(usuario, senha)
    }

    private fun getHomeIntent() = Intent(HOME_INTENT)

    private fun doLogin(usuario: String, senha: String) {
        val loginRequest = LoginRequest(usuario, senha, getIpv4HostAddress(), MODEL, "web", 2)
        Log.i("login", loginRequest.toString())
        if (loggingIn) {
            return
        }
        loggingIn = true
        disableLoginButton(loggingIn)
        loginClient.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable): Unit {
                binding.loginError.text = "Ocorreu um erro interno durante a autenticação!"
                loggingIn = false
                disableLoginButton(loggingIn)
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.e("login", "Autenticação feita e com código: " + response.code())
                when {
                    response.isSuccessful -> response.body()?.let {
                        sessionManager.startSession(it)
                        startActivity(getHomeIntent())
                    } ?: let {
                        binding.loginError.text = "Ocorreu algum erro durante a autenticação!"
                    }
                    response.code() == 401 -> binding.loginError.text =
                        extractMessageError(response, "Usuário ou senha incorretos!")
                    else -> binding.loginError.text =
                        extractMessageError(response, "Ocorreu algum erro durante a autenticação!")
                }
                loggingIn = false
                disableLoginButton(loggingIn)
            }
        })

    }

    private fun disableLoginButton(isDisable: Boolean) {
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
            val errorResponse: LoginResponse? = errorConverter.convert(it)
            errorResponse?.messages?.get(0) ?: alternativeMessage
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