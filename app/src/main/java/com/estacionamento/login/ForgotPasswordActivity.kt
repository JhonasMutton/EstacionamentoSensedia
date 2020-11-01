package com.estacionamento.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.estacionamento.R
import com.estacionamento.api.carrorama.NetworkConfig
import com.estacionamento.api.carrorama.login.LoginClient
import com.estacionamento.api.carrorama.login.model.LoginResponse
import com.estacionamento.databinding.LayoutActivityForgotPasswordBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response


class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: LayoutActivityForgotPasswordBinding
    private lateinit var loginClient: LoginClient
    private var sendingRecovery: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.layout_activity_forgot_password
        )

        binding.buttonVoltar.setOnClickListener {
            val openMainActivity = Intent(this, LoginActivity::class.java)
            openMainActivity.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(openMainActivity)
            finish()
        }

        binding.buttonEnviarEmail.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            when {
                email == "" -> binding.inputEmail.error = "Preencha com seu email!"
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                    binding.inputEmail.error = "Endereço de email de formato errado!"
                else -> enviarRecuperacao(email)
            }
        }

        loginClient = LoginClient()
    }

    fun enviarRecuperacao(email: String) {
        if (sendingRecovery) {
            return
        }
        sendingRecovery = true
        disableButton(sendingRecovery)
        loginClient.forgotPassword(email).enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable): Unit {
                binding.message.text = "Ocorreu um erro interno!"
                sendingRecovery = false
                disableButton(sendingRecovery)
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.e("login", "Autenticação feita e com código: " + response.code())
                when {
                    response.isSuccessful -> run {
                        binding.message.text = "Verifique seu email e siga as instruções!"
                        binding.message.setTextColor(Color.GRAY)
                    }
                    response.code() == 403 -> run {
                        binding.message.text = "Usuário não cadastrado!"
                        binding.message.setTextColor(Color.RED)
                    }
                    else -> run {
                        Log.e("w", "code" + response.code())
                        binding.message.text = extractMessageError(response)
                        binding.message.setTextColor(Color.RED)
                    }
                }
                sendingRecovery = false
                disableButton(sendingRecovery)
            }
        })

    }

    private fun disableButton(isDisable: Boolean) {
        binding.buttonEnviarEmail.isEnabled = !isDisable
    }

    private fun extractMessageError(
        response: Response<LoginResponse>
    ): String {
        val alternativeMessage: String = "Ocorreu algum erro, tente novamente!"
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

}