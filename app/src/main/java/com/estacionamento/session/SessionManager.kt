package com.estacionamento.session

import android.content.Context
import android.content.SharedPreferences
import com.estacionamento.api.carrorama.login.model.LoginResponse
import java.util.*

class SessionManager(context: Context) {
     private var sharedPreferences: SharedPreferences = context.getSharedPreferences("AppKey", 0)
     private var editor: SharedPreferences.Editor  = sharedPreferences.edit()

    companion object {
        const val USERNAME_KEY = "USERNAME"
        const val HASH_KEY = "HASH"
        const val LOGGED_IN_LEY = "LOGGED_IN_TIME"
        const val IS_LOGGED = "IS_LOGGED"
    }

    fun startSession(loginResponse: LoginResponse){
        editor.putString(USERNAME_KEY, loginResponse.objeto.nome)
        editor.putString(HASH_KEY, loginResponse.objeto.hashAcesso)
        val userLoggedInTime = Calendar.getInstance().time.time
        editor.putLong(LOGGED_IN_LEY, userLoggedInTime)
        editor.putBoolean(IS_LOGGED, true)
        editor.commit()
    }

    fun endSession(){
        editor.clear()
        editor.putBoolean(IS_LOGGED, false)
        editor.commit()
    }

    fun getUsername() = sharedPreferences.getString(USERNAME_KEY,"Usuário")

    fun getHash() = sharedPreferences.getString(HASH_KEY,"invalid")

    fun getLoginTime() =  sharedPreferences.getString(LOGGED_IN_LEY,"")

    fun isLogged() =  sharedPreferences.getBoolean(IS_LOGGED,false)

}