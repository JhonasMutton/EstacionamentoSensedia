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
import com.estacionamento.databinding.LayoutActivityLoginBinding
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LayoutActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var viewModelFactory: LoginViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
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

        binding.buttonDevolverCarro.setOnClickListener{
            login( binding.inputUsuarioInterno.text.toString().toUpperCase(), binding.inputSenhaInterno.text.toString().toUpperCase() )
        }}

    private fun login(usuario : String, senha : String){
        when {
            usuario == "" -> binding.inputUsuarioInterno.error = "Preencha o campo de usuario"
            senha == "" -> binding.inputSenhaInterno.error = "Preencha o campo de senha"
            else -> startActivity(getHomeIntent())
        }
    }

    private fun getHomeIntent() = Intent(HOME_INTENT)


    companion object{
        private const val HOME_INTENT = "com.estacionamento.home.START"
    }
}